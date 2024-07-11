package com.example.demo.app.queryBuilder.model;

import static com.example.demo.utils.Schemas.information_schema;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name ="tables", schema =information_schema)
@Getter
@Setter
public class AllTablesModel {


	@Id
	@Column(name = "TABLE_NAME")
	private String TABLE_NAME;
	
	@Column(name = "TABLE_SCHEMA")
	private String OWNER;
	
}
