package com.lorram.grade.services.tests;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.lorram.grade.dto.StudentDTO;
import com.lorram.grade.entities.Classroom;
import com.lorram.grade.entities.Student;
import com.lorram.grade.repositories.ClassroomRepository;
import com.lorram.grade.repositories.StudentRepository;
import com.lorram.grade.services.StudentService;
import com.lorram.grade.tests.Factory;

@ExtendWith(SpringExtension.class)
public class StudentServiceTests {

	@InjectMocks
	private StudentService service;
	
	@Mock
	private StudentRepository repository;
	
	@Mock
	private ClassroomRepository classroomRepository;
	
	private long existingId;
	private long nonExistingId;
	private Student student;
	private StudentDTO studentDto;
	private Classroom classroom;
	private PageImpl<Student> page;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		student = Factory.createStudent();
		studentDto = Factory.createStudentDTO();
		classroom = Factory.createClassroom();
		
		page = new PageImpl<>(List.of(student));
		
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(student));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.getReferenceById(existingId)).thenReturn(student);
		Mockito.when(classroomRepository.getReferenceById(existingId)).thenReturn(classroom);
		Mockito.when(repository.save(student)).thenReturn(student);
		
	}
	
	@Test
	public void findAllShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 12);
		
		Page<StudentDTO> result = service.findAll(pageable);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
		
	}
	
	@Test
	public void findByIdShouldReturnStudentDTO() {
		
		StudentDTO dto = service.findById(existingId);
		
		Assertions.assertNotNull(dto);
		Mockito.verify(repository, Mockito.times(1)).findById(existingId);
		
	}
	
	@Test
	public void updateShouldReturnStudentDTO() {
		
		StudentDTO dto = service.update(studentDto, existingId);
	
		
		Assertions.assertNotNull(dto);
		Mockito.verify(repository, Mockito.times(1)).getReferenceById(existingId);
		Mockito.verify(repository, Mockito.times(1)).save(student);
	}
	
	@Test
	public void insertShouldReturnStudentDTO() {
		
		//StudentDTO dto = service.insert(studentDto);
		
		//Assertions.assertNotNull(dto);
		//Mockito.verify(repository, Mockito.times(1)).save(student);
	}
}
