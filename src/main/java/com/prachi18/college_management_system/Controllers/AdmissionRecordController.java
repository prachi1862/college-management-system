package com.prachi18.college_management_system.Controllers;

import com.prachi18.college_management_system.Entities.AdmissionRecord;
import com.prachi18.college_management_system.Entities.Professor;
import com.prachi18.college_management_system.Services.AdmissionRecordService;
import com.prachi18.college_management_system.Services.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/admissionRecord")
public class AdmissionRecordController {
    private final AdmissionRecordService admissionRecordService;

    @PostMapping
    public AdmissionRecord createAdmissionRecord(@RequestBody AdmissionRecord admissionRecord) {
        return admissionRecordService.createAdmissionRecord(admissionRecord);
    }

    @GetMapping("/{id}")
    public AdmissionRecord getAdmissionRecordById(@PathVariable Long id){
        return admissionRecordService.getAdmissionRecordById(id);
    }

    @GetMapping
    public List<AdmissionRecord> getAllAdmissionRecords(){
        return admissionRecordService.getAllAdmissionRecords();
    }

    @DeleteMapping("/{id}")
    public void deleteAdmissionRecordById(@PathVariable Long id){
        admissionRecordService.deleteAdmissionRecordById(id);
    }
}
