package lebah.test;

import java.util.List;

import lebah.db.entity.Db;
import lebah.db.entity.Persistence;
import qard.entity.Borrower;

public class Test {
	
	public static void main(String[] args) throws Exception {
		
		/*
		Persistence db = Persistence.db();
		String q = "select b from Borrower b";
		List<Borrower> list = db.list(q);
		long total = db.getTotalRecords(q);
		*/
		new Thread(() -> {
			try {
				Db db = Db.db();
				String q = "select b from Borrower b";
				List<Borrower> list = db.list(q);
				for ( Borrower b : list ) {
					System.out.println(b);
				}
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}).start();;
		
		new Thread(() -> {
			try {
				Db db = Db.db();
				String q = "select count(*) from Borrower b";
				long total = db.getTotalRecords(q);
				System.out.println("Total Records: " + total);
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}).start();
		
	}
	

}
