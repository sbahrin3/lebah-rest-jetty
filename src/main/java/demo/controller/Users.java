package demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import demo.data.LoginRequest;
import demo.data.TestingRequest;
import demo.data.UserDataRequest;
import demo.data.UserDataResponse;
import demo.entity.User;
import demo.services.UserService;
import lebah.rest.api.RestRequest;
import lebah.rest.api.exception.ApiResponseException;
import lebah.rest.api.exception.DataNotFoundException;
import lebah.rest.servlets.Delete;
import lebah.rest.servlets.Get;
import lebah.rest.servlets.Post;
import lebah.rest.servlets.Put;



public class Users extends RestRequest {
	
	
	
	@Post("/")
	public void registerUser(UserDataRequest udr) throws Exception {
		User user = UserService.registerUser(udr);
		sendAsResponse(new UserDataResponse(user));
	}
	
	@Get("/")
	public void listUsers() throws Exception {
		List<User> users = UserService.listUsers();
		response.put("count", users.size());
		response.put("list", users.stream().map(u -> new UserDataResponse(u)).collect(Collectors.toList()));
	}

	@Post("/login")
	public void login(LoginRequest loginRequest) throws Exception {
		User user = UserService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
		if ( user != null ) {
			sendAsResponse(new UserDataResponse(user));
		} else 
			throw new DataNotFoundException();
	
	}
	
	@Get("/{userId}")
	public void getUser() throws Exception {		
		User user = UserService.findUser(getPathVariable("userId"));
		if ( user != null ) sendAsResponse(new UserDataResponse(user));
		else throw new DataNotFoundException();
	}
	
	@Put("/{userId}")
	public void updateUser() throws Exception {
		String userId = getPathVariable("userId");
		response.put("message", "UPDATE user profile: " + userId);
		
		
	}
	
	@Delete("/{userId}")
	public void deleteUser() throws Exception {
		String userId = getPathVariable("userId");
		response.put("message", "DELETE user profile: " + userId);
	}
	
	@Get("/email/{email}")
	public void getUserByEmail() throws Exception {
		User user = UserService.findUser(getPathVariable("email"));
		if ( user != null ) sendAsResponse(new UserDataResponse(user));
		else throw new DataNotFoundException();
	}
	
	
	@Post("/{userId}/spouse/{spouseId}/child/{childId}")
	public void testMethod(TestingRequest testingRequest) throws Exception {
		System.out.println("Testing Only");
		String userId = getPathVariable("userId");
		String spouseId = getPathVariable("spouseId");
		String childId = getPathVariable("childId");
		
		String parameter1 = testingRequest.getParameter1();
		String parameter2 = testingRequest.getParameter2();
		System.out.println("parameter1 = " + parameter1);
		System.out.println("parameter2 = " + parameter2);
		
		response.put("userId", userId);
		response.put("spouseId", spouseId);
		response.put("childId", childId);
		response.put("testingRequest", testingRequest);
	}
	
	@Get("/testError/{value}")
	public void testApiResponseError() throws Exception {
		
		String value = getPathVariable("value");
		if ( "0".equals(value)) throw  new ApiResponseException(513, "Incorrect given value.");
		response.put("message", "value is " + value);
		
	}

}
