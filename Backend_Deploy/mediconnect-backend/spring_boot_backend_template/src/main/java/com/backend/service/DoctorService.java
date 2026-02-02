package com.backend.service;

import java.time.LocalDate;
import java.util.List;

import com.backend.dtos.DoctorAvailabilityResponseDto;
import com.backend.dtos.DoctorResponseDto;
import com.backend.entities.DoctorAvailability;

public interface DoctorService {
	List<DoctorResponseDto> getAllDoctors();

	List<DoctorAvailabilityResponseDto> getAvailabilityForDates(Long doctor_id, LocalDate date);

}
