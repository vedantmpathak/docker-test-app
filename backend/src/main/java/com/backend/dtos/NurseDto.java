package com.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NurseDto {
	private String name;
    private String email;
    private String phone;
    private Long departmentId;
    private boolean active;
}
