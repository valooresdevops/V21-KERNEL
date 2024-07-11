package com.valoores.v21.usm.app.securitymanagement.usermanagement.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class USMMultiMiscDto {

//	private long id;
	private long userId;
	private String userRoleId;
	//private Integer bsnGroupId;
//	private Integer strucDataId;
	//private Integer bsnGroupTypeCode;
	private Integer roleId;
//	private String isDefault;
	@Hidden
	private Date creationDate;
	private long createdBy;
//	private Date updateDate;
//	private Integer updatedBy;
	@Hidden
	private String accessCode;
	private String menuCode;
	
}
