package com.valoores.inDisplayApplication.app.ScreenBuilder.model;
import static com.valoores.inDisplayApplication.utils.Schemas.SUITEDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name ="CFG_OBJECT_DEF", schema =SUITEDBA)
@Getter
@Setter
public class TableGridDataModel {

	@Id
	@Column(name = "OBJECT_ID")
	private long OBJECT_ID;
	
	
	@Column(name = "OBJECT_TYPE")
	private int OBJECT_TYPE;
	
	@Column(name = "OBJECT_NAME")
	private String OBJECT_NAME;
	
	@Column(name = "REPORT_ID")
	private long REPORT_ID;
	
	
}
