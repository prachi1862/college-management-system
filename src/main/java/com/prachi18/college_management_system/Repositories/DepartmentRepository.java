package com.prachi18.college_management_system.Repositories;

import com.prachi18.college_management_system.Entities.Department;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Transactional
    @Modifying
    @Query("""
            SELECT d
            FROM Department d
            WHERE LOWER(d.departmentName)
            LIKE LOWER (CONCAT('%', :departmentName, '%'))
            """)
    List<Department> findByNameContainingIgnoreCase(@Param("departmentName") String departmentName);
}