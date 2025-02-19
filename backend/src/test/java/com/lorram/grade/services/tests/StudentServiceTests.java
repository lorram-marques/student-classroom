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
import com.lorram.grade.entities.Student;
import com.lorram.grade.repositories.StudentRepository;
import com.lorram.grade.services.StudentService;
import com.lorram.grade.tests.Factory;

@ExtendWith(SpringExtension.class)
public class StudentServiceTests {

	@InjectMocks
	private StudentService service;
	
	@Mock
	private StudentRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private Student student;
	private PageImpl<Student> page;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		student = Factory.createStudent();
		page = new PageImpl<>(List.of(student));
		
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(student));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
	}
	
	@Test
	public void findAllShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 12);
		
		Page<StudentDTO> result = service.findAll(pageable);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
		
	}
	
}
