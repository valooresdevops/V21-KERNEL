package com.valoores.inDisplayApplication.app.objectBuilder.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CfgObjectKpiDto {
	private long kpiId ; 
	private String kpiName;
	private long qbeId;
	private String isRatio;
	private String mainValue;
	private String mainLabel;
	private String extraLabel;
	private String extraValue;
	private String isPercentage;
	private String backgroundColor;
	private String textColor;
    private String chart;
	private String grid;
	private String report;
	private long userId;
	private String dropdownChart;
	private String dropdownGrid;
	 
}
