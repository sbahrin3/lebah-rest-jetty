package lebah.rest.api;


//400
public class InvalidRequestException extends Exception {
	
	public InvalidRequestException() {
		super("Invalid Request Data or Parameters.");
	}

}
