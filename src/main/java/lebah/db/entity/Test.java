package lebah.db.entity;

import java.util.List;

import qard.entity.Borrower;

public class Test {
	
	public static void main(String[] args) throws Exception {
		
		Persistence db = Persistence.db();
		String q = "select b from Borrower b where b.fullName like :p1 and b.identificationNumber like :p2";
		List<Borrower> list = db.list(q,"%a%", "H%");
		System.out.println(list.size());
		list.stream().forEach(System.out::println);
		
		Borrower borrower = db.get("select b from Borrower b where b.fullName like :p1", "Tengku%");
		System.out.println(borrower);
	}
	

}
