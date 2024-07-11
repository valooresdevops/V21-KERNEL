package com.valoores.v21.usm.app.common.logs.model;
import static com.valoores.v21.usm.utils.Schemas.USMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "USM_USER_MISC_INFO", schema = USMDBA)
@Getter
@Setter
public class UsmUserMiscInfo {

	@Id
	@Column(name="USER_ID")
	private long id;
	
	@Column(name= "USER_FULL_NAME")
	private String name;
	
	@Column(name= "USER_LOGIN")
	private String empUserLogin;

	@Column(name = "LAST_ACCESSED_APP_CODE")
	private long appCode;
}
