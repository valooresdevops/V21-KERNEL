package com.valoores.v21.usm.app.common.logs.model;
import static com.valoores.v21.usm.utils.Schemas.USMDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USM_USER_LOG", schema = USMDBA)
@Data
@NoArgsConstructor
public class LogUser {
	@Id
	@Column(name = "LOG_ID")
	private long logId;
	
	@Column(name = "LOGIN_DATE")
	private Date loginDate;
	
	@Column(name = "LOGOUT_DATE")
	private String logoutDate;
	
	@Column(name = "LOG_IP")
	private String logIp;

	@Column(name = "EMP_ID")
	private long empId;
	
	@Column(name = "logout_incidence")
	private long logoutIncidence;
	
}



