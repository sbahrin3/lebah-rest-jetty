package demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import demo.data.RoleDTO;
import demo.entity.Role;
import demo.entity.User;
import demo.services.ServiceUtil;
import lebah.db.entity.Persistence;
import lebah.rest.api.RestRequest;
import lebah.rest.api.exception.DataNotFoundException;
import lebah.rest.servlets.Delete;
import lebah.rest.servlets.Get;
import lebah.rest.servlets.Path;
import lebah.rest.servlets.Post;
import lebah.rest.servlets.Put;

@Path("/roles")
public class Roles extends RestRequest {

	@Get("/")
	public void listRoles() throws Exception {
		List<Role> roles = Persistence.db().list("select r from Role r order by r.name");
		response.put("list", roles.stream().map(ServiceUtil::toRoleDTO).toList());
		
	}

	@Post("/")
	public void addRole(RoleDTO roleDTO) throws Exception {
		Role role = new Role();
		role.setName(roleDTO.name());
		Persistence.db().save(role);
		sendAsResponse(ServiceUtil.toRoleDTO(role));
	}

	@Put("/{roleId}")
	public void updateRole(RoleDTO roleDTO) throws Exception {
		String roleId = this.getPathVariable("roleId");
		Role role = Persistence.db().find(Role.class, roleId);
		if ( role == null ) throw new DataNotFoundException();

		role.setName(roleDTO.name());
		Persistence.db().update(role);
		sendAsResponse(ServiceUtil.toRoleDTO(role));
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
	 * 
	 * Example: http://localhost:8080/roles/3/users
	 * 
	 * Where 3 is a role id.
	 */
	@Get("/{roleId}/users")
	public void listUsersByRole() throws Exception {
		String roleId = this.getPathVariable("roleId");
		Role role = Persistence.db().find(Role.class, roleId);
		if ( role == null ) throw new DataNotFoundException();

		List<User> users = Persistence.db().list("SELECT u FROM User u JOIN u.roles r WHERE r.id = :p1", roleId);
		
		response.put("role", ServiceUtil.toRoleDTO(role));
		response.put("users", users.stream().map(ServiceUtil::toUserDTO).toList());
	}

}
