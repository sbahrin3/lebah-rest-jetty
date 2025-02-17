package lebah.test;

import java.util.List;
import java.util.Optional;

import lebah.db.entity.Persistence;
import lebah.rest.api.exception.DataNotFoundException;
import qard.data.LegalCapacityBorrowerDataRes;
import qard.entity.Borrower;
import qard.entity.LegalCapacity;
import qard.entity.LegalCapacityOption;
import qard.entity.LegalCapacityResult;
import qard.services.BorrowerService;

public class LegalCapacities {
	
	
	public static void main(String... args) throws Exception {
		
		Borrower borrower = BorrowerService.find("d28c11bf9d6792859db606f62afac5bde99e82b8");
		if ( borrower == null ) throw new DataNotFoundException();
		
		Persistence db = Persistence.db();
		List<LegalCapacity> legalCapacities = db.list("select l from LegalCapacity l order by l.sequence");
		List<LegalCapacityResult> legalCapacityResults = borrower.getLegalCapacityResults();
		System.out.println("Count: " + legalCapacityResults.size());
		
		LegalCapacityBorrowerDataRes lgBorrowerData = new LegalCapacityBorrowerDataRes(legalCapacities, borrower);
		
		lgBorrowerData.getLegalCapacities().stream().forEach(lg -> {
			System.out.println(lg.getName());
			lg.getResults().stream().forEach( result -> {
				System.out.println(result.getLegalCapacityOption().getDescription());
				System.out.println(result.getAnswer());
			});
		});
		
		
		
		
	}

}
