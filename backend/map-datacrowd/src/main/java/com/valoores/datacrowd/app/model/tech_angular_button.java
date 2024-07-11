package com.valoores.datacrowd.app.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tech_angular_button", schema = "techdba")
public class tech_angular_button {

	@Id
	@Hidden
	@Column(name = "BTN_ID")
	private long btnid;

	@Column(name = "BTN_NAME")
	private String btnname;
	
	@Column(name = "BTN_ICON")
	private String btnicon;
	
	@Column(name = "BTN_STYLE")
	private String btnstyle;
	
	@Column(name = "BTN_FUNCTION")
	private String btnfucntion;
	
	@Column(name = "BTN_ICON_OFFLINE")
	private String btniconoffline;
	
	@Column(name = "FLAG")
	private int flag;
	
	@Column(name = "MORETOOLS")
	private int moretools;
	


}