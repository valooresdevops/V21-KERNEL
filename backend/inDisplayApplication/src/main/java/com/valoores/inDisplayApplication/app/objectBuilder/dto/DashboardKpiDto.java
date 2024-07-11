package com.valoores.inDisplayApplication.app.objectBuilder.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardKpiDto {
	private long dashboardKpiId;
	private String kpiId;
	private long templateId;
	private long order;
	private long size;
	private long userId;
}
