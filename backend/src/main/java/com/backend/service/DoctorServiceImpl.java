package com.backend.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.dialect.function.TruncFunction.DatetimeTrunc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.dtos.DoctorAvailabilityResponseDto;
import com.backend.dtos.DoctorResponseDto;
import com.backend.entities.DoctorAvailability;
import com.backend.entities.DoctorEntity;
import com.backend.entities.WeekDay;
import com.backend.repository.DoctorAvailabilityRepository;
import com.backend.repository.DoctorRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

	@Autowired
	private final DoctorRepository doctorRepository;
	
	@Autowired
	private final DoctorAvailabilityRepository doctorAvailabilityRepository;
	
	 @Override
	    public List<DoctorResponseDto> getAllDoctors() {
	        return doctorRepository.findAll()
	                .stream()
	                .map(this::mapToDto)
	                .toList();
	    }

	    private DoctorResponseDto mapToDto(DoctorEntity doctor) {
	        return new DoctorResponseDto(
	                doctor.getId(),
	                doctor.getUser().getName(),      
	                doctor.getSpecialization(),
	                doctor.getDepartment().getDeptName(),
	                doctor.getYearsOfExperience(),
	                doctor.getDateOfJoining()
	        );
	    }

		@Override
		public List<DoctorAvailabilityResponseDto> getAvailabilityForDates(Long doctor_id, LocalDate date) {
			
			DayOfWeek javaDay = date.getDayOfWeek();
			List<DoctorAvailabilityResponseDto> res = new ArrayList<>();
			WeekDay weekDay = WeekDay.valueOf(javaDay.name());
			
			List<DoctorAvailability> doctorAvailabilities= doctorAvailabilityRepository.findByDoctorIdAndDayOfWeekAndActiveTrue(doctor_id,weekDay);
			
			for(DoctorAvailability d: doctorAvailabilities) {
				DoctorAvailabilityResponseDto dto = new DoctorAvailabilityResponseDto();
				dto.setStartTime(d.getStartTime());
				dto.setEndTime(d.getEndTime());
				
				res.add(dto);
			}
			
			return res;
			
		}
}
