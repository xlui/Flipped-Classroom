package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.User;

import java.util.List;

public interface UserService {
	User findUserById(Long id);

	User findUserByUsername(String username);

	User save(User user);

	List<User> save(Iterable<User> iterable);

	void delete(User user);
}
