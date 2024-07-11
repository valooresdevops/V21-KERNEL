package com.valoores.inDisplayApplication.app.formBuilder.dto;

import java.util.List;

import lombok.Data;

@Data
public class CustomAPIDto {
	private List<ApiColumnsDto> columns;
//	private long customerId;
	private long userId;
	private long nearBy;
	private long sameSession;
//	private long sessionId;
	private String parameters;
}
