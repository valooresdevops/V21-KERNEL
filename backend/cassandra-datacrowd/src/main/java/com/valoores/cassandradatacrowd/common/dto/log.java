package com.valoores.cassandradatacrowd.common.dto;

import org.json.JSONObject;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Hidden
public class log {
	private String code;
	private String status;
	private String description;
	private String userId;
	private long id;
	
	@Override
	public String toString() {
		return (new JSONObject(this)).toString();	}

}
