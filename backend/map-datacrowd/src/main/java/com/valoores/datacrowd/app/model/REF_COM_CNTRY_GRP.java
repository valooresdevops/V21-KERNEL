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
@Table(name = "REF_COM_CNTRY_GRP ", schema = "SDEDBA")
public class REF_COM_CNTRY_GRP {

	@Id
	@Hidden
	@Column(name = "GCO_ID")
	private int GCO_ID;
	

	
	@Column(name = "GCO_P_ID")
	private int GCO_P_ID;
	
	@Column(name = "GCO_INTERNAL_CODE")
	private int GCO_INTERNAL_CODE;
	
	

}
