package com.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NurseResponseDto {
	private Long id;
    private String name;
    private String email;
    private String phone;
    private String department;
    private String status;
}
