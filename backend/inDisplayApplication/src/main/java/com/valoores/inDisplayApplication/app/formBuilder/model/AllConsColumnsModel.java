package com.valoores.inDisplayApplication.app.formBuilder.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Table(name = "KEY_COLUMN_USAGE", schema = "INFORMATION_SCHEMA")
@Getter
public class AllConsColumnsModel{

	@Column(name="TABLE_NAME", insertable = false, updatable = false)
	private String tableName;
	
	@Column(name="COLUMN_NAME", insertable = false, updatable = false)
	private String columnName;
	
	@Column(name="CONSTRAINT_NAME", insertable = false, updatable = false)
	private String constraintName;
	
	@Id
	@Column(name="TABLE_SCHEMA", insertable = false, updatable = false)
	private String owner;
	
}
