package com.valoores.v21.usm.app.securitymanagement.role.model;

import static com.valoores.v21.usm.utils.Schemas.USMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USM_ROLE_DEFINITION", schema = USMDBA)
@Getter
@Setter
public class USMRoleDefinition {
	@Id
	@Column(name = "ROLE_ID")
	private long id;	

	@Column(name = "ROLE_NAME")
	private String roleName;
	
	@Column(name="CREATED_BY")
	private String CREATED_BY;
}