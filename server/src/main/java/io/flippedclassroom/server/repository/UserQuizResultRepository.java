package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.Quiz;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.entity.UserQuizResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuizResultRepository extends JpaRepository<UserQuizResult, Long> {
	UserQuizResult findById(Long id);

	UserQuizResult findByUserAndQuiz(User user, Quiz quiz);
}
