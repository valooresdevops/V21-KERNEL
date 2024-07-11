package com.valoores.v21.usm.app.securitymanagement.role.adrolemapping.model;

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
@Table(name = "usm_suite_ldap_role_synchro", schema = USMDBA)
@Getter
@Setter
@NoArgsConstructor
public class USMAdRoleMapping {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADD_ROLE_MAPPING")
	@SequenceGenerator(name = "ADD_ROLE_MAPPING", schema = "USMDBA", sequenceName = "S_SUITE_LDAP_ROLE_SYNCHRO", allocationSize = 1)

	@Column(name = "SUITE_LDAP_ROLE_ID")
	private long id;

	@Column(name = "ROLE_ID")
	private long roleId;

	@Column(name = "LDAP_CONFIG_ID")
	private long ldapId;
	
	@Column(name = "LDAP_ROLE_LINKED_TO_TYPE")
	private long objectTypeName;

	@Column(name = "LDAP_ROLE_LINKED_TO_DESC")
	private String objectCN;

	@Column(name = "CREATION_DATE")
	@Transient
	private Date creationDate;
	
	@Column(name = "UPDATE_DATE")
	@Transient
	private Date updateDate;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	

}
