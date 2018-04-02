package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.Comment;
import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	Comment findById(Long id);

	List<Comment> findAllByUser(User user);

	List<Comment> findAllByCourse(Course course);

	List<Comment> findAllByReply(Long commentId);

	void deleteById(Long commentId);
}
