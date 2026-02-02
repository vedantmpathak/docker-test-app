package com.backend.service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import com.backend.controller.PatientController;
import com.backend.dtos.MedicalRecordDetailsDto;
import com.backend.dtos.MedicalRecordDownloadDTO;
import com.backend.dtos.MedicalRecordDto;
import com.backend.dtos.MedicalRecordResponseDTO;
import com.backend.dtos.MedicalRecordViewDTO;
import com.backend.entities.AppointmentEntity;
import com.backend.entities.DoctorEntity;
import com.backend.entities.MedicalRecordEntity;
import com.backend.repository.AppointmentRepository;
import com.backend.repository.MedicalRecordRepository;
import com.backend.repository.PrescriptionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {
	
	private final MedicalRecordRepository medicalRecordRepository;
	
	private final PrescriptionRepository prescriptionRepository;
	
	private final AppointmentRepository appointmentRepository;

	
	//Get All Medical Records --> ADMIN
	public List<MedicalRecordViewDTO> getAllMedicalRecords() {
		
		List<Object[]> rows = medicalRecordRepository.fetchMedicalRecordViewRaw();

        List<MedicalRecordViewDTO> result = new ArrayList<>();

        for (Object[] r : rows) {
            result.add(new MedicalRecordViewDTO(
                    ((Number) r[0]).longValue(),     // medicalRecordId
                    (String) r[1],                   // recordType
                    (String) r[2],                   // fileName

                    ((Number) r[3]).longValue(),     // appointmentId
                    ((Date) r[4]).toLocalDate(),     // appointmentDate
                    ((Time) r[5]).toLocalTime(),     // startTime
                    ((Time) r[6]).toLocalTime(),     // endTime
                    (String) r[7],                   // reason
                    (String) r[8],                   // appointmentStatus

                    ((Number) r[9]).longValue(),     // patientId
                    (String) r[10],                  // patientName

                    ((Number) r[11]).longValue(),    // doctorId
                    (String) r[12]                   // doctorName
            ));
        }

        return result;
	}

	

	@Override
	public List<MedicalRecordResponseDTO> searchRecordsByDoctorName(String name) {
		// TODO Auto-generated method stub
		return medicalRecordRepository.findRecordsByDoctorName(name)
						.stream()
						.map(mr -> new MedicalRecordResponseDTO(
								mr.getId(),
								mr.getRecordType(),
								mr.getFileName()
								))
						.toList();
	}

	@Override
	public Page<MedicalRecordDto> getMedicalRecordsByPrescription(Long prescriptionId, int page) {
		// TODO Auto-generated method stub
		
		Long appointmentId = prescriptionRepository
                .findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"))
                .getAppointment()
                .getId();

        // 2️⃣ Page size fixed to 2
        Pageable pageable = PageRequest.of(page, 2);

        // 3️⃣ Fetch records
        Page<MedicalRecordEntity> records =
                medicalRecordRepository.findByAppointmentId(appointmentId, pageable);

        // 4️⃣ Convert Entity → DTO
        return records.map(record ->
                new MedicalRecordDto(
                        record.getId(),
                        record.getRecordType(),
                        record.getFileName()
                )
        );
	}

	//Add medical record --> ADMIN
	@Override
	public void saveMedicalRecord(Long appointmentId, String recordType, MultipartFile file) {

	    if (appointmentId == null) {
	        throw new IllegalArgumentException("Appointment ID is required");
	    }

	    if (recordType == null || recordType.isBlank()) {
	        throw new IllegalArgumentException("Record type is required");
	    }

	    if (file == null || file.isEmpty()) {
	        throw new IllegalArgumentException("Uploaded file is empty");
	    }

	    AppointmentEntity appointmentEntity =
	            appointmentRepository.findById(appointmentId)
	                    .orElseThrow(() ->
	                            new RuntimeException("Appointment not found with id: " + appointmentId)
	                    );

	    try {
	        MedicalRecordEntity medicalRecordEntity = new MedicalRecordEntity();

	        medicalRecordEntity.setAppointment(appointmentEntity);
	        medicalRecordEntity.setRecordType(recordType.toUpperCase());
	        medicalRecordEntity.setFileName(file.getOriginalFilename());
	        medicalRecordEntity.setFileData(file.getBytes());

	        medicalRecordRepository.save(medicalRecordEntity);

	    } catch (Exception e) {
	        throw new RuntimeException("File upload failed", e);
	    }
	}

	
	//Search Medical Records by patient, doctor or title --> ADMIN
	@Override
	public List<MedicalRecordEntity> searchMedicalRecords(String patientName, String doctorName, String recordType) {
		// TODO Auto-generated method stub
				
		return medicalRecordRepository.searchRecords(patientName, doctorName, recordType);
	}

	//Search Patient's - ALL TYPES (UI) --> ADMIN
	@Override
	public List<String> getAllRecordTypes() {
		// TODO Auto-generated method stub
		return medicalRecordRepository.findAllRecordTypes();
	}

	//Get Particular Medical Records to be shown on UI after clicking --> ADMIN
	@Override
	public MedicalRecordDetailsDto getMedicalRecordDetails(Long id) {
		// TODO Auto-generated method stub
		return medicalRecordRepository.findRecordDetails(id)
									  .orElseThrow(() -> new RuntimeException("Medical Record not found with Id : " + id));
	}


	//Download Medical Record
	@Override
	public MedicalRecordDownloadDTO downloadMedicalRecord(Long recordId) {

        MedicalRecordEntity record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Medical record not found"));

        return new MedicalRecordDownloadDTO(
                record.getFileName(),
                record.getFileData()
        );
    }
	


}
