package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findById(Long id);

	Role findByRole(String role);
}
