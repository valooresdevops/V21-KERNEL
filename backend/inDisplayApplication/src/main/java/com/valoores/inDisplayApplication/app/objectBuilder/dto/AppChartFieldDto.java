package com.valoores.inDisplayApplication.app.objectBuilder.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppChartFieldDto {
	private long fieldId;
	private long chartId;
	private String fieldName;
	private String queryFieldName;
	private String fieldColor;
	private Boolean isSerie;
	private Integer serieType;
	private Integer drilldown;
	private Integer drilldownType;
}
