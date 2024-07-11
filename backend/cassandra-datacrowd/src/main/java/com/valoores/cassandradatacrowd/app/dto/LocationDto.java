package com.valoores.cassandradatacrowd.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDto {

	private String min_latitude;
	private String max_latitude;
	private String min_longitude;
	private String max_longitude;
	private String devices;
	private String fromDate ;
	private String toDate ;
	private String serviceProvider;
	private String type;
	private String shapeType ;
	private Integer shapeId;
	
	// shape section
	private String value;

}
