package com.example.demo.app.queryBuilder.dto;
import java.sql.Date;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class QbeApplicationDto {
	@Hidden
	private long QBE_ID;
	
	private String QUERY_NAME;
	private Date CREATION_DATE;
	private Integer DATA_STORE_ID;
	private Integer VERSION_NO;
	private String EMP_NAME;
	
	private String FILE_PATH;
	private String COMMENTS;
	 
}





	
	