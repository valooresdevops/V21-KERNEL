package com.valoores.datacrowd.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.Hidden;

//@Getter
//@Setter
//@NoArgsConstructor
@Entity
//@Table(name="ref_sys13000",schema="SDEDBA")
@Table(name="TECH_REPORT_PARAMS",schema="TECHDBA")
public class TECH_REPORT_PARAMS {

	

	@Id
	@Hidden
	@Column(name = "ATTRIBUTE_ID")
	private long code;

	@NotBlank(message = "ATTRIBUTE_NAME cannot be empty!")
	@Column(name = "ATTRIBUTE_NAME")
	private String attribute_name;
	
	@NotBlank(message = "ATTRIBUTE_DESC cannot be empty!")
	@Column(name = "ATTRIBUTE_DESC")
	private String int_code;

}
