package com.prachi18.college_management_system.Repositories;

import com.prachi18.college_management_system.Entities.Professor;
import jakarta.transaction.Transactional;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@ReadingConverter
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    @Transactional
    @Modifying
    @Query("""
            SELECT p
            FROM Professor p
            WHERE LOWER(p.profName)
            LIKE LOWER (CONCAT('%', :profName, '%'))
            """)
    List<Professor> findByProfNameContainingIgnoreCase(@Param("profName") String profName);
}