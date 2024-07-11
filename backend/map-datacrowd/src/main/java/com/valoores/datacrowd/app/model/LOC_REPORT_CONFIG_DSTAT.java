package com.valoores.datacrowd.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "LOC_REPORT_CONFIG_DSTAT ", schema = "LOCDBA")
public class LOC_REPORT_CONFIG_DSTAT {
	
	@Id
	@Hidden
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_REPORT_CONFIG_DSTAT")
	@SequenceGenerator(name = "S_REPORT_CONFIG_DSTAT", schema = "LOCDBA", sequenceName = "S_REPORT_CONFIG_DSTAT", allocationSize = 1)
	@Column(name="status_id")
	private Integer  statusId;

	@Column(name = "loc_report_config_id")
	private Integer simulationId;
	
	@Column(name = "status_bdate")
	private Date statusBDate;
	
	@Column(name = "status_code")
	private Integer statusCode;
	
	@Column(name = "comments")
	private String comments;
	
	@Column(name = "creation_date")
	private Date creationDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "update_date")
	private Date updateDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	

		
}
