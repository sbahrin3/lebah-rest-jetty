package lebah.rest.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author shamsulbahrin
 * @since 24 Jan 2025
 */
public abstract class JSONData  extends RestBase {

	public abstract Object doAction(HttpServletRequest req, HttpServletResponse res, String action) throws Exception;

	public void doService(HttpServletRequest req, HttpServletResponse res, String action) throws Exception {
		init(req, res);
		try {
			jsonIn = getJSONInput(req);
			out.print(doAction(req, res, action));
		} catch (Exception e) {
			e.printStackTrace(out);
		}
	}

}
