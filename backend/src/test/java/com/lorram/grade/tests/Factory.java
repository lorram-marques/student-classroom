package com.lorram.grade.tests;

import java.time.Instant;

import com.lorram.grade.dto.ClassroomDTO;
import com.lorram.grade.dto.StudentDTO;
import com.lorram.grade.entities.Classroom;
import com.lorram.grade.entities.Student;

public class Factory {
	
	public static Student createStudent() {
		Student student = new Student(1L, "Anderson", "Silva", 314511321L, Instant.parse("2007-08-23T10:30:00Z"));
		Classroom classroom = createClassroom();
		student.setClassroom(classroom);
		return student;
	}
	
	public static StudentDTO createStudentDTO() {
		Student student = createStudent();
		return new StudentDTO(student);
	}
	
	public static Classroom createClassroom() {
		Classroom classroom = new Classroom(1L, 1, "A");	
		return classroom;
	}
	
	public static ClassroomDTO createClassroomDTO() {
		Classroom classroom = createClassroom();	
		return new ClassroomDTO(classroom);
	}
}
