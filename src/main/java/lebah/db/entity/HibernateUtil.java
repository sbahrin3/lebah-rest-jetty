package lebah.db.entity;

import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
           // return new Configuration().configure().buildSessionFactory();
            Configuration cfg = new Configuration();
    		cfg.configure();
    		SessionFactory factory = createSessionFactory("");
    		return factory;
        } catch (Throwable ex) {
            System.err.println("SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    private static SessionFactory createSessionFactory(String key) throws Exception {
    	if ( "".equals(key) ) key = "default";
		try {
			String dialect = DbProperties.dialect();
			String driver = DbProperties.driver();
			String url = DbProperties.url();
			String username = DbProperties.user();
			String password = DbProperties.password();

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
			SessionFactory factory = cfg.buildSessionFactory();
			return factory;
		} catch (Exception e) {
			throw e;
		}
	}

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }

    public static void shutdown() {
        sessionFactory.close();
    }
}
