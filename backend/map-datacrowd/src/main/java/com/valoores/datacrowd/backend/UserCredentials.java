package com.valoores.datacrowd.backend;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.valoores.datacrowd.utils.StringUtils;

import lombok.Data;

@Data
public class UserCredentials {
	
	@NotNull
	@NotBlank
	private String username;
	
	@NotNull
	@NotBlank
	private String password;
	
	@Override
	public String toString() {
		return StringUtils.toJsonString(this);
	}

}