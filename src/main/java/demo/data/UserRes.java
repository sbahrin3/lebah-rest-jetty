package demo.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import demo.entity.User;

public class UserRes {

	String id;
	String email;
	String fullName;
	String identificationNumber;
	List<RoleRes> roles = new ArrayList<>();

	public UserRes(User u) {
		this.id = u.getId();
		this.email = u.getEmail();
		this.fullName = u.getFullName();
		this.identificationNumber = u.getIdentificationNumber();
		this.roles = u.getRoles().stream().map(RoleRes::new).toList();
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

	public List<RoleRes> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleRes> roles) {
		this.roles = roles;
	}



}
