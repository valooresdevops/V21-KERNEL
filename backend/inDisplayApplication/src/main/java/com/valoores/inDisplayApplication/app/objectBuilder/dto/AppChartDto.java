package com.valoores.inDisplayApplication.app.objectBuilder.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppChartDto {
	
private long chartId;
private String chartName;
private Integer objectKpiId;
private Integer query;
private Integer chartSize;
private String chartHTitle;
private String chartVTitle;
private String showLegend;
private String is3d;
private String isHorizontal;
private long userId;
private Integer chartType;


//Chart Field
private Integer fieldId;
private String fieldName;
private String queryFieldName;
private String fieldColor;
private String isSerie;
private Integer serieType;
private String drilldown;
private Integer drilldownType;
}
