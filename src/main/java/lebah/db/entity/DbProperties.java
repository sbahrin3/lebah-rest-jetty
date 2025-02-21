package lebah.db.entity;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DbProperties {

	private static ResourceBundle rb;

	static {
		try {
			rb = ResourceBundle.getBundle("dbpersistence");
		} catch ( MissingResourceException e ) {
			e.printStackTrace();
		}
	}

	public static String valueOf(String key) throws Exception {
		return rb.getString(key);
	}

	public static String dialect() throws Exception {
		return valueOf("dialect");
	}

	public static String driver() throws Exception {
		return valueOf("driver");
	}

	public static String url() throws Exception {
		return valueOf("url");
	}

	public static String user() throws Exception {
		return valueOf("user");
	}

	public static String password() throws Exception {
		return valueOf("password");
	}

}
