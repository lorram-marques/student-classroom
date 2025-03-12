package com.lorram.grade.services.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.lorram.grade.dto.ClassroomDTO;
import com.lorram.grade.repositories.ClassroomRepository;
import com.lorram.grade.services.ClassroomService;
import com.lorram.grade.services.exceptions.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class ClassroomServiceTestsIT {

	@Autowired
	private ClassroomService service;
	
	@Autowired
	private ClassroomRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalClassrooms;
	
	@BeforeEach
	void setUp() {
		
		existingId = 2L;
		nonExistingId = 100L;
		countTotalClassrooms = 3L;
		
	}
	
	@Test
	public void deleteShouldDeleteResourceWhenIdExists() {
		
		service.delete(existingId);
		
		Assertions.assertEquals(countTotalClassrooms - 1, repository.count());	
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
		
		Page<ClassroomDTO> page = service.findAll(pageRequest);
		
		Assertions.assertFalse(page.isEmpty());
		Assertions.assertEquals(0, page.getNumber());
		Assertions.assertEquals(10, page.getSize());
		Assertions.assertEquals(countTotalClassrooms, page.getTotalElements());
	}
	
	@Test
	public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {
		
		PageRequest pageRequest = PageRequest.of(50, 10);
		
		Page<ClassroomDTO> result = service.findAll(pageRequest);
		
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void findAllPagedShouldReturnSortedPageWhenSortByName() {
		
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("grade"));
		
		Page<ClassroomDTO> result = service.findAll(pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(1, result.getContent().get(0).getGrade());
		Assertions.assertEquals(2, result.getContent().get(1).getGrade());	
		Assertions.assertEquals(3, result.getContent().get(2).getGrade());	
	}
}
