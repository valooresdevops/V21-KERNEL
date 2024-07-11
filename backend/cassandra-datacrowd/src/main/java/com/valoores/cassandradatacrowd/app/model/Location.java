package com.valoores.cassandradatacrowd.app.model;

import java.io.Serializable;

import org.json.JSONObject;

//import org.springframework.data.cassandra.core.mapping.PrimaryKey;
//import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;



//@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonInclude(Include.)
public class Location implements Serializable{


	private static final long serialVersionUID = 1L;
//	@PrimaryKey
	private String device_id;
	private String location_main_data_id;
    private String data_category;
	private String device_carrier_name;
	private String device_hit_count;
	private String device_manufacturer_brand;
	private String device_model;
	private String location_accuracy;
	private String location_altitude;
	private String location_cou_code_num;
	private String location_density;
	private String location_latitude;
	private String location_longitude;
	private String location_name;
	private String service_provider_id;
    private String usage_date;
	private String usage_timeframe;
	private String usage_timeline;
	private Integer shape_id ; 
	private String  country_code;

	
	// cdr fields 
	private String called_no ;
	private String call_edate ;
	private String calling_imsi_id;
	private String call_bdate;
	private String calling_end_cgi_id;
	private String calling_end_location_bts_distance;
	private String calling_imei_id;
	private String calling_no;
	private String calling_start_cgi_id;
	private String calling_start_location_bts_distance;
	private String data_source;  
   private String creation_date ;
  private String  start_location_latitude ;
  private String  start_location_longitude ;
  private String  call_duration ;
   private String call_type ;
  private String  called_end_cgi_id ;
   private String called_extension_no ;
  private String  called_imei_id ;
  private String  called_imsi_id ;
  private String  called_lte_cgi_id ;
  private String  called_msisdn_no ;
   private String called_smssc_id ;
  private String  called_start_cgi_id ;
   private String calling_end_lte_cgi_id ;
  private String  calling_extension_no ;
  private String  calling_lte_cgi_id ;
   private String calling_msisdn_no ;
  private String  calling_private_ip_number ;
  private String  calling_public_ip_number ;
  private String  calling_smssc_id ;
  private String  calling_start_lte_cgi_id ;
  private String  calling_tmsi_id ;
  private String  cdr_internal_code ;
  private String  conference_call_id ;
  private String  created_by ;
  private String  customer_id ;
  private String  destination_url_link ;
  private String  device_role ;
  private String  divert_call_no ;
  private String  download_data_size ;
  private String  download_data_size_uni_id ;
   private String end_loc_bts_distance_uni_id ;
   private String end_location_altitude ;
  private String  end_location_azimuth ;
   private String end_location_bts_distance ;
   private String end_location_coordinates ;
   private String end_location_latitude ;
   private String end_location_longitude ;
   private String end_location_name ;
  private String  end_network_interface_typ_code ;
  private String  end_rat_name ;
 private String   evolved_node_b ;
  private String  ip_address ;
  private String  last_event_type_code ;
  private String  last_state_type_code ;
  private String  location_area_code ;
  private String  network_element_id ;
  private String  network_interface_type_code ;
  private String  network_ip_type_code ;
  private String  network_type_code ;
  private String  operating_system_name ;
  private String  operating_system_version ;
  private String  ringing_duration ;
  private String  ringing_duration_uni_id ;
  private String  server_host_name ;
  private String  server_ip_address ;
  private String  service_application_code ;
  private String  service_content_category_code ;
  private String  service_content_provider_code ;
   private String service_type_code ;
  private String  session_bdate ;
  private String  session_duration ;
  private String  session_duration_uni_id ;
  private String  session_edate ;
  private String  signal_bdate ;
  private String  signal_duration ;
  private String  signal_duration_uni_id ;
  private String  signal_edate ;
   private String signal_type_code ;
   private String sms_file_id ;
  private String  start_event_type_code ;
  private String  start_loc_bts_distance_uni_id ;
  private String  start_location_altitude ;
  private String  start_location_azimuth ;
  private String  start_location_bts_distance ;
  private String  start_location_coordinates ;
  private String  start_location_name ;
  private String  start_network_interface_typ_cd ;
  private String  start_rat_name ;
  private String  update_date ;
  private String  updated_by ;
  private String  upload_data_size ;
  private String  upload_data_size_uni_id ;
  private String  volte_call_type_code;

  

	
	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getLocation_main_data_id() {
		return location_main_data_id;
	}

