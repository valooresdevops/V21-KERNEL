package com.valoores.v21.usm.app.logs.Filter.model;

import static com.valoores.v21.usm.utils.Schemas.USMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "usm_user_log_exception", schema = USMDBA)
@Getter
@Setter
public class userLogExeption {
	@Id
	@Column(name="LOG_EXCEP_ID")
	private long id;
	
	@Column(name= "LOG_ID")
	private long logId;



}
