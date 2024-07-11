package com.example.demo.app.queryBuilder.dto;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ParamDto {
	
	public int userId;
	public String paramName;
	public String paramType;
	public String paramDefault;
	public String sessionSerial;
	
	public String allParameters;

}
