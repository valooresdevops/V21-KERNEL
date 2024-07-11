package com.valoores.cassandra.app.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimulationDto {
	
	private long   simulationId;
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
}
