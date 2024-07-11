package com.valoores.v21.usm.app.securitymanagement.role.model;

import lombok.Data;

@Data
public class USMSanction {
	private String columnName;
	private String columnValue;
	private long RoleId;
	private long entitySourecId;
	private String createdBy;

}