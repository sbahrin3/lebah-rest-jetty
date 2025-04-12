package lebah.rest.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import demo.graphql.HelloDataFetcherProvider;
import demo.services.GraphQLDataFetcherProvider;
import demo.services.GraphQLService;
import demo.services.UserDataFetcherProvider;
import graphql.ExecutionResult;
import lebah.rest.jetty.JettyApp;

/**
 * 
 * @author shamsulbahrin
 * @since 24 Jan 2025
 */

@WebServlet(urlPatterns = "/*")
public class RestTemplate extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String[] params = null;
	
	//GraphQL
	private GraphQLService graphQLService;
    static class GraphQLRequest {
        String query;
        Map<String, Object> variables;
    }
    private final Gson gson = new Gson();
    
    public void init(ServletConfig config) {
    	System.out.println("Initalizing GraphQL Service.");
    	graphQLService = new GraphQLService(JettyApp.graphQLProviders);
    	
    }
    
    //GraphQL
  	void doGraphQL(HttpServletRequest req, HttpServletResponse res) throws IOException {
          GraphQLRequest gqlRequest = gson.fromJson(req.getReader(), GraphQLRequest.class);
          ExecutionResult executionResult = graphQLService.getGraphQL().execute(gqlRequest.query);  //graphQLService.execute(gqlRequest.query);
          res.setContentType("application/json");
          gson.toJson(executionResult.toSpecification(), res.getWriter());
  		
  	}

	public static String getAuthorizationHeader(HttpServletRequest req) {
		return req.getHeader("Authorization");
	}

	private void addCorsHeaders(HttpServletResponse response) {

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

		addCorsHeaders(res);

		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");

		PrintWriter out = res.getWriter();
		String pathInfo = req.getPathInfo();
		
		//pathInfo not available, so get out
		if ( pathInfo == null || "".equals(pathInfo)) {
			System.out.println("pathInfo Not Available.");
			showDefaultMessage(out);
			return;
		}
		
		pathInfo = pathInfo.substring(pathInfo.indexOf("/") + 1);
		if ( pathInfo.indexOf("/") > 1 ) {
			String paramPath = pathInfo.substring(pathInfo.indexOf("/") + 1);
			pathInfo = pathInfo.substring(0, pathInfo.indexOf("/"));
			params = paramPath.split("/");
		}
				
		if ( "graphql".equals(pathInfo)) {
			
			doGraphQL(req, res);
			
		}
		else {
			String controllerClass = JettyApp.controllersMap.get(pathInfo);
			try {
	
				Object object = Class.forName(controllerClass)
						.getDeclaredConstructor()
						.newInstance();
				
				switch ( object ) {
					case RestServlet restServlet -> {
						boolean isAuthorized = true;
						if ( restServlet.needAuthorization() ) {
							isAuthorized = AuthorizationHandler.isAuthorized(getAuthorizationHeader(req));
						}
						if ( isAuthorized )
							restServlet.doService(req, res, action, params);
						else
							showNotAuthorizedMessage(out);
						
					}
					case null -> {
						System.out.println("Object is Null");
					}
					default -> {
						System.out.println("Switch Default");
					}
				}
				
			} catch ( ClassNotFoundException e ) {
				res.setStatus(HttpServletResponse.SC_NOT_FOUND);
				e.printStackTrace();
				showErrorMessage(out, "Class Not Found Exception has occured.");
			} catch ( InstantiationException e ) {
				res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				e.printStackTrace();
				showErrorMessage(out, "Instantiation Exception has occured.");
			} catch ( IllegalAccessException e ) {
				res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				e.printStackTrace();
				showErrorMessage(out, "Illegal Access Exception has occured.");
			} catch ( NullPointerException e ) {
				System.out.println("NULL POINTER");
				res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				e.printStackTrace();
				//showBadRequestMessage(out);
				showErrorMessage(out, "Bad Request.  Path is not defined.");
			} catch ( Exception e ) {
				res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				e.printStackTrace();
				showErrorMessage(out, "An error has occured.");
			}
		}


	}
	
	

	void showDefaultMessage(PrintWriter out) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("message", "Welcome to LeBAH Rest API");
			out.print(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	void showNotAuthorizedMessage(PrintWriter out) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("message", "Authorization Needed.");
			out.print(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	void showErrorMessage(PrintWriter out, String message) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("message", message);
			out.print(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
