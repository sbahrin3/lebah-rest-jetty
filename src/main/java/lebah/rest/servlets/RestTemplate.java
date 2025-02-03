package lebah.rest.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author shamsulbahrin
 * @since 24 Jan 2025
 */

@WebServlet(urlPatterns = "/*")
public class RestTemplate extends HttpServlet {
	
	protected String[] params = null;
	
	public static String getAuthorizationHeader(HttpServletRequest req) {
		return req.getHeader("Authorization");
	}

	private void addCorsHeaders(HttpServletResponse response) {
		
		System.out.println("Rest Template: addCorsHeaders");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE, PATCH");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, hx-request, hx-current-url, hx-trigger, hx-target, hx-swap");
		

	}

	public void doOptions(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		addCorsHeaders(response);
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		addCorsHeaders(response);
		chain.doFilter(req, res);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)  throws ServletException, IOException    {
		doService(req, res, "get");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException  {
		doService(req, res, "post");

	}
	
	public void doPut(HttpServletRequest req, HttpServletResponse res)  throws ServletException, IOException    {
		doService(req, res, "put");
	}
	
	public void doDelete(HttpServletRequest req, HttpServletResponse res)  throws ServletException, IOException    {
		doService(req, res, "delete");
	}
	
	public void doPatch(HttpServletRequest req, HttpServletResponse res)  throws ServletException, IOException    {
		doService(req, res, "patch");
	}
	
	public void doService(HttpServletRequest req, HttpServletResponse res, String action) throws ServletException, IOException  {
		
		System.out.println("RestTemplate doService");
		addCorsHeaders(res);
		
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
		
		String controllerPath = (String) req.getServletContext().getAttribute("controllerPath");
		if ( controllerPath == null ) controllerPath = "";

		PrintWriter out = res.getWriter();
		String pathInfo = req.getPathInfo();
				
		if ( pathInfo != null && !"".equals(pathInfo) ) {
						
			pathInfo = pathInfo.substring(pathInfo.indexOf("/") + 1);
			
			if ( pathInfo.indexOf("/") > 1 ) {
				String paramPath = pathInfo.substring(pathInfo.indexOf("/") + 1);
				
				pathInfo = pathInfo.substring(0, pathInfo.indexOf("/"));
				
				params = paramPath.split("/");
				
			}
			
			String module = controllerPath + "/" + pathInfo;

			module = module.replace("/", ".");
			String cname = module.substring(module.lastIndexOf(".") + 1);
			cname = cname.substring(0,1).toUpperCase() + cname.substring(1);
			module = module.substring(0, module.lastIndexOf(".")) + "." + cname;
			try {
				Object object = Class.forName(module).newInstance();	
				if ( object instanceof RestServlet ) {
					RestServlet restServlet = (RestServlet) object;
					
					if ( restServlet.needAuthorization() ) {
						String authorizationHeader = getAuthorizationHeader(req);
						//do something with authorization
					}

					restServlet.doService(req, res, action, params);

				}
			} catch ( ClassNotFoundException cnfex ) {
				res.setStatus(HttpServletResponse.SC_NOT_FOUND);
				cnfex.printStackTrace();
				out.print("Module Not Found Error: " + module);
			} catch ( InstantiationException iex ) {
				res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				iex.printStackTrace();
				out.print("Module Instantiation Error: " + module);
			} catch ( IllegalAccessException illex ) {
				res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				illex.printStackTrace();
				out.print("Illegal Access Error: " + module);
			} catch ( Exception ex ) {
				res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				ex.printStackTrace();
				//out.print("Module Error: " + module);
			}	
		}
		else {
			
			JSONObject obj = new JSONObject();
			try {
				obj.put("message", "Welcome to LeBAH Rest API");
				out.print(obj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
		}

	}

}
