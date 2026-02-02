package com.backend.dtos;

//import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class DoctorDTO {

    private Long id;           
    private String fullName;   
    private String speciality; // optional 
    
    public DoctorDTO(Long id, String name, String specialization) {
        this.id = id;
        this.fullName = name;
        this.speciality = specialization;
    }
}
