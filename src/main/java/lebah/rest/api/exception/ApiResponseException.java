package lebah.rest.api.exception;

public class ApiResponseException extends Exception {
	
	int status = 512;
	String message = "Error";
	
	public ApiResponseException() {
		super("An Error has occured.");
	}
	
	public ApiResponseException(int status) {
		super("An Error has occured.");
		this.status = status;
		this.message = "An Error has occured.";
	}
	
	public ApiResponseException(int status, String message) {
		super(message);
		this.status = status;
		this.message = message;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getMessage() {
		return message;
	}

}
