package com.prachi18.college_management_system.Services;

import com.prachi18.college_management_system.DTO.StudentResponseDTO;
import com.prachi18.college_management_system.DTO.SubjectRequestDTO;
import com.prachi18.college_management_system.DTO.SubjectResponseDTO;
import com.prachi18.college_management_system.Entities.Professor;
import com.prachi18.college_management_system.Entities.Student;
import com.prachi18.college_management_system.Entities.Subject;
import com.prachi18.college_management_system.Exceptions.ResourceNotFoundException;
import com.prachi18.college_management_system.Repositories.ProfessorRepository;
import com.prachi18.college_management_system.Repositories.StudentRepository;
import com.prachi18.college_management_system.Repositories.SubjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

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

    @Transactional
    public SubjectResponseDTO createSubject(SubjectRequestDTO dto) {
        Subject subject = modelMapper.map(dto, Subject.class);
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
    public List<SubjectResponseDTO> getAllSubjects() {
        List<Subject> subjects= subjectRepository.findAll();
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
        List<Student> students = subject.getStudents();
        return students.stream()
                .map(
                        student -> {
                            StudentResponseDTO dto= modelMapper.map(student, StudentResponseDTO.class);

                            if(student.getDepartment()!=null){
                                dto.setDepartmentName(student.getDepartment().getName());
                            }
                            return dto;
                        }
                )
                .toList();
    }
}
