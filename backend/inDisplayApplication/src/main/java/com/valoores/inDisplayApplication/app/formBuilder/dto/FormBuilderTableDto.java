package com.valoores.inDisplayApplication.app.formBuilder.dto;

import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;

@Data
public class FormBuilderTableDto {
	@Hidden
	private String tableName;
	private String type;
	private String tableOwner;
	private List<FormBuilderDto> columns;
	private long orderNo;
	private String rowSlectedStatus;
	private long createdBy;
	private Date creationDate;
	private List<String> columnsId;
}
