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
import com.lorram.grade.dto.ClassroomDTO;
import com.lorram.grade.tests.Factory;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ClassroomControllerTestsIT {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalClassrooms;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 100L;
		countTotalClassrooms = 3L;
	}
	
	@Test
	public void findAllShouldReturnSortedPageWhenSortByName() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/classrooms?page=0&size=12&sort=grade,asc")
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(countTotalClassrooms));
		result.andExpect(jsonPath("$.content").exists());		
		result.andExpect(jsonPath("$.content[0].grade").value(1));	
	}
	
	
	@Test
	public void updateShouldReturnClassroomDTOWhenIdExists() throws Exception {
		
		ClassroomDTO classroomDTO = Factory.createClassroomDTO();
		String jsonBody = objectMapper.writeValueAsString(classroomDTO);
		
		Integer expectedGrade = classroomDTO.getGrade();
		String expectedTeam = classroomDTO.getTeam();
		
		ResultActions result = 
				mockMvc.perform(put("/classrooms/{id}", existingId)
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.grade").value(expectedGrade));
		result.andExpect(jsonPath("$.team").value(expectedTeam));
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		
		ClassroomDTO classroomDTO = Factory.createClassroomDTO();
		String jsonBody = objectMapper.writeValueAsString(classroomDTO);
		
		ResultActions result = 
				mockMvc.perform(put("/classrooms/{id}", nonExistingId)
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
}
