package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.User;

public interface UserService {
	User findUserById(Long id);
	User findUserByUsername(String username);
	User save(User user);
}
