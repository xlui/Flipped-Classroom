package io.flippedclassroom.server.service.impl;

import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.repository.CourseRepository;
import io.flippedclassroom.server.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
	@Autowired
	private CourseRepository courseRepository;

	@Override
	public Course findById(Long id) {
		return courseRepository.findById(id);
	}

	@Override
	public Course findCourseByCourseName(String courseName) {
		return courseRepository.findByName(courseName);
	}

	@Override
	public Course save(Course course) {
		return courseRepository.save(course);
	}

	@Override
	public List<Course> save(Iterable<Course> iterable) {
		return courseRepository.save(iterable);
	}

	@Override
	public void delete(Course course) {
		courseRepository.delete(course);
	}

	@Override
	public void deleteAll() {
		courseRepository.deleteAll();
	}
}
