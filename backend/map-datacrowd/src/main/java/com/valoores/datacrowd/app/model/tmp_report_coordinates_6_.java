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
@Table(name="tmp_report_coordinates_6_",schema="ssdx_eng")
public class tmp_report_coordinates_6_ {

	@Id
	@Hidden
	@NotBlank(message = "queryId cannot be empty!")
//	@Column(name = "queryId")
//	private long queryId;
	@Column(name = "LOCATION_COU_CODE_NUM")
	private int location_cou_code_num;
	
	@Column(name = "LOCATION_NAME")
	private String location_name;
	
	@Column(name = "DEVICE_ID")
	private String device_id;
	
	@Column(name = "USAGE_TIMEFRAME")
	private int usage_timeframe;
	
	@Column(name = "LOCATION_LONGITUDE")
	private String location_longitude;
	
	@Column(name = "LOCATION_LATITUDE")
	private String location_latitude;
	
	@Column(name = "LOCATION_ACCURACY")
	private int location_accuracy;
	
	@Column(name = "CALLED_NO")
	private String called_no;
	
	@Column(name = "CALLING_NO")
	private String calling_no;
	
	@Column(name = "CALL_BDATE")
	private int call_bdate;
	
	@Column(name = "CALL_EDATE")
	private int call_edate;

 
	
	
}
