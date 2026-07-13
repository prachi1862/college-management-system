package com.prachi18.college_management_system.Controllers;

import com.prachi18.college_management_system.DTO.ProfessorRequestDTO;
import com.prachi18.college_management_system.DTO.ProfessorResponseDTO;
import com.prachi18.college_management_system.Services.ProfessorService;
import io.swagger.v3.oas.annotations.Operation;
import com.prachi18.college_management_system.Advices.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/prof")
public class ProfessorController {
    private final ProfessorService professorService;
    private static int PAGE_SIZE = 10;

    @Operation(summary = "create a new professor")
    @PostMapping
    public ApiResponse<ProfessorResponseDTO> save(@Valid @RequestBody ProfessorRequestDTO dto) {
        return new ApiResponse<>(professorService.createProfessor(dto));
    }

    @Operation(summary = "get professor by id")
    @GetMapping("/{id}")
    public ApiResponse<ProfessorResponseDTO> getProfessorById(@PathVariable Long id){
        return new ApiResponse<>(professorService.getProfessorById(id));
    }

    @Operation(summary = "get all professors")
    @GetMapping
    public ApiResponse<List<ProfessorResponseDTO>> getAllProfessors(@RequestParam(defaultValue = "id") String sortBy,
                                                       @RequestParam(defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE,  Sort.by(Sort.Direction.ASC, sortBy));
        return new ApiResponse<>(professorService.getAllProfessors(pageable));
    }

    @Operation(summary = "delete professor by id")
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteProfessorById(@PathVariable Long id){
        professorService.deleteProfessorById(id);
        return new ApiResponse<>("delete professor successfully");
    }

    @Operation(summary = "find by professor name")
    @GetMapping("/profName")
    public ApiResponse<List<ProfessorResponseDTO>> findByProfName(@RequestParam String profName){
        return new ApiResponse<>(professorService.findByProfNameContainingIgnoreCase(profName));
    }
}
