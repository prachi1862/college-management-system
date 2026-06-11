package com.prachi18.college_management_system.Repositories;

import com.prachi18.college_management_system.Entities.Professor;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;
@ReadingConverter
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}