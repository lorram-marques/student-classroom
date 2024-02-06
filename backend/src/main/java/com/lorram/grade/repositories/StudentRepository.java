package com.lorram.grade.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lorram.grade.entities.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{
	
}
