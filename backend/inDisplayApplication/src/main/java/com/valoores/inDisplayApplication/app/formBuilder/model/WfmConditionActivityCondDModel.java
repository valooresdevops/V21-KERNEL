package com.valoores.inDisplayApplication.app.formBuilder.model;
import static com.valoores.inDisplayApplication.utils.Schemas.WFMDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "WFM_BP_ACTIVITY_COND_D", schema = WFMDBA)
@Getter
@Setter
public class WfmConditionActivityCondDModel {

	@Id
	@Column(name = "CONDITION_ID")
	private long conditionId;
	
	@Column(name = "BP_CONDITION_ID")
	private long bpConditionId;
	
	@Column(name = "BOOLEAN_OPERATOR_CODE")
	private int booleanOperatorCode;

	@Column(name = "CREATED_BY")
	private long createdBy;
	
	@Column(name = "CREATION_DATE")
	private Date creationDate;
	
//	@Column(name = "UPDATED_BY")
//	private long updatedBy;
//
//	@Column(name = "UPDATE_DATE")
//	private Date updateDate;
}







