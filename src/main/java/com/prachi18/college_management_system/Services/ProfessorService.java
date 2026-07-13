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
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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
//        Professor professor= modelMapper.map(dto, Professor.class);
        log.info("Creating professor");
        Professor professor = new Professor();
        professor.setProfName(dto.getProfName());
        Department department= departmentRepository.findById(dto.getDeptId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        professor.setDepartment(department);
        Professor savedProfessor=professorRepository.save(professor);
        log.info("Saved professor");
        return mapToResponseDTO(savedProfessor);
    }

    @Transactional
    public ProfessorResponseDTO getProfessorById(Long id) {
        log.info("Fetching professor with id {}", id);
        Professor professor= professorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Professor with id "+id+" not found"));
        log.info("Professor with id {} found", id);
        return mapToResponseDTO(professor);
    }

    @Transactional
    public List<ProfessorResponseDTO> getAllProfessors(Pageable pageable) {
        log.info("Fetching professors. Page number: {}, size: {}",
                pageable.getPageNumber(),
                pageable.getPageSize());
        List<Professor> professors= professorRepository.findAll(pageable).getContent();
        log.info("Fetched {} professors", pageable.getPageSize());
        return professors.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Transactional
    public void deleteProfessorById(Long id) {
        log.info("Deleting professor with id {}", id);
        Professor professor= professorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Professor with id "+id+" not found"));
        professorRepository.delete(professor);
        log.info("Deleted professor");
    }

    @Transactional
    public List<ProfessorResponseDTO> findByProfNameContainingIgnoreCase(String profName) {
        log.info("Fetching professors by profName {}", profName);
        List<Professor> professors= professorRepository.findByProfNameContainingIgnoreCase(profName);
        if(professors.isEmpty()){ throw new ResourceNotFoundException("Professor with name "+profName+" not found");}
        log.info("Fetched {} professors", professors.size());
        return professors.stream()
                .map(this::mapToResponseDTO)
                .toList();

    }
}
