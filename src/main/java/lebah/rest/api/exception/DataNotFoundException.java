package lebah.rest.api.exception;

//404
public class DataNotFoundException extends Exception {
	
	public DataNotFoundException() {
		super("Resource Not Found.");
	}
	
	public DataNotFoundException(String message) {
		super(message);
	}
}
