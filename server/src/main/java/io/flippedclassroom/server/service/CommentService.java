package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.Comment;
import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.User;

import java.util.List;

public interface CommentService {
	Comment findCommentById(Long id);

	List<Comment> findCommentsByUser(User user);

	List<Comment> findCommentsByCourse(Course course);

	Comment save(Comment comment);

	void delete(Comment comment);
}
