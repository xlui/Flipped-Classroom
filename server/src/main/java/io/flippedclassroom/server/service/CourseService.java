package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.Course;

import java.util.List;

public interface CourseService {
	Course findCourseById(Long id);

	Course findCourseByCourseName(String courseName);

	Course save(Course course);

	List<Course> save(Iterable<Course> iterable);

	void delete(Course course);
}
