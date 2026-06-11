package com.prachi18.college_management_system.Controllers;

import com.prachi18.college_management_system.DTO.StudentResponseDTO;
import com.prachi18.college_management_system.DTO.SubjectRequestDTO;
import com.prachi18.college_management_system.DTO.SubjectResponseDTO;
import com.prachi18.college_management_system.Entities.Professor;
import com.prachi18.college_management_system.Entities.Subject;
import com.prachi18.college_management_system.Services.ProfessorService;
import com.prachi18.college_management_system.Services.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subject")
public class SubjectController {
    private final SubjectService subjectService;

    @PostMapping
    public SubjectResponseDTO save(@Valid @RequestBody SubjectRequestDTO dto) {
        return subjectService.createSubject(dto);
    }

    @GetMapping("/{id}")
    public SubjectResponseDTO getSubjectById(@PathVariable Long id){
        return subjectService.getSubjectById(id);
    }

    @GetMapping
    public List<SubjectResponseDTO> getAllSubjects(){
        return subjectService.getAllSubjects();
    }

    @DeleteMapping("/{id}")
    public void deleteSubjectById(@PathVariable Long id){
        subjectService.deleteSubjectById(id);
    }

    @GetMapping("/{id}/students")
    public List<StudentResponseDTO> getStudentsBySubjectId(@PathVariable Long id){
      return subjectService.getStudentsBySubjectId(id);
    }
}
