package com.valoores.inDisplayApplication.app.formBuilder.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Table(name = "table_constraints", schema = "information_schema")
@Getter
public class AllConstraintsModel{

	@Column(name="CONSTRAINT_TYPE", insertable = false, updatable = false)
	private String constraintType;
	
	@Column(name="CONSTRAINT_NAME", insertable = false, updatable = false)
	private String constraintName;
	
	@Id
	@Column(name="CONSTRAINT_SCHEMA", insertable = false, updatable = false)
	private String owner;
	
}
