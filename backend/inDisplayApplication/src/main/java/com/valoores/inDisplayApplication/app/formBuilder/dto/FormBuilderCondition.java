package com.valoores.inDisplayApplication.app.formBuilder.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormBuilderCondition {
	
	private String tableName;
	private String columnName;
	private String condition;
	private String value;
	
}
