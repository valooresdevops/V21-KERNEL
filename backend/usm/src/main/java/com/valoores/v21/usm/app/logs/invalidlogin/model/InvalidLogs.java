package com.valoores.v21.usm.app.logs.invalidlogin.model;
import static com.valoores.v21.usm.utils.Schemas.USMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USM_INVALID_LOGIN_LOG", schema = USMDBA)
@Getter
@Setter

public class InvalidLogs {
	@Id
	@Column(name="invalid_login_log_id")
	private long id;
	
	@Column(name= "invalid_login_user")
	private String Performer;
	
	@Column(name= "invalid_login_date")
	private String Login_Date;
	
	@Column(name= "log_ip")
	private String Login_IP;

}
