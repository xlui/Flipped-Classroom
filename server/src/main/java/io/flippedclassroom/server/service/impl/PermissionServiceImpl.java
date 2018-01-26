package io.flippedclassroom.server.service.impl;

import io.flippedclassroom.server.entity.Permission;
import io.flippedclassroom.server.repository.PermissionRepository;
import io.flippedclassroom.server.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements PermissionService {
	@Autowired
	private PermissionRepository permissionRepository;

	@Override
	public Permission findPermissionById(Long id) {
		return permissionRepository.findById(id);
	}

	@Override
	public Permission findPermissionByPermissionName(String permissionName) {
		return permissionRepository.findByPermission(permissionName);
	}

	@Override
	public Permission save(Permission permission) {
		return permissionRepository.save(permission);
	}

	@Override
	public void delete(Permission permission) {
		permissionRepository.delete(permission);
	}
}
