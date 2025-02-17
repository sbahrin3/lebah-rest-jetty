package lebah.test;

import java.util.List;

import lebah.db.entity.Persistence;
import qard.data.LegalCapacityData;
import qard.data.LegalCapacityOptionData;
import qard.entity.LegalCapacity;
import qard.entity.LegalCapacityOption;
import qard.services.LegalCapacityService;

public class CreateLegalCapacitiesData {
	
	public static void main(String...strings) throws Exception {
		Persistence db = Persistence.db();
		
		db.execute("delete from LegalCapacityOption o");
		db.execute("delete from LegalCapacity o");
		
		List<LegalCapacityData> list = LegalCapacityService.getLegalcapacities();
		
		int n = 0;
		for ( int i = 0; i < list.size(); i++ ) {
			LegalCapacityData data = list.get(i);
			List<LegalCapacityOptionData> options =  data.getOptions();
			
			LegalCapacity legalCapacity = new LegalCapacity();
			legalCapacity.setId(Integer.toString(data.getId()));
			legalCapacity.setName(data.getTitle());
			legalCapacity.setSequence(i+1);
			
			
			for ( int j = 0; j < options.size(); j++ ) {
				LegalCapacityOptionData optionData = options.get(j);
				LegalCapacityOption lgoption = new LegalCapacityOption();
				lgoption.setId(legalCapacity.getId() + "_" + Integer.toString(optionData.getId()));
				lgoption.setDescription(optionData.getDescription());
				lgoption.setLegalCapacity(legalCapacity);
				lgoption.setSequence(++n);
				legalCapacity.getOptions().add(lgoption);
				
				System.out.println(lgoption.getDescription());
				
			}
			db.save(legalCapacity);
		}
		
	}

}
