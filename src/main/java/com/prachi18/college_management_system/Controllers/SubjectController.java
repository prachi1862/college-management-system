package com.prachi18.college_management_system.Controllers;

import com.prachi18.college_management_system.Advices.ApiResponse;
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
    public ApiResponse<SubjectResponseDTO> save(@Valid @RequestBody SubjectRequestDTO dto) {
        return new ApiResponse<>(subjectService.createSubject(dto));
    }

    @Operation(summary = "get subject by id")
    @GetMapping("/{id}")
    public ApiResponse<SubjectResponseDTO> getSubjectById(@PathVariable Long id){
        return new ApiResponse<>(subjectService.getSubjectById(id));
    }

    @Operation(summary = "get all subjects")
    @GetMapping
    public ApiResponse<List<SubjectResponseDTO>> getAllSubjects(@RequestParam(defaultValue = "id") String sortBy,
                                                   @RequestParam(defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE,  Sort.by(Sort.Direction.ASC, sortBy));
        return new ApiResponse<>(subjectService.getAllSubjects(pageable));
    }

    @Operation(summary = "delete subject by id")
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteSubjectById(@PathVariable Long id){
        subjectService.deleteSubjectById(id);
        return new ApiResponse<>("delete subject successfully");
    }

    @Operation(summary = "get all students enrolled in a subject")
    @GetMapping("/{id}/students")
    public ApiResponse<List<StudentResponseDTO>> getStudentsBySubjectId(@PathVariable Long id){
      return new ApiResponse<>(subjectService.getStudentsBySubjectId(id));
    }

    @Operation(summary = "get subject by subject name")
    @GetMapping("/subjectName")
    public ApiResponse<List<SubjectResponseDTO>> findBySubjectNameContainingIgnoreCase(@RequestParam String subjectName){
        return new ApiResponse<>(subjectService.findBySubjectNameContainingIgnoreCase(subjectName));
    }

    @Operation(summary = "find by professor id")
    @GetMapping("/professor/{professorId}")
    public ApiResponse<List<SubjectResponseDTO>> findByProfessorId(@PathVariable Long professorId){
        return new ApiResponse<>(subjectService.findByProfessorId(professorId));
    }
}
