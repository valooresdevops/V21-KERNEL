package com.valoores.v21.usm.app.common.application.model;

import static com.valoores.v21.usm.utils.Schemas.USMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "usm_suite_application", schema = USMDBA)
@Data
public class USMApplication {
	@Id
	@Column(name = "APP_CODE")
	private long id;

	@Column(name = "APP_NAME")
	private String name;


}