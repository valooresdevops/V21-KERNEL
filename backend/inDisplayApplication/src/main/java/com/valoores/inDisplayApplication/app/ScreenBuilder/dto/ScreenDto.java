package com.valoores.inDisplayApplication.app.ScreenBuilder.dto;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ScreenDto {

	private int userId;
	private long screenId;
	private String screenName;
	private boolean isTemplate;
	private String isSuspended;
	private String applicationId;
	private String application;
	private String parentMenuId;
	private String parentMenu;
	private String data;
	private String NextAction;


}
