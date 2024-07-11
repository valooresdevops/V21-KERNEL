package com.valoores.inDisplayApplication.app.logs.model;

import static com.valoores.inDisplayApplication.utils.Schemas.SSDX_ENG;

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
@Table(name = "V21_INDISPLAY_LOGS", schema = SSDX_ENG)
@Getter
@Setter
public class InDisplayLogsModel {

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="S_INDISPLAY_LOG_ID")
	@SequenceGenerator(name = "S_INDISPLAY_LOG_ID", schema = "SSDX_ENG", sequenceName = "S_INDISPLAY_LOG_ID", allocationSize = 1)
	@Column(name = "LOG_ID")
	private long logId;
	
	@Column(name = "TABLE_NAME")
	private String tableName;
	
	@Column(name = "ACTION_TYPE")
	private String actionType;
	
	@Column(name = "LOG_DATE")
	private Date logDate;
	
	@Column(name = "LOGGED_BY")
	private long loggedBy;
	
	@Column(name = "CHANGES")
	private String changes;
	
	@Column(name = "ACTION_TEXT")
	private String actionText;

	
}
