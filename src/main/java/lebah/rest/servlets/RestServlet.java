

package lebah.rest.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lebah.rest.api.exception.MethodNotFoundException;

/**
 * 
 * @author shamsulbahrin
 * @since 24 Jan 2025
 */

public interface RestServlet {
	
	boolean needAuthorization();
		
	void doService(HttpServletRequest request, HttpServletResponse response, String action, String[] params) 
			throws IOException, ServletException, MethodNotFoundException;
}
