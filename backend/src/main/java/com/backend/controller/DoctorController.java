package com.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.backend.dtos.*;

import com.backend.service.DoctorService;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {
	
	@Autowired
	private final DoctorService doctorService;
	
	// http://localhost:8080/doctors
    @GetMapping
    public ResponseEntity<List<DoctorResponseDto>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }
    
    
    @PostMapping("/doctor_available")
    public ResponseEntity<?> getDoctorSlotDayWise(@RequestBody @Valid DoctorAvailableRequestDto request){
    	
    	
    	return ResponseEntity.ok(doctorService.getAvailabilityForDates(request.getDoctorId(),request.getDate()));
    			
    			
    	
    	
    }

}
