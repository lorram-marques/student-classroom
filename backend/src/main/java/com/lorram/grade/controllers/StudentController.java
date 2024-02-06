package com.lorram.grade.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lorram.grade.dto.StudentDTO;
import com.lorram.grade.services.StudentService;

@RestController
@RequestMapping(value = "/students")
public class StudentController {

	@Autowired
	private StudentService service;
	
	@GetMapping 
	public ResponseEntity<Page<StudentDTO>> findAll(Pageable pageable) {
		Page<StudentDTO> list = service.findAll(pageable);
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<StudentDTO> findById(@PathVariable Long id) {
		StudentDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@PutMapping(value = "{id}")
	public ResponseEntity<StudentDTO> update(@RequestBody StudentDTO dto, @PathVariable Long id) {
		StudentDTO newDto = service.update(dto, id);
		return ResponseEntity.ok().body(newDto);
	}
	
	@PostMapping
	public ResponseEntity<StudentDTO> insert(@RequestBody StudentDTO dto) {
		StudentDTO newDto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newDto.getId()).toUri();
		return ResponseEntity.created(uri).body(newDto);
	}
	
	@DeleteMapping(value = "{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
