package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
	Course findById(Long id);

	Course findByName(String course);

	void deleteById(Long courseId);
}
