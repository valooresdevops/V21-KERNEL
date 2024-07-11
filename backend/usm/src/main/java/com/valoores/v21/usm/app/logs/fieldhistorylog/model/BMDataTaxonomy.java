package com.valoores.v21.usm.app.logs.fieldhistorylog.model;

import static com.valoores.v21.usm.utils.Schemas.BMDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bm_data_taxonomy", schema = BMDBA)
@Getter
@Setter

public class BMDataTaxonomy {
	@Id
	@Column(name="ELEM_DATA_ID")
	private long ELEM_DATA_ID;
	
	@Column(name= "elem_data_name")
	private String elem_data_name;   
	
	
}
