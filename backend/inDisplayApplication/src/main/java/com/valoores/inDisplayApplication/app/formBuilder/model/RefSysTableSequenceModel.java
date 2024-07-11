package com.valoores.inDisplayApplication.app.formBuilder.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "ref_sys_table_sequence", schema = "sdedba")
@Data
public class RefSysTableSequenceModel {

	@Id
	@Column(name="TABLE_NAME")
	private String tableName;
	
	@Column(name="COLUMN_NAME")
	private String COLUMN_NAME;
	
	@Column(name="SEQUENCE_NAME")
	private String SEQUENCE_NAME;
}
