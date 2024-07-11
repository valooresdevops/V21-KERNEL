package com.valoores.inDisplayApplication.app.objectBuilder.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardTemplateDto {
	private long templateID;
	private String templateName;
	private String userIds;
	private String roleNames;
	private String userNames;
	private String PREF_DATA;
	private long templatePId;
	private long userId;
	 
}