	public void setLocation_main_data_id(String location_main_data_id) {
		this.location_main_data_id = location_main_data_id;
	}

	public String getData_category() {
		return data_category;
	}

	public void setData_category(String data_category) {
		this.data_category = data_category;
	}

	public String getDevice_carrier_name() {
		return device_carrier_name;
	}

	public void setDevice_carrier_name(String device_carrier_name) {
		this.device_carrier_name = device_carrier_name;
	}

	public String getDevice_hit_count() {
		return device_hit_count;
	}

	public void setDevice_hit_count(String device_hit_count) {
		this.device_hit_count = device_hit_count;
	}

	public String getDevice_manufacturer_brand() {
		return device_manufacturer_brand;
	}

	public void setDevice_manufacturer_brand(String device_manufacturer_brand) {
		this.device_manufacturer_brand = device_manufacturer_brand;
	}

	public String getDevice_model() {
		return device_model;
	}

	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}

	public String getLocation_accuracy() {
		return location_accuracy;
	}

	public void setLocation_accuracy(String location_accuracy) {
		this.location_accuracy = location_accuracy;
	}

	public String getLocation_altitude() {
		return location_altitude;
	}

	public void setLocation_altitude(String location_altitude) {
		this.location_altitude = location_altitude;
	}

	public String getLocation_cou_code_num() {
		return location_cou_code_num;
	}

	public void setLocation_cou_code_num(String location_cou_code_num) {
		this.location_cou_code_num = location_cou_code_num;
	}

	public String getLocation_density() {
		return location_density;
	}

	public void setLocation_density(String location_density) {
		this.location_density = location_density;
	}

	public String getLocation_latitude() {
		return location_latitude;
	}

	public void setLocation_latitude(String location_latitude) {
		this.location_latitude = location_latitude;
	}

	public String getLocation_longitude() {
		return location_longitude;
	}

	public void setLocation_longitude(String location_longitude) {
		this.location_longitude = location_longitude;
	}

	public String getLocation_name() {
		return location_name;
	}

	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}

	public String getService_provider_id() {
		return service_provider_id;
	}

	public void setService_provider_id(String service_provider_id) {
		this.service_provider_id = service_provider_id;
	}

	public String getUsage_date() {
		return usage_date;
	}

	public void setUsage_date(String usage_date) {
		this.usage_date = usage_date;
	}

	public String getUsage_timeframe() {
		return usage_timeframe;
	}

	public void setUsage_timeframe(String usage_timeframe) {
		this.usage_timeframe = usage_timeframe;
	}

	public String getUsage_timeline() {
		return usage_timeline;
	}

	public void setUsage_timeline(String usage_timeline) {
		this.usage_timeline = usage_timeline;
	}

	public Integer getShape_id() {
		return shape_id;
	}

	public void setShape_id(Integer shape_id) {
		this.shape_id = shape_id;
	}

	public String getCalled_no() {
		if(this.called_no == null)
		{
			called_no = "0";
		}
		return called_no;
	}

	public void setCalled_no(String called_no) {
		this.called_no = called_no;
	}

	public String getCall_bdate() {
		if(this.call_bdate == null)
		{
			call_bdate = "0";
		}
		return call_bdate;
	}

	public void setCall_bdate(String call_bdate) {
		this.call_bdate = call_bdate;
	}

	public String getCall_edate() {
		if(this.call_edate == null)
		{
			call_edate = "0";
		}
		return call_edate;
	}

	public void setCall_edate(String call_edate) {
		this.call_edate = call_edate;
	}

	public String getCalling_no() {
		if(this.calling_no == null)
		{
			calling_no = "0";
		}
		return calling_no;
	}

	public void setCalling_no(String calling_no) {
		this.calling_no = calling_no;
	}

