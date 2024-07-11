package com.valoores.wfm.app.monitor.model;

import static com.valoores.wfm.utils.Schemas.SYS;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ALL_TRIGGERS", schema = SYS)
@Getter
@Setter

public class AllTriggersModel {
	@Id
	@Column(name = "OWNER")
	private long owner;
	
	@Column(name = "TRIGGER_NAME")
	private long triggerName;
	
	@Column(name = "TRIGGER_TYPE")
	private long triggerType;
		
	@Column(name = "TRIGGERING_EVENT")
	private Date triggeringEvent;
	
	@Column(name = "TABLE_OWNER")
	private byte[] tableOwner;
	
	@Column(name = "BASE_OBJECT_TYPE")
	private String baseObjectType;
	
	@Column(name = "TABLE_NAME")
	private String tableName;
	
	@Column(name = "COLUMN_NAME")
	private long columnName;
		
	@Column(name = "REFERENCING_NAMES")
	private Date referencingNames;
	
	@Column(name = "WHEN_CLAUSE")
	private long whenClause;
	
	@Column(name = "STATUS")
	private Date status;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "ACTION_TYPE")
	private long actionType;
		
	@Column(name = "TRIGGER_BODY")
	private String triggerBody;
	
	@Column(name = "CROSSEDITION")
	private String crossedition;
	
	@Column(name = "BEFORE_STATEMENT")
	private Date beforeStatement;
	
	@Column(name = "BEFORE_ROW")
	private long beforeRow;
	
	@Column(name = "AFTER_ROW")
	private Date afterRow;
	
	@Column(name = "AFTER_STATEMENT")
	private String afterStatement;
	
	@Column(name = "INSTEAD_OF_ROW")
	private long insteadOfRow;
		
	@Column(name = "FIRE_ONCE")
	private String fireOnce;
	
	@Column(name = "APPLY_SERVER_ONLY")
	private String applyServerOnly;
	

}
