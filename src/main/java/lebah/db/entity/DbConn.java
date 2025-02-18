package lebah.db.entity;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConn {

	private static Connection connection;

	public static Connection getConnection(String key) throws Exception {
		if ( key == null || key.equals("")) key = "default";
		String driver = DbProperties.driver(key);
		String url = DbProperties.url(key);
		String username = DbProperties.user(key);
		String password = DbProperties.password(key);

		Class.forName(driver);
		connection = DriverManager.getConnection(url, username, password);
		return connection;
	}

	public static Connection getConnection() throws Exception {
		return getConnection(null);
	}



	public static void main(String[] args) {
		try {
			Connection conn = DbConn.getConnection();
			System.out.println(conn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
