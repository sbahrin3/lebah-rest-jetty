package demo.data;

import demo.entity.User;

public class UserRes {
	
	String id;
	String email;
	String fullName;
	String identificationNumber;
	
	public UserRes(User u) {
		this.id = u.getId();
		this.email = u.getEmail();
		this.fullName = u.getFullName();
		this.identificationNumber = u.getIdentificationNumber();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getIdentificationNumber() {
		return identificationNumber;
	}
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}
	
	

}
