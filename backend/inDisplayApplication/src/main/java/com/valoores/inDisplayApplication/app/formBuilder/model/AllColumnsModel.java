package com.valoores.inDisplayApplication.app.formBuilder.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Table(name = "columns", schema = "information_schema")
@Getter
@IdClass(AllColumnsModelId.class)
public class AllColumnsModel  implements Serializable{

	@Id
	@Column(name="table_schema", insertable = false, updatable = false)
	private String owner;

	@Id
	@Column(name="TABLE_NAME", insertable = false, updatable = false)
	private String tableName;
	
	@Id
	@Column(name="column_name", insertable = false, updatable = false)
	private String columnName;
	
	@Column(name="data_type", insertable = false, updatable = false)
	private String dataType;
	
	@Column(name="is_nullable", insertable = false, updatable = false)
	private String nullable;
}
