package com.lorram.grade.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.lorram.grade.entities.Classroom;

public class ClassroomDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Integer grade;
	private String team;
	
	private Set<StudentDTO> students = new HashSet<>();

	public ClassroomDTO() {
	}

	public ClassroomDTO(Long id, Integer grade, String team, Set<StudentDTO> students) {
		this.id = id;
		this.grade = grade;
		this.team = team;
		this.students = students;
	}
	
	public ClassroomDTO(Classroom classroom) {
		id = classroom.getId();
		grade = classroom.getGrade();
		team = classroom.getTeam();
		students = classroom.getStudents().stream().map(x -> new StudentDTO(x)).collect(Collectors.toSet());
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public Set<StudentDTO> getStudents() {
		return students;
	}
}
