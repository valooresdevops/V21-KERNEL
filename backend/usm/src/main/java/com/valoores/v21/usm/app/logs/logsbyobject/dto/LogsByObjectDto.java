package com.valoores.v21.usm.app.logs.logsbyobject.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LogsByObjectDto {
	private String application;
	private String menuName;
	private String loginDate;
	private String logoutDate;
	private int operationtype;
	private String operationHint;
	private String operationDate;
	private String empName;
	private String menuPath;

}
