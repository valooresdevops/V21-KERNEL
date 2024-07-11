package com.valoores.cassandra.app.dto;


import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DateDto {
	
	  private List<Integer> month;
	    private int year;
	    private List<Integer> day;
	    private List<Integer> hours;

	    

}
