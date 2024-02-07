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

import com.lorram.grade.dto.ClassroomDTO;
import com.lorram.grade.services.ClassroomService;

@RestController
@RequestMapping(value = "/classrooms")
public class ClassroomController {

	@Autowired
	private ClassroomService service;
	
	@GetMapping 
	public ResponseEntity<Page<ClassroomDTO>> findAll(Pageable pageable) {
		Page<ClassroomDTO> list = service.findAll(pageable);
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ClassroomDTO> findById(@PathVariable Long id) {
		ClassroomDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<ClassroomDTO> update(@RequestBody ClassroomDTO dto, @PathVariable Long id) {
		ClassroomDTO newDto = service.update(dto, id);
		return ResponseEntity.ok().body(newDto);
	}
	
	@PostMapping
	public ResponseEntity<ClassroomDTO> insert(@RequestBody ClassroomDTO dto) {
		ClassroomDTO newDto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newDto.getId()).toUri();
		return ResponseEntity.created(uri).body(newDto);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
