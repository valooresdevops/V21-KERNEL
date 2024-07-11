package com.valoores.inDisplayApplication.app.ScreenBuilder.model;

import static com.valoores.inDisplayApplication.utils.Schemas.USMDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USM_MENU", schema = USMDBA)
@Getter
@Setter
public class ScreenMenuModel {

	@Id
	@Column(name = "MENU_CODE")
	private String MENU_CODE;
	
	@Column(name = "MENU_NAME")
	private String MENU_NAME;

	@Column(name = "MENU_VARIABLE")
	private String MENU_VARIABLE;
	
	@Column(name = "MENU_P_CODE")
	private String MENU_P_CODE;
	
	@Column(name = "MENU_MANAGED")
	private String MENU_MANAGED;
	
	@Column(name = "MENU_TYPE")
	private long MENU_TYPE;

	@Column(name = "CREATED_BY")
	private Integer CREATED_BY;
	
	@Column(name = "CREATION_DATE")
	private Date CREATION_DATE;
	
	@Column(name = "MENU_ICON_DESC")
	private String menuIconDesc;
	
}
