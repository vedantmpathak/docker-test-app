package com.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dtos.MedicalRecordDto;
import com.backend.service.MedicalRecordService;
import com.backend.service.PrescriptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/prescription")
@RequiredArgsConstructor
public class PrescriptionController {
	
	private final PrescriptionService prescriptionService;
	
	private final MedicalRecordService medicalRecordService;
	
	@GetMapping("/active-prescriptions/{patientId}")
	 public long getActivePrescriptions(@PathVariable Long patientId) {
		 
		 return prescriptionService.countActivePrescriptions(patientId);
	 }

	@GetMapping("/medical-records/{prescriptionId}")
	public Page<MedicalRecordDto> getMedicalRecords(
            @PathVariable Long prescriptionId,
            @RequestParam(defaultValue = "0") int page
    ) {
        return medicalRecordService
                .getMedicalRecordsByPrescription(prescriptionId, page);
    }
}
