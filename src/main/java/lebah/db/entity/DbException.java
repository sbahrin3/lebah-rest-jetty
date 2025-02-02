package lebah.db.entity;

public class DbException extends Exception {
	
	
	public DbException(String message) {
		super(message);
	}
	
	public DbException() {
		super();
	}

	public DbException(String message, Exception e) {
		super(message);
	}

}
