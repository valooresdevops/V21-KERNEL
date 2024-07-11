package com.valoores.v21.usm.app.securitymanagement.accessrights.model;

import static com.valoores.v21.usm.utils.Schemas.USMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;



@Entity
@Table(name = "USM_Suite_Application", schema = USMDBA)
@Data
public class USMSuiteApplication {
	
	@Id
	@Column(name = "APP_CODE")
	private long appCode;
	
	@Column(name = "APP_NAME")
	private String appName;

}
