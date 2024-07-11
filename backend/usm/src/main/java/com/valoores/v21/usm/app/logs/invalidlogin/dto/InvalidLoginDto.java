package com.valoores.v21.usm.app.logs.invalidlogin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidLoginDto {
	private String performer;

	private String loginDate;
	private String loginIp;
	
}
