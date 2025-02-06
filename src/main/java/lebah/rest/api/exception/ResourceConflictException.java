package lebah.rest.api.exception;


//409
public class ResourceConflictException extends Exception {
	
	public ResourceConflictException() {
		super("Conflict or Duplicate Resource.");
	}
	
	public ResourceConflictException(String message) {
		super(message);
	}

}
