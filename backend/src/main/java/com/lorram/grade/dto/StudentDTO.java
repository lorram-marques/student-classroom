package com.lorram.grade.dto;

import java.io.Serializable;

import com.lorram.grade.entities.Student;

public class StudentDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String firstName;
	private String lastName;
	private Long enrollment;
	
	public StudentDTO() {
	}

	public StudentDTO(Long id, String firstName, String lastName, Long enrollment) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.enrollment = enrollment;
	}
	
	public StudentDTO(Student student) {
		id = student.getId();
		firstName = student.getFirstName();
		lastName = student.getLastName();
		enrollment = student.getEnrollment();
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
}
