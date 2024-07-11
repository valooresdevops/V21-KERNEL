package com.valoores.v21.usm.app.securitymanagement.role.model;

import static com.valoores.v21.usm.utils.Schemas.USMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USM_ROLE_SNCTION_LST_ENT_SRC", schema = USMDBA)
@Getter
@Setter
public class USMRoleSnctionLstEntSrc {
	@Id
	@Column(name = "ROLE_ID")
	private long id;	

	@Column(name = "ENTITY_SOURCE_ID")
	private String entitySourceId;
	
	@Column(name="CREATED_BY")
	private String CREATED_BY;

}