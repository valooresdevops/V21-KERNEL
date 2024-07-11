package com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.model;

import static com.valoores.v21.usm.utils.Schemas.USMDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usm_ldap_config", schema = USMDBA)
@Getter
@Setter
@NoArgsConstructor
public class LdapConfiguration {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USM_LDAP_GEN_ID")
	@SequenceGenerator(name = "USM_LDAP_GEN_ID", sequenceName = "USM_SEQ_LDAP_CONFIG_ID", allocationSize = 1)
	@Column(name = "LDAP_CONFIG_ID")
	private long ldapConfigId;

	@Column(name = "LDAP_SERVER_URL")
	private String ldapServerUrl;

	@Column(name = "LDAP_SERVER_PORT")
	private String ldapServerPort;

	@Column(name = "LDAP_ADMIN_LOGIN")
	private String ldapAdminLogin;

	@Column(name = "LDAP_ADMIN_PWD")
	private String ldapAdminPwd;

	@Column(name = "LDAP_SEARCH_BASE")
	private String ldapSearchBase;

	@Column(name = "CREATED_BY")
	private long createdBy;

	@Transient
	@Column(name = "CREATION_DATE")
	private Date creationDate;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	@Column(name = "IS_LDAP_CONFIG_ACTIVE")
	private char isLdapConfigActive;

	@Column(name = "LDAP_SEARCH_BASE_ROOT")
	private String ldapSearchBaseRoot;

	@Column(name = "LDAP_CONNECTION_TIMEOUT_PERIOD")
	private long ldapConnectionTimeoutPeriod;

	@Column(name = "LDAP_RESPONSE_TIMEOUT_PERIOD")
	private long ldapResponseTimeoutPeriod;

}
