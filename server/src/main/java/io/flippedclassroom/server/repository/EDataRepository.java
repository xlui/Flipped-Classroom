package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.EData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EDataRepository extends JpaRepository<EData, Long> {
	EData findById(Long id);

	List<EData> findByCourse(Course course);

	void deleteById(Long eDataId);
}
