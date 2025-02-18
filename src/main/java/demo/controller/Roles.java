package demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import demo.data.RoleReq;
import demo.data.RoleRes;
import demo.data.UserRes;
import demo.entity.Role;
import demo.entity.User;
import lebah.db.entity.Persistence;
import lebah.rest.api.RestRequest;
import lebah.rest.api.exception.DataNotFoundException;
import lebah.rest.servlets.Delete;
import lebah.rest.servlets.Get;
import lebah.rest.servlets.Post;
import lebah.rest.servlets.Put;

public class Roles extends RestRequest {
	
	@Get("/")
	public void listRoles() throws Exception {
		List<Role> roles = Persistence.db().list("select r from Role r order by r.name");
		response.put("list", roles.stream().map(RoleRes::new).collect(Collectors.toList()));
	}
	
	@Post("/")
	public void addRole(RoleReq roleReq) throws Exception {
		Role role = new Role();
		role.setName(roleReq.getName());
		Persistence.db().save(role);
		sendAsResponse(new RoleRes(role));
	}
	
	@Put("/{roleId}")
	public void updateRole(RoleReq roleReq) throws Exception {
		String roleId = this.getPathVariable("roleId");
		Role role = Persistence.db().find(Role.class, roleId);
		if ( role == null ) throw new DataNotFoundException();
		
		role.setName(roleReq.getName());
		Persistence.db().update(role);
		sendAsResponse(new RoleRes(role));
	}
	
	@Delete("/{roleId")
	public void deleteRole() throws Exception {
		String roleId = this.getPathVariable("roleId");
		Role role = Persistence.db().find(Role.class, roleId);
		if ( role == null ) throw new DataNotFoundException();
		
		Persistence.db().delete(role);
		
		response.put("message", "Role has been deleted.");
	}
	
	
	/*
	 * Return list of Users by a given Role
	 * Example: http://localhost:8080/roles/3/users
	 * Where 3 is a role id.
	 */
	@Get("{roleId}/users")
	public void listUsersByRole() throws Exception {
		String roleId = this.getPathVariable("roleId");
		Role role = Persistence.db().find(Role.class, roleId);
		if ( role == null ) throw new DataNotFoundException();
		
		List<User> users = Persistence.db().list("SELECT u FROM User u JOIN u.roles r WHERE r.id = :p1", roleId);
		
		response.put("role", new RoleRes(role));
		response.put("users", users.stream().map(UserRes::new).collect(Collectors.toList()));
	}

}
