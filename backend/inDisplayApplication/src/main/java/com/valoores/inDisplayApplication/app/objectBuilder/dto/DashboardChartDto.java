package com.valoores.inDisplayApplication.app.objectBuilder.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardChartDto {
	private long dashboardChartId;
	private String chartId;
	private long templateId;
	private long order;
	private long size;
	private long userId;
}
