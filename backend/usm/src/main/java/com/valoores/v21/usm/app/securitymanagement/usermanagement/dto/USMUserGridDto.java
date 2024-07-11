package com.valoores.v21.usm.app.securitymanagement.usermanagement.dto;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class USMUserGridDto {
	
	@Hidden
	private long id;
	private String username;
	private String fullName;
	private String isPwdLdapAuth;
	private String status;
	private String defaultRole;


}
