package lebah.rest.api;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;


/**
 * 
 */

public class ResponseExceptionHandler {

	private static final Map<Class<? extends Throwable>, Integer> EXCEPTION_STATUS_MAP = Map.of(
			MethodNotFoundException.class, HttpServletResponse.SC_NOT_IMPLEMENTED, 
			DataNotFoundException.class, HttpServletResponse.SC_NOT_FOUND, 
			DuplicateIdentificationException.class, HttpServletResponse.SC_CONFLICT
			);

	public static void setResponseErrorStatus(Throwable cause, HttpServletResponse res) {
		int status = EXCEPTION_STATUS_MAP.getOrDefault(cause.getClass(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		res.setStatus(status);
		
	}
	
	/**
	 * The while will go until to the root cause
	 */
	public static Throwable getRootCause(Throwable throwable) {
	    Throwable cause = throwable;
	    while (cause.getCause() != null && cause.getCause() != cause) {
	        cause = cause.getCause();
	    }
	    
	    System.out.println("CAUSEDBY: " + cause);
	    
	    return cause;
	}
	
	

}
