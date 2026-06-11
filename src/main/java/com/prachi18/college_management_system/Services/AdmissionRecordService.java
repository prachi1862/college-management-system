package com.prachi18.college_management_system.Services;

import com.prachi18.college_management_system.Entities.AdmissionRecord;
import com.prachi18.college_management_system.Entities.Professor;
import com.prachi18.college_management_system.Repositories.AdmissionRecordRepository;
import com.prachi18.college_management_system.Repositories.ProfessorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdmissionRecordService {
    private final AdmissionRecordRepository admissionRecordRepository;

    @Transactional
    public AdmissionRecord createAdmissionRecord(AdmissionRecord admissionRecord) {
        return admissionRecordRepository.save(admissionRecord);
    }

    @Transactional
    public AdmissionRecord getAdmissionRecordById(Long id) {
        return admissionRecordRepository.findById(id).orElseThrow();
    }

    @Transactional
    public List<AdmissionRecord> getAllAdmissionRecords() {
        return admissionRecordRepository.findAll();
    }

    @Transactional
    public void deleteAdmissionRecordById(Long id) {
        admissionRecordRepository.deleteById(id);
    }
}
