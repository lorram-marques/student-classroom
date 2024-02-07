package com.lorram.grade.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lorram.grade.dto.ClassroomDTO;
import com.lorram.grade.entities.Classroom;
import com.lorram.grade.repositories.ClassroomRepository;

@Service
public class ClassroomService {
	
	@Autowired
	private ClassroomRepository repository;
	
	public Page<ClassroomDTO> findAll(Pageable pageable) {
		Page<Classroom> list = repository.findAll(pageable);
		return list.map(x -> new ClassroomDTO(x));
	}
	
	public ClassroomDTO findById(Long id) {
		Optional<Classroom> obj = repository.findById(id);
		Classroom entity = obj.orElseThrow(() -> new RuntimeException()); // TODO ResourceNotFoundException
		return new ClassroomDTO(entity);
	}
	
	public ClassroomDTO update(ClassroomDTO dto, Long id) {
		Classroom entity = repository.getReferenceById(id);
		fromDto(dto, entity);
		entity = repository.save(entity);
		return new ClassroomDTO(entity);
	}
	
	public ClassroomDTO insert(ClassroomDTO dto) {
		Classroom entity = new Classroom();
		fromDto(dto, entity);
		entity = repository.save(entity);
		return new ClassroomDTO(entity);
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}

	private void fromDto(ClassroomDTO dto, Classroom entity) {
		entity.setGrade(dto.getGrade());
		entity.setTeam(dto.getTeam());
	}
}
