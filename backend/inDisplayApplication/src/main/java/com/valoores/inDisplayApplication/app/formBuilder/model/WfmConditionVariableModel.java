package com.valoores.inDisplayApplication.app.formBuilder.model;
import static com.valoores.inDisplayApplication.utils.Schemas.WFMDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "WFM_CONDITION_VARIABLE", schema = WFMDBA)
@Getter
@Setter
@IdClass(WfmConditionVariablesPrimModel.class)
public class WfmConditionVariableModel {

	@Id
	@Column(name = "CONDITION_ID")
	private long conditionId;
	
	@Id
	@Column(name = "CONDITION_VAR_ORDER")
	private long conditionVarOrder;
	
	@Column(name = "CONDITION_VAR_TECHNICAL_NAME")
	private String conditionVarTechnicalName;
	
	@Column(name = "CONDITION_VAR_VALUE")
	private String conditionVarValue;
	
	@Column(name = "CREATED_BY")
	private long createdBy;
	
	@Column(name = "CREATION_DATE")
	private Date creationDate;
	
	@Column(name = "IS_COMPOUND_KEY")
	private String isCompoundKey;

	@Column(name = "UPDATED_BY")
	private long updatedBy;

	@Column(name = "UPDATE_DATE")
	private Date updateDate;
}







