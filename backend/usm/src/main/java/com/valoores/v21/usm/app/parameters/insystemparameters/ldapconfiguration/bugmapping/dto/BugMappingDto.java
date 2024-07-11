package com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.bugmapping.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BugMappingDto {

	private long suiteLdapBsngpId;
	private long bsnGroupId;
	private long ldapConfigId;
	private long ldapRoleLinkedToType;
	private String ldapRoleLinkedToDesc;
	private Date creationDate;
	private long createdBy;
	private Date updateDate;
	private long updatedBy;

}
