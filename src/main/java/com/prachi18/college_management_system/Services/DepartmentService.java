package com.prachi18.college_management_system.Services;

import com.prachi18.college_management_system.DTO.DepartmentRequestDTO;
import com.prachi18.college_management_system.DTO.DepartmentResponseDTO;
import com.prachi18.college_management_system.Entities.Department;
import com.prachi18.college_management_system.Exceptions.ResourceNotFoundException;
import com.prachi18.college_management_system.Repositories.DepartmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        Department savedDepartment=departmentRepository.save(modelMapper.map(dto,Department.class));
        return mapToResponse(savedDepartment);
    }

    @Transactional
    public DepartmentResponseDTO getDepartmentById(Long id){
        Department department= departmentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Department not found with id: " + id));
        return mapToResponse(department);
    }

    @Transactional
    public List<DepartmentResponseDTO> getAllDepartments(Pageable pageable) {
         List<Department> departments= departmentRepository.findAll(pageable).getContent();
         return departments.stream()
                 .map(this::mapToResponse)
                 .toList();
    }

    @Transactional
    public void deleteDepartmentById(Long id){
        Department department = departmentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Department not found with id: " + id));

        if(department.getStudents()!=null && !department.getStudents().isEmpty()){
              throw new IllegalStateException("Cannot delete department because students have not been deleted");
        }
        departmentRepository.delete(department);

    }

    @Transactional
    public DepartmentResponseDTO updateDepartment(Long id , DepartmentRequestDTO dto){
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Department not found with id: " + id));
        existingDepartment.setName(dto.getName());
        existingDepartment.setDeptCode(dto.getDeptCode());
        existingDepartment.setHodName(dto.getHodName());
        Department updatedDepartment= departmentRepository.save(existingDepartment);
        return mapToResponse(updatedDepartment);
    }

    @Transactional
    public DepartmentResponseDTO patchDepartment(Long id, DepartmentRequestDTO dto){
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Department not found with id: " + id));

        if(dto.getName() != null){
            existingDepartment.setName(dto.getName());
        }

        if(dto.getDeptCode() != null){
            existingDepartment.setDeptCode(dto.getDeptCode());
        }

        if(dto.getHodName() != null){
            existingDepartment.setHodName(dto.getHodName());
        }
        Department updateddepartment= departmentRepository.save(existingDepartment);
        return mapToResponse(updateddepartment);
    }

}
