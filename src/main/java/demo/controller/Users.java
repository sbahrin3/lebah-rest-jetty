package demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import demo.data.PageAttr;
import demo.data.UserReq;
import demo.data.UserRes;
import demo.entity.User;
import demo.services.ServiceUtil;
import lebah.db.entity.Persistence;
import lebah.rest.api.RestRequest;
import lebah.rest.api.exception.DataNotFoundException;
import lebah.rest.servlets.Delete;
import lebah.rest.servlets.Get;
import lebah.rest.servlets.Post;
import lebah.rest.servlets.Put;

public class Users  extends RestRequest  {
	
	
	@Get("/")
	public void listUsers() throws Exception {
		PageAttr page = new PageAttr();
		String queryString = getQueryString();
		List<User> users = ServiceUtil.listByQueryParams(User.class, queryString, page);
		
		response.put("list", users.stream().map(UserRes::new).collect(Collectors.toList()));
		response.put("count", users.size());
		response.put("total", page.getTotal());
		response.put("pageSize", page.getPageSize());
		response.put("pageNumber", page.getPageNumber());
		response.put("nextPageNumber", page.getNextPageNumber());
		response.put("prevPageNumber", page.getPrevPageNumber());
		response.put("totalPages", page.getTotalPages());
		response.put("startCount", page.getStartCount());
		
	}
	
	@Post("/")
	public void addUser(UserReq userReq) throws Exception {
		
		String fullName = userReq.getFullName();
		String email = userReq.getEmail();
		String identificationNumber = userReq.getIdentificationNumber();
		
		User user = new User();
		user.setEmail(email);
		user.setFullName(fullName);
		user.setIdentificationNumber(identificationNumber);
		
		Persistence.db().save(user);
		
		sendAsResponse(new UserRes(user));
	}
	
	@Get("/{userId}")
	public void getUser() throws Exception {
		String userId = this.getPathVariable("userId");
		User user = Persistence.db().find(User.class, userId);
		if ( user == null ) throw new DataNotFoundException();
		
		sendAsResponse(new UserRes(user));
	}
	
	@Put("{userId}")
	public void updateUser(UserReq userReq) throws Exception {
		String userId = this.getPathVariable("userId");
		User user = Persistence.db().find(User.class, userId);
		if ( user == null ) throw new DataNotFoundException();
		
		if ( userReq.getFullName() != null) user.setFullName(userReq.getFullName());
		if ( userReq.getEmail() != null ) user.setEmail(userReq.getEmail());
		if ( userReq.getIdentificationNumber() != null ) user.setIdentificationNumber(userReq.getIdentificationNumber());
		
		Persistence.db().update(user);
		
		sendAsResponse(new UserRes(user));
		
	}
	
	@Delete("{userId}")
	public void deleteUser() throws Exception {
		String userId = this.getPathVariable("userId");
		User user = Persistence.db().find(User.class, userId);
		if ( user == null ) throw new DataNotFoundException();
		
		Persistence.db().delete(user);
		
		response.put("message", "User has been deleted.");
	}
	

}
