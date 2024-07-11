package com.example.demo.app.queryBuilder.model;

import static com.example.demo.utils.Schemas.information_schema;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name ="columns", schema =information_schema)
@Getter
@Setter
public class TableColumns {

	@Id
	@Column(name = "COLUMN_NAME")
	private String COLUMN_NAME;
	
	@Column(name = "table_schema")
	private String OWNER;
	
	@Column(name = "TABLE_NAME")
	private String TABLE_NAME;
	
}
