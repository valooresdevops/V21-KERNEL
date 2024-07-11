package com.valoores.datacrowd.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "LOC_REPORT_CONFIG_USER ", schema = "LOCDBA")
public class LOC_REPORT_CONFIG_USER {
	
	@Id
	@Hidden
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_REPORT_CONFIG_DSTAT")
	@Column(name="EMP_ID")
	private Integer  empid;

	@Column(name = "loc_report_config_id")
	private Integer simulationId;
	
	
	@Column(name = "creation_date")
	private Date creationDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "update_date")
	private Date updateDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
		
}
