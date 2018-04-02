package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.Preview;

import java.util.List;

public interface PreviewService extends BaseService<Preview> {
	List<Preview> findPreviewsByCourse(Course course);
}
