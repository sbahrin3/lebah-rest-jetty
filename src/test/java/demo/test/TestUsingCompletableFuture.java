package demo.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import demo.entity.User;
import lebah.db.entity.Persistence;

public class TestUsingCompletableFuture {
	
	@SuppressWarnings("unchecked")
	public static void main(String...strings) throws Exception {
		
		Persistence db = Persistence.db();
		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
			
			System.out.println("Fetching with concurrency method.");
			System.out.println("Begin benchmark.");
			long startTime = System.nanoTime();
			String query = "select u from User u order by u.fullName";
	
			CompletableFuture<List<?>> listFuture1 = listData(executor, db, query, 1, 2000);
			CompletableFuture<List<?>> listFuture2 = listData(executor, db, query, 2, 2000);
			CompletableFuture<List<?>> listFuture3 = listData(executor, db, query, 3, 2000);
			CompletableFuture<List<?>> listFuture4 = listData(executor, db, query, 4, 2000);
			CompletableFuture<List<?>> listFuture5 = listData(executor, db, query, 5, 2000);

			
			CompletableFuture<Void> allTasks = CompletableFuture.allOf(listFuture1, listFuture2, listFuture3, listFuture4, listFuture5);		
			allTasks.join();
			
			List<?> list1 = listFuture1.join();
			List<?> list2 = listFuture2.join();
			List<?> list3 = listFuture3.join();
			List<?> list4 = listFuture4.join();
			List<?> list5 = listFuture5.join();
			
			
			
			List<User> listAll = new ArrayList<>();
			listAll.addAll((List<User>) list1);
			listAll.addAll((List<User>) list2);
			listAll.addAll((List<User>) list3);
			listAll.addAll((List<User>) list4);
			listAll.addAll((List<User>) list5);
			
			
			long endTime = System.nanoTime();
			System.out.println("End benchmark.");
			
            long duration = endTime - startTime; // Calculate elapsed time
            double elapsedTimeMs = duration / 1_000_000.0; // Convert to milliseconds

            System.out.println("Total records = " + listAll.size());
            System.out.println("Elapsed Time = " + elapsedTimeMs);
			
		}
		
	}
	
	public static CompletableFuture<List<?>> listData(ExecutorService executor, Persistence db, String query, int pageNo, int max) {
		CompletableFuture<List<?>> listFuture = CompletableFuture.supplyAsync(() -> {
			return db.listDataByPage(query, pageNo, max);
		}, executor);
		return listFuture;
	}

}
