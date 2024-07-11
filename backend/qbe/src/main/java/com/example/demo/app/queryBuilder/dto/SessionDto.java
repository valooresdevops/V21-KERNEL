package com.example.demo.app.queryBuilder.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SessionDto {

	private long userId;
	private String sessionSerial;
	private String sessionAttribute;
	private byte[] sessionValue;
	private Date creationDate;
	private int updatable;

	
}
