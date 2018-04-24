package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.Quiz;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.entity.UserQuizResult;

public interface UserQuizResultService extends BaseService<UserQuizResult> {
	UserQuizResult findByUserAndQuiz(User user, Quiz quiz);
}
