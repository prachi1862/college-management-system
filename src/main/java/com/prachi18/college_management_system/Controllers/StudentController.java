package com.prachi18.college_management_system.Controllers;

import com.prachi18.college_management_system.DTO.StudentRequestDTO;
import com.prachi18.college_management_system.DTO.StudentResponseDTO;
import com.prachi18.college_management_system.DTO.SubjectResponseDTO;
import com.prachi18.college_management_system.Entities.Student;
import com.prachi18.college_management_system.Repositories.DepartmentRepository;
import com.prachi18.college_management_system.Repositories.StudentRepository;
import com.prachi18.college_management_system.Services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/student")
public class StudentController {
    @Autowired
    StudentService studentService;
    private static final int PAGE_SIZE=5;

    @Operation(summary = "add a new student")
    @PostMapping
    public StudentResponseDTO addStudent(@Valid @RequestBody StudentRequestDTO dto){
        return studentService.createStudent(dto);
    }

    @Operation(summary = "get student by id")
    @GetMapping("/{id}")
    public StudentResponseDTO getStudentById(@PathVariable Long id){
        return studentService.getStudentById(id);
    }

    @Operation(summary = "get all students")
    @GetMapping
    public List<StudentResponseDTO> getAllStudents(@RequestParam(defaultValue = "id") String sortBy,
                                                   @RequestParam (defaultValue ="0") int page){
        Pageable pageable= PageRequest.of(page,PAGE_SIZE, Sort.by(Sort.Direction.ASC, sortBy));
        return studentService.getAllStudents(pageable);
    }

    @Operation(summary = "delete student by id")
    @DeleteMapping("/{id}")
    public void deleteStudentById(@PathVariable Long id){
        studentService.deleteStudentById(id);
    }

    @Operation(summary = "enroll student in a subject")
    @PostMapping("/{studentId}/subject/{subjectId}")
    public void enrollStudent(@PathVariable Long studentId , @PathVariable Long subjectId){
        studentService.enrollStudentInSubject(studentId, subjectId);
    }

    @Operation(summary = "get all subjects a student is enrolled in")
    @GetMapping("/{studentId}/subjects")
    public List<SubjectResponseDTO> getStudentSubjects(@PathVariable Long studentId){
      return studentService.getStudentSubjects(studentId);
    }

    @Operation(summary = "update student by id")
    @PutMapping("/{id}")
    public StudentResponseDTO updateStudentById(@Valid @RequestBody StudentRequestDTO dto, @PathVariable Long id){
      return studentService.updateStudent(id, dto);
    }
}
