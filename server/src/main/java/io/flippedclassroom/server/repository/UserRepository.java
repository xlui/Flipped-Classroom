package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(Long id);

    User findByUsername(String username);

    void deleteById(Long userId);
}
