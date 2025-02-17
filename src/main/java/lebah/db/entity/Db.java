package lebah.db.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Db {

    private static Map<String, Db> persistenceMap = new ConcurrentHashMap<>();
    private static SessionFactory factory = null;
    private static ThreadLocal<Session> threadLocalSession = ThreadLocal.withInitial(() -> null);

    private String dialect = "";
    private String driver = "";
    private String url = "";
    private String username = "";
    private String password = "";

    private boolean add = true;

    private Db() {
        try {
            createSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Db(String key) throws Exception {
        createSessionFactory(key);
    }

    public synchronized static Db db() throws DbException {
        try {
            return db("default");
        } catch (Exception e) {
            e.printStackTrace();
            throw new DbException();
        }
    }

    public synchronized static Db db(HttpServletRequest request) throws Exception {
        String key = (String) request.getSession().getAttribute("_ref_key");
        if (key == null || "null".equals(key)) key = "";
        if ("".equals(key) || key.length() == 0) key = "default";
        return db(key);
    }

    public synchronized static Db db(String key) throws Exception {
        if (persistenceMap.get(key) == null) {
            persistenceMap.put(key, new Db(key));
        }
        return persistenceMap.get(key);
    }

    private void createSessionFactory() {
        Configuration cfg = new Configuration();
        cfg.configure();
        factory = cfg.buildSessionFactory();
    }

    private void createSessionFactory(String key) throws Exception {
        try {
            dialect = DbProperties.dialect(key);
            driver = DbProperties.driver(key);
            url = DbProperties.url(key);
            username = DbProperties.user(key);
            password = DbProperties.password(key);

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
        } catch (Exception e) {
            throw e;
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

    public void closeSession() {
        Session session = threadLocalSession.get();
        if (session != null && session.isOpen()) {
            session.close();
        }
        threadLocalSession.remove();
    }

    public synchronized void save(Object object) throws DbException {
        Transaction transaction = null;
        try (Session session = getSession()) {
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
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            session.update(object);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error updating object", e);
        }
    }

    public void delete(Object object) throws Exception {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            session.delete(object);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new Exception("Error deleting object", e);
        }
    }

    public <T> T find(Class<T> klass, Object id) {
        try (Session session = getSession()) {
            return session.find(klass, id);
        }
    }

    public void close() {
        if (factory != null) {
            factory.close();
        }
    }
    
	@SuppressWarnings("unchecked")
	public <T> List<T> list(String hql) {
		
        try (Session session = getSession()) {
    		List<T> list = new ArrayList<>();
    		Query q = session.createQuery(hql);
    		list = q.getResultList();

    		return list;        
        }
	}
	
	public long getTotalRecords(String hql) {
		try (Session session = getSession()) {
			String countHql = "SELECT COUNT(*) " + hql.substring(hql.toLowerCase().indexOf("from"));
	
			Query countQuery = session.createQuery(countHql, Long.class);
			return (Long) countQuery.getSingleResult(); // Return the total count
		}
	}
}
