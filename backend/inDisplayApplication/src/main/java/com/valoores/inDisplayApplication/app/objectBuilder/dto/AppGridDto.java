package com.valoores.inDisplayApplication.app.objectBuilder.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppGridDto {
	private long gridId;
	private long objectKpiId;
	private Integer query;
	private String gridName;
	private long userId;
	
	//Grid Field
	
	private String fieldName;
	private String qryFieldName;
	private Integer drilldown;
	private String rowSource;
	private String columnSource;
	
}
