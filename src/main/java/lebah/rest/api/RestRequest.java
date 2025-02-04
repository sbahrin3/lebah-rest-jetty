package lebah.rest.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysql.cj.log.Log;

import lebah.rest.servlets.Delete;
import lebah.rest.servlets.Get;
import lebah.rest.servlets.Patch;
import lebah.rest.servlets.Post;
import lebah.rest.servlets.Put;

/**
 * 
 * @author shamsulbahrin
 * @since 24 Jan 2025
 */

public abstract class RestRequest extends JSONData {
	
	//protected JSONObject jsonOut =  new JSONObject();
	protected Map<String, Object> response = new HashMap<>();
	protected String[] params;
	protected HttpServletRequest httpServletRequest;
	protected HttpServletResponse httpServletResponse;
	private boolean found = false;
	
	/**
	 * Main method being called
	 */
	
	public void doService(HttpServletRequest req, HttpServletResponse res, String action, String[] params) 
			throws IOException, ServletException {
		this.httpServletRequest = req;
		this.httpServletResponse = res;
		this.params = params;
		
		init(req, res);
		try {
						
			jsonIn = getJSONInput(req);
			
			Object obj = doAction(req, res, action);
						
			res.setStatus(HttpServletResponse.SC_OK);
			out.print(new Gson().toJson(obj));
			
			
		} catch ( Exception e ) {
			e.printStackTrace();
			Throwable cause = ResponseExceptionHandler.getRootCause(e);
					    
		    ResponseExceptionHandler.setResponseErrorStatus(cause, res);

		    response.put("message", cause.getMessage() != null ? cause.getMessage() : "Message Not Defined.");
        	
			out.print(new Gson().toJson(response));

		}
	}
	
	public Object doAction(HttpServletRequest req, HttpServletResponse res, String action) throws Exception {
		
	
		if ( "post".equals(action)) return doPost();
		else if ( "get".equals(action)) return doGet();
		else if ( "delete".equals(action)) return doDelete();
		else if ( "put".equals(action)) return doPut();
		else if ( "patch".equals(action)) return doPatch();
		else return doGet();

		
	}
	
	
	protected String getPathVariable(String variableName) {
		return (String) httpServletRequest.getAttribute(variableName);
	}
	
	protected String getRequestParameter(String paramName) {
		return httpServletRequest.getParameter(paramName);
	}
	
	public Object doPost() throws Exception {
		selectMethodToInvoke("post");
		return response;
	}
	
	public Object doGet() throws Exception{
		selectMethodToInvoke("get");
		return response;
	}
	
	public Object doPut() throws Exception {
		selectMethodToInvoke("put");
		return response;
	}
	
	public Object doPatch() throws Exception {
		selectMethodToInvoke("patch");
		return response;
	}
	
	public Object doDelete() throws Exception {
		selectMethodToInvoke("delete");
		return response;
	}
	
	private void selectMethodToInvoke(String action) throws Exception {
		try {
			
			String command = "";
			
			String pathInfo = httpServletRequest.getPathInfo();
		
			pathInfo = pathInfo.substring(pathInfo.indexOf("/") + 1);
			if ( pathInfo.indexOf("/") > 0 ) 
				command = pathInfo.substring(pathInfo.indexOf("/"));
						
			if ( command == null ) command = "";
			if ( command.equals("") ) command = "/";
			
			Method[] methods = getClass().getDeclaredMethods();
			
			try {
				findStaticMethodToInvoke(action, command, methods);	
				if ( !found ) findDynamicMethodToInvoke(action, command, methods);	
			} catch ( Exception e ) {
				//e.printStackTrace();
				throw e;
			}
			
			if ( !found ) {
				throw new MethodNotFoundException();
			}
			
		} catch ( Exception e ) {
			throw e;
		}
	}

	private void findStaticMethodToInvoke(String action, String command, Method[] methods) throws Exception {
		found = false;
		for ( Method method : methods ) {
			String annotationPattern = getAnnotationPattern(action, method);
			found = invokeStaticMethod(annotationPattern, command, method);
			if ( found ) break;
		}
	}
	
