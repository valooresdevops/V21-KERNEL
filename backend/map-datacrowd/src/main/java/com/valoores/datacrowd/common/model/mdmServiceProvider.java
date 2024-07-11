package com.valoores.datacrowd.common.model;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.Hidden;

@Entity
@Table(name = "MDM_SERVICE_PROVIDER", schema = "MDMDBA")
public class mdmServiceProvider {

	@Id
	@Hidden
	@NotBlank(message = "SERVICE_PROVIDER_ID cannot be empty!")
	@Column(name = "SERVICE_PROVIDER_ID")
	private long ServiceProviderID;

	@NotBlank(message = "SERVICE_PROVIDER_NAME cannot be empty!")
	@Column(name = "SERVICE_PROVIDER_NAME")
	private String ServiceProviderName;
	
	@Column(name = "SERVICE_PROVIDER_TYPE_ID")
	private int ServiceProviderTypeID;
	
	@NotBlank(message = "SERVICE_PROVIDER_ID cannot be empty!")
	@Column(name = "CREATION_DATE")
	private Date CreationDate;
	
	@NotBlank(message = "CREATED_BY cannot be empty!")
	@Column(name = "CREATED_BY")
	private int CreatedBy;

	
	@Column(name = "UPDATE_DATE")
	private Date UpdateDate;
	

	@Column(name = "UPDATED_BY")
	private int UpdatedBy;
	
	@Column(name = "SERVICE_PROVIDER_INTERNL_CODE")
	private int ServiceProviderInternlCode;
	
	
	 

}
