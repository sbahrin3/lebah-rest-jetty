package demo.test;

import java.util.List;

import demo.entity.User;
import lebah.db.entity.Persistence;

public class TestFetchData {
	
	public static void main(String...strings) throws Exception {
		
		Persistence db = Persistence.db();

		System.out.println("Fetching with traditional method.");
		System.out.println("Begin benchmark.");
		
		String query = "select u from User u order by u.fullName";
		long startTime = System.nanoTime();
		List<User> users = db.listDataByPage(query, 1, 2000);
		long endTime = System.nanoTime();
		
		
		
		long duration = endTime - startTime; // Calculate elapsed time
        double elapsedTimeMs = duration / 1_000_000.0; // Convert to milliseconds
        
        System.out.println("End benchmark.");

       
        System.out.println("Total records = " + users.size());
        System.out.println("Elapsed Time = " + elapsedTimeMs);
		
		
	}

}
