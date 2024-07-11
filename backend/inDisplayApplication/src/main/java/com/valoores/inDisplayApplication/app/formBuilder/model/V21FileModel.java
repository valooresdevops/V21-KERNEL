package com.valoores.inDisplayApplication.app.formBuilder.model;

import static com.valoores.inDisplayApplication.utils.Schemas.TECHDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "V21_FILES", schema = TECHDBA)
@Getter
@Setter
public class V21FileModel {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="S_V21_Files_ID")
	@SequenceGenerator(name = "S_V21_Files_ID", schema = "TECHDBA", sequenceName = "S_V21_Files_ID", allocationSize = 1)
	@Column(name = "V21_FILES_ID")
	private long V21_FILES_ID;
	
	@Column(name="TABLE_NAME")
	private String tableName;

	@Column(name="PRIMARY_COL")
	private String primaryCol;
	
	@Column(name="PRIMARY_VAL")
	private String primaryVal;
	
	@Column(name="FILE_COL")
	private String fileCol;
	
	@Column(name="FILE_DATA")
	private byte[] fileData;
}

