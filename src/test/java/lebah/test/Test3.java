package lebah.test;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;

import lebah.db.entity.Persistence;
import qard.entity.Borrower;
import qard.entity.LegalCapacityResult;

public class Test3 {
	
	@Transactional
	public static void main(String...strings) throws Exception {
		
		String id = "d28c11bf9d6792859db606f62afac5bde99e82b8";
		Persistence db = Persistence.db();
		Borrower b = db.find(Borrower.class, id);
		Hibernate.initialize(b.getLegalCapacityResults());
		List<LegalCapacityResult> legalCapacityResults = b.getLegalCapacityResults();
		
		System.out.println(legalCapacityResults.size());
	}

}
