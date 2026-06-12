package com.prachi18.college_management_system.Controllers;

import com.prachi18.college_management_system.DTO.DepartmentRequestDTO;
import com.prachi18.college_management_system.DTO.DepartmentResponseDTO;
import com.prachi18.college_management_system.Entities.Department;
import com.prachi18.college_management_system.Services.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path= "/dept")
public class DepartmentController {

    private final DepartmentService departmentService;
    private static int PAGE_SIZE = 10;

    @Operation(summary = "create a new department")
    @PostMapping
    public DepartmentResponseDTO createDepartment(@Valid @RequestBody DepartmentRequestDTO dto) {
        return departmentService.createDepartment(dto);
    }

    @Operation(summary = "update department")
    @PutMapping("/{id}")
    public DepartmentResponseDTO updateDepartment(@PathVariable Long id,
                                       @Valid @RequestBody DepartmentRequestDTO dto) {
        return departmentService.updateDepartment(id, dto);
    }

    @Operation(summary = "delete department by id")
    @DeleteMapping("/{id}")
    public void deleteDepartmentById(@PathVariable Long id){
        departmentService.deleteDepartmentById(id);
    }

    @Operation(summary = "get department by id")
    @GetMapping("/{id}")
    public DepartmentResponseDTO getDepartmentById(@PathVariable Long id){
        return departmentService.getDepartmentById(id);
    }

    @Operation(summary = "get all departments")
    @GetMapping
    public List<DepartmentResponseDTO> getAllDepartments(@RequestParam(defaultValue ="id") String sortBy,
                                                         @RequestParam(defaultValue ="0") int page){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.ASC, sortBy));
        return departmentService.getAllDepartments(pageable);
    }

    @Operation(summary = "partially update department")
    @PatchMapping("/{id}")
    public DepartmentResponseDTO patchDepartment(@RequestBody DepartmentRequestDTO dto,
                                       @PathVariable Long id){
        return departmentService.patchDepartment(id,  dto);
    }

    @Operation(summary = "find by department name")
    @GetMapping("/departmentName")
    public List<DepartmentResponseDTO> searchDepartment(@RequestParam String departmentName){
        return departmentService.searchDepartmentByName(departmentName);
    }
}
