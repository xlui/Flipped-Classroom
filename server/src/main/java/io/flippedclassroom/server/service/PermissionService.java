package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.Permission;

import java.util.List;

public interface PermissionService {
	Permission findPermissionById(Long id);

	Permission findPermissionByPermissionName(String permissionName);

	Permission save(Permission permission);

	List<Permission> save(Iterable<Permission> iterable);

	void delete(Permission permission);
}
