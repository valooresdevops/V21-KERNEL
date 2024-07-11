package com.valoores.inDisplayApplication.app.formBuilder.dto;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class AllColumnsDto {
	@Hidden
	private String owner;
	private String tableName;
	private String columnName;
	private String nullable;
	private String dataType;
}
