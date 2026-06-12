package com.prachi18.college_management_system.Services;

import com.prachi18.college_management_system.DTO.ProfessorRequestDTO;
import com.prachi18.college_management_system.DTO.ProfessorResponseDTO;
import com.prachi18.college_management_system.Entities.Department;
import com.prachi18.college_management_system.Entities.Professor;
import com.prachi18.college_management_system.Entities.Subject;
import com.prachi18.college_management_system.Exceptions.ResourceNotFoundException;
import com.prachi18.college_management_system.Repositories.DepartmentRepository;
import com.prachi18.college_management_system.Repositories.ProfessorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorService {
    private final ProfessorRepository professorRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    public ProfessorResponseDTO mapToResponseDTO(Professor professor){
        ProfessorResponseDTO dto= modelMapper.map(professor, ProfessorResponseDTO.class);
        if(professor.getDepartment()!=null){
            dto.setDeptName(professor.getDepartment().getDepartmentName());
        }
        dto.setSubjectCount(
                professor.getSubjects()==null?0:professor.getSubjects().size()
        );
        return dto;
    }

    @Transactional
    public ProfessorResponseDTO createProfessor(ProfessorRequestDTO dto) {
        Professor professor= modelMapper.map(dto, Professor.class);
        Department department= departmentRepository.findById(dto.getDeptId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        professor.setDepartment(department);
        Professor savedProfessor=professorRepository.save(professor);
        return mapToResponseDTO(savedProfessor);
    }

    @Transactional
    public ProfessorResponseDTO getProfessorById(Long id) {
        Professor professor= professorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Professor with id "+id+" not found"));
        return mapToResponseDTO(professor);
    }

    @Transactional
    public List<ProfessorResponseDTO> getAllProfessors(Pageable pageable) {
        List<Professor> professors= professorRepository.findAll(pageable).getContent();
        return professors.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Transactional
    public void deleteProfessorById(Long id) {
        Professor professor= professorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Professor with id "+id+" not found"));
        professorRepository.delete(professor);
    }

    @Transactional
    public List<ProfessorResponseDTO> findByProfNameContainingIgnoreCase(String profName) {
        List<Professor> professors= professorRepository.findByProfNameContainingIgnoreCase(profName);
        if(professors.isEmpty()){ throw new ResourceNotFoundException("Subject with name "+profName+" not found");}
        return professors.stream()
                .map(this::mapToResponseDTO)
                .toList();

    }
}
