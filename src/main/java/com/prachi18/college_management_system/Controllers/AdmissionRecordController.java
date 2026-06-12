package com.prachi18.college_management_system.Controllers;

import com.prachi18.college_management_system.Entities.AdmissionRecord;
import com.prachi18.college_management_system.Entities.Professor;
import com.prachi18.college_management_system.Services.AdmissionRecordService;
import com.prachi18.college_management_system.Services.ProfessorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/admissionRecord")
public class AdmissionRecordController {
    private final AdmissionRecordService admissionRecordService;
    private static int PAGE_SIZE = 10;
    @Operation(summary = "create a new admission record")
    @PostMapping
    public AdmissionRecord createAdmissionRecord(@RequestBody AdmissionRecord admissionRecord) {
        return admissionRecordService.createAdmissionRecord(admissionRecord);
    }

    @Operation(summary = "get admission record by id")
    @GetMapping("/{id}")
    public AdmissionRecord getAdmissionRecordById(@PathVariable Long id){
        return admissionRecordService.getAdmissionRecordById(id);
    }

    @Operation(summary = "get all admission records")
    @GetMapping
    public List<AdmissionRecord> getAllAdmissionRecords(@RequestParam(defaultValue = "id") String sortBy,
                                                        @RequestParam(defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE,  Sort.by(Sort.Direction.ASC, sortBy));
        return admissionRecordService.getAllAdmissionRecords(pageable);
    }

    @Operation(summary = "delete admission record by id")
    @DeleteMapping("/{id}")
    public void deleteAdmissionRecordById(@PathVariable Long id){
        admissionRecordService.deleteAdmissionRecordById(id);
    }
}
