package com.example.demo.customResponse;


import com.example.demo.utils.StringUtils;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@Hidden
public class CustomResponse {
	
	private String code;
	private String status;
	private String description;
	private long Id; 
	
	@Override
	public String toString() {
		return StringUtils.toJsonString(this);
	}
}

