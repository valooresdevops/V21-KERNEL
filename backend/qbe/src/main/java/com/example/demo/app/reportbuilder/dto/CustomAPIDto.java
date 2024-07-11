package com.example.demo.app.reportbuilder.dto;

import java.util.List;

import lombok.Data;

@Data
public class CustomAPIDto {
	private List<ApiColumnsDto> columns;
	private long customerId;
	private long userId;
	private long nearBy;
}
