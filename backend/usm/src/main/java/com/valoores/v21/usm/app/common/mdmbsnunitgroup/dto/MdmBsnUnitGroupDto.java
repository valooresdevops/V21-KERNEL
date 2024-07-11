package com.valoores.v21.usm.app.common.mdmbsnunitgroup.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MdmBsnUnitGroupDto {
	private long bsnGroupId;
	private String bsnGroupName;
	private Date creationDate;
	private String createdBy;
	private Date updateDate;
	private String updatedBy;

}
