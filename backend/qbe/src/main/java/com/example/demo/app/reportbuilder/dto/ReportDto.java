package com.example.demo.app.reportbuilder.dto;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ReportDto {

	long reportId;
	String reportName;
	String reportDesc;
	Date reportDate;
	long userId;
	String file;
	
}
