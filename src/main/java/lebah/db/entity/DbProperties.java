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
    	if ( key == null || "null".equals(key) ) key = "default";
        return rb.getString(key);
    }
    
    public static String dialect(String key) throws Exception {
    	key = "dialect.".concat(key);
    	return valueOf(key);
    }
    
    public static String driver(String key) throws Exception {
    	key = "driver.".concat(key);
    	return valueOf(key);
    }
    
    public static String url(String key) throws Exception {
    	key = "url.".concat(key);
    	return valueOf(key);
    }
    
    public static String user(String key) throws Exception {
    	key = "user.".concat(key);
    	return valueOf(key);
    }

    public static String password(String key) throws Exception {
    	key = "password.".concat(key);
    	return valueOf(key);
    }
    
    public static void main(String[] args) {
    	
    	try {
			String url1 = DbProperties.valueOf("url.cucms");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			String url2 = DbProperties.valueOf("url.erican");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	
    }

}
