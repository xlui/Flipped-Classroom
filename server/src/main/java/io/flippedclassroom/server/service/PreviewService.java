package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.Preview;

import java.util.List;

public interface PreviewService {
	Preview findPreviewById(Long id);

	List<Preview> findPreviewsByCourse(Course course);

	Preview save(Preview preview);

	List<Preview> save(Iterable<Preview> iterable);

	void delete(Preview preview);
}
