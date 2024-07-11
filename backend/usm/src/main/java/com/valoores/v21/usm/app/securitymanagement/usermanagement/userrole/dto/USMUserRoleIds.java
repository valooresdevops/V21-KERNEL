package com.valoores.v21.usm.app.securitymanagement.usermanagement.userrole.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class USMUserRoleIds implements Serializable { 
	
    private long empId;
    private long roleId;

}