package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.Preview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreviewRepository extends JpaRepository<Preview, Long> {
	Preview findById(Long id);

	List<Preview> findByCourse(Course course);
}
