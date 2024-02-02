package com.lorram.grade.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lorram.grade.services.ClassroomService;

@RestController
@RequestMapping(value = "/classrooms")
public class ClassroomController {

	@Autowired
	private ClassroomService service;
	
	@GetMapping // TODO
	public ResponseEntity<?/*DTO*/> findAll() {
		service.findAll();
		return null;
	}
}
