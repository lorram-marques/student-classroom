package com.lorram.grade.controllers.tests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lorram.grade.controllers.ClassroomController;
import com.lorram.grade.dto.ClassroomDTO;
import com.lorram.grade.services.ClassroomService;
import com.lorram.grade.services.exceptions.DatabaseException;
import com.lorram.grade.services.exceptions.ResourceNotFoundException;
import com.lorram.grade.tests.Factory;

@WebMvcTest(ClassroomController.class)
public class ClassroomControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ClassroomService service;
	
	@Autowired
	ObjectMapper objectMapper;
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private ClassroomDTO classroomDto;
	private PageImpl<ClassroomDTO> page;
	
	@BeforeEach
	void setUp() throws Exception {
		
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		classroomDto = Factory.createClassroomDTO();
		page = new PageImpl<>(List.of(classroomDto));
		
		Mockito.when(service.findAll(ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(service.findById(existingId)).thenReturn(classroomDto);
		Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		Mockito.when(service.update(ArgumentMatchers.any(), ArgumentMatchers.eq(existingId))).thenReturn(classroomDto);
		Mockito.when(service.update(ArgumentMatchers.any(), ArgumentMatchers.eq(nonExistingId))).thenThrow(ResourceNotFoundException.class);
		
		Mockito.when(service.insert(ArgumentMatchers.any())).thenReturn(classroomDto);
		
		Mockito.doNothing().when(service).delete(existingId);;
		Mockito.doThrow(DatabaseException.class).when(service).delete(dependentId);;
	}
	
	@Test
	public void findAllShouldReturnPage() throws Exception {
		ResultActions result = 
				mockMvc.perform(get("/classrooms")
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
	}
	
	@Test
	public void findByIdShouldReturnClassroomDTOWhenIdExists() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/classrooms/{id}", existingId)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.grade").exists());
		result.andExpect(jsonPath("$.team").exists());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/classrooms/{id}", nonExistingId)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void updateShouldReturnClassroomWhenIdExists() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(classroomDto);
		
		ResultActions result = mockMvc.perform(put("/classrooms/{id}", existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.grade").exists());
		result.andExpect(jsonPath("$.team").exists());
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(classroomDto);
		
		ResultActions result = mockMvc.perform(put("/classrooms/{id}", nonExistingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());

	}

	@Test
	public void insertShouldReturnClassroomDTO() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(classroomDto);
		
		ResultActions result = mockMvc.perform(post("/classrooms")
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.grade").exists());
		result.andExpect(jsonPath("$.team").exists());
	}

	@Test
	public void deleteShouldDoNothingWhenNonDependentId() throws Exception {
		
		
		ResultActions result = mockMvc.perform(delete("/classrooms/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNoContent());
	}
	
	@Test
	public void deleteShouldReturnBadRequestWhenDependentId() throws Exception {
		
		
		ResultActions result = mockMvc.perform(delete("/classrooms/{id}", dependentId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isBadRequest());
	}
	
	
	
	

}
