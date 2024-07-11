package com.valoores.inDisplayApplication.app.ScreenBuilder.model;
import static com.valoores.inDisplayApplication.utils.Schemas.USMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name ="USM_MENU", schema =USMDBA)
@Getter
@Setter
public class ParentMenuListModel {
	@Id
	@Column(name = "MENU_CODE")
	private String MENU_CODE;
	
	
	@Column(name = "MENU_NAME")
	private String MENU_NAME;
	
	@Column(name = "MENU_P_CODE")
	private String MENU_P_CODE;
	
	@Column(name = "MENU_TYPE")
	private int MENU_TYPE;
}

