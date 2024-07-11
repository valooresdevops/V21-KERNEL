package com.valoores.v21.usm.backend;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.valoores.v21.usm.utils.StringUtils;

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