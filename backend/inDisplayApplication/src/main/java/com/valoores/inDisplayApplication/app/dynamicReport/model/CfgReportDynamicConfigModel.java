package com.valoores.inDisplayApplication.app.dynamicReport.model;


import static com.valoores.inDisplayApplication.utils.Schemas.SUITEDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Null;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cfg_report_dynamic_config", schema = SUITEDBA)
@Getter
@Setter
public class CfgReportDynamicConfigModel {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="S_REPORT_DYNAMIC_CONFIG")
	@SequenceGenerator(name = "S_REPORT_DYNAMIC_CONFIG", schema = "SUITEDBA", sequenceName = "S_REPORT_DYNAMIC_CONFIG", allocationSize = 1)
	@Column(name = "REPORT_DYNAMIC_CONFIG_ID")
	private long reportDynamicConfigId;
	
	@Column(name = "REPORT_NAME")
	private String reportName;
	
	@Null
	@Column(name = "REPORT_DYNAMIC_CONFIG_P_ID")
	private Long reportDynamicConfigPId;
		
	@Null
	@Column(name = "REPORT_INTERNAL_CODE")
	private String reportInternalCode;

	@Column(name = "EXECUTION_DATE")
	private Date executionDate;
	
	@Column(name = "CREATION_DATE")
	private Date creationDate;
	
	@Null
	@Column(name = "QUERY_DESC")
	private String queryDesc;
	
	@Column(name = "IS_SYSTEM_GENERATED")
	private String isSystemGenerated;
	
	@Column(name = "CREATED_BY")
	private long createdBy;
	
	@Null
	@Column(name = "BP_ID")
	private Long bpId;

	@Column(name = "EXECUTED_BY")
	private long executedBy;

	
}

