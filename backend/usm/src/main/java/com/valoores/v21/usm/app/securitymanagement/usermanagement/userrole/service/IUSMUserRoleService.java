package com.valoores.v21.usm.app.securitymanagement.usermanagement.userrole.service;

import java.util.List;

import com.valoores.v21.usm.app.securitymanagement.usermanagement.userrole.dto.USMUserRoleDto;

public interface IUSMUserRoleService {
	
	public List<USMUserRoleDto> getAllUSMUsersRole(long empId);

	
}