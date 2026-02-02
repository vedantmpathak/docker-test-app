package com.backend.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordViewDTO {

    private Long medicalRecordId;
    private String recordType;
    private String fileName;

    private Long appointmentId;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String reason;
    private String appointmentStatus;

    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;
}

