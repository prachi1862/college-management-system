package com.prachi18.college_management_system.Controllers;

import com.prachi18.college_management_system.DTO.StudentRequestDTO;
import com.prachi18.college_management_system.DTO.StudentResponseDTO;
import com.prachi18.college_management_system.DTO.SubjectResponseDTO;
import com.prachi18.college_management_system.Entities.Student;
import com.prachi18.college_management_system.Repositories.DepartmentRepository;
import com.prachi18.college_management_system.Repositories.StudentRepository;
import com.prachi18.college_management_system.Services.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/student")
public class StudentController {
    @Autowired
    StudentService studentService;

    @PostMapping
    public StudentResponseDTO addStudent(@Valid @RequestBody StudentRequestDTO dto){
        return studentService.createStudent(dto);
    }

    @GetMapping("/{id}")
    public StudentResponseDTO getStudentById(@PathVariable Long id){
        return studentService.getStudentById(id);
    }

    @GetMapping
    public List<StudentResponseDTO> getAllStudents(){
        return studentService.getAllStudents();
    }

    @DeleteMapping("/{id}")
    public void deleteStudentById(@PathVariable Long id){
        studentService.deleteStudentById(id);
    }

    @PostMapping("/{studentId}/subject/{subjectId}")
    public void enrollStudent(@PathVariable Long studentId , @PathVariable Long subjectId){
        studentService.enrollStudentInSubject(studentId, subjectId);
    }

    @GetMapping("/{studentId}/subjects")
    public List<SubjectResponseDTO> getStudentSubjects(@PathVariable Long studentId){
      return studentService.getStudentSubjects(studentId);
    }

    public StudentResponseDTO updateStudentById(@Valid @RequestBody StudentRequestDTO dto, @PathVariable Long id){
      return studentService.updateStudent(id, dto);
    }
}
