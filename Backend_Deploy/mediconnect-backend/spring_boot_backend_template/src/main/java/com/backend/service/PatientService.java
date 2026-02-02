package com.backend.service;

import java.util.List;

import com.backend.dtos.DoctorDTO;
import com.backend.dtos.PatientDTO;

public interface PatientService {

	//Search Patient's - ALL --> ADMIN
	List<String> getAllPatientNames();

	//Get All Patients --> ADMIN
	List<PatientDTO> getAllPatients();

	//Get All Doctors --> ADMIN
	List<DoctorDTO> getAllDoctors();

}
