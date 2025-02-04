package demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import com.github.javafaker.Faker;

import demo.data.UserDataRequest;
import demo.entity.User;
import lebah.rest.api.DataNotFoundException;
import lebah.rest.api.DuplicateIdentificationException;



public class UserService {
	
	static {
		initializeDemoData();
	}
	
	public static List<User> usersData;
	
	public static void initializeDemoData() {
		System.out.println("Initialize Demo Data");
		usersData = new ArrayList<>();
		
		Faker faker = new Faker();
		for ( int i=0; i < 167; i++ ) {
	        String firstName = faker.name().firstName();
	        String lastName = faker.name().lastName();
	        String email = faker.internet().emailAddress();
	        usersData.add(new User(email, "1234", firstName, lastName));
	        
		}
		
        
		System.out.println("Demo users records = " + usersData.size());
	}
	
	public static User registerUser(UserDataRequest udr) throws Exception {
		if ( findUser(udr.getUsername()) != null) throw new DuplicateIdentificationException();
		User user = new User();
		if ( udr.getUsername() != null ) user.setUsername(udr.getUsername());
		if ( udr.getFirstName() != null ) user.setFirstName(udr.getFirstName());
		if ( udr.getLastName() != null ) user.setLastName(udr.getLastName());
		if ( udr.getPassword() != null ) user.setPassword(udr.getPassword());
		usersData.add(user);
		return user;	}
	
	public static User updateUser(UserDataRequest udr) throws Exception {
		User user = findUser(udr.getUsername());
		if ( udr.getFirstName() != null ) user.setFirstName(udr.getFirstName());
		if ( udr.getLastName() != null ) user.setLastName(udr.getLastName());
		if ( udr.getPassword() != null ) user.setPassword(udr.getPassword());
		return user;
	}
	
	public static List<User> listUsers() throws Exception {
		return usersData;
		
	}
	
	public static User authenticate(String username, String password) throws Exception{
		
		Optional<User> userFind =
		usersData.stream()
			.filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
			.findFirst()
			;
		
		if ( userFind.isPresent() ) return userFind.get();
		else {
			throw new DataNotFoundException();
		}
	}
	
	public static User findUser(String username) throws Exception{
		
		Optional<User> userFind =
		usersData.stream()
			.filter(user -> user.getUsername().equals(username))
			.findFirst()
			;
		
		if ( userFind.isPresent() ) return userFind.get();
		else 
			return null;
		
	}

}
