package com.prachi18.college_management_system.Controllers;

import com.prachi18.college_management_system.DTO.ProfessorRequestDTO;
import com.prachi18.college_management_system.DTO.ProfessorResponseDTO;
import com.prachi18.college_management_system.Entities.Professor;
import com.prachi18.college_management_system.Services.ProfessorService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/prof")
public class ProfessorController {
    private final ProfessorService professorService;

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
    public List<ProfessorResponseDTO> getAllProfessors(){
        return professorService.getAllProfessors();
    }

    @Operation(summary = "delete professor by id")
    @DeleteMapping("/{id}")
    public void deleteProfessorById(@PathVariable Long id){
        professorService.deleteProfessorById(id);
    }
}
