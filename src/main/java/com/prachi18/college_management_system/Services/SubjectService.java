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
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


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
        Subject subject = new Subject();
        subject.setSubjectName(dto.getSubjectName());
        Professor professor = professorRepository.findById(dto.getProfessorId())
                .orElseThrow(()-> new ResourceNotFoundException("Professor with id "+dto.getProfessorId()+" is not fpund"));
        subject.setProfessor(professor);
        Subject savedSubject = subjectRepository.save(subject);
        return mapToResponseDTO(savedSubject);
    }

    @Transactional
    public SubjectResponseDTO getSubjectById(Long id) {
        Subject subject= subjectRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Subject with id "+id+" not found"));
        return mapToResponseDTO(subject);
    }

    @Transactional
    public List<SubjectResponseDTO> getAllSubjects(Pageable pageable) {
        List<Subject> subjects= subjectRepository.findAll(pageable).getContent();
        return subjects.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Transactional
    public void deleteSubjectById(Long id) {
        Subject subject= subjectRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Subject with id "+id+" not found"));
        subjectRepository.delete(subject);
    }

    @Transactional
    public List<StudentResponseDTO> getStudentsBySubjectId(Long id) {
        Subject subject= subjectRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Subject with id "+id+" not found"));
        return subject.getStudents()
                .stream()
                .map(this::mapStudentToResponse)
                .toList();
    }

    @Transactional
    public List<SubjectResponseDTO> findBySubjectNameContainingIgnoreCase(String subjectName) {
        List<Subject> subjects= subjectRepository.findBySubjectNameContainingIgnoreCase(subjectName);
        if(subjects.isEmpty()){ throw new ResourceNotFoundException("Subject with name "+subjectName+" not found");}
        return subjects.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Transactional
    public List<SubjectResponseDTO> findByProfessorId(Long professorId){
        Professor professor= professorRepository.findById(professorId)
                .orElseThrow( ()->new ResourceNotFoundException("professor with id "+professorId+" not found"));

        List<Subject> subjects= subjectRepository.findByProfessorId(professorId);
        if(subjects.isEmpty()){ throw new  ResourceNotFoundException("Subject with professor id "+professorId+" not found");}
        return  subjects.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }
}
