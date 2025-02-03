package lebah.rest.api;


//409
public class DuplicateIdentificationException extends Exception {
	
	public DuplicateIdentificationException() {
		super("Conflict or Duplicate Data.");
	}

}