//	public static long getSerialversionuid() {
//		return serialVersionUID;
//	}
	
	@Override
	public String toString() {
		
		return (new JSONObject(this)).toString();	}

	public String getCalling_imsi_id() {
		return calling_imsi_id;
	}

	public void setCalling_imsi_id(String calling_imsi_id) {
		this.calling_imsi_id = calling_imsi_id;
	}

	public String getCalling_end_cgi_id() {
		return calling_end_cgi_id;
	}

	public void setCalling_end_cgi_id(String calling_end_cgi_id) {
		this.calling_end_cgi_id = calling_end_cgi_id;
	}

	public String getCalling_end_location_bts_distance() {
		return calling_end_location_bts_distance;
	}

	public void setCalling_end_location_bts_distance(String calling_end_location_bts_distance) {
		this.calling_end_location_bts_distance = calling_end_location_bts_distance;
	}

	public String getCalling_imei_id() {
		return calling_imei_id;
	}

	public void setCalling_imei_id(String calling_imei_id) {
		this.calling_imei_id = calling_imei_id;
	}

	public String getCalling_start_cgi_id() {
		return calling_start_cgi_id;
	}

	public void setCalling_start_cgi_id(String calling_start_cgi_id) {
		this.calling_start_cgi_id = calling_start_cgi_id;
	}

	public String getCalling_start_location_bts_distance() {
		return calling_start_location_bts_distance;
	}

	public void setCalling_start_location_bts_distance(String calling_start_location_bts_distance) {
		this.calling_start_location_bts_distance = calling_start_location_bts_distance;
	}

	public String getData_source() {
		return data_source;
	}

	public void setData_source(String data_source) {
		this.data_source = data_source;
	}

	public String getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(String creation_date) {
		this.creation_date = creation_date;
	}

	public String getStart_location_latitude() {
		return start_location_latitude;
	}

	public void setStart_location_latitude(String start_location_latitude) {
		this.start_location_latitude = start_location_latitude;
	}

	public String getStart_location_longitude() {
		return start_location_longitude;
	}

	public void setStart_location_longitude(String start_location_longitude) {
		this.start_location_longitude = start_location_longitude;
	}

	public String getCall_duration() {
		return call_duration;
	}

	public void setCall_duration(String call_duration) {
		this.call_duration = call_duration;
	}

	public String getCall_type() {
		return call_type;
	}

	public void setCall_type(String call_type) {
		this.call_type = call_type;
	}

	public String getCalled_end_cgi_id() {
		return called_end_cgi_id;
	}

	public void setCalled_end_cgi_id(String called_end_cgi_id) {
		this.called_end_cgi_id = called_end_cgi_id;
	}

	public String getCalled_extension_no() {
		return called_extension_no;
	}

	public void setCalled_extension_no(String called_extension_no) {
		this.called_extension_no = called_extension_no;
	}

	public String getCalled_imei_id() {
		return called_imei_id;
	}

	public void setCalled_imei_id(String called_imei_id) {
		this.called_imei_id = called_imei_id;
	}

	public String getCalled_imsi_id() {
		return called_imsi_id;
	}

	public void setCalled_imsi_id(String called_imsi_id) {
		this.called_imsi_id = called_imsi_id;
	}

	public String getCalled_lte_cgi_id() {
		return called_lte_cgi_id;
	}

	public void setCalled_lte_cgi_id(String called_lte_cgi_id) {
		this.called_lte_cgi_id = called_lte_cgi_id;
	}

	public String getCalled_msisdn_no() {
		return called_msisdn_no;
	}

	public void setCalled_msisdn_no(String called_msisdn_no) {
		this.called_msisdn_no = called_msisdn_no;
	}

	public String getCalled_smssc_id() {
		return called_smssc_id;
	}

	public void setCalled_smssc_id(String called_smssc_id) {
		this.called_smssc_id = called_smssc_id;
	}

	public String getCalled_start_cgi_id() {
		return called_start_cgi_id;
	}

	public void setCalled_start_cgi_id(String called_start_cgi_id) {
		this.called_start_cgi_id = called_start_cgi_id;
	}

	public String getCalling_end_lte_cgi_id() {
		return calling_end_lte_cgi_id;
	}

	public void setCalling_end_lte_cgi_id(String calling_end_lte_cgi_id) {
		this.calling_end_lte_cgi_id = calling_end_lte_cgi_id;
	}

	public String getCalling_extension_no() {
		return calling_extension_no;
	}

	public void setCalling_extension_no(String calling_extension_no) {
		this.calling_extension_no = calling_extension_no;
	}

	public String getCalling_lte_cgi_id() {
		return calling_lte_cgi_id;
	}

	public void setCalling_lte_cgi_id(String calling_lte_cgi_id) {
		this.calling_lte_cgi_id = calling_lte_cgi_id;
	}

	public String getCalling_msisdn_no() {
		return calling_msisdn_no;
	}

	public void setCalling_msisdn_no(String calling_msisdn_no) {
		this.calling_msisdn_no = calling_msisdn_no;
	}

	public String getCalling_private_ip_number() {
		return calling_private_ip_number;
	}

	public void setCalling_private_ip_number(String calling_private_ip_number) {
		this.calling_private_ip_number = calling_private_ip_number;
	}

	public String getCalling_public_ip_number() {
		return calling_public_ip_number;
	}

	public void setCalling_public_ip_number(String calling_public_ip_number) {
		this.calling_public_ip_number = calling_public_ip_number;
	}

	public String getCalling_smssc_id() {
		return calling_smssc_id;
	}

	public void setCalling_smssc_id(String calling_smssc_id) {
		this.calling_smssc_id = calling_smssc_id;
	}

	public String getCalling_start_lte_cgi_id() {
		return calling_start_lte_cgi_id;
	}

	public void setCalling_start_lte_cgi_id(String calling_start_lte_cgi_id) {
		this.calling_start_lte_cgi_id = calling_start_lte_cgi_id;
	}

	public String getCalling_tmsi_id() {
		return calling_tmsi_id;
	}

	public void setCalling_tmsi_id(String calling_tmsi_id) {
		this.calling_tmsi_id = calling_tmsi_id;
	}

	public String getCdr_internal_code() {
		return cdr_internal_code;
	}

	public void setCdr_internal_code(String cdr_internal_code) {
		this.cdr_internal_code = cdr_internal_code;
	}

	public String getConference_call_id() {
		return conference_call_id;
	}

	public void setConference_call_id(String conference_call_id) {
		this.conference_call_id = conference_call_id;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getDestination_url_link() {
		return destination_url_link;
	}

	public void setDestination_url_link(String destination_url_link) {
		this.destination_url_link = destination_url_link;
	}

	public String getDevice_role() {
		return device_role;
	}

	public void setDevice_role(String device_role) {
		this.device_role = device_role;
	}

	public String getDivert_call_no() {
		return divert_call_no;
	}

	public void setDivert_call_no(String divert_call_no) {
		this.divert_call_no = divert_call_no;
	}

	public String getDownload_data_size() {
		return download_data_size;
	}

	public void setDownload_data_size(String download_data_size) {
		this.download_data_size = download_data_size;
	}

	public String getDownload_data_size_uni_id() {
		return download_data_size_uni_id;
	}

	public void setDownload_data_size_uni_id(String download_data_size_uni_id) {
		this.download_data_size_uni_id = download_data_size_uni_id;
	}

	public String getEnd_loc_bts_distance_uni_id() {
		return end_loc_bts_distance_uni_id;
	}

	public void setEnd_loc_bts_distance_uni_id(String end_loc_bts_distance_uni_id) {
		this.end_loc_bts_distance_uni_id = end_loc_bts_distance_uni_id;
	}

	public String getEnd_location_altitude() {
		return end_location_altitude;
	}

	public void setEnd_location_altitude(String end_location_altitude) {
		this.end_location_altitude = end_location_altitude;
	}

	public String getEnd_location_azimuth() {
		return end_location_azimuth;
	}

	public void setEnd_location_azimuth(String end_location_azimuth) {
		this.end_location_azimuth = end_location_azimuth;
	}

	public String getEnd_location_bts_distance() {
		return end_location_bts_distance;
	}

	public void setEnd_location_bts_distance(String end_location_bts_distance) {
		this.end_location_bts_distance = end_location_bts_distance;
	}

	public String getEnd_location_coordinates() {
		return end_location_coordinates;
	}

	public void setEnd_location_coordinates(String end_location_coordinates) {
		this.end_location_coordinates = end_location_coordinates;
	}

	public String getEnd_location_latitude() {
		return end_location_latitude;
	}

	public void setEnd_location_latitude(String end_location_latitude) {
		this.end_location_latitude = end_location_latitude;
	}

	public String getEnd_location_longitude() {
		return end_location_longitude;
	}

	public void setEnd_location_longitude(String end_location_longitude) {
		this.end_location_longitude = end_location_longitude;
	}

	public String getEnd_location_name() {
		return end_location_name;
	}

	public void setEnd_location_name(String end_location_name) {
		this.end_location_name = end_location_name;
	}

	public String getEnd_network_interface_typ_code() {
		return end_network_interface_typ_code;
	}

	public void setEnd_network_interface_typ_code(String end_network_interface_typ_code) {
		this.end_network_interface_typ_code = end_network_interface_typ_code;
	}

	public String getEnd_rat_name() {
		return end_rat_name;
	}

	public void setEnd_rat_name(String end_rat_name) {
		this.end_rat_name = end_rat_name;
	}

	public String getEvolved_node_b() {
		return evolved_node_b;
	}

	public void setEvolved_node_b(String evolved_node_b) {
		this.evolved_node_b = evolved_node_b;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public String getLast_event_type_code() {
		return last_event_type_code;
	}

	public void setLast_event_type_code(String last_event_type_code) {
		this.last_event_type_code = last_event_type_code;
	}

	public String getLast_state_type_code() {
		return last_state_type_code;
	}

	public void setLast_state_type_code(String last_state_type_code) {
		this.last_state_type_code = last_state_type_code;
	}

	public String getLocation_area_code() {
		return location_area_code;
	}

	public void setLocation_area_code(String location_area_code) {
		this.location_area_code = location_area_code;
	}

	public String getNetwork_element_id() {
		return network_element_id;
	}

	public void setNetwork_element_id(String network_element_id) {
		this.network_element_id = network_element_id;
	}

	public String getNetwork_interface_type_code() {
		return network_interface_type_code;
	}

	public void setNetwork_interface_type_code(String network_interface_type_code) {
		this.network_interface_type_code = network_interface_type_code;
	}

	public String getNetwork_ip_type_code() {
		return network_ip_type_code;
	}

	public void setNetwork_ip_type_code(String network_ip_type_code) {
		this.network_ip_type_code = network_ip_type_code;
	}

	public String getNetwork_type_code() {
		return network_type_code;
	}

	public void setNetwork_type_code(String network_type_code) {
		this.network_type_code = network_type_code;
	}

	public String getOperating_system_name() {
		return operating_system_name;
	}

	public void setOperating_system_name(String operating_system_name) {
		this.operating_system_name = operating_system_name;
	}

	public String getOperating_system_version() {
		return operating_system_version;
	}

	public void setOperating_system_version(String operating_system_version) {
		this.operating_system_version = operating_system_version;
	}

	public String getRinging_duration() {
		return ringing_duration;
	}

	public void setRinging_duration(String ringing_duration) {
		this.ringing_duration = ringing_duration;
	}

	public String getRinging_duration_uni_id() {
		return ringing_duration_uni_id;
	}

	public void setRinging_duration_uni_id(String ringing_duration_uni_id) {
		this.ringing_duration_uni_id = ringing_duration_uni_id;
	}

	public String getServer_host_name() {
		return server_host_name;
	}

	public void setServer_host_name(String server_host_name) {
		this.server_host_name = server_host_name;
	}

	public String getServer_ip_address() {
		return server_ip_address;
	}

	public void setServer_ip_address(String server_ip_address) {
		this.server_ip_address = server_ip_address;
	}

	public String getService_application_code() {
		return service_application_code;
	}

	public void setService_application_code(String service_application_code) {
		this.service_application_code = service_application_code;
	}

	public String getService_content_category_code() {
		return service_content_category_code;
	}

	public void setService_content_category_code(String service_content_category_code) {
		this.service_content_category_code = service_content_category_code;
	}

	public String getService_content_provider_code() {
		return service_content_provider_code;
	}

	public void setService_content_provider_code(String service_content_provider_code) {
		this.service_content_provider_code = service_content_provider_code;
	}

	public String getService_type_code() {
		return service_type_code;
	}

	public void setService_type_code(String service_type_code) {
		this.service_type_code = service_type_code;
	}

	public String getSession_bdate() {
		return session_bdate;
	}

	public void setSession_bdate(String session_bdate) {
		this.session_bdate = session_bdate;
	}

	public String getSession_duration() {
		return session_duration;
	}

	public void setSession_duration(String session_duration) {
		this.session_duration = session_duration;
	}

	public String getSession_duration_uni_id() {
		return session_duration_uni_id;
	}

	public void setSession_duration_uni_id(String session_duration_uni_id) {
		this.session_duration_uni_id = session_duration_uni_id;
	}

	public String getSession_edate() {
		return session_edate;
	}

	public void setSession_edate(String session_edate) {
		this.session_edate = session_edate;
	}

	public String getSignal_bdate() {
		return signal_bdate;
	}

	public void setSignal_bdate(String signal_bdate) {
		this.signal_bdate = signal_bdate;
	}

	public String getSignal_duration() {
		return signal_duration;
	}

	public void setSignal_duration(String signal_duration) {
		this.signal_duration = signal_duration;
	}

	public String getSignal_duration_uni_id() {
		return signal_duration_uni_id;
	}

	public void setSignal_duration_uni_id(String signal_duration_uni_id) {
		this.signal_duration_uni_id = signal_duration_uni_id;
	}

	public String getSignal_edate() {
		return signal_edate;
	}

	public void setSignal_edate(String signal_edate) {
		this.signal_edate = signal_edate;
	}

	public String getSignal_type_code() {
		return signal_type_code;
	}

	public void setSignal_type_code(String signal_type_code) {
		this.signal_type_code = signal_type_code;
	}

	public String getSms_file_id() {
		return sms_file_id;
	}

	public void setSms_file_id(String sms_file_id) {
		this.sms_file_id = sms_file_id;
	}

	public String getStart_event_type_code() {
		return start_event_type_code;
	}

	public void setStart_event_type_code(String start_event_type_code) {
		this.start_event_type_code = start_event_type_code;
	}

	public String getStart_loc_bts_distance_uni_id() {
		return start_loc_bts_distance_uni_id;
	}

	public void setStart_loc_bts_distance_uni_id(String start_loc_bts_distance_uni_id) {
		this.start_loc_bts_distance_uni_id = start_loc_bts_distance_uni_id;
	}

	public String getStart_location_altitude() {
		return start_location_altitude;
	}

	public void setStart_location_altitude(String start_location_altitude) {
		this.start_location_altitude = start_location_altitude;
	}

	public String getStart_location_azimuth() {
		return start_location_azimuth;
	}

	public void setStart_location_azimuth(String start_location_azimuth) {
		this.start_location_azimuth = start_location_azimuth;
	}

	public String getStart_location_bts_distance() {
		return start_location_bts_distance;
	}

	public void setStart_location_bts_distance(String start_location_bts_distance) {
		this.start_location_bts_distance = start_location_bts_distance;
	}

	public String getStart_location_coordinates() {
		return start_location_coordinates;
	}

	public void setStart_location_coordinates(String start_location_coordinates) {
		this.start_location_coordinates = start_location_coordinates;
	}

	public String getStart_location_name() {
		return start_location_name;
	}

	public void setStart_location_name(String start_location_name) {
		this.start_location_name = start_location_name;
	}

	public String getStart_network_interface_typ_cd() {
		return start_network_interface_typ_cd;
	}

	public void setStart_network_interface_typ_cd(String start_network_interface_typ_cd) {
		this.start_network_interface_typ_cd = start_network_interface_typ_cd;
	}

	public String getStart_rat_name() {
		return start_rat_name;
	}

	public void setStart_rat_name(String start_rat_name) {
		this.start_rat_name = start_rat_name;
	}

	public String getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}

	public String getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}

	public String getUpload_data_size() {
		return upload_data_size;
	}

	public void setUpload_data_size(String upload_data_size) {
		this.upload_data_size = upload_data_size;
	}

	public String getUpload_data_size_uni_id() {
		return upload_data_size_uni_id;
	}

	public void setUpload_data_size_uni_id(String upload_data_size_uni_id) {
		this.upload_data_size_uni_id = upload_data_size_uni_id;
	}

	public String getVolte_call_type_code() {
		return volte_call_type_code;
	}

	public void setVolte_call_type_code(String volte_call_type_code) {
		this.volte_call_type_code = volte_call_type_code;
	}

	public String getCountry_code() {
		return country_code;
	}

	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	
}
