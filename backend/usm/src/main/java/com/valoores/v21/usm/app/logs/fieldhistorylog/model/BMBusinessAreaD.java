package com.valoores.v21.usm.app.logs.fieldhistorylog.model;

import static com.valoores.v21.usm.utils.Schemas.BMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bm_business_area_d", schema = BMDBA)
@Getter
@Setter
public class BMBusinessAreaD {
	
	@Id
	@Column(name="COL_SEQUENCE")
	private long id;
	
	@Column(name= "business_area_id")
	private String business_area_id;   
	
	@Column(name= "elem_data_id")
	private String elem_data_id;

}
