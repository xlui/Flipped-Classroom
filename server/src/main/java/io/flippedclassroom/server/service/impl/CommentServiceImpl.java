package io.flippedclassroom.server.service.impl;

import io.flippedclassroom.server.entity.Comment;
import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.repository.CommentRepository;
import io.flippedclassroom.server.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
	@Autowired
	private CommentRepository commentRepository;

	@Override
	public Comment findCommentById(Long id) {
		return commentRepository.findById(id);
	}

	@Override
	public List<Comment> findCommentsByUser(User user) {
		return commentRepository.findByUser(user);
	}

	@Override
	public List<Comment> findCommentsByCourse(Course course) {
		return commentRepository.findByCourse(course);
	}

	@Override
	public Comment save(Comment comment) {
		return commentRepository.save(comment);
	}

	@Override
	public void delete(Comment comment) {
		commentRepository.delete(comment);
	}
}
