package com.valoores.inDisplayApplication.app.formBuilder.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class queryDto {
	private long userId;
	private String queryName;
	private String query;
	private String queryFolder;
	private String queryComments;
	private Integer queryVersion;
	private String session_serial;
	private int queryId;
	private int storeId;

	private String queryObject;

}
