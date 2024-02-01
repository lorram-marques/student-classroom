package com.lorram.grade.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lorram.grade.entities.Classroom;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long>{

}
