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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.lorram.grade.dto.ClassroomDTO;
import com.lorram.grade.entities.Classroom;
import com.lorram.grade.repositories.ClassroomRepository;
import com.lorram.grade.services.ClassroomService;
import com.lorram.grade.services.exceptions.DatabaseException;
import com.lorram.grade.services.exceptions.ResourceNotFoundException;
import com.lorram.grade.tests.Factory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ClassroomServiceTests {
	
	@InjectMocks
	private ClassroomService service;
	
	@Mock
	private ClassroomRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long dependantId;
	
	private Classroom classroom;
	private ClassroomDTO classroomDto;
	private PageImpl<Classroom> page;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		dependantId = 3L;
		classroom = Factory.createClassroom();
		classroomDto = Factory.createClassroomDTO();
		
		page = new PageImpl<>(List.of(classroom));
		
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(classroom));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.getReferenceById(existingId)).thenReturn(classroom);
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(classroom);
		
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependantId);
	}
	
	@Test
	public void findAllShouldReturnPage() {
		
		Pageable pageable = PageRequest.of(0, 12);
		
		Page<ClassroomDTO> page = service.findAll(pageable);
		
		Assertions.assertNotNull(page);
		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
	}
	
	@Test
	public void findByIdShouldReturnClassroomDTOWhenIdExists() {
		
		
		ClassroomDTO dto = service.findById(existingId);
		
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
	public void updateShouldReturnClassroomDTOWhenIdExists() {
		
		ClassroomDTO dto = service.update(classroomDto, existingId);
		
		Assertions.assertNotNull(dto);
		Mockito.verify(repository, Mockito.times(1)).getReferenceById(existingId);
		Mockito.verify(repository, Mockito.times(1)).save(classroom);
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(classroomDto, nonExistingId);
			
		});
		
		Mockito.verify(repository, Mockito.times(1)).getReferenceById(nonExistingId);
	}
	
	@Test
	public void insertShouldReturnClassroomDTO() {
		
		ClassroomDTO dto = service.insert(classroomDto);

		Assertions.assertNotNull(dto);
		Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any());
		
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
			
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependantId);
			
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependantId);
	}
}
