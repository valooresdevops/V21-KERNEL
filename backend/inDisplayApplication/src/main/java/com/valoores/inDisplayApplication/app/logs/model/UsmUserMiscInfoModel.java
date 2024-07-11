package com.valoores.inDisplayApplication.app.logs.model;
import static com.valoores.inDisplayApplication.utils.Schemas.USMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usm_user_misc_info", schema = USMDBA)
@Getter
@Setter
public class UsmUserMiscInfoModel {

	@Id
	@Column(name = "USER_ID")
	private long userId;
	
	@Column(name = "USER_LOGIN")
	private String userLogin;
	
}


