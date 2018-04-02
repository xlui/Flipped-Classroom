package io.flippedclassroom.server.service.impl;

import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.repository.UserRepository;
import io.flippedclassroom.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public User findById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public List<User> save(Iterable<User> iterable) {
		return userRepository.save(iterable);
	}

	@Override
	public void delete(User user) {
		userRepository.delete(user);
	}

	@Override
	public void deleteAll() {
		userRepository.deleteAll();
	}
}
