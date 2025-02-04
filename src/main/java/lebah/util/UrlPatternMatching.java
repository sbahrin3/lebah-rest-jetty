package lebah.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlPatternMatching {
    public static void main(String[] args) {
        // Define the annotation pattern
        String annotationPattern = "/login"; // "/update/{userId}/spouse/{spouseId}/child/{childId}";
        Pattern annpattern = Pattern.compile("\\{(\\w+)\\}");
        Matcher annmatcher = annpattern.matcher(annotationPattern);
        List<String> parameterNames = new ArrayList<>();
        while (annmatcher.find()) {
            String paramName = annmatcher.group(1); // Extract the name inside { and }
            parameterNames.add(paramName);
        }
        

        // Define the request URL
        String requestUrl = "/login"; // "/update/faizal/spouse/mizan/child/fatin";

        // Convert the annotation pattern to a regex pattern
        String regexPattern = annotationPattern.replaceAll("\\{\\w+\\}", "([^/]+)");
        Pattern pattern = Pattern.compile(regexPattern);

        // Perform pattern matching
        Matcher matcher = pattern.matcher(requestUrl);

        if (matcher.matches()) {
        	
        	System.out.println("Match Found.");

        	Map<String, String> attr = new HashMap<>();
        	for ( int i=0; i < parameterNames.size(); i++ ) {
        		attr.put(parameterNames.get(i), matcher.group(i+1));
        	}
        	
        	System.out.println(attr);
        	
        	
        } else {
            System.out.println("No match found for the request URL.");
        }
    }
}
