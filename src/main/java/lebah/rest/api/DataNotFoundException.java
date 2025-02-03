package lebah.rest.api;

//404
public class DataNotFoundException extends Exception {
	
	public DataNotFoundException() {
		super("Resource Not Found.");
	}
}
