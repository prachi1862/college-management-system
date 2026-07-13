package com.prachi18.college_management_system.Controllers;

import com.prachi18.college_management_system.Advices.ApiResponse;
import com.prachi18.college_management_system.DTO.DepartmentRequestDTO;
import com.prachi18.college_management_system.DTO.DepartmentResponseDTO;
import com.prachi18.college_management_system.Services.DepartmentService;
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
@RequestMapping(path= "/dept")
public class DepartmentController {

    private final DepartmentService departmentService;
    private static int PAGE_SIZE = 10;

    @Operation(summary = "create a new department")
    @PostMapping
    public ApiResponse<DepartmentResponseDTO> createDepartment(@Valid @RequestBody DepartmentRequestDTO dto) {
        return new ApiResponse<>(departmentService.createDepartment(dto));
    }

    @Operation(summary = "update department")
    @PutMapping("/{id}")
    public ApiResponse<DepartmentResponseDTO> updateDepartment(@PathVariable Long id,
                                       @Valid @RequestBody DepartmentRequestDTO dto) {
        return new ApiResponse<> (departmentService.updateDepartment(id, dto));
    }

    @Operation(summary = "delete department by id")
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteDepartmentById(@PathVariable Long id){
        departmentService.deleteDepartmentById(id);
        return new ApiResponse<>("deleted department by id successfully");
    }

    @Operation(summary = "get department by id")
    @GetMapping("/{id}")
    public ApiResponse<DepartmentResponseDTO> getDepartmentById(@PathVariable Long id){
        return new ApiResponse<>( departmentService.getDepartmentById(id));
    }

    @Operation(summary = "get all departments")
    @GetMapping
    public ApiResponse<List<DepartmentResponseDTO>> getAllDepartments(@RequestParam(defaultValue ="id") String sortBy,
                                                         @RequestParam(defaultValue ="0") int page){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.ASC, sortBy));
        return new ApiResponse<> (departmentService.getAllDepartments(pageable));
    }

    @Operation(summary = "partially update department")
    @PatchMapping("/{id}")
    public ApiResponse<DepartmentResponseDTO> patchDepartment(@RequestBody DepartmentRequestDTO dto,
                                       @PathVariable Long id){
        return new ApiResponse<> (departmentService.patchDepartment(id,  dto));
    }

    @Operation(summary = "find by department name")
    @GetMapping("/departmentName")
    public ApiResponse<List<DepartmentResponseDTO>> searchDepartment(@RequestParam String departmentName){
        return new ApiResponse<> (departmentService.searchDepartmentByName(departmentName));
    }
}
