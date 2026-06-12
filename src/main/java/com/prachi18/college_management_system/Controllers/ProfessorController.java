package com.prachi18.college_management_system.Controllers;

import com.prachi18.college_management_system.DTO.ProfessorRequestDTO;
import com.prachi18.college_management_system.DTO.ProfessorResponseDTO;
import com.prachi18.college_management_system.Entities.Professor;
import com.prachi18.college_management_system.Services.ProfessorService;
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
@RequestMapping(path="/prof")
public class ProfessorController {
    private final ProfessorService professorService;
    private static int PAGE_SIZE = 10;

    @Operation(summary = "create a new professor")
    @PostMapping
    public ProfessorResponseDTO save(@Valid @RequestBody ProfessorRequestDTO dto) {
        return professorService.createProfessor(dto);
    }

    @Operation(summary = "get professor by id")
    @GetMapping("/{id}")
    public ProfessorResponseDTO getProfessorById(@PathVariable Long id){
        return professorService.getProfessorById(id);
    }

    @Operation(summary = "get all professors")
    @GetMapping
    public List<ProfessorResponseDTO> getAllProfessors(@RequestParam(defaultValue = "id") String sortBy,
                                                       @RequestParam(defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE,  Sort.by(Sort.Direction.ASC, sortBy));
        return professorService.getAllProfessors(pageable);
    }

    @Operation(summary = "delete professor by id")
    @DeleteMapping("/{id}")
    public void deleteProfessorById(@PathVariable Long id){
        professorService.deleteProfessorById(id);
    }
}
