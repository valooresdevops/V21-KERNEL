package com.valoores.v21.usm.app.parameters.inworkflowparameter.serverparameter.model;

import static com.valoores.v21.usm.utils.Schemas.USMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USM_SUITE_APPLICATION", schema = USMDBA)
@Getter
@Setter
@NoArgsConstructor
public class ServerParameter {
	@Id
	@Column(name="APP_CODE")
	private long id;
	
	@Column(name= "APP_NAME")
	private String name;
	

	@Column(name= "APP_URL")
	private String appUrl;
	

	@Column(name= "APP_PORT")
	private String appPort;
	
	
	
}
