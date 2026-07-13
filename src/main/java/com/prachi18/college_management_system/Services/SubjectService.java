package com.prachi18.college_management_system.Services;

import com.prachi18.college_management_system.DTO.StudentResponseDTO;
import com.prachi18.college_management_system.DTO.SubjectRequestDTO;
import com.prachi18.college_management_system.DTO.SubjectResponseDTO;
import com.prachi18.college_management_system.Entities.Professor;
import com.prachi18.college_management_system.Entities.Student;
import com.prachi18.college_management_system.Entities.Subject;
import com.prachi18.college_management_system.Exceptions.ResourceNotFoundException;
import com.prachi18.college_management_system.Repositories.ProfessorRepository;
import com.prachi18.college_management_system.Repositories.SubjectRepository;
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
public class SubjectService {
    private final ModelMapper modelMapper;
    private final SubjectRepository subjectRepository;
    private final ProfessorRepository professorRepository;

    public SubjectResponseDTO mapToResponseDTO(Subject subject){
        SubjectResponseDTO dto= modelMapper.map(subject,SubjectResponseDTO.class);
        if(subject.getProfessor()!=null){
            dto.setProfessorName(subject.getProfessor().getProfName());
        }
        if(subject.getStudents()!=null){
            dto.setStudentCount(subject.getStudents().size());
        }
        return dto;
    }

    private StudentResponseDTO mapStudentToResponse(Student student){
        StudentResponseDTO dto = new StudentResponseDTO();

        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());

        if(student.getDepartment() != null){
            dto.setDepartmentName(
                    student.getDepartment().getDepartmentName()
            );
        }

        return dto;
    }

    @Transactional
    public SubjectResponseDTO createSubject(SubjectRequestDTO dto) {
//        Subject subject = modelMapper.map(dto, Subject.class);
        log.info("Creating new subject ");
        Subject subject = new Subject();
        subject.setSubjectName(dto.getSubjectName());
        Professor professor = professorRepository.findById(dto.getProfessorId())
                .orElseThrow(()-> new ResourceNotFoundException("Professor with id "+dto.getProfessorId()+" is not found"));
        subject.setProfessor(professor);
        Subject savedSubject = subjectRepository.save(subject);
        log.info("Subject created successfully");
        return mapToResponseDTO(savedSubject);
    }

    @Transactional
    public SubjectResponseDTO getSubjectById(Long id) {
        log.info("Fetching subject with id {}",id);
        Subject subject= subjectRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Subject with id "+id+" not found"));
        log.info("Subject found successfully");
        return mapToResponseDTO(subject);
    }

    @Transactional
    public List<SubjectResponseDTO> getAllSubjects(Pageable pageable) {
        log.info("Fetching all subjects. page number: {}, page size: {}",pageable.getPageNumber(), pageable.getPageSize());
        List<Subject> subjects= subjectRepository.findAll(pageable).getContent();
        log.info("Fetched {} subjects", subjects.size());
        return subjects.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Transactional
    public void deleteSubjectById(Long id) {
        log.info("Deleting subject with id {}",id);
        Subject subject= subjectRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Subject with id "+id+" not found"));
        subjectRepository.delete(subject);
        log.info("Subject deleted successfully");
    }

    @Transactional
    public List<StudentResponseDTO> getStudentsBySubjectId(Long id) {
        log.info("Fetching students with subject id {}",id);
        Subject subject= subjectRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Subject with id "+id+" not found"));
        log.info("Fetched {} students", subject.getStudents().size());
        return subject.getStudents()
                .stream()
                .map(this::mapStudentToResponse)
                .toList();
    }

    @Transactional
    public List<SubjectResponseDTO> findBySubjectNameContainingIgnoreCase(String subjectName) {
        log.info("Fetching subjects with subject name {}",subjectName);
        List<Subject> subjects= subjectRepository.findBySubjectNameContainingIgnoreCase(subjectName);
        if(subjects.isEmpty()){ throw new ResourceNotFoundException("Subject with name "+subjectName+" not found");}
        log.info("Fetched {} subjects", subjects.size());
        return subjects.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Transactional
    public List<SubjectResponseDTO> findByProfessorId(Long professorId){
        log.info("Fetching subjects with professor id {}",professorId);
        Professor professor= professorRepository.findById(professorId)
                .orElseThrow( ()->new ResourceNotFoundException("professor with id "+professorId+" not found"));

        List<Subject> subjects= subjectRepository.findByProfessorId(professorId);
        if(subjects.isEmpty()){ throw new  ResourceNotFoundException("Subject with professor id "+professorId+" not found");}
        log.info("Fetched {} subjects", subjects.size());
        return  subjects.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }
}
