package lebah.test;

import java.util.Date;

import com.github.javafaker.Faker;

import lebah.db.entity.Persistence;
import qard.entity.Borrower;

public class CreateBorrowersData {
	
	public static void main(String...strings) throws Exception {
		
		Persistence db = Persistence.db();
		Faker faker = new Faker();
		for ( int i=0; i < 270; i++ ) {
			String fullName = faker.name().fullName();
	        String identificationNumber = faker.bothify("???########", true);
	        int gender = Math.random() * 10 > 5 ? 1 : 0;
	        Date birthDate = faker.date().birthday();
	        
	        Borrower borrower = new Borrower();
	        borrower.setFullName(fullName);
	        borrower.setIdentificationNumber(identificationNumber);
	        borrower.setGender(gender);
	        borrower.setBirthDate(birthDate);
	        
	        db.save(borrower);
	        
	        
		}
		
	}

}
