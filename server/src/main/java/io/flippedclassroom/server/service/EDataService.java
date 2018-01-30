package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.EData;

import java.util.List;

public interface EDataService {
	EData findEDataById(Long id);

	List<EData> findEDatasByCourse(Course course);

	EData save(EData eData);

	List<EData> save(Iterable<EData> iterable);

	void delete(EData eData);
}
