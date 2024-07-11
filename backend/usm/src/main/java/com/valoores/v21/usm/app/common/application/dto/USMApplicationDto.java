package com.valoores.v21.usm.app.common.application.dto;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class USMApplicationDto {
	
	@Hidden
	private Integer id;
	private String name;


}
