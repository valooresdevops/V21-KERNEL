package com.valoores.inDisplayApplication.app.formBuilder.dto;

import lombok.Data;

@Data
public class InsertCustomField {
	public String columnName;
	public String columnDescription;
	public long createdBy;
	public long objectId;
}
