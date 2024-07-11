package com.valoores.v21.usm.app.logs.fieldhistorylog.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FieldHistoryLogDto {
	private String menu;
	private String objectId;
	private String field;
	private String user;
	private String oldValue;
	private String newValue;
	private String creationDate;
	

}
