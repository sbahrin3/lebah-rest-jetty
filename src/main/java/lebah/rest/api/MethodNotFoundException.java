package lebah.rest.api;

//422
public class MethodNotFoundException extends Exception {
	
	public MethodNotFoundException() {
		super("Bad Request or Method Not Found.");
	}

}
