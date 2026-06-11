package com.prachi18.college_management_system.Controllers;

import com.prachi18.college_management_system.DTO.ProfessorRequestDTO;
import com.prachi18.college_management_system.DTO.ProfessorResponseDTO;
import com.prachi18.college_management_system.Entities.Professor;
import com.prachi18.college_management_system.Services.ProfessorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/prof")
public class ProfessorController {
    private final ProfessorService professorService;

    @PostMapping
    public ProfessorResponseDTO save(@Valid @RequestBody ProfessorRequestDTO dto) {
        return professorService.createProfessor(dto);
    }

    @GetMapping("/{id}")
    public ProfessorResponseDTO getProfessorById(@PathVariable Long id){
        return professorService.getProfessorById(id);
    }

    @GetMapping
    public List<ProfessorResponseDTO> getAllProfessors(){
        return professorService.getAllProfessors();
    }

    @DeleteMapping("/{id}")
    public void deleteProfessorById(@PathVariable Long id){
        professorService.deleteProfessorById(id);
    }
}
