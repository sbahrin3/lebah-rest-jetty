package lebah.rest.api;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import lebah.rest.api.exception.BadRequestException;
import lebah.rest.api.exception.DataNotFoundException;
import lebah.rest.api.exception.MethodNotFoundException;
import lebah.rest.api.exception.ResourceConflictException;


/**
 * 
 */

public class ResponseExceptionHandler {

	private static final Map<Class<? extends Throwable>, Integer> EXCEPTION_STATUS_MAP = Map.of(
			MethodNotFoundException.class, HttpServletResponse.SC_NOT_IMPLEMENTED, 
			DataNotFoundException.class, HttpServletResponse.SC_NOT_FOUND, 
			ResourceConflictException.class, HttpServletResponse.SC_CONFLICT,
			BadRequestException.class, HttpServletResponse.SC_BAD_REQUEST
			);

	public static int getStatus(Throwable cause) {
		return EXCEPTION_STATUS_MAP.getOrDefault(cause.getClass(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
