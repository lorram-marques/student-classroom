package com.lorram.grade.dto;

import java.io.Serializable;
import java.time.Instant;

import com.lorram.grade.entities.Classroom;
import com.lorram.grade.entities.Student;

public class StudentDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String firstName;
	private String lastName;
	private Long enrollment;
	private Instant birthdate;
	private Long classroomId;

	public StudentDTO() {
	}

	public StudentDTO(Long id, String firstName, String lastName, Long enrollment, Instant birthdate, Classroom classroom) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.enrollment = enrollment;
		this.birthdate = birthdate;
		this.setClassroomId(classroom.getId());
	}
	
	public StudentDTO(Student student) {
		id = student.getId();
		firstName = student.getFirstName();
		lastName = student.getLastName();
		enrollment = student.getEnrollment();
		birthdate = student.getBirthdate();
		classroomId = student.getClassroom().getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Long getEnrollment() {
		return enrollment;
	}

	public void setEnrollment(Long enrollment) {
		this.enrollment = enrollment;
	}
	
	public Instant getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Instant birthdate) {
		this.birthdate = birthdate;
	}

	public Long getClassroomId() {
		return classroomId;
	}

	public void setClassroomId(Long classroomId) {
		this.classroomId = classroomId;
	}
}
