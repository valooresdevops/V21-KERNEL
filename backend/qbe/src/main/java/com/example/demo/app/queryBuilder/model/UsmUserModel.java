package com.example.demo.app.queryBuilder.model;

import static com.example.demo.utils.Schemas.USMDBA;


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
public class UsmUserModel {
	
	@Id
	@Column(name = "USER_ID")
	private long userId;
	
	@Column(name = "user_login")
	private String userLogin;
	

}

