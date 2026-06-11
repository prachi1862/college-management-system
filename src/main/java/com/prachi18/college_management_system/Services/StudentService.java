package com.prachi18.college_management_system.Services;

import com.prachi18.college_management_system.DTO.StudentRequestDTO;
import com.prachi18.college_management_system.DTO.StudentResponseDTO;
import com.prachi18.college_management_system.DTO.SubjectResponseDTO;
import com.prachi18.college_management_system.Entities.Department;
import com.prachi18.college_management_system.Entities.Professor;
import com.prachi18.college_management_system.Entities.Student;
import com.prachi18.college_management_system.Entities.Subject;
import com.prachi18.college_management_system.Exceptions.ResourceNotFoundException;
import com.prachi18.college_management_system.Repositories.DepartmentRepository;
import com.prachi18.college_management_system.Repositories.ProfessorRepository;
import com.prachi18.college_management_system.Repositories.StudentRepository;
import com.prachi18.college_management_system.Repositories.SubjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final SubjectRepository subjectRepository;
    private final ModelMapper modelMapper;

    public StudentResponseDTO mapToResponse(Student student){
        StudentResponseDTO dto= modelMapper.map(student,StudentResponseDTO.class);
        if (student.getDepartment() != null) {
            dto.setDepartmentName(
                    student.getDepartment().getName()
            );
        }
        return dto;
    }

    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO dto) {

        Student student= modelMapper.map(dto, Student.class);
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(()->new ResourceNotFoundException("Department not found with id: " + dto.getDepartmentId()));
        student.setDepartment(department);
        Student savedStudent=studentRepository.save(student);
       return mapToResponse(savedStudent);
    }

    @Transactional
    public StudentResponseDTO getStudentById(Long id) {
        Student student=studentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Student not found with id: " + id));
        return  mapToResponse(student);
    }

    @Transactional
    public List<StudentResponseDTO> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public  StudentResponseDTO updateStudent(Long id, StudentRequestDTO dto) {
        Student student=studentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Student not found with id: " + id));
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        Department department = departmentRepository.findById(dto.getDepartmentId()).orElseThrow();
        student.setDepartment(department);
        Student updatedStudent=studentRepository.save(student);
        return mapToResponse(updatedStudent);
    }

    @Transactional
    public void deleteStudentById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow();
        studentRepository.delete(student);
    }

    @Transactional
    public void enrollStudentInSubject(Long studentId, Long subjectId) {
        Student student= studentRepository.findById(studentId)
                .orElseThrow(()->new ResourceNotFoundException("Student not found with id: " + studentId));
        Subject subject= subjectRepository.findById(subjectId)
                .orElseThrow(()->new ResourceNotFoundException("Subject not found with id: " + subjectId));

        student.getSubjects().add(subject);
        studentRepository.save(student);
    }

    @Transactional
    public List<SubjectResponseDTO> getStudentSubjects(Long studentId) {
        Student student= studentRepository.findById(studentId)
                .orElseThrow(()->new ResourceNotFoundException("Student not found with id: " + studentId));
        List<Subject> subjects= student.getSubjects();

        return subjects.stream()
                .map( subject->{
                    SubjectResponseDTO dto= modelMapper.map(subject,SubjectResponseDTO.class);
                    if (subject.getProfessor() != null) {
                        dto.setProfessorName(dto.getProfessorName());
                    }
                    dto.setStudentCount(
                            subject.getStudents()==null?0:subject.getStudents().size()
                    );
                    return dto;
                })
                .toList();
    }
}
