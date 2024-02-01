package com.lorram.grade.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.lorram.grade.entities.Student;
import com.lorram.grade.repositories.StudentRepository;

@Service
public class StudentService {
	
	@Autowired
	private StudentRepository repository;
	
	// TODO
	public Page<Student> findAll() {
		repository.findAll();
		return null;
	}

}
