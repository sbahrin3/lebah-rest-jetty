package lebah.util;

import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class QueryStringParser {
	public static Map<String, String> getQueryParams(String query) {
		Map<String, String> queryPairs = new HashMap<>();
		try {
			// URI uri = new URI(url);
			//String query = uri.getQuery();
			if (query != null) {
				for (String param : query.split("&")) {
					String[] pair = param.split("=", 2);
					String key = URLDecoder.decode(pair[0], "UTF-8");
					String value = pair.length > 1 ? URLDecoder.decode(pair[1], "UTF-8") : "";
					queryPairs.put(key, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryPairs;
	}

	public static void main(String[] args) throws Exception {
		String url = "http://localhost:8080/borrowers?fullName=faizal&location=gombak&tags=hobby";
		URI uri = new URI(url);
		String query = uri.getQuery();
		Map<String, String> queryParams = getQueryParams(query);

		// Print the extracted key-value pairs
		queryParams.forEach((key, value) -> System.out.println(key + " = " + value));
	}
}
