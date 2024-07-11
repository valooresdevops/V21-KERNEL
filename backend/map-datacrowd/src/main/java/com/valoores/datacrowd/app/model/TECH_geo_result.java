package com.valoores.datacrowd.app.model;

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
@Table(name="TECH_geo_result_",schema="TECHDBA")
public class TECH_geo_result {

	
	@Id
	@Hidden
	@NotBlank(message = "queryId cannot be empty!")
//	@Column(name = "queryId")
//	private long queryId;
	@Column(name = "location_main_data_id")
	private int location_main_data_id;
	
	@Column(name = "Polyline")
	private String polyline;
	

	

}
