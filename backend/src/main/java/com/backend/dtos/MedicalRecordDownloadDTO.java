package com.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MedicalRecordDownloadDTO {
    private String fileName;
    private byte[] fileData;
}
