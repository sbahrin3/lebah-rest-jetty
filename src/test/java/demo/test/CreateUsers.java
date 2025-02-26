package demo.test;

import com.github.javafaker.Faker;

import demo.entity.User;
import lebah.db.entity.Persistence;

public class CreateUsers {
	
	public static void main(String...strings) throws Exception {
		Persistence db = Persistence.db();
		
		Faker faker = new Faker();
		for ( int i=0; i < 10000; i++ ) {
			User user = new User();
			user.setFullName(faker.name().fullName());
			user.setEmail(faker.internet().emailAddress());
			user.setIdentificationNumber(faker.bothify("???########", true));
			
			db.save(user);
			System.out.println(i + ")" + user.getFullName());
		}
		
	}

}
