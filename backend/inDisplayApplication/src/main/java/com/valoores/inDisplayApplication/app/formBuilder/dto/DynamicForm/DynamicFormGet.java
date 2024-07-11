package com.valoores.inDisplayApplication.app.formBuilder.dto.DynamicForm;

import java.util.List;

import com.valoores.inDisplayApplication.app.formBuilder.dto.FormBuilderCondition;

import lombok.Data;

@Data
public class DynamicFormGet {
	private String primaryColumn;
	private String selectedRowId;
	private long objectId;
	private String tableName;
	private String actionType;
//	private String isMain;
	private List<DynamicFormDto> dynamicTable;
    private List<FormBuilderCondition> whereConditions;

	
}
