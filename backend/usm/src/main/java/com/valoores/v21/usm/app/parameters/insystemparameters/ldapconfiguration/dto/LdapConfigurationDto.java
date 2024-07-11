package com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.dto;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LdapConfigurationDto {

	@Hidden
	private long ldapConfigId;
	private String ldapServerUrl;
	private String ldapServerPort;
	private String ldapAdminLogin;
	private String ldapAdminPwd;
	private String ldapSearchBase;
	private long createdBy;
	private char isLdapConfigActive;
	private String ldapSearchBaseRoot;
	private long ldapConnectionTimeoutPeriod;
	private long ldapResponseTimeoutPeriod;

}
