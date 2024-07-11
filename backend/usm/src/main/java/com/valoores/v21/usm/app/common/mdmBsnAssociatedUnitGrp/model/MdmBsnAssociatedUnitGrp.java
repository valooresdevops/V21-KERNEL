package com.valoores.v21.usm.app.common.mdmBsnAssociatedUnitGrp.model;

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
@Table(name = "MDM_BSN_ASSOCIATED_UNIT_GRP", schema = MDMDBA)
@Getter
@Setter
@NoArgsConstructor
public class MdmBsnAssociatedUnitGrp {
	
	@Id
	@Column(name = "BSN_GROUP_FUNC_ID")
	private long bsnGroupFuncId;
	
	@Column(name = "BSN_GROUP_P_ID")
	private long bsnGroupPId;
	
	@Column(name = "BSN_GROUP_C_ID")
	private long bsnGroupCId;
	
	@Hidden
	@JsonIgnore
	@Column(name = "BSN_GROUP_BDATE")
	private Date bsnGroupBdate;
	
	@Hidden
	@JsonIgnore
	@Column(name = "BSN_GROUP_EDATE")
	private Date bsnGroupEdate;
	
	@Hidden
	@JsonIgnore
	@Column(name = "CREATION_DATE")
	private Date creationDate;
	
	@Hidden
	@JsonIgnore
	@Column(name = "UPDATE_DATE")
	private Date updateDate;
	
	@Hidden
	@JsonIgnore
	@Column(name = "CREATED_BY")
	private long createdBy;
	
	@Hidden
	@JsonIgnore
	@Column(name = "UPDATED_BY")
	private long updatedBy;
}
