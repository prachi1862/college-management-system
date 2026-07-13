package com.prachi18.college_management_system.Services;

import com.prachi18.college_management_system.DTO.DepartmentRequestDTO;
import com.prachi18.college_management_system.DTO.DepartmentResponseDTO;
import com.prachi18.college_management_system.Entities.Department;

import com.prachi18.college_management_system.Exceptions.ResourceNotFoundException;
import com.prachi18.college_management_system.Repositories.DepartmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentService {

    public final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    public DepartmentResponseDTO mapToResponse(Department department) {
        DepartmentResponseDTO dto=modelMapper.map(department,DepartmentResponseDTO.class);
        dto.setStudentCount(
                department.getStudents()==null?0:department.getStudents().size()
        );
        dto.setProfessorCount(
                department.getProfessors()==null?0:department.getProfessors().size()
        );
        return dto;
    }

    @Transactional
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO dto) {
        log.info("Creating Department");
        Department savedDepartment=departmentRepository.save(modelMapper.map(dto,Department.class));
        log.info("Saved Department");
        return mapToResponse(savedDepartment);
    }

    @Transactional
    public DepartmentResponseDTO getDepartmentById(Long id){
        log.info("Fetching department with ID: {}", id);
        Department department= departmentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Department not found with id: " + id));
        log.info("Department found with ID: {}", id);
        return mapToResponse(department);
    }

    @Transactional
    public List<DepartmentResponseDTO> getAllDepartments(Pageable pageable) {
        log.info("Fetching all Departments. page number: {}, page size {}", pageable.getPageNumber(), pageable.getPageSize());
         List<Department> departments= departmentRepository.findAll(pageable).getContent();
         log.info("Fetched {} departments", departments.size());
         return departments.stream()
                 .map(this::mapToResponse)
                 .toList();
    }

    @Transactional
    public void deleteDepartmentById(Long id){
        log.info("Deleting Department with ID: {}", id);
        Department department = departmentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Department not found with id: " + id));
        log.warn("Cannot delete department {} because it still has {} students",
                id, department.getStudents().size());
        if(department.getStudents()!=null && !department.getStudents().isEmpty()){
              throw new IllegalStateException("Cannot delete department because students have not been deleted");
        }
        departmentRepository.delete(department);
        log.info("Department deleted with ID: {}", id);

    }

    @Transactional
    public DepartmentResponseDTO updateDepartment(Long id , DepartmentRequestDTO dto){
        log.info("Updating Department with ID: {}", id);
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Department not found with id: " + id));
        existingDepartment.setDepartmentName(dto.getName());
        existingDepartment.setDeptCode(dto.getDeptCode());
        existingDepartment.setHodName(dto.getHodName());
        Department updatedDepartment= departmentRepository.save(existingDepartment);
        log.info("Updated Department with ID: {}", id);
        return mapToResponse(updatedDepartment);
    }

    @Transactional
    public DepartmentResponseDTO patchDepartment(Long id, DepartmentRequestDTO dto){
        log.info("Patching Department with ID: {}", id);
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Department not found with id: " + id));

        if(dto.getName() != null){
            existingDepartment.setDepartmentName(dto.getName());
        }

        if(dto.getDeptCode() != null){
            existingDepartment.setDeptCode(dto.getDeptCode());
        }

        if(dto.getHodName() != null){
            existingDepartment.setHodName(dto.getHodName());
        }
        Department updateddepartment= departmentRepository.save(existingDepartment);
        log.info("Successfully patched Department with ID: {}", id);
        return mapToResponse(updateddepartment);
    }

    @Transactional
    public List<DepartmentResponseDTO> searchDepartmentByName(String departmentName){
        log.info("Searching for Department with name: {}", departmentName);
        List<Department> departments= departmentRepository.findByNameContainingIgnoreCase(departmentName);

        if(departments.isEmpty()){throw new ResourceNotFoundException("Department not found with name: " + departmentName);}
        log.info("Found {} departments matching '{}'",
                departments.size(), departmentName);
        return departments.stream()
                .map(this::mapToResponse)
                .toList();
    }
}
