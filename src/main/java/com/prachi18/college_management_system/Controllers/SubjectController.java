package com.prachi18.college_management_system.Controllers;

import com.prachi18.college_management_system.DTO.StudentResponseDTO;
import com.prachi18.college_management_system.DTO.SubjectRequestDTO;
import com.prachi18.college_management_system.DTO.SubjectResponseDTO;
import com.prachi18.college_management_system.Services.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subject")
public class SubjectController {
    private final SubjectService subjectService;
    private static int PAGE_SIZE = 10;
    @Operation(summary = "save a new subject")
    @PostMapping
    public SubjectResponseDTO save(@Valid @RequestBody SubjectRequestDTO dto) {
        return subjectService.createSubject(dto);
    }

    @Operation(summary = "get subject by id")
    @GetMapping("/{id}")
    public SubjectResponseDTO getSubjectById(@PathVariable Long id){
        return subjectService.getSubjectById(id);
    }

    @Operation(summary = "get all subjects")
    @GetMapping
    public List<SubjectResponseDTO> getAllSubjects(@RequestParam(defaultValue = "id") String sortBy,
                                                   @RequestParam(defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE,  Sort.by(Sort.Direction.ASC, sortBy));
        return subjectService.getAllSubjects(pageable);
    }

    @Operation(summary = "delete subject by id")
    @DeleteMapping("/{id}")
    public void deleteSubjectById(@PathVariable Long id){
        subjectService.deleteSubjectById(id);
    }

    @Operation(summary = "get all students enrolled in a subject")
    @GetMapping("/{id}/students")
    public List<StudentResponseDTO> getStudentsBySubjectId(@PathVariable Long id){
      return subjectService.getStudentsBySubjectId(id);
    }
}
