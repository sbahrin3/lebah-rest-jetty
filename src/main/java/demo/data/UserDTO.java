package demo.data;

import java.util.List;

public record UserDTO(
		String id, 
		String fullName, 
		String identificationNumber, 
		String email,
		List<RoleDTO> roles
		) {}
