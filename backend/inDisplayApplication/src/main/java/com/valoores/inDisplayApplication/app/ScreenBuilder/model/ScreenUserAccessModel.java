package com.valoores.inDisplayApplication.app.ScreenBuilder.model;

import static com.valoores.inDisplayApplication.utils.Schemas.USMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name ="USM_USER_MULTI_MISC_INFO", schema =USMDBA)
@Getter
@Setter
@Data
public class ScreenUserAccessModel {
	
	@Id
	@Column(name = "USER_MULTI_MISC_INFO_ID")
	private long USER_MULTI_MISC_INFO_ID;
	
	@Column(name = "MENU_CODE")
	private String MENU_CODE;

	@Column(name = "ACCESS_CODE")
	private String ACCESS_CODE;
}
