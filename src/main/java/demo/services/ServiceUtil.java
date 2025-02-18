package demo.services;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import demo.data.PageAttr;
import lebah.db.entity.Persistence;
import lebah.util.DateUtil;
import lebah.util.QueryStringParser;

public class ServiceUtil {
	
	
	public static <T> List<T> listByQueryParams(Class<?> entityClass, String queryString, PageAttr page) throws Exception {

		Method[] methods = entityClass.getMethods();

		Map<String, String> queryParams = QueryStringParser.getQueryParams(queryString);
		String pageNumber = queryParams.get("pageNumber") != null ? queryParams.get("pageNumber") : "1";
		String pageSize = queryParams.get("pageSize") != null ? queryParams.get("pageSize") : "0";
		String orderBy = queryParams.get("orderBy") != null ? queryParams.get("orderBy") : "";

		List<Method> filteredMethods =
				Arrays.asList(methods).stream()
				.filter(m -> m.getName().startsWith("get"))
				.filter(m -> !m.getName().equals("getClass"))
				.filter(m -> queryParams.containsKey(m.getName().substring(3,4).toLowerCase() + m.getName().substring(4)))
				.collect(Collectors.toList());
		;

		Map<String, Object> params = new HashMap<>();
		String q = "select a from " + entityClass.getName() + " a where 1=1 ";
		for (Method m : filteredMethods) {
			Class<?> type = m.getReturnType();		    
			String key = m.getName().substring(3, 4).toLowerCase() + m.getName().substring(4);

			q += " and a." + key + " ";
			if (type == String.class) {
				q += "LIKE :" + key;
				params.put(key, "%" + queryParams.get(key) + "%");
			} else if (Number.class.isAssignableFrom(type) || type.isPrimitive()) {
				q += "= :" + key;
				params.put(key, queryParams.get(key));
			} else if (type == Boolean.class || type == boolean.class) {
				q += "= :" + key;
			} else if (type == Date.class || type == LocalDate.class || type == LocalDateTime.class) {
				String dateParam = queryParams.get(key);
				Date date1 = DateUtil.toDate(dateParam);
				Calendar c1 = Calendar.getInstance();
				c1.setTime(date1);
				c1.add(Calendar.DATE, 1);
				Date date2 = c1.getTime();
				String dateEndParam = DateUtil.toStr(date2);
				params.put( key + "_start", dateParam);
				params.put( key + "_end", dateEndParam);
				q += "BETWEEN :" + key + "_start AND :" + key + "_end";
			}
		}
		if ( !"".equals(orderBy)) {
			q += " order by a." + orderBy;
		}

		int pageNo = Integer.parseInt(pageNumber);
		int max = Integer.parseInt(pageSize);
		
		Persistence db = Persistence.db();
		
		String query = q;
		List<?> records = new ArrayList<>();
		Long totalRecords = 0L;
		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
						
			CompletableFuture<List<?>> listFuture = CompletableFuture.supplyAsync(() -> {
				return max > 0 ? db.listByPage(pageNo, max, query, params) : db.list(query, params);
            }, executor);
			CompletableFuture<Long> countFuture = CompletableFuture.supplyAsync(() -> {
				return db.getTotalRecords(query, params);
            }, executor);
			
			CompletableFuture<Void> allTasks = CompletableFuture.allOf(listFuture, countFuture);
            allTasks.join();
            
            records = listFuture.join();
            totalRecords = countFuture.join();
            
            page.setTotal(totalRecords);
		    page.setCount(records.size());
		}
		
		page.setPageNumber(pageNo);
		page.setPageSize(max);

		return (List<T>) records;
	}

}
