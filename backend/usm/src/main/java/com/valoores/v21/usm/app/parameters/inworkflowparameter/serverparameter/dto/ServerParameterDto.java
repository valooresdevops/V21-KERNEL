package com.valoores.v21.usm.app.parameters.inworkflowparameter.serverparameter.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerParameterDto {

	private String currentServerIp; 
	
	private String currentServerName;
	
	private String wfmServerIp;
	
	private String wfmServerPort;
	
	private String infoLinkServerIp;
	
	private String infoLinkServerPort;
	
	private String StatusEngineActive;
	
	private String wfmEngineActive;
	
}
