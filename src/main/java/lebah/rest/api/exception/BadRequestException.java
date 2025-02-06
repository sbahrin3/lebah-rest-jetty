package lebah.rest.api.exception;

public class BadRequestException extends Exception {
	
	public BadRequestException() {
		super("The request could not be understood by the server due to malformed syntax.");
	}
	
	public BadRequestException(String message) {
		super(message);
	}

}
