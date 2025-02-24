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
import com.lorram.grade.controllers.StudentController;
import com.lorram.grade.dto.StudentDTO;
import com.lorram.grade.services.StudentService;
import com.lorram.grade.services.exceptions.DatabaseException;
import com.lorram.grade.services.exceptions.ResourceNotFoundException;
import com.lorram.grade.tests.Factory;

@WebMvcTest(StudentController.class)
public class StudentControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private StudentService service;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private StudentDTO studentDto;
	private PageImpl<StudentDTO> page;
	
	@BeforeEach
	void setUp() throws Exception {
		
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		studentDto = Factory.createStudentDTO();
		page = new PageImpl<>(List.of(studentDto));
		
		Mockito.when(service.findAll(ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(service.findById(existingId)).thenReturn(studentDto);
		Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		Mockito.when(service.update(ArgumentMatchers.any(), ArgumentMatchers.eq(existingId))).thenReturn(studentDto);
		Mockito.when(service.update(ArgumentMatchers.any(), ArgumentMatchers.eq(nonExistingId))).thenThrow(ResourceNotFoundException.class);
		
		Mockito.when(service.insert(ArgumentMatchers.any())).thenReturn(studentDto);
		
		Mockito.doNothing().when(service).delete(existingId);;
		Mockito.doThrow(DatabaseException.class).when(service).delete(dependentId);;
	}
	
	@Test
	public void findAllShouldReturnPage() throws Exception {
		ResultActions result = 
				mockMvc.perform(get("/students")
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
	}
	
	@Test
	public void findByIdShouldReturnStudentDTOWhenIdExists() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/students/{id}", existingId)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.firstName").exists());
		result.andExpect(jsonPath("$.lastName").exists());
		result.andExpect(jsonPath("$.enrollment").exists());
		result.andExpect(jsonPath("$.birthdate").exists());
		result.andExpect(jsonPath("$.classroomId").exists());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/students/{id}", nonExistingId)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void updateShouldReturnStudentWhenIdExists() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(studentDto);
		
		ResultActions result = mockMvc.perform(put("/students/{id}", existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.firstName").exists());
		result.andExpect(jsonPath("$.lastName").exists());
		result.andExpect(jsonPath("$.enrollment").exists());
		result.andExpect(jsonPath("$.birthdate").exists());
		result.andExpect(jsonPath("$.classroomId").exists());
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(studentDto);
		
		ResultActions result = mockMvc.perform(put("/students/{id}", nonExistingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());

	}

	@Test
	public void insertShouldReturnStudentDTO() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(studentDto);
		
		ResultActions result = mockMvc.perform(post("/students")
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.firstName").exists());
		result.andExpect(jsonPath("$.lastName").exists());
		result.andExpect(jsonPath("$.enrollment").exists());
		result.andExpect(jsonPath("$.birthdate").exists());
		result.andExpect(jsonPath("$.classroomId").exists());
	}

	@Test
	public void deleteShouldDoNothingWhenNonDependentId() throws Exception {
		
		
		ResultActions result = mockMvc.perform(delete("/students/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNoContent());
	}
	
	@Test
	public void deleteShouldReturnBadRequestWhenDependentId() throws Exception {
		
		
		ResultActions result = mockMvc.perform(delete("/students/{id}", dependentId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isBadRequest());
	}
}
