package com.valoores.v21.usm.app.common.country.model;

import static com.valoores.v21.usm.utils.Schemas.SDEDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "REF_COM_COUNTRY", schema = SDEDBA)
@Getter
@Setter
@NoArgsConstructor

public class RefComCountry {
	@Id
	@Column(name = "COU_ID")
	private long couId;
	
	@Column(name = "COU_NAME")
	private String couName;
	
	@Column(name = "ZCO_ID")
	private long zcoId;
	
	@Column(name = "GCO_ID")
	private long gcoId;
	
	@Column(name = "LEVEL1_DESC")
	private String level1Desc;
	
	@Column(name = "IS_LEVEL1")
	private String isLevel1;
	
	@Column(name = "LEVEL2_DESC")
	private String level2Desc;
	
	@Column(name = "IS_LEVEL2")
	private String isLevel2;
	
	@Column(name = "ISO_COU_CODE_NUM")
	private String isoCouCodeNum;
	
	@Column(name = "ISO_COU_CODE_ALPHA")
	private String isoCouCodeAlpha;
	
	@Hidden
	@JsonIgnore
	@Column(name = "CREATION_DATE")
	private Date creationDate;
	
	@Hidden
	@JsonIgnore
	@Column(name = "CREATED_BY")
	private long createdBy;
	
	@Hidden
	@JsonIgnore
	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	@Hidden
	@JsonIgnore
	@Column(name = "UPDATED_BY")
	private long updatedBy;
}
