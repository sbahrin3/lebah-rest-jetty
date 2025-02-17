package lebah.db.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;

public class PersistenceX {
	
	private static Map<String, PersistenceX> persistenceMap = new HashMap<>();

	private static SessionFactory factory = null;
	private Transaction transaction;
	private Session session;

	private String dialect = "";
	private String driver = "";
	private String url = "";
	private String username = "";
	private String password = "";

	private boolean add = true;

	private PersistenceX() {
		try {
			createSessionFactory();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private PersistenceX(String key) throws Exception {
		createSessionFactory(key);
	}


	public synchronized static PersistenceX db() throws DbException {
		try {
			return db("default");
		} catch (Exception e) {
			e.printStackTrace();
			throw new DbException();

		}
	}


	public synchronized static PersistenceX db(HttpServletRequest request) throws Exception {
		String key = (String) request.getSession().getAttribute("_ref_key");
		if ( key == null || "null".equals(key) ) key = "";
		if ( "".equals(key) || key.length() == 0 ) key = "default";
		return db(key);
	}

	public synchronized static PersistenceX db(String key) throws Exception {

		if ( persistenceMap.get(key) == null ) {
			persistenceMap.put(key, new PersistenceX(key));
		}
		return persistenceMap.get(key);
	}

	private void createSessionFactory() {
		Configuration cfg = new Configuration();
		cfg.configure();
		factory = cfg.buildSessionFactory();

		session = factory.openSession();

	}

	/*
	private void createSessionFactory2() {
		StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build(); 
		Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
		factory = meta.getSessionFactoryBuilder().build();  

		session = factory.openSession();
	}
	*/

	private void createSessionFactory(String dialect, String driver, String url, String username, String password) {

		Configuration cfg = new Configuration();

		Properties properties = new Properties();
		if ( dialect != null && !"".equals(dialect)) properties.put("hibernate.dialect", dialect);
		properties.put("hibernate.connection.driver_class", driver);
		properties.put("hibernate.connection.url", url);
		properties.put("hibernate.connection.username", username);
		properties.put("hibernate.connection.password", password);
		properties.put("show_sql", "true");
		properties.put("hbm2ddl.auto", "update");
		cfg.setProperties(properties);

		cfg.configure();
		factory = cfg.buildSessionFactory();

		session = factory.openSession();



	}

	private void createSessionFactory(String key) throws Exception {

		try {

			dialect = DbProperties.dialect(key);
			driver = DbProperties.driver(key);
			url = DbProperties.url(key);
			username = DbProperties.user(key);
			password = DbProperties.password(key);

			createSessionFactory(dialect, driver, url, username, password);

		} catch ( Exception e ) {
			throw e;
		}


	}

	public SessionFactory factory() {
		return factory;
	}

	public void close() {
		factory.close();
	}

	public synchronized void save(Object object) throws DbException {
		try {
			transaction = session.beginTransaction();

			session.save(object);
			transaction.commit();

		} catch ( Exception e ) {
			System.out.println("ERROR when saving object " + object);
			transaction.rollback();
			//e.printStackTrace();
			throw new DbException();
		}
	}

	public void save(Object[] objects) throws DbException {

		try {
			transaction = session.beginTransaction();
			for ( Object object : objects ) session.save(object);
			transaction.commit();

		} catch ( Exception e ) {
			transaction.rollback();
			e.printStackTrace();
			throw new DbException();
		}
	}

	public void update(Object object) {

		try {
			transaction = session.beginTransaction();
			session.update(object);
			transaction.commit();

		} catch ( Exception e ) {

			transaction.rollback();
			e.printStackTrace();
			throw e;
		}

	}



	public void update(Object[] objects) {

		try {
			transaction = session.beginTransaction();
			Arrays.asList(objects).stream().forEach(object -> session.update(object));
			transaction.commit();

		} catch ( Exception e ) {
			transaction.rollback();
			e.printStackTrace();
			throw e;
		}
	}	

	public void delete(Object object) throws Exception {

		try {
			transaction = session.beginTransaction();
			session.delete(object);
			transaction.commit();

		} catch ( Exception e ) {
			transaction.rollback();
			e.printStackTrace();
			throw e;
		}
	}

	public void delete(Object[] objects) throws Exception{

		try {
			transaction = session.beginTransaction();
			Arrays.asList(objects).stream().forEach(object -> session.delete(object));
			transaction.commit();

		} catch ( Exception e ) {
			transaction.rollback();
			e.printStackTrace();
			throw e;
		}
	}

	public <T> T find(Class<T> klass, Object id) {

		T t = session.find(klass, id);
		return t;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> list(String hql) {

		List<T> list = new ArrayList<>();
		Query q = session.createQuery(hql);
		list = q.getResultList();

		return list;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String hql) {

		Query q = session.createQuery(hql);
		List<T> list = q.getResultList();
		return list.size() > 0 ? list.get(0) : null;
	}

	/*
	@SuppressWarnings("unchecked")
	public <T> List<T> list(String hql, Map<String, Object> h) {

		Query q = session.createQuery(hql);
		for ( Enumeration<String> e = h.keys(); e.hasMoreElements(); ) {
			String key = (String) e.nextElement();
			Object value = h.get(key);
			q.setParameter(key, value);
		}
		List<T> list = q.getResultList();
		return list;
	}
	 */

	public <T> List<T> list(String hql, Map<String, Object> h) {
		Query q = session.createQuery(hql);
		for (Map.Entry<String, Object> entry : h.entrySet()) {
			q.setParameter(entry.getKey(), entry.getValue());
		}
		return q.getResultList();
	}
	

	/*
	 *  Example: List<Entity> entities = db.list(hql, "value1", "value2", 4);
	 */

	public <T> List<T> list(String hql, Object... params ) {
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

		Query q = session.createQuery(hql);
		q.setFirstResult(start);
		q.setMaxResults(max);
		List<T> list = q.getResultList();

		return list;
	}
	
	//****
	public <T> List<T> listByPage(int pageNumber, int max, String hql, Map<String, Object> h) {
		int start = (pageNumber - 1) * max;
		
		Query q = session.createQuery(hql);
		q.setFirstResult(start);
		q.setMaxResults(max);		
		for (Map.Entry<String, Object> entry : h.entrySet()) {
			q.setParameter(entry.getKey(), entry.getValue());
		}
		List<T> list = q.getResultList();
		return list;
	}


	public <T> List<T> listByPage(String hql, int pageNumber, int max) {
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
		// Modify the HQL to count query
		String countHql = "SELECT COUNT(*) " + hql.substring(hql.toLowerCase().indexOf("from"));

		Query countQuery = session.createQuery(countHql, Long.class);
		return (Long) countQuery.getSingleResult(); // Return the total count
	}

	public long getTotalRecords(String hql, Map<String, Object> h) {
		// Modify the HQL to count query
		String countHql = "SELECT COUNT(*) " + hql.substring(hql.toLowerCase().indexOf("from"));

		Query countQuery = session.createQuery(countHql, Long.class);

		for (Map.Entry<String, Object> entry : h.entrySet()) {
			countQuery.setParameter(entry.getKey(), entry.getValue());
		}
		return (Long) countQuery.getSingleResult(); // Return the total count
	}

	public long getTotalRecords(String hql, Object... params ) {
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


	/*
	 *  remove this method... tersilap buat
	 */
	/*
	public <T> T get(String hql, int start, int max) {

		//session = factory.openSession();

		Query q = session.createQuery(hql);
		q.setFirstResult(start);
		q.setMaxResults(max);
		List<T> list = q.getResultList();
		return list.size() > 0 ? list.get(0) : null;
	}
	*/

	public <T> T get(String hql, int chunkSize) {
		return get(hql, 0, chunkSize);
	}

	public int execute(String q) throws ConstraintViolationException {

		transaction = session.beginTransaction();
		Query query = session.createQuery(q);
		int n = query.executeUpdate();
		transaction.commit();
		return n;
	}

	public PersistenceX ifAdd(boolean b) {
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
		transaction = session.beginTransaction();
	}

	public void commitTransaction() {
		transaction.commit();
	}

	public void rollbackTransaction() {
		transaction.rollback();
	}

	public void saveOnCommit(Object object) throws Exception {
		session.save(object);
	}

	public void saveOnCommit(Object[] objects) throws Exception {
		for ( Object object : objects ) session.save(object);
	}

	public void updateOnCommit(Object object) throws Exception {
		session.update(object);
	}

	public void updateOnCommit(Object[] objects) throws Exception {
		Arrays.asList(objects).stream().forEach(object -> session.update(object));
	}

	public void deleteOnCommit(Object object) throws Exception {
		session.delete(object);
	}

	public void deleteOnCommit(Object[] objects) throws Exception {
		Arrays.asList(objects).stream().forEach(object -> session.delete(object));
	}

}
