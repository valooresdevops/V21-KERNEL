package com.valoores.inDisplayApplication.app.formBuilder.dto.DynamicForm;

import java.util.List;

import lombok.Data;

@Data
public class DynamicFormDto {
	private String tableName;
	private String objectId;
	private long orderNo;
	private List<DynamicFormColsDto> columns;
}
