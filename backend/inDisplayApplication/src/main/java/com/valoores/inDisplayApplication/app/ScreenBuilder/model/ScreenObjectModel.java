package com.valoores.inDisplayApplication.app.ScreenBuilder.model;


import static com.valoores.inDisplayApplication.utils.Schemas.SUITEDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name ="CFG_OBJECT_DEF", schema =SUITEDBA)
@Getter
@Setter
@Data
public class ScreenObjectModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="S_OBJECT_DEF")
	@SequenceGenerator(name = "S_OBJECT_DEF", schema = "SUITEDBA", sequenceName = "S_OBJECT_DEF", allocationSize = 1)
	@Column(name = "OBJECT_ID")
	private long objectId;
	
	
	@Column(name = "OBJECT_TYPE")
	private int OBJECT_TYPE;
	
	@Column(name = "OBJECT_NAME")
	private String OBJECT_NAME;
//	
//	@Column(name = "OBJECT_P_ID")
//	private long OBJECT_P_ID;
	
	@Column(name = "OBJECT_PARAM")
	private byte[] OBJECT_PARAM;
	
	@Column(name = "CREATED_BY")
	private long CREATED_BY;
	
	@Column(name = "CREATION_DATE")
	private Date CREATION_DATE;

	@Column(name = "MENU_CODE")
	private String MENU_CODE;
	
	@Column(name = "IS_MAIN")
	private int IS_MAIN;
	
}
