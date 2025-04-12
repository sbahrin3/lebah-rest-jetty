package lebah.db.entity;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;

/**
 * 
 * @author Shamsul Bahrin
 *
 */
public class Persistence implements AutoCloseable {

	private static Persistence instance = null;
	private static SessionFactory factory = null;
	private Transaction transaction;
	private static ThreadLocal<Session> threadLocalSession = ThreadLocal.withInitial(() -> null);
	//private static ThreadLocal<Session> threadLocalSession = ThreadLocal.withInitial(() -> HibernateUtil.getSessionFactory().openSession());

	private String dialect = "";
	private String driver = "";
	private String url = "";
	private String username = "";
	private String password = "";

	private boolean add = true;

	private Persistence() {
		try {
			createSessionFactory();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	private static Persistence getInstance() {
		if ( instance == null ) {
			instance = new Persistence();
		}
		return instance;
	}

	
	public static Persistence db() throws DbException {
		try {
			return getInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DbException();

		}
	}

	public Session getSession() {
		Session session = threadLocalSession.get();
		if (session == null || !session.isOpen()) {
			session = factory.openSession();
			threadLocalSession.set(session);
		}
		return session;
	}

	private void createSessionFactory() throws Exception {
		try {
			dialect = DbProperties.dialect();
			driver = DbProperties.driver();
			url = DbProperties.url();
			username = DbProperties.user();
			password = DbProperties.password();

			Configuration cfg = new Configuration();

			Properties properties = new Properties();
			if (dialect != null && !"".equals(dialect)) properties.put("hibernate.dialect", dialect);
			properties.put("hibernate.connection.driver_class", driver);
			properties.put("hibernate.connection.url", url);
			properties.put("hibernate.connection.username", username);
			properties.put("hibernate.connection.password", password);
			properties.put("show_sql", "true");
			properties.put("hbm2ddl.auto", "update");
			cfg.setProperties(properties);

			cfg.configure();
			factory = cfg.buildSessionFactory();
			System.out.println("Session factory created.");
		} catch (Exception e) {
			throw e;
		}
	}

	public SessionFactory factory() {
		return factory;
	}

	public void close() {
		Session session = getSession();
		session.close();
	}
	
	public void closeFactory() {
		factory.close();
	}
	
	public void closeSession() {
		Session session = threadLocalSession.get();
		if (session != null && session.isOpen()) {
			session.close();
		}
		threadLocalSession.remove();
	}

	public synchronized void save(Object object) throws DbException {
		Transaction transaction = null;
		//try (Session session = getSession()) {
		try {
			Session session = getSession();
			transaction = session.beginTransaction();
			session.save(object);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			throw new DbException("Error saving object", e);
		}
	}

	public void update(Object object) {
		Transaction transaction = null;
		//try (Session session = getSession()) {
		try {
			Session session = getSession();
			transaction = session.beginTransaction();
			session.update(object);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			throw new RuntimeException("Error updating object", e);
		}
	}
	
	public void update(Object...objects) {
		Transaction transaction = null;
		//try ( Session session = getSession()) {
		try {
			Session session = getSession();
			transaction = session.beginTransaction();
			Arrays.asList(objects).stream().forEach(object -> session.update(object));
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			throw new RuntimeException("Error updating object", e);
		}
	}
	
	public void delete(Object object) throws Exception {
		Transaction transaction = null;
		//try (Session session = getSession()) {
		try {
			Session session = getSession();
			transaction = session.beginTransaction();
			session.delete(object);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			throw new Exception("Error deleting object", e);
		}
	}
	
	
	
	

	public <T> T find(Class<T> klass, Object id) {
		//try (Session session = getSession()) {
			Session session = getSession();
			return session.find(klass, id);
		//}
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> list(String hql) {
		//try (Session session = getSession()) {
			Session session = getSession();
			List<T> list = new ArrayList<>();
			Query q = session.createQuery(hql);
			list = q.getResultList();

			return list;
		//}
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String hql) {
		//try (Session session = getSession()) {
			Session session = getSession();
			Query q = session.createQuery(hql);
			List<T> list = q.getResultList();
			return list.size() > 0 ? list.get(0) : null;
		//}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String hql, Map<String, Object> h) {
		//try (Session session = getSession()) {
			Session session = getSession();
			Query q = session.createQuery(hql);
			for (Map.Entry<String, Object> entry : h.entrySet()) {
				q.setParameter(entry.getKey(), entry.getValue());
			}
			List<T> list = q.getResultList();
			return list.size() > 0 ? list.get(0) : null;
		//}
	}


	public <T> List<T> list(String hql, Map<String, Object> h) {
		//try (Session session = getSession()) {
			Session session = getSession();
			Query q = session.createQuery(hql);
			for (Map.Entry<String, Object> entry : h.entrySet()) {
				q.setParameter(entry.getKey(), entry.getValue());
			}
			return q.getResultList();
		//}
	}


	/*
	 *  Example: List<Entity> entities = db.list(hql, "value1", "value2", 4);
	 */

	public <T> List<T> list(String hql, Object... params ) {
		
		int paramIndex = 1;
        while (hql.contains("?")) {
            hql = hql.replaceFirst("\\?", ":p" + paramIndex);
            paramIndex++;
        }
		
		Map<String, Object> h = new HashMap<>();
		for ( int i=1; i < params.length + 1; i++ ) {
			h.put("p" + i, params[i-1]);
		}
		return list(hql, h);
	}

	/*
	 *  Example: Entity entity = db.get(hql, "value1", "value2", 4);
	 */
	public <T> T get(String hql, Object... params ) {
		List<T> list = list(hql, params);
		return list.size() > 0 ? list.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> list(String hql, int start, int max) {
		//try (Session session = getSession()) {
			Session session = getSession();
			Query q = session.createQuery(hql);
			q.setFirstResult(start);
			q.setMaxResults(max);
			List<T> list = q.getResultList();

			return list;
		//}
	}

	//****
	@Transactional
	public <T> List<T> listByPage(int pageNumber, int max, String hql, Map<String, Object> h) {
		//try (Session session = getSession()) {
			Session session = getSession();
			int start = (pageNumber - 1) * max;
			Query q = session.createQuery(hql);
			q.setFirstResult(start);
			q.setMaxResults(max);		
			for (Map.Entry<String, Object> entry : h.entrySet()) {
				q.setParameter(entry.getKey(), entry.getValue());
			}
			List<T> list = q.getResultList();
			return list;
		//}
	}


	public <T> List<T> listDataByPage(String hql, int pageNumber, int max) {
		int start = (pageNumber - 1) * max; // Calculate the start index
		return list(hql, start, max);
	}


	/**
	 * To get TOTAL NUMBER OF RECORDS for pagination purpose
	 * @param hql
	 * @param h
	 * @return
	 */

	public long getTotalRecords(String hql) {
		//try (Session session = getSession()) {
			Session session = getSession();
			// Modify the HQL to count query
			String countHql = "SELECT COUNT(*) " + hql.substring(hql.toLowerCase().indexOf("from"));

			Query countQuery = session.createQuery(countHql, Long.class);
			return (Long) countQuery.getSingleResult(); // Return the total count
		//}
	}

	public long getTotalRecords(String hql, Map<String, Object> h) {
		//try (Session session = getSession()) {
			Session session = getSession();
			// Modify the HQL to count query
			String countHql = "SELECT COUNT(*) " + hql.substring(hql.toLowerCase().indexOf("from"));

			Query countQuery = session.createQuery(countHql, Long.class);

			for (Map.Entry<String, Object> entry : h.entrySet()) {
				countQuery.setParameter(entry.getKey(), entry.getValue());
			}
			return (Long) countQuery.getSingleResult(); // Return the total count
		//}
	}

	public long getTotalRecords(String hql, Object... params ) {
		
		int paramIndex = 1;
        while (hql.contains("?")) {
            hql = hql.replaceFirst("\\?", ":p" + paramIndex);
            paramIndex++;
        }
        
		Map<String, Object> h = new HashMap<>();
		for ( int i=1; i < params.length + 1; i++ ) {
			h.put("p" + i, params[i-1]);
		}
		return getTotalRecords(hql, h);
	}


	/**
	 * For PAGINATION purpose
	 * @param <T>
	 * @param start
	 * @param max
	 * @param hql
	 * @param params
	 * @return
	 */

	public <T> List<T> list(int start, int max, String hql, Object... params ) {
		
		int paramIndex = 1;
        while (hql.contains("?")) {
            hql = hql.replaceFirst("\\?", ":p" + paramIndex);
            paramIndex++;
        }
        
		Map<String, Object> h = new HashMap<>();
		for ( int i=1; i < params.length + 1; i++ ) {
			h.put("p" + i, params[i-1]);
		}
		return list(start, max, hql, h);
	}


	/**
	 * When given a page number.
	 * @param <T>
	 * @param pageNumber
	 * @param max
	 * @param hql
	 * @param params
	 * @return
	 */

	public <T> List<T> listByPage(int pageNumber, int max, String hql, Object... params ) {
		int start = (pageNumber - 1) * max; // Calculate the start index
		return list(start, max, hql, params);
	}


	public <T> T get(String hql, int chunkSize) {
		return get(hql, 0, chunkSize);
	}

	public int execute(String q) throws ConstraintViolationException {
		//try (Session session = getSession()) {
			Session session = getSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(q);
			int n = query.executeUpdate();
			transaction.commit();
			return n;
		//}
	}

	public Persistence ifAdd(boolean b) {
		add = b;
		return this;
	}

	public void saveOrUpdate(Object object) throws DbException {
		if ( add )
			save(object);
		else
			update(object);
	}


	public void beginTransaction() {
		/*
		try (Session session = getSession()) {
			transaction = session.beginTransaction();
		}
		*/
		getSession().beginTransaction();
	}

	public void commitTransaction() {
		transaction.commit();
		getSession().close();
	}

	public void rollbackTransaction() {
		transaction.rollback();
	}

	public void saveOnCommit(Object object) throws Exception {
		/*
		try (Session session = getSession()) {
			session.save(object);
		}
		*/
		
		getSession().save(object);
	}

	public void saveOnCommit(Object[] objects) throws Exception {
		//try (Session session = getSession()) {
			Session session = getSession();
			for ( Object object : objects ) session.save(object);
		//}
	}

	public void updateOnCommit(Object object) throws Exception {
		//try (Session session = getSession()) {
			Session session = getSession();
			session.update(object);
		//}
	}

	public void updateOnCommit(Object[] objects) throws Exception {
		//try (Session session = getSession()) {
		Session session = getSession();
			Arrays.asList(objects).stream().forEach(object -> session.update(object));
		//}
	}

	public void deleteOnCommit(Object object) throws Exception {
		//try (Session session = getSession()) {
			Session session = getSession();
			session.delete(object);
		//}
	}

	public void deleteOnCommit(Object[] objects) throws Exception {
		//try (Session session = getSession()) {
			Session session = getSession();
			Arrays.asList(objects).stream().forEach(object -> session.delete(object));
		//}
	}

}
