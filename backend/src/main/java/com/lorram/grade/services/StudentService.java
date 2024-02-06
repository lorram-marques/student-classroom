package com.lorram.grade.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lorram.grade.dto.StudentDTO;
import com.lorram.grade.entities.Student;
import com.lorram.grade.repositories.ClassroomRepository;
import com.lorram.grade.repositories.StudentRepository;

@Service
public class StudentService {
	
	@Autowired
	private StudentRepository repository;
	
	@Autowired
	private ClassroomRepository classroomRepository;
	
	public Page<StudentDTO> findAll(Pageable pageable) {
		Page<Student> list = repository.findAll(pageable);
		return list.map(x -> new StudentDTO(x));
	}
	
	public StudentDTO findById(Long id) {
		Optional<Student> obj = repository.findById(id);
		Student student = obj.orElseThrow(() -> new RuntimeException("Entity not found")); //TODO ResourceNotFoundException
		return new StudentDTO(student);
	}
	
	public StudentDTO update(StudentDTO dto, Long id) {
		Student entity = repository.getReferenceById(id);
		fromDto(dto, entity);
		entity = repository.save(entity);
		return new StudentDTO(entity);
	}

	public StudentDTO insert(StudentDTO dto) {
		Student entity = new Student();
		fromDto(dto, entity);
		entity = repository.save(entity);
		return new StudentDTO(entity);
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	private void fromDto(StudentDTO dto, Student entity) {
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEnrollment(dto.getEnrollment());
		entity.setBirthdate(dto.getBirthdate());
		entity.setClassroom(classroomRepository.getReferenceById(dto.getClassroomId())); // TODO IntegrityConstraintViolationException
	}
}
