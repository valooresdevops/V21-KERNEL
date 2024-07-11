package com.valoores.v21.usm.app.securitymanagement.usermanagement.dto;

import java.util.HashSet;
import java.util.Set;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class USMComboDto {
	
	@Hidden
	private Set<String> id = new HashSet<>();
}
