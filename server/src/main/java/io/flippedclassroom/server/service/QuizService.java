package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.Quiz;

import java.util.List;

public interface QuizService extends BaseService<Quiz> {
	Quiz findQuizByContentAndAnswer(String content, String answer);

	List<Quiz> findQuizsByCourse(Course course);
}
