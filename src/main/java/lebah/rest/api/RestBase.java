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
	protected Map<String, Object> jsonOut = new HashMap<>();
	
	void init(HttpServletRequest req, HttpServletResponse res)  throws IOException, ServletException {

		//serverUrl = getServerUrl(req);
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
	
	public String getString(String key) {
		try {
			return jsonIn.getString(key);
		} catch (JSONException e) {
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
