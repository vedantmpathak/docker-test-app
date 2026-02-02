package com.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.dtos.AppointmentViewDTO;
import com.backend.dtos.DoctorDTO;
import com.backend.dtos.MedicalRecordDownloadDTO;
import com.backend.dtos.MedicalRecordResponseDTO;
import com.backend.dtos.MedicalRecordViewDTO;
import com.backend.dtos.NurseResponseDto;
import com.backend.dtos.PatientDTO;
import com.backend.entities.AppointmentEntity;
import com.backend.entities.MedicalRecordEntity;
import com.backend.service.AppointmentService;
import com.backend.service.MedicalRecordEmailService;
import com.backend.service.MedicalRecordService;
import com.backend.service.NurseService;
import com.backend.service.PatientService;

import jakarta.mail.MessagingException;


@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

	
	@Autowired
	private MedicalRecordService medicalRecordService;
	
	@Autowired
	private PatientService patientService;

	@Autowired
	private NurseService nurseService;
	
	@Autowired
	private AppointmentService appointmentService;
	
	@Autowired
	private MedicalRecordEmailService emailService;

	
	//Get All Patients
	@GetMapping("/patients")
	public List<PatientDTO> getAllPatients() {
	    return patientService.getAllPatients();
	}
	
	
	//Get All Doctors
	@GetMapping("/doctors")
	public List<DoctorDTO> getAllDoctors() {
	    return patientService.getAllDoctors();
	}
	
	//Get All Appointments
	@GetMapping("/appointments")
	public List<AppointmentViewDTO> getAllAppointments() {
	    return appointmentService.getAllAppointments();
	}
	
	//Get Appointment By Doctor & Patient
	@GetMapping("/appointments/by-patient-doctor")
    public ResponseEntity<Map<String, Long>> getAppointmentByPatientDoctor(
            @RequestParam Long patientId,
            @RequestParam Long doctorId
    ) {
        AppointmentEntity appointment =
                appointmentService.getAppointmentByPatientAndDoctor(
                        patientId, doctorId
                );

        return ResponseEntity.ok(
            Map.of("appointmentId", appointment.getId())
        );
    }

	
	//Get All Medical Records --> ADMIN
	@GetMapping("/medical-records")
	public ResponseEntity<List<MedicalRecordViewDTO>> getAllMedicalRecords() {
	    return ResponseEntity.ok(medicalRecordService.getAllMedicalRecords());
	}
	
	
	// DOWNLOAD MEDICAL RECORD
    @GetMapping("/medical-records/{recordId}/download")
    public ResponseEntity<byte[]> download(@PathVariable Long recordId) {

        MedicalRecordDownloadDTO dto =
                medicalRecordService.downloadMedicalRecord(recordId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + dto.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(dto.getFileData());
    }
	
    
	//Add Medical Records - Admin
	@PostMapping(value = "/add-medical-record", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> addMedicalRecord(@RequestParam Long appointmentId, @RequestParam String recordType, @RequestParam MultipartFile file){
		
	    try {
	    	System.out.println("appointmentId = " + appointmentId);
		    System.out.println("recordType = " + recordType);
		    System.out.println("file empty = " + file.isEmpty());
	    	
	        medicalRecordService.saveMedicalRecord(appointmentId, recordType, file);
	        return ResponseEntity.ok("Medical record added successfully");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Upload failed: " + e.getMessage());
	    }
	}
	
	
	//Search Medical Records by patient, doctor or title --> ADMIN
	@GetMapping("/search")
	public ResponseEntity<List<MedicalRecordEntity>> searchRecords(
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) String doctorName,
            @RequestParam(required = false) String recordType) {

        return ResponseEntity.ok(
                medicalRecordService.searchMedicalRecords(
                        patientName,
                        doctorName,
                        recordType
                )
        );
    }
	
	//Search Patient's - ALL (UI) --> ADMIN
	@GetMapping("/patients/name")
	public ResponseEntity<List<String>> getPatientNames(){
		return ResponseEntity.ok(patientService.getAllPatientNames());
	}
	
	
	//Get All Nurses - ADMIN (NURSES)
	@GetMapping("/nurses")
	public ResponseEntity<List<NurseResponseDto>> getAllNurses(){
		
		return ResponseEntity.ok(nurseService.getAllNurses());
	}
	
	
	//Send Email to Patient and Doctor - ADMIN (Medical Records)
	@PostMapping("/medical-records/{id}/send")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> sendRecord(@PathVariable Long id) throws MessagingException {
		
		emailService.sendRecordToPatientAndDoctor(id);
		
		return ResponseEntity.ok("Medical Record Emailed Successfully!");
		
	}
}
