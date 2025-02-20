package com.lorram.grade.services.tests;

import static org.mockito.Mockito.times;

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
import org.springframework.dao.DataIntegrityViolationException;
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
import com.lorram.grade.services.exceptions.DatabaseException;
import com.lorram.grade.services.exceptions.ResourceNotFoundException;
import com.lorram.grade.tests.Factory;

import jakarta.persistence.EntityNotFoundException;

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
	private long dependantId;
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
		
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		Mockito.when(classroomRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(student);
		
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependantId);
		
	}
	
	@Test
	public void findAllShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 12);
		
		Page<StudentDTO> result = service.findAll(pageable);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
		
	}
	
	@Test
	public void findByIdShouldReturnStudentDTOWhenIdExists() {
		
		StudentDTO dto = service.findById(existingId);
		
		Assertions.assertNotNull(dto);
		Mockito.verify(repository, Mockito.times(1)).findById(existingId);
		
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
	}
	
	@Test
	public void updateShouldReturnStudentDTOWhenIdExists() {
		
		StudentDTO dto = service.update(studentDto, existingId);
	
		Assertions.assertNotNull(dto);
		Mockito.verify(repository, Mockito.times(1)).getReferenceById(existingId);
		Mockito.verify(repository, Mockito.times(1)).save(student);
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(studentDto, nonExistingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).getReferenceById(nonExistingId);
	}
	
	@Test
	public void insertShouldReturnStudentDTO() {
		
		StudentDTO dto = service.insert(studentDto);
		
		Assertions.assertNotNull(dto);
		Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any());
	}
	
	@Test
	public void deleteShouldDoNothing() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		
		Mockito.verify(repository, times(1)).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependantId() {
		
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependantId);
		});
		
		Mockito.verify(repository, times(1)).deleteById(dependantId);
	}
}
