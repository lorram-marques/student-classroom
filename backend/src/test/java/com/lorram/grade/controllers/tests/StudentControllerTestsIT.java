package com.lorram.grade.controllers.tests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lorram.grade.dto.StudentDTO;
import com.lorram.grade.tests.Factory;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class StudentControllerTestsIT {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalStudents;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 100L;
		countTotalStudents = 3L;
	}
	
	@Test
	public void findAllShouldReturnSortedPageWhenSortByName() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/students?page=0&size=12&sort=firstName,asc")
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(countTotalStudents));
		result.andExpect(jsonPath("$.content").exists());		
		result.andExpect(jsonPath("$.content[0].firstName").value("Anderson"));	
	}
	
	
	@Test
	public void updateShouldReturnStudentDTOWhenIdExists() throws Exception {
		
		StudentDTO studentDTO = Factory.createStudentDTO();
		String jsonBody = objectMapper.writeValueAsString(studentDTO);
		
		String expectedFirstName = studentDTO.getFirstName();
		String expectedLastName = studentDTO.getLastName();
		
		ResultActions result = 
				mockMvc.perform(put("/students/{id}", existingId)
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.firstName").value(expectedFirstName));
		result.andExpect(jsonPath("$.lastName").value(expectedLastName));
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		
		StudentDTO studentDTO = Factory.createStudentDTO();
		String jsonBody = objectMapper.writeValueAsString(studentDTO);
		
		ResultActions result = 
				mockMvc.perform(put("/students/{id}", nonExistingId)
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
}
