package com.valoores.v21.usm.app.securitymanagement.usermanagement.model;

import static com.valoores.v21.usm.utils.Schemas.SDEDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.dto.USMSyslinesIds;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;

@Entity
@IdClass(USMSyslinesIds.class)
@Table(name = "REF_SYS_PARAM_LINES", schema = SDEDBA)
@Data
public class USMRefSysParamLines {

	@Id
	@Hidden
	@JsonIgnore
	@Column(name = "HEA_CODE")
	private long heaCode;

	@Id
	@Column(name = "LIN_CODE")
	private long id;

	@Column(name = "LIN_NAME")
	private String name;

}
