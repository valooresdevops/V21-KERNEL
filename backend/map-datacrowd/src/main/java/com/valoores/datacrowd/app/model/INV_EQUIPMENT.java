package com.valoores.datacrowd.app.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "INV_EQUIPMENT ", schema = "INVDBA")
public class INV_EQUIPMENT {

	@Id
	@Hidden
	@Column(name = "EQUIPMENT_ID")
	private int equipmentid;
	
	@Column(name = "EQUIPMENT_MODEL_NAME ")
	private String type;
	
	@Column(name = "EQUIPMENT_TYPE_ID ")
	private int equipmentTypeId;
	
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="EQUIPMENT_ID", nullable=false ,insertable=false, updatable=false)
	private LOC_TELCO_BTS_CELL_DEF loctelco; 
		
	  

}
