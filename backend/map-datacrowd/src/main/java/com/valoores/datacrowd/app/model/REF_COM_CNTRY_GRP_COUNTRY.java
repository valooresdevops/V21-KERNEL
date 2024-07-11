package com.valoores.datacrowd.app.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "REF_COM_CNTRY_GRP_COUNTRY", schema = "SDEDBA")
public class REF_COM_CNTRY_GRP_COUNTRY {

	@Id
	@Hidden
	@Column(name = "GCO_ID")
	private int GCO_ID;
	
	@Column(name = "COU_ID")
	private int COU_ID;

	

}
