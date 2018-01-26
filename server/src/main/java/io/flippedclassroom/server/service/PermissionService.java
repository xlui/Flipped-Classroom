package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.Permission;

public interface PermissionService {
	Permission findPermissionById(Long id);

	Permission findPermissionByPermissionName(String permissionName);

	Permission save(Permission permission);

	void delete(Permission permission);
}
