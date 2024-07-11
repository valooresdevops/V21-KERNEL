package com.valoores.v21.usm.app.menu.model;

import static com.valoores.v21.usm.utils.Schemas.USMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "usm_suite_application", schema = USMDBA)
@Data
public class USMMenuApplication {
	@Id
	@Column(name = "APP_CODE")
	private Integer appCode;

	@Column(name = "APP_NAME")
	private String appName;
	
	@Column(name = "APP_SNAME")
	private String appSName;


}