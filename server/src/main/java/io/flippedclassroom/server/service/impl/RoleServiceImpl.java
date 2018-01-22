package io.flippedclassroom.server.service.impl;

import io.flippedclassroom.server.entity.Role;
import io.flippedclassroom.server.repository.RoleRepository;
import io.flippedclassroom.server.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Role findRoleById(Long id) {
		return roleRepository.findById(id);
	}

	@Override
	public Role findRoleByRoleName(String role) {
		return roleRepository.findByRole(role);
	}

	@Override
	public Role save(Role role) {
		return roleRepository.save(role);
	}
}
