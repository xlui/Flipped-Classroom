package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.Quiz;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.entity.UserQuizResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserQuizResultRepository extends JpaRepository<UserQuizResult, Long> {
	UserQuizResult findById(Long id);

	List<UserQuizResult> findByUser(User user);

	List<UserQuizResult> findByQuiz(Quiz quiz);

	UserQuizResult findByUserAndQuiz(User user, Quiz quiz);

	void deleteById(Long userQuizResultId);
}
