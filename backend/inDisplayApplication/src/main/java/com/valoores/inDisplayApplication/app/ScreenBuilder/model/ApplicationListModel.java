package com.valoores.inDisplayApplication.app.ScreenBuilder.model;

import static com.valoores.inDisplayApplication.utils.Schemas.USMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name ="USM_SUITE_APPLICATION", schema =USMDBA)
@Getter
@Setter
public class ApplicationListModel {
	@Id
	@Column(name = "APP_CODE")
	private long APP_CODE;
	
	
	@Column(name = "APP_NAME")
	private String APP_NAME;
	
	@Column(name = "APP_SNAME")
	private String APP_SNAME;
	
	@Column(name = "APP_MANAGED")
	private String APP_MANAGED;
	
	
}
