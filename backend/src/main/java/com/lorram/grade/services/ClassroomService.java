package com.lorram.grade.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.lorram.grade.entities.Classroom;
import com.lorram.grade.repositories.ClassroomRepository;

@Service
public class ClassroomService {
	
	@Autowired
	private ClassroomRepository repository;
	
	// TODO
	public Page<Classroom> findAll() {
		repository.findAll();
		return null;
	}
}
