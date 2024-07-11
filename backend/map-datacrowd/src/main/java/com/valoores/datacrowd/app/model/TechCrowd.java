package com.valoores.datacrowd.app.model;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.Hidden;

@Entity
@Table(name = "tech_crowd_table", schema = "techdba")
public class TechCrowd {

	@Id
	@Hidden
	@NotBlank(message = "CROWD_ID cannot be empty!")
	@Column(name = "CROWD_ID")
	private long crowdID;

	@Column(name = "CROWD_NAME")
	private String crowdName;
	
	@Column(name = "CROWD_INTERNAL_CODE")
	private int crowdInternalCode;
	
	@Column(name = "CROWD_GENDER")
	private String crowdGender;
	
	@Column(name = "CROWD_DOB")
	private Date crowdDob;
	
	@Column(name = "CREATION_DATE")
	private Date CreationDate;
	
	@NotBlank(message = "CREATED_BY cannot be empty!")
	@Column(name = "CREATED_BY")
	private int CreatedBy;

	
	@Column(name = "UPDATE_DATE")
	private Date UpdateDate;
	

	@Column(name = "UPDATED_BY")
	private int UpdatedBy;
	
	@Column(name = "CROWD_NAME_ML")
	private String crowdNameMl;
	
	
	
 
	
	 

}
