package com.valoores.v21.usm.app.securitymanagement.usermanagement.model;

import static com.valoores.v21.usm.utils.Schemas.USMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USM_ROLE_MISC_INFO", schema = USMDBA)
@Getter
@Setter
public class USMUserRoles {
	@Id
	@Column(name = "ROLE_ID")
	private long id;

	@Column(name = "ROLE_NAME")
	private String name;
	
	
}