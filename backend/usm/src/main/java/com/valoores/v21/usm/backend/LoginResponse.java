package com.valoores.v21.usm.backend;

import com.valoores.v21.usm.utils.StringUtils;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Hidden
public class LoginResponse {
	private String code;
	private String status;
	private String description;
	private String userId;
	private String roleName;
	private String roleId;
	
	@Override
	public String toString() {
		return StringUtils.toJsonString(this);
	}
}
