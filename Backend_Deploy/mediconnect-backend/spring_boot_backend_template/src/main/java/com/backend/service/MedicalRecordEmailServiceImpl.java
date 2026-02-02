package com.backend.service;

import org.springframework.stereotype.Service;
import com.backend.controller.AppointmentController;
//import com.backend.entities.M
import com.backend.entities.AppointmentEntity;
import com.backend.entities.DoctorEntity;
import com.backend.entities.MedicalRecordEntity;
import com.backend.entities.PatientEntity;
import com.backend.entities.User;
import com.backend.repository.AppointmentRepository;
import com.backend.repository.DoctorRepository;
import com.backend.repository.MedicalRecordRepository;
import com.backend.repository.PatientRepository;
import com.backend.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class MedicalRecordEmailServiceImpl implements MedicalRecordEmailService  {

	private final MedicalRecordRepository medicalRecordRepository;
	
	private final AppointmentRepository appointmentRepository;
		
	private final UserRepository userRepository;
	
	private final PatientRepository patientRepository;

	private final DoctorRepository doctorRepository;
	
	private final EmailService emailService;
	
	
	@Override
	public void sendRecordToPatientAndDoctor(Long id) throws MessagingException  {
		// TODO Auto-generated method stub
	
		MedicalRecordEntity medicalRecord =
			    medicalRecordRepository.findById(id)
			        .orElseThrow(() -> new RuntimeException("Medical record not found"));

		
		AppointmentEntity appointmentEntity = 
				medicalRecord.getAppointment();
		
		
		// PATIENT -> users
        PatientEntity patient = 
        		appointmentEntity.getPatient();

        User patientUser = patient.getUser();

        // DOCTOR-> users
        DoctorEntity doctor = 
        		appointmentEntity.getDoctor();

        User doctorUser = doctor.getUser();
        
        System.out.println("Patient email = " + patientUser.getEmail());
        System.out.println("Doctor email  = " + doctorUser.getEmail());
		
		String subject = "Medical Record Available – " + medicalRecord.getFileName();

        String emailBody =
                "Hello,\n\n" +
                "A medical record has been shared.\n\n" +
                "Details:\n" +
                "Patient: " + patientUser.getName() + "\n" +
                "Doctor: " + doctorUser.getName() + "\n" +
                "Record Type: " + medicalRecord.getRecordType() + "\n" +
                "Status: COMPLETED\n\n" +
                "Please find the attached file.\n\n" +
                "Regards,\nHospital Admin";

        // Send to patient
        emailService.sendMedicalRecord(
                patientUser.getEmail(),
                subject,
                emailBody,
                medicalRecord.getFileData(),
                medicalRecord.getFileName()
        );

        // Send to doctor
        emailService.sendMedicalRecord(
                doctorUser.getEmail(),
                subject,
                emailBody,
                medicalRecord.getFileData(),
                medicalRecord.getFileName()
        );
        
        System.out.println("Emails sent successfully");
		
	}

}
