package com.valoores.v21.usm.app.securitymanagement.role.adrolemapping.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class USMAdRoleMappingDto {
	@Hidden
	private long id;
	@Hidden
	private long roleId;
	private long objectTypeName;
	private String objectCN;
	private Date creationDate;
	private Date updateDate;
	private String createdBy;
}
