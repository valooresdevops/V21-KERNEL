package com.valoores.v21.usm.app.securitymanagement.usermanagement.userrole.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.valoores.v21.usm.app.securitymanagement.usermanagement.userrole.dto.USMUserRoleDto;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.userrole.model.USMUserRole;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.userrole.repository.IUSMUserRoleRepository;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.userrole.service.IUSMUserRoleService;
	
@Service
public class USMUserRoleServiceImpl implements IUSMUserRoleService {
	
	@Resource
	private IUSMUserRoleRepository usmUserRoleRepository;

	@Autowired private 
	javax.sql.DataSource dataSource;
	
	@Override
	public List<USMUserRoleDto> getAllUSMUsersRole(long empId) {
		Connection conn = DataSourceUtils.getConnection(dataSource);
		try {
		List<USMUserRoleDto> usmUserRoleDtos = new ArrayList<>();
		usmUserRoleRepository.findAllByEmpId(empId).forEach(usmUserRole -> {
			USMUserRoleDto usmUserRoleDto = mapEntityToDto(usmUserRole);
			usmUserRoleDtos.add(usmUserRoleDto);
		});	
		return usmUserRoleDtos;
		} finally {
	        DataSourceUtils.releaseConnection(conn, dataSource);
	        
	    }
	}
	
	private USMUserRoleDto mapEntityToDto(USMUserRole usmUserRole) {
		Connection conn = DataSourceUtils.getConnection(dataSource);
		try {
		USMUserRoleDto responseDto = new USMUserRoleDto();
		responseDto.setEmpId(usmUserRole.getEmpId());
		responseDto.setRoleId(usmUserRole .getRoleId());
		responseDto.setIsDefaultRole(usmUserRole .getIsDefaultRole());
		responseDto.setRoleName(usmUserRole.getUsmRole().getRoleName());
		responseDto.setCreationDate(usmUserRole.getCreationDate());
		return responseDto;
		} finally {
	        DataSourceUtils.releaseConnection(conn, dataSource);
	        
	    }
	}


}
