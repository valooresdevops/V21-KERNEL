package com.valoores.v21.usm.app.securitymanagement.role.adrolemapping.model;

import static com.valoores.v21.usm.utils.Schemas.USMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity

@Table(name = "usm_ldap_config", schema = USMDBA)
@Data
public class USMAdRoleMappingLdapConfig {

	@Id
	@Column(name = "LDAP_CONFIG_ID")
	private Integer configId;

	@Column(name = "LDAP_SERVER_URL")
	private String configUrl;

	@Column(name = "LDAP_SEARCH_BASE")
	private String configBasePath;
	
	@Column(name = "LDAP_SEARCH_BASE_ROOT")
	private String configBaseRootPath;

	public Integer getId() {
		return null;
	}

	public String getName() {
		return null;
	}
	
	
}
