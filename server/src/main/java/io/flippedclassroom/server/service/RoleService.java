package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.Role;

public interface RoleService {
	Role findRoleById(Long id);

	Role findRoleByRoleName(String role);

	Role save(Role role);

	void delete(Role role);
}
