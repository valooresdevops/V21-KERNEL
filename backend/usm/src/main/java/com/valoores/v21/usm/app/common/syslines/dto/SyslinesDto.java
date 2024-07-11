package com.valoores.v21.usm.app.common.syslines.dto;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SyslinesDto {

	@Hidden
	private long heaCode;

	@Hidden
	private long id;
	private String name;

}
