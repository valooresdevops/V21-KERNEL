package com.valoores.cassandradatacrowd.app.model;

import java.io.Serializable;
import org.json.JSONObject;

public class CDRLocation implements Serializable {

	private String imsi_id;
	private String imei_id;
	private String location_latitude;
	private String location_longitude;
	private Integer service_provider_id;
	private String cgi_id;
	private String location_azimuth;
	private String phone_number;
	private long usage_timeframe;
	private Integer type_id;

	public String getImsi_id() {
		return imsi_id;
	}

	public void setImsi_id(String imsi_id) {
		this.imsi_id = imsi_id;
	}

	public String getImei_id() {
		return imei_id;
	}

	public void setImei_id(String imei_id) {
		this.imei_id = imei_id;
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

	public Integer getService_provider_id() {
		return service_provider_id;
	}

	public void setService_provider_id(Integer service_provider_id) {
		this.service_provider_id = service_provider_id;
	}

	public String getCgi_id() {
		return cgi_id;
	}

	public void setCgi_id(String cgi_id) {
		this.cgi_id = cgi_id;
	}

	public String getLocation_azimuth() {
		return location_azimuth;
	}

	public void setLocation_azimuth(String location_azimuth) {
		this.location_azimuth = location_azimuth;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public long getUsage_timeframe() {
		return usage_timeframe;
	}

	public void setUsage_timeframe(long usage_timeframe) {
		this.usage_timeframe = usage_timeframe;
	}

	public Integer getType_id() {
		return type_id;
	}

	public void setType_id(Integer type_id) {
		this.type_id = type_id;
	}

	@Override
	public String toString() {

		return (new JSONObject(this)).toString();
	}

}
