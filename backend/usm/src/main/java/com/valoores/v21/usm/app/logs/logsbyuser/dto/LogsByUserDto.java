package com.valoores.v21.usm.app.logs.logsbyuser.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogsByUserDto {
	private String application;
	private int operationType;
	private String operationDescription;
	private String operationDate;
	private String loginDate;
	private String logoutDate;
	private String ip;
	private String userLogs;
}
