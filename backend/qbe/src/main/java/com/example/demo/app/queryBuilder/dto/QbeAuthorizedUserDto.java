package com.example.demo.app.queryBuilder.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QbeAuthorizedUserDto {
	
    private long qbeId;
	private String usrCode;
	private long createdBy;
	private long updatedBy;

	private Date creationDate;
	private Date updateDate;
	private byte hasUpdate;

}
