package com.valoores.datacrowd.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.Hidden;

//@Getter
//@Setter
//@NoArgsConstructor
@Entity
//@Table(name="ref_sys13000",schema="SDEDBA")
@Table(name="LOC_PAR_LOCATION_MAP",schema="LOCDBA")
public class LOC_PAR_LOCATION_MAP {

	

	@Id
	@Hidden
	@Column(name = "par_location_map_id")
	private long parID;

	@Column(name = "POL_LIMIT")
	private String pollimit;
	
	@Column(name = "CIRCLE_LIMIT")
	private String circlelimit;

	@Column(name = "RECTANGLE_LIMIT")
	private String rectanglelimit;

	@Column(name = "TIME_LIMIT")
	private String timelimit;
	
	@Column(name = "MAP_LAYOUT_TYPE")
	private String maplayouttype;

	@Column(name = "DESCRIPTION1")
	private String description1;
	
	@Column(name = "DESCRIPTION2")
	private String description2;
	
	@Column(name = "DESCRIPTION3")
	private String description3;

	@Column(name = "DESCRIPTION4")
	private String description4;
	
	@Column(name = "LAST_SIMULATION_DESC")
	private String lastsimulationdesc;
	
	
	@Column(name = "CREATION_DATE")
	private Date CreationDate;
	
	@NotBlank(message = "CREATED_BY cannot be empty!")
	@Column(name = "CREATED_BY")
	private int CreatedBy;

	
	@Column(name = "UPDATE_DATE")
	private Date UpdateDate;
	

	@Column(name = "UPDATED_BY")
	private int UpdatedBy;

}
