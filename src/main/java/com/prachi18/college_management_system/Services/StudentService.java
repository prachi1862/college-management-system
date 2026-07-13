package com.prachi18.college_management_system.Services;

import com.prachi18.college_management_system.DTO.StudentRequestDTO;
import com.prachi18.college_management_system.DTO.StudentResponseDTO;
import com.prachi18.college_management_system.DTO.SubjectResponseDTO;
import com.prachi18.college_management_system.Entities.Department;
import com.prachi18.college_management_system.Entities.Student;
import com.prachi18.college_management_system.Entities.Subject;
import com.prachi18.college_management_system.Exceptions.ResourceNotFoundException;
import com.prachi18.college_management_system.Exceptions.StudentAlreadyEnrolledException;
import com.prachi18.college_management_system.Repositories.DepartmentRepository;
import com.prachi18.college_management_system.Repositories.StudentRepository;
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
public class StudentService {
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final SubjectRepository subjectRepository;
    private final ModelMapper modelMapper;

    public StudentResponseDTO mapToResponse(Student student){
        StudentResponseDTO dto= new StudentResponseDTO();

        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());

        if (student.getDepartment() != null) {
            dto.setDepartmentName(
                    student.getDepartment().getDepartmentName()
            );
        }
        return dto;
    }

    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO dto) {

//        Student student= modelMapper.map(dto, Student.class);
        Student student = new Student();
        log.info("Creating a student with name: {} {}", dto.getFirstName(), dto.getLastName());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(()->new ResourceNotFoundException("Department not found with id: " + dto.getDepartmentId()));
        student.setDepartment(department);
        Student savedStudent=studentRepository.save(student);
        log.info("Successfully saved student with id: {} ", savedStudent.getId());
       return mapToResponse(savedStudent);
    }

    @Transactional
    public StudentResponseDTO getStudentById(Long id) {
        log.info("Retrieving student with id: {} ", id);
        Student student=studentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Student not found with id: " + id));
        log.info("Successfully retrieved student with id: {} ", student.getId());
        return  mapToResponse(student);
    }

    @Transactional
    public List<StudentResponseDTO> getAllStudents(Pageable pageable) {
        log.info("Retrieving all students. Page number: {}, size:{} ", pageable.getPageNumber(), pageable.getPageSize());
        List<Student> students = studentRepository.findAll(pageable).getContent();
        log.info("Fetched {} students", students.size());
        return students.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public  StudentResponseDTO updateStudent(Long id, StudentRequestDTO dto) {
        log.info("Updating student with id: {} ", id);
        Student student=studentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Student not found with id: " + id));
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        Department department = departmentRepository.findById(dto.getDepartmentId()).orElseThrow();
        student.setDepartment(department);
        Student updatedStudent=studentRepository.save(student);
        log.info("Successfully updated student with id: {} ", updatedStudent.getId());
        return mapToResponse(updatedStudent);
    }

    @Transactional
    public void deleteStudentById(Long id) {
        log.info("Deleting student with id: {} ", id);
        Student student = studentRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Student not found with id: " + id));
        studentRepository.delete(student);
        log.info("Successfully deleted student with id: {} ", id);
    }

    @Transactional
    public void enrollStudentInSubject(Long studentId, Long subjectId) {
        log.info("Enrolling student with id: {} into subject {}", studentId, subjectId);
        Student student= studentRepository.findById(studentId)
                .orElseThrow(()->new ResourceNotFoundException("Student not found with id: " + studentId));
        Subject subject= subjectRepository.findById(subjectId)
                .orElseThrow(()->new ResourceNotFoundException("Subject not found with id: " + subjectId));
        if(student.getSubjects().contains(subject)) {
            log.warn("Student with id: {} has already enrolled in subject {}", studentId, subjectId);
            throw new StudentAlreadyEnrolledException("Student is already enrolled in this subject");
        }
        student.getSubjects().add(subject);
        log.info("Successfully enrolled student with id: {} ", student.getId());
        studentRepository.save(student);
    }

    @Transactional
    public List<SubjectResponseDTO> getStudentSubjects(Long studentId) {
        log.info("Fetching subjects for student id: {} ", studentId);
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

    @Transactional
    public List<StudentResponseDTO> findByFirstNameContainingIgnoreCase(String firstName){
        log.info("Finding student with first name: {} ", firstName);
         List<Student> students= studentRepository.findByFirstNameContainingIgnoreCase(firstName);

         if(students.isEmpty()){throw new ResourceNotFoundException("Student not found with name: " + firstName);}

         log.info("Found {} students with first name: {} ",students.size(), firstName);
         return students.stream()
                     .map(this::mapToResponse)
                     .toList();
    }

    @Transactional
    public List<StudentResponseDTO> findByDepartmentId(Long departmentId){
        log.info("Finding student with department id: {} ", departmentId);
        Department department= departmentRepository.findById(departmentId)
                .orElseThrow(()->new ResourceNotFoundException("Department not found with id: " + departmentId));
        List<Student> students= studentRepository.findByDepartmentId(departmentId);
        log.info("Found {} students for department id: {} ", students.size(), departmentId);
        return students.stream().map(this::mapToResponse).toList();
    }
}
