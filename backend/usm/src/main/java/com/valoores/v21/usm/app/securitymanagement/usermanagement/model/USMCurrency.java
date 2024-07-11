package com.valoores.v21.usm.app.securitymanagement.usermanagement.model;

import static com.valoores.v21.usm.utils.Schemas.SDEDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;

@Entity
@Table(name = "REF_COM_CURRENCY", schema = SDEDBA)
@Data
public class USMCurrency {

	@Id
	@Hidden
	@Column(name = "CUR_ID")
	private long id;

	@Column(name = "CUR_NAME")
	private String name;

}
