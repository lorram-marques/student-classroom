package com.lorram.grade.dto;

import java.io.Serializable;

import com.lorram.grade.entities.Classroom;

public class ClassroomDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Integer grade;
	private String team;
	
	public ClassroomDTO() {
	}

	public ClassroomDTO(Long id, Integer grade, String team) {
		this.id = id;
		this.grade = grade;
		this.team = team;
	}
	
	public ClassroomDTO(Classroom classroom) {
		id = classroom.getId();
		grade = classroom.getGrade();
		team = classroom.getTeam();
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
}
