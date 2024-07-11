package com.valoores.datacrowd.app.dto;

import java.util.Date;

import org.json.JSONArray;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimulationDto {
	
	private long simulationId;
	private String simulationName;
	private String simulationType;
	private String filteredate;
	private String filterbdate;
	private String filterstd;
	private String executionDate;
	private String executedBy;
	private String creationDate;
	private int    createdBy;
	private String filterdevicesId;
	private JSONArray arrayOfDate;
	
	private Integer locReportConfigId ;
	private Date    statusBDate;
	private String  comments;
	private Integer statusCode;

	 
}
