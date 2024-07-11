package com.valoores.cassandra.app.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CoordDto {
	
	private long ID;
	private String Name;
	private String Value;
	private String Type;
	private String Bounds;
	private String radius;
	private String center;
	private long leafletid;
	private String PolyBoundsCoords;

}


