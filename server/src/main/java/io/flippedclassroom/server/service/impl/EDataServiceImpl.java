package io.flippedclassroom.server.service.impl;

import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.EData;
import io.flippedclassroom.server.repository.EDataRepository;
import io.flippedclassroom.server.service.EDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EDataServiceImpl implements EDataService {
	@Autowired
	private EDataRepository eDataRepository;

	@Override
	public EData findById(Long id) {
		return eDataRepository.findById(id);
	}

	@Override
	public List<EData> findEDatasByCourse(Course course) {
		return eDataRepository.findByCourse(course);
	}

	@Override
	public EData save(EData eData) {
		return eDataRepository.save(eData);
	}

	@Override
	public List<EData> save(Iterable<EData> iterable) {
		return eDataRepository.save(iterable);
	}

	@Override
	public void delete(EData eData) {
		eDataRepository.delete(eData);
	}

	@Transactional
    @Override
    public void deleteById(Long id) {
        eDataRepository.deleteById(id);
    }

    @Override
	public void delete(Iterable<EData> iterable) {
		eDataRepository.delete(iterable);
	}

	@Override
	public void deleteAll() {
		eDataRepository.deleteAll();
	}
}
