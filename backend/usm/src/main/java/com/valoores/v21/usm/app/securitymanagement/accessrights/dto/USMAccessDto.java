package com.valoores.v21.usm.app.securitymanagement.accessrights.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class USMAccessDto {

	//private String menuCode;
	
	private String id;
	private String name;
	private String isDisplay;
	private String isAdd;
	private String isModify;
	private String isDelete;
	private String isPrint;
	private String isExport;
	private String isTranslate;
	
	//private String modeType;
	
}
