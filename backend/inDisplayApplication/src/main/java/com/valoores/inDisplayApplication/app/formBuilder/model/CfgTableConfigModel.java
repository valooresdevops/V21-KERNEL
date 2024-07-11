package com.valoores.inDisplayApplication.app.formBuilder.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
@Entity
@Table(name = "CFG_TABLE_CONFIG", schema = "SUITEDBA")
@Data
public class CfgTableConfigModel {
	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_table_config")
    @SequenceGenerator(name = "s_table_config", schema = "SUITEDBA", sequenceName = "s_table_config", allocationSize = 1)
	@Id
	@Column(name="table_id")
	private long tableId;

	@Column(name="table_name")
	private String tableName;
	
	@Column(name="table_owner")
	private String tableOwner;
	
	@Column(name="created_by")
	private long createdBy;
	
}

