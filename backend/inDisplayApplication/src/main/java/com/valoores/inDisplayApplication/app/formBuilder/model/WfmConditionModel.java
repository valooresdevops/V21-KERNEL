package com.valoores.inDisplayApplication.app.formBuilder.model;
import static com.valoores.inDisplayApplication.utils.Schemas.WFMDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "WFM_CONDITION", schema = WFMDBA)
@Getter
@Setter
public class WfmConditionModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="WFM_SEQ_CONDITION_ID")
	@SequenceGenerator(name = "WFM_SEQ_CONDITION_ID", schema = "WFMDBA", sequenceName = "WFM_SEQ_CONDITION_ID", allocationSize = 1)
	@Column(name = "CONDITION_ID")
	private long conditionId;
	
	@Column(name = "CONDITION_ELEM_DATA_ID")
	private long conditionElemDataId;
	
	@Column(name = "CONDITION_OPERATOR")
	private int conditionOperator;
	
	@Column(name = "CONDITION_VALUE")
	private String conditionValue;
	
	@Column(name = "CREATED_BY")
	private long createdBy;
	
	@Column(name = "CREATION_DATE")
	private Date creationDate;
	
	@Column(name = "ELEM_DATA_ID")
	private long elemDataId;
	
	@Column(name = "QBE_ID")
	private long qbeId;
	
	@Column(name = "STRUC_DATA_ID")
	private long strucDataId;
	
	@Column(name = "UPDATED_BY")
	private long updatedBy;

	@Column(name = "UPDATE_DATE")
	private Date updateDate;
}







