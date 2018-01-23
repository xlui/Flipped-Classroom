package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.Course;

public interface CourseService {
	Course findCourseById(Long id);

	Course findCourseByCourseName(String courseName);

	Course save(Course course);

	void delete(Course course);
}
