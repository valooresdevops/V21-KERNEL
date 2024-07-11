package com.valoores.datacrowd.app.model;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TECH_LOC_REPORT_CONFIG ", schema = "TECHDBA")
public class tech_loc_report_config {

	@Id
	@Hidden
	@Column(name = "LOC_REPORT_CONFIG_ID")
	private long simulationId;

	@Column(name = "LOC_REPORT_NAME")
	private String simulationName;
	
	@Column(name = "LOC_REPORT_TYPE")
	private String simulationType;
	
	
	@Column(name = "FILTER_BDATE")
	private Date filteredate;
	
	@Column(name = "FILTER_EDATE")
	private Date filterbdate;
	
	@Column(name = "FILTER_STD_TIME_ZONE_CODE")
	private String filterstd;
	
	@Column(name = "EXECUTION_DATE")
	private Date executionDate;
	
	@Column(name = "EXECUTED_BY")
	private String executedBy;
	
	@Column(name = "FILTER_DEVICES_ID")
	private String filterdevicesId;
	
	  
	@Column(name = "CREATION_DATE")
	private Date creationDate;
	
	@Column(name = "CREATED_BY")
	private int createdBy;

	
	@Column(name = "UPDATE_DATE")
	private Date UpdateDate;
	

	@Column(name = "UPDATED_BY")
	private int UpdatedBy;
	
	@Column(name = "P_ID")
	private int pid;
	
	@Column(name = "IS_DEVICE_REQUIRED")
	private int isDeviceRequired;

	@Column(name = "ID_DESC")
	private String iddesc;


}
