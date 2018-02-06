package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.Role;

import java.util.List;

public interface RoleService {
	Role findRoleById(Long id);

	Role findRoleByRoleName(String role);

	Role save(Role role);

	List<Role> save(Iterable<Role> iterable);

	void delete(Role role);
}
