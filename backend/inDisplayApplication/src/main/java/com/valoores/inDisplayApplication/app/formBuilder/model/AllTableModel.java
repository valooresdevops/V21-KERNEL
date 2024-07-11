package com.valoores.inDisplayApplication.app.formBuilder.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "tables", schema = "information_schema") 
@Immutable

public class AllTableModel {
	@Id
	@Column(name="table_schema", insertable = false, updatable = false)
	private String owner;

	@Column(name="TABLE_NAME", insertable = false, updatable = false)
	private String tableName;

	public String getOwner() {
		return owner;
	}
	
	public String getTableName() {
		return tableName;
	}


}
