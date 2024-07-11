package com.valoores.datacrowd.app.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "LOC_LOCATION_MAP_OBJECT_SHAPE ", schema = "LOCDBA")
public class LOC_LOCATION_MAP_OBJECT_SHAPE {

	@Id
	@Hidden
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_LOCATION_MAP_OBJECT_SHAPE")
	@SequenceGenerator(name = "S_LOCATION_MAP_OBJECT_SHAPE", schema = "LOCDBA", sequenceName = "S_LOCATION_MAP_OBJECT_SHAPE", allocationSize = 1)
	@Column(name = "LOCATION_MAP_OBJECT_SHAPE_ID")
	private int shapeid;
	
	@Column(name = "OBJECT_SHAPE_DESC")
	private String shapeName;
	
	@Column(name = "MAP_ID")
	private int map_id;

	@Column(name = "OBJECT_SHAPE_TYPE")
	private int object_shape_type;
	
	@Column(name = "CREATED_BY")
	private int createdby;
	
	@Lob
	@Column(name = "OBJECT_SHAPE_DATA")
	private byte[]  objectShapeValue;
	

	
}
