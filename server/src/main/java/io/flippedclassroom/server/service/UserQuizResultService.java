package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.Quiz;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.entity.UserQuizResult;

import java.util.List;

public interface UserQuizResultService extends BaseService<UserQuizResult> {
	UserQuizResult findByUserAndQuiz(User user, Quiz quiz);

	List<UserQuizResult> findByUser(User user);

	List<UserQuizResult> findByQuiz(Quiz quiz);
}
