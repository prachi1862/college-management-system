package com.prachi18.college_management_system.Repositories;

import com.prachi18.college_management_system.Entities.Subject;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    @Transactional
    @Modifying
    @Query("""
            SELECT s 
            FROM Subject s
            WHERE LOWER(s.subjectName)
            LIKE LOWER (CONCAT('%', :subjectName, '%'))
            """)
    List<Subject> findBySubjectNameContainingIgnoreCase(@Param("subjectName") String subjectName);

    @Transactional
    @Modifying
    @Query("""
            SELECT s
            FROM Subject s
            WHERE s.professor.id= :professorId
            """)
    List<Subject> findByProfessorId(@Param("professorId") Long professorId);
}