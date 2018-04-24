package io.flippedclassroom.server.service.impl;

import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.Quiz;
import io.flippedclassroom.server.repository.QuizRepository;
import io.flippedclassroom.server.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {
	@Autowired
	private QuizRepository quizRepository;

	@Override
	public Quiz findQuizByContentAndAnswer(String content, String answer) {
		return quizRepository.findByContentAndAnswer(content, answer);
	}

	@Override
	public List<Quiz> findQuizsByCourse(Course course) {
		return quizRepository.findByCourse(course);
	}

	@Override
	public Quiz findById(Long id) {
		return quizRepository.findById(id);
	}

	@Override
	public Quiz save(Quiz quiz) {
		return quizRepository.save(quiz);
	}

	@Override
	public List<Quiz> save(Iterable<Quiz> iterable) {
		return quizRepository.save(iterable);
	}

	@Override
	public void delete(Quiz quiz) {
		quizRepository.delete(quiz);
	}

	@Override
	public void delete(Iterable<Quiz> iterable) {
		quizRepository.delete(iterable);
	}

	@Override
	public void deleteAll() {
		quizRepository.deleteAll();
	}
}
