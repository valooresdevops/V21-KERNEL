package com.valoores.datacrowd.app.model;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TECH_ANGULAR_BUTTON", schema = "TECHDBA")
public class TechCrowdResidentialInfo {

	@Id
	@Hidden
	@NotBlank(message = "CROWD_ID cannot be empty!")
	@Column(name = "CROWD_RE_ID")
	private long crowdreid;

	@Column(name = "IS_RESIDENT")
	private String isresident;
	
	@Column(name = "COUNTRY_RESIDENCE")
	private String countryresidence;
	
	@Column(name = "RESIDENT_STATUS")
	private String residentstatus;
	
	@Column(name = "CITY")
	private String city;
	
	@Column(name = "RESIDENTIAL_ZONE")
	private String residentialzone;
	
	@Column(name = "RESIDENTIAL_ZIP_CODE")
	private String residentialzipcode;
	
	@Column(name = "RESIDENTIAL_STREET")
	private String residentialstreet;
	
	@Column(name = "FLOOR_RESIDENTIAL_ADDRESS")
	private int floorresidentialaddress;
	
	@Column(name = "PHONE")
	private String phone;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "CREATION_DATE")
	private Date CreationDate;
	
	@NotBlank(message = "CREATED_BY cannot be empty!")
	@Column(name = "CREATED_BY")
	private int CreatedBy;

	
	@Column(name = "UPDATE_DATE")
	private Date UpdateDate;
	

	@Column(name = "UPDATED_BY")
	private int UpdatedBy;
	
	@Column(name = "CROWD_ID")
	private long crowdid ;
	
	@NotBlank(message = "RESIDENTIAL_STATE cannot be empty!")
	@Column(name = "RESIDENTIAL_STATE")
	private String residentialState ;
	

	
 
	 

}
