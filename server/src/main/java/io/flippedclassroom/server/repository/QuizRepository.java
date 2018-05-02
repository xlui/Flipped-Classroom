package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
	Quiz findById(Long id);

	Quiz findByContentAndAnswer(String content, String answer);

	List<Quiz> findByCourse(Course course);

	void deleteById(Long quizId);
}
