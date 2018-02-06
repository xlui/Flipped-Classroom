package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
	Permission findById(Long id);

	Permission findByPermission(String permission);
}
