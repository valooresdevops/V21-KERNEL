package com.valoores.inDisplayApplication.app.formBuilder.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "dba_Tab_cols", schema = "sys")
@Data
public class DbaTabColsModel {
	@Id
	@Column(name="OWNER", insertable = false, updatable = false)
	private String ownerName;

	@Column(name="TABLE_NAME", insertable = false, updatable = false)
	private String tableName;

	@Column(name="COLUMN_NAME", insertable = false, updatable = false)
	private String columnName;
}
