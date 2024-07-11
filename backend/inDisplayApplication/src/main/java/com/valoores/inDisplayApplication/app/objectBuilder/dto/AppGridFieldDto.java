package com.valoores.inDisplayApplication.app.objectBuilder.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppGridFieldDto {
	private long gridId;
	private String fieldName;
	private String qryFieldName;
	private Integer drilldown;
}
