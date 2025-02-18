package demo.data;

import demo.entity.Role;

public class RoleRes {

	String id;
	String name;

	public RoleRes(Role role) {
		this.id = role.getId();
		this.name = role.getName();
	}

	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
