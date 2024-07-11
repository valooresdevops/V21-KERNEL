package com.valoores.v21.usm.app.common.status.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusDto {
	private long id;
	private String name;
	private String creationDate;
	private String createdBy;
	private String updateDate;
	private String updatedBy;
}
