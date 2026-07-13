package com.prachi18.college_management_system.Controllers;

import com.prachi18.college_management_system.Advices.ApiResponse;
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
    public ApiResponse<StudentResponseDTO> addStudent(@Valid @RequestBody StudentRequestDTO dto){
        return new ApiResponse<>(studentService.createStudent(dto));
    }

    @Operation(summary = "get student by id")
    @GetMapping("/{id}")
    public ApiResponse<StudentResponseDTO> getStudentById(@PathVariable Long id){
        return new ApiResponse<>(studentService.getStudentById(id));
    }

    @Operation(summary = "get all students")
    @GetMapping
    public ApiResponse<List<StudentResponseDTO>> getAllStudents(@RequestParam(defaultValue = "id") String sortBy,
                                                   @RequestParam (defaultValue ="0") int page){
        Pageable pageable= PageRequest.of(page,PAGE_SIZE, Sort.by(Sort.Direction.ASC, sortBy));
        return new ApiResponse<> (studentService.getAllStudents(pageable));
    }

    @Operation(summary = "delete student by id")
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteStudentById(@PathVariable Long id){
        studentService.deleteStudentById(id);
        return new ApiResponse<>("Deleted student by id successfully");
    }

    @Operation(summary = "enroll student in a subject")
    @PostMapping("/{studentId}/subject/{subjectId}")
    public ApiResponse<String > enrollStudent(@PathVariable Long studentId , @PathVariable Long subjectId){
        studentService.enrollStudentInSubject(studentId, subjectId);
        return new ApiResponse<>("Student enrolled successfully");
    }

    @Operation(summary = "get all subjects a student is enrolled in")
    @GetMapping("/{studentId}/subjects")
    public ApiResponse<List<SubjectResponseDTO>> getStudentSubjects(@PathVariable Long studentId){
      return new ApiResponse<> (studentService.getStudentSubjects(studentId));
    }

    @Operation(summary = "update student by id")
    @PutMapping("/{id}")
    public ApiResponse<StudentResponseDTO> updateStudentById(@Valid @RequestBody StudentRequestDTO dto, @PathVariable Long id){
      return new ApiResponse<> (studentService.updateStudent(id, dto));
    }

    @Operation(summary = "search students by first name")
    @GetMapping("/search")
    public ApiResponse<List<StudentResponseDTO>> findByFirstName(@RequestParam String firstName){
        return new ApiResponse<> (studentService.findByFirstNameContainingIgnoreCase(firstName));
    }

    @Operation(summary = "find student by department id")
    @GetMapping("/department/{departmentId}")
    public ApiResponse<List<StudentResponseDTO>> findByDepartmentId(@PathVariable Long departmentId){
        return new ApiResponse<>(studentService.findByDepartmentId(departmentId));
    }
}
