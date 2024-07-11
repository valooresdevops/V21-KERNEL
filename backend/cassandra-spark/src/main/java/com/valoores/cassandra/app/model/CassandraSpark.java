package com.valoores.cassandra.app.model;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


@Getter
@Setter
@NoArgsConstructor
@EntityScan
@Table(name="test.loc_location_main_data_test2",schema="test")
public class CassandraSpark {

	
	@Id
	@Column(name="location_main_data_id")
	private long  location_main_data_id;
	
	@Column(name="location_name")
	private String  location_name;
	
	@Column(name="device_id")
	private String  device_id;
	
	@Column(name="location_cou_code_num")
	private String  location_cou_code_num;
	
	@Column(name="device_carrier_name")
	private String  device_carrier_name;
	
	@Column(name="device_hit_count")
	private Integer  device_hit_count;
	
	@Column(name="usage_timeline")
	private String  usage_timeline;
	
	@Column(name="usage_timeframe")
	private String  usage_timeframe;
	
	@Column(name="device_manufacturer_brand")
	private String  device_manufacturer_brand;
	
	@Column(name="device_model")
	private String  device_model;

	@Column(name="location_longitude")
	private String  location_longitude;
	
	@Column(name="location_latitude")
	private String  location_latitude;
	
	@Column(name="location_altitude")
	private String  location_altitude;
	
	@Column(name="location_density")
	private String  location_density;
	
	@Column(name="location_accuracy")
	private Integer  location_accuracy;

	@Column(name="service_provider_id")
	private Integer  service_provider_id;
	
	@Column(name="data_category")
	private Integer  data_category;
	
	@Column(name="creation_date")
	private String  creation_date;
	
	@Column(name="created_by")
	private Integer  created_by;
	
	@Column(name="update_date")
	private String  update_date;

	@Column(name="updated_by")
	private Integer  updated_by;
	
	@Column(name="usage_date")
	private String  usage_date;
	
	@Column(name="customer_id")
	private Integer  customer_id;
	
	
}
