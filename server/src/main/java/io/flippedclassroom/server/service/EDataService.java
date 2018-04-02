package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.EData;

import java.util.List;

public interface EDataService extends BaseService<EData> {
	List<EData> findEDatasByCourse(Course course);
}
