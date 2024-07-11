package com.valoores.v21.usm.app.logs.fieldhistorylog.model;

import static com.valoores.v21.usm.utils.Schemas.BMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bm_business_area_m", schema = BMDBA)
@Getter
@Setter
public class BMBusinessAreaM {
	
	@Id
	@Column(name="BUSINESS_AREA_ID")
	private long id;
	
	@Column(name= "business_area_id")
	private String business_area_id;   
	
	@Column(name= "elem_data_id")
	private long elem_data_id;

}
