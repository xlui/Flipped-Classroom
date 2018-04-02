package io.flippedclassroom.server.service.impl;

import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.Preview;
import io.flippedclassroom.server.repository.PreviewRepository;
import io.flippedclassroom.server.service.PreviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreviewServiceImpl implements PreviewService {
	@Autowired
	private PreviewRepository previewRepository;

	@Override
	public Preview findById(Long id) {
		return previewRepository.findById(id);
	}

	@Override
	public List<Preview> findPreviewsByCourse(Course course) {
		return previewRepository.findByCourse(course);
	}

	@Override
	public Preview save(Preview preview) {
		return previewRepository.save(preview);
	}

	@Override
	public List<Preview> save(Iterable<Preview> iterable) {
		return previewRepository.save(iterable);
	}

	@Override
	public void delete(Preview preview) {
		previewRepository.delete(preview);
	}

	@Override
	public void deleteAll() {
		previewRepository.deleteAll();
	}
}
