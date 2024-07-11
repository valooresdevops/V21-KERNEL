package com.valoores.inDisplayApplication.app.dynamicReport.model;


import static com.valoores.inDisplayApplication.utils.Schemas.SUITEDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CFG_REPORT_COLUMN_OUTPUT", schema = SUITEDBA)
@Getter
@Setter
@IdClass(CfgReportColumnOutputIdModel.class)
public class CfgReportColumnOutputModel {

	@Id
	@Column(name = "REPORT_DYNAMIC_CONFIG_ID")
	private long reportDynamicConfigId;
	
	@Id
	@Column(name = "COLUMN_ID")
	private long columnId;

	@Column(name = "CREATION_DATE")
	private Date creationDate;
	
	@Column(name = "CREATED_BY")
	private long createdBy;
	
	@Column(name = "UPDATE_DATE")
	private Date updateDate;
	
	@Column(name = "UPDATED_BY")
	private long updatedBy;
	
	@Column(name = "ORDER_NO")
	private long orderNO;
}


