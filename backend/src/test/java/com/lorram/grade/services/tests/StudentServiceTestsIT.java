package com.lorram.grade.services.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.lorram.grade.dto.StudentDTO;
import com.lorram.grade.repositories.StudentRepository;
import com.lorram.grade.services.StudentService;
import com.lorram.grade.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class StudentServiceTestsIT {

	@Autowired
	private StudentService service;
	
	@Autowired
	private StudentRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalStudents;
	
	@BeforeEach
	void setUp() {
		
		existingId = 1L;
		nonExistingId = 100L;
		countTotalStudents = 3L;
		
	}
	
	@Test
	public void deleteShouldDeleteResourceWhenIdExists() {
		
		service.delete(existingId);
		
		Assertions.assertEquals(countTotalStudents - 1, repository.count());	
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
	}
	
	@Test
	public void findAllShouldReturnPageWhenPage0Size10 () {
		
		PageRequest pageRequest = PageRequest.of(0, 10);
		
		Page<StudentDTO> page = service.findAll(pageRequest);
		
		Assertions.assertFalse(page.isEmpty());
		Assertions.assertEquals(0, page.getNumber());
		Assertions.assertEquals(10, page.getSize());
		Assertions.assertEquals(countTotalStudents, page.getTotalElements());
	}
	
	@Test
	public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {
		
		PageRequest pageRequest = PageRequest.of(50, 10);
		
		Page<StudentDTO> result = service.findAll(pageRequest);
		
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void findAllPagedShouldReturnSortedPageWhenSortByName() {
		
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("firstName"));
		
		Page<StudentDTO> result = service.findAll(pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Anderson", result.getContent().get(0).getFirstName());
		Assertions.assertEquals("Bruno", result.getContent().get(1).getFirstName());	
		Assertions.assertEquals("Carlos", result.getContent().get(2).getFirstName());	
	}
}
