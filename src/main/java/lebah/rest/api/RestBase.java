package lebah.rest.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import lebah.rest.servlets.RestServlet;

/**
 * 
 * @author shamsulbahrin
 * @since 24 Jan 2025
 */
public abstract class RestBase implements RestServlet {
	
	protected PrintWriter out;
	protected String serverUrl;
	//protected JSONObject jsonOut = new JSONObject();
	protected Map<String, Object> jsonOut = new HashMap<>();
	
	String getServerUrl(HttpServletRequest req) {
		String serverName = req.getServerName();
		int serverPort = req.getServerPort();
        String server = serverPort != 80 ? serverName + ":" + serverPort : serverName;
        String http = req.getRequestURL().toString().substring(0, req.getRequestURL().toString().indexOf("://") + 3);
        String serverUrl = http + server;		
		String uri = req.getRequestURI();
				
		String s1 = uri.substring(1);
		
		String appName = s1.indexOf("/") > -1 ? s1.substring(0, s1.indexOf("/")) : "";
        String url = serverUrl + "/" + appName;
        
		return url;
	}
	
	void init(HttpServletRequest req, HttpServletResponse res)  throws IOException, ServletException {

		serverUrl = getServerUrl(req);
		out = res.getWriter();
	}
	
	protected JSONObject jsonIn;

	static JSONObject getJSONInput(HttpServletRequest req) throws IOException, JSONException {
		StringBuilder sb = new StringBuilder();
        BufferedReader br = req.getReader();
        String str = null;
        while ((str = br.readLine()) != null) sb.append(str);
        if ( sb.toString().equals("")) sb.append("{}");
        return new JSONObject(sb.toString());
	}
	
	static String getRequestInput(HttpServletRequest req) throws IOException, JSONException {
		StringBuilder sb = new StringBuilder();
        BufferedReader br = req.getReader();
        String str = null;
        while ((str = br.readLine()) != null) sb.append(str);
        if ( sb.toString().equals("")) sb.append("{}");
        return sb.toString();
	}
	
	public String getString(String key) {
		try {
			return jsonIn.getString(key);
		} catch (JSONException e) {
			System.out.println(key + " is not available");
			//e.printStackTrace();
		}
		return "";
	}
	
	public int getInt(String key) {
		try {
			return jsonIn.getInt(key);
		} catch (JSONException e) {
			//e.printStackTrace();
		}
		return 0;	
	}
	
	public double getDouble(String key) {
		try {
			return jsonIn.getDouble(key);
		} catch (JSONException e) {
			//e.printStackTrace();
		}
		return 0;	
	}
	

}
