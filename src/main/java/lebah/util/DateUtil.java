package lebah.util;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {
	
	public static Date toDate(String datef) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			date = sdf.parse(datef);
		} catch ( Exception e ) {
			//e.printStackTrace();
		}
		return date;
	}
	
	public static String toStr(Date date) {
		String datef = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			datef = sdf.format(date);
		} catch ( Exception e ) {
			//e.printStackTrace();
		}
		return datef;
	}

}
