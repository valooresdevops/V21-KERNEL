package com.valoores.v21.usm.app.securitymanagement.usermanagement.dto;


import lombok.Data;

@Data
public class UserLogsDto {
	String request;
	String ip;
	String method;
	long userId;
	//ObjectNode headers;
	String body;
}
