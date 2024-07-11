package com.valoores.v21.usm.app.parameters.insystemparameters.passwordsettings.model;

import static com.valoores.v21.usm.utils.Schemas.USMDBA;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USM_CLIENT_PARAMETER", schema = USMDBA)
@Getter
@Setter

public class ClientParam {
	@Id
	@Column(name="CLIENT_PARAM_ID")
	private long id;
	
	@Column(name= "PARAM_VALUE")
	private String ParamValue;
	
	@Column(name= "YEAR")
	private String year;
	
	@Column(name= "CREATION_DATE")
	private String creationDate;

	@Column(name= "UPDATE_DATE")
	private String updateDate;
	
	@Column(name= "CREATED_BY")
	private String createdBy;
	
	@Column(name= "UPDATED_BY")
	private String updatedBy;
	
	@Column(name= "BSN_GROUP_ID")
	private String BsnGroupId;
	
	
}
