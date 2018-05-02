package io.flippedclassroom.server.service.impl;

import io.flippedclassroom.server.entity.Quiz;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.entity.UserQuizResult;
import io.flippedclassroom.server.repository.UserQuizResultRepository;
import io.flippedclassroom.server.service.UserQuizResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserQuizResultServiceImpl implements UserQuizResultService {
	@Autowired
	private UserQuizResultRepository userQuizResultRepository;

	@Override
	public UserQuizResult findByUserAndQuiz(User user, Quiz quiz) {
		return userQuizResultRepository.findByUserAndQuiz(user, quiz);
	}

	@Override
	public List<UserQuizResult> findByUser(User user) {
		return userQuizResultRepository.findByUser(user);
	}

	@Override
	public List<UserQuizResult> findByQuiz(Quiz quiz) {
		return userQuizResultRepository.findByQuiz(quiz);
	}

	@Override
	public UserQuizResult findById(Long id) {
		return userQuizResultRepository.findById(id);
	}

	@Override
	public UserQuizResult save(UserQuizResult userQuizResult) {
		return userQuizResultRepository.save(userQuizResult);
	}

	@Override
	public List<UserQuizResult> save(Iterable<UserQuizResult> iterable) {
		return userQuizResultRepository.save(iterable);
	}

	@Override
	public void delete(UserQuizResult userQuizResult) {
		userQuizResultRepository.delete(userQuizResult);
	}

    @Override
    public void deleteById(Long id) {
        userQuizResultRepository.deleteById(id);
    }

    @Override
	public void delete(Iterable<UserQuizResult> iterable) {
		userQuizResultRepository.delete(iterable);
	}

	@Override
	public void deleteAll() {
		userQuizResultRepository.deleteAll();
	}
}
