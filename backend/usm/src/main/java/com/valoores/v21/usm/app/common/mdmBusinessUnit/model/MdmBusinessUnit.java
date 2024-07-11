package com.valoores.v21.usm.app.common.mdmBusinessUnit.model;

import static com.valoores.v21.usm.utils.Schemas.MDMDBA;

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
@Table(name = "MDM_BUSINESS_UNIT", schema = MDMDBA)
@Getter
@Setter
@NoArgsConstructor
public class MdmBusinessUnit {
	
	@Id
	@Column(name = "BSN_UNIT_ID")
	private long bsnUnitId;
	
	@Column(name = "OPER_PARTY_ID")
	private long operPartyId;
	
	@Column(name = "BSN_GROUP_ID")
	private long bsnGroupId;
	
	@Column(name = "BSN_UNIT_TYPE_CODE")
	private long bsnUnitTypeCode;
	
	@Column(name = "BSN_UNIT_NAME")
	private String bsnUnitName;
	
	@Column(name = "ENT_CODE")
	private long entCode;
	
	@Column(name = "CREATION_DATE")
	@Hidden
	@JsonIgnore
	private Date creationDate;

	@Column(name = "CREATED_BY")
	@Hidden
	@JsonIgnore
	private String createdBy;

	@Column(name = "UPDATE_DATE")
	@Hidden
	@JsonIgnore
	private Date updateDate;

	@Column(name = "UPDATED_BY")
	@Hidden
	@JsonIgnore
	private String updatedBy;	
}