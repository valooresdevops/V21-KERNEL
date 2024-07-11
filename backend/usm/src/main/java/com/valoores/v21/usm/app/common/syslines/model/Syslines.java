package com.valoores.v21.usm.app.common.syslines.model;

import static com.valoores.v21.usm.utils.Schemas.SDEDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;

@Entity
@IdClass(SyslinesIds.class)
@Table(name = "REF_SYS_LINES", schema = SDEDBA)
@Data
public class Syslines {

	@Id   
	@Hidden
	@JsonIgnore
	@Column(name = "HEA_CODE")
	private Integer heaCode;

	@Id
	@Column(name = "LIN_CODE")
	private Integer id;

	@Column(name = "LIN_NAME")
	private String name;

}
