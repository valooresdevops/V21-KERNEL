package com.valoores.datacrowd.app.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "LOC_TELCO_BTS_CELL_DEF ", schema = "LOCDBA")
public class LOC_TELCO_BTS_CELL_DEF {

	@Id
	@SequenceGenerator(name = "S_TELCO_BTS_CELL_DEF", schema = "LOCDBA", sequenceName = "S_TELCO_BTS_CELL_DEF", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_TELCO_BTS_CELL_DEF")
	@Hidden
	@Column(name = "BTS_CELL_ID")
	private int shapeid;

	@Column(name = "BTS_CELL_NAME")
	private String name;

	@Column(name = "LOCATION_LONGITUDE ")
	private String lng;

	@Column(name = "LOCATION_LATITUDE ")
	private String lat;

	@Column(name = "EQUIPMENT_ID ")
	private int equipmentid;

	@Column(name = "CREATED_BY")
	private int createdby;
	
	@OneToMany(mappedBy = "equipmentid")
	private Set<INV_EQUIPMENT> invequipment;

}
