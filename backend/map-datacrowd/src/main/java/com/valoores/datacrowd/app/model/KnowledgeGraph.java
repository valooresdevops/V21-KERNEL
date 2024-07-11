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
@Table(name="TMP_CYB_GRAPH_RECORDS_",schema="SDEDBA")
public class KnowledgeGraph {

	
	@Id
	@Hidden
	@NotBlank(message = "queryId cannot be empty!")
//	@Column(name = "queryId")
//	private long queryId;
	@Column(name = "location_main_data_id")
	private int location_main_data_id;
	
	@Column(name = "location_name")
	private String location_name;
	
	@Column(name = "device_id")
	private String device_id;
	
	@Column(name = "usage_timeframe")
	private int usage_timeframe;
	
	@Column(name = "location_longitude")
	private String location_longitude;
	
	@Column(name = "location_latitude")
	private String location_latitude;
	
	@Column(name = "location_accuracy")
	private int location_accuracy;
	
	@Column(name = "service_provider_id")
	private int service_provider_id;
	
	@Column(name = "called_no")
	private String called_no;
	
	@Column(name = "calling_no")
	private String calling_no;
	
	@Column(name = "call_bdate")
	private int call_bdate;
	
	@Column(name = "call_edate")
	private int call_edate;
	

}
