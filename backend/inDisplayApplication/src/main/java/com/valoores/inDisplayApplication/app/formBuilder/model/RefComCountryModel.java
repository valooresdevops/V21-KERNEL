package com.valoores.inDisplayApplication.app.formBuilder.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "REF_COM_COUNTRY", schema = "SDEDBA")
@Data
public class RefComCountryModel {
	@Id
	@Column(name="COU_ID")
	private String couId;
	
	@Column(name="ISO_COU_CODE_NUM")
	private String isoCouCodeNum;
	
	@Column(name="ISO_COU_CODE_ALPHA")
	private String isoCouCodeAlpha;
	
}
