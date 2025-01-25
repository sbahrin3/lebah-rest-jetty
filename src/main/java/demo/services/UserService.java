package demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import demo.data.UserDataRequest;
import demo.entity.User;
import lebah.rest.api.DataNotFoundException;



public class UserService {
	
	static {
		initializeDemoData();
	}
	
	public static List<User> usersData;
	
	public static void initializeDemoData() {
		System.out.println("Initialize Demo Data");
		usersData = new ArrayList<>();
		usersData.add(new User("ali@gmail.com", "1234", "Ali", "Baba"));
		usersData.add(new User("dino@gmail.com", "1234", "Iszuddin", "Ismail"));
		usersData.add(new User("jafar@gmail.com", "1234", "Jafar", "Darwis"));
		usersData.add(new User("faizal@gmail.com", "1234", "Faizal", "Hassan"));
		usersData.add(new User("hazwan@gmail.com", "1234", "Hazwan", "Ali"));
		System.out.println("Demo users records = " + usersData.size());
	}
	
	public static void registerUser(String username, String password, String firstName, String lastName) throws Exception {
		User user = new User(username, password, firstName, lastName);
		usersData.add(user);
	}
	
	public static void registerUser(UserDataRequest udr) throws Exception {
		registerUser(udr.getUsername(), udr.getPassword(), udr.getFirstName(), udr.getLastName());
	}
	
	public static void updateUser(String username, String firstName, String lastName) throws Exception {
		User user = findUser(username);
		user.setFirstName(firstName);
		user.setLastName(lastName);
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
		else {
			throw new DataNotFoundException();
		}
	}

}
