package com.valoores.inDisplayApplication.backend;

import com.valoores.inDisplayApplication.backend.UserCredentials;
import  com.valoores.inDisplayApplication.utils.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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