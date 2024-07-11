package com.valoores.v21.usm.app.securitymanagement.usermanagement.userrole.dto;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class USMUserRoleDto {
	
	@Hidden
	private long empId;
	
	@Hidden
	private long roleId;
	
	private String isDefaultRole;
	
	private String roleName;
	
	private String creationDate;
	
}