	private void findDynamicMethodToInvoke(String action, String command, Method[] methods) throws Exception {
		found = false;
		for ( Method method : methods ) {
			String annotationPattern = getAnnotationPattern(action, method);
			found = invokeDynamicMethod(annotationPattern, command, method);
			if ( found ) break;
		}
	}

	private String getAnnotationPattern(String action, Method method) {
		if ( "post".equals(action)) {
			if ( method.isAnnotationPresent(Post.class)) {
				Post cmd = method.getAnnotation(Post.class);	
				return cmd.value();
			}
		}
		else if ( "get".equals(action)) {
			if ( method.isAnnotationPresent(Get.class)) {
				Get cmd = method.getAnnotation(Get.class);	
				return cmd.value();
			}
			
		}
		else if ( "delete".equals(action)) {
			if ( method.isAnnotationPresent(Delete.class)) {
				Delete cmd = method.getAnnotation(Delete.class);	
				return cmd.value();
			}
		}
		else if ( "put".equals(action)) {
			if ( method.isAnnotationPresent(Put.class)) {
				Put cmd = method.getAnnotation(Put.class);	
				return cmd.value();
			}
		}
		else if ( "patch".equals(action)) {
			if ( method.isAnnotationPresent(Patch.class)) {
				Patch cmd = method.getAnnotation(Patch.class);	
				return cmd.value();
			}
		}
		return "";
	}
	
	

	
	public boolean invokeStaticMethod(String annotationPattern, String command, Method method) throws Exception {

		if ( annotationPattern.equals(command)) {
			
			Class<?>[] parameterTypes = method.getParameterTypes();
			if (parameterTypes.length == 1) {
				Class<?> paramType = parameterTypes[0];
				Gson gson = new Gson();
		        Object instance = gson.fromJson(jsonIn.toString(), paramType);
				
				method.invoke(this, instance);
			}
			else {
				method.invoke(this, null);
			}
			return true;
		}
		return false;
	}
	
	
	public boolean invokeDynamicMethod(String annotationPattern, String command, Method method) throws Exception {
		Pattern annpattern = Pattern.compile("\\{(\\w+)\\}");
        Matcher annmatcher = annpattern.matcher(annotationPattern);
        List<String> parameterNames = new ArrayList<>();
        while (annmatcher.find()) {
            String paramName = annmatcher.group(1); // Extract the name inside { and }
            parameterNames.add(paramName);
        }

        // Define the request URL
        String requestUrl = command;

        // Convert the annotation pattern to a regex pattern
        String regexPattern = annotationPattern.replaceAll("\\{\\w+\\}", "([^/]+)");
        Pattern pattern = Pattern.compile(regexPattern);

        // Perform pattern matching
        Matcher matcher = pattern.matcher(requestUrl);

        if (matcher.matches()) {
        	
            // Extract parameter values
        	for ( int i=0; i < parameterNames.size(); i++ ) {
        		httpServletRequest.setAttribute(parameterNames.get(i), matcher.group(i+1));
        	}
        	
        	//Invoke the method
        	Class<?>[] parameterTypes = method.getParameterTypes();
			if (parameterTypes.length == 1) {
				Class<?> paramType = parameterTypes[0];
				Gson gson = new Gson();
		        Object instance = gson.fromJson(jsonIn.toString(), paramType);
				
				method.invoke(this, instance);
			}
			else {
				method.invoke(this, null);
			}
        	        	
			return true;
            
        }
        
        return false;
	}
	
	
	public String getExceptionStackTrace(Exception e) {
		StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
	}
	
	public String getQueryString() {
		return httpServletRequest.getQueryString();
	}


	@Override
	public boolean needAuthorization() {
		return false;
	}
	
	/*
	 * This method convert an object to a json and then to a response map
	 */
	public void sendAsResponse(Object object) {
		Gson gson = new Gson();
		//convert object to json
		String json = gson.toJson(object);
		//convert json to map 
		Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
		response = gson.fromJson(json, mapType);
	}

}
