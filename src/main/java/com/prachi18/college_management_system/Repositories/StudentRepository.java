package com.prachi18.college_management_system.Repositories;

import com.prachi18.college_management_system.DTO.StudentResponseDTO;
import com.prachi18.college_management_system.Entities.Student;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Transactional
    @Modifying
    @Query("""
            SELECT s
            FROM Student s
            WHERE LOWER(s.firstName)
            LIKE LOWER (CONCAT('%', :firstName, '%'))
            """)
    List<Student> findByFirstNameContainingIgnoreCase(@Param("firstName") String firstName);

    List<Student> findByDepartmentId(Long departmentId);
}