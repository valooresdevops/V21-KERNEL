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
@Table(name = "TECH_DYN_MAP_TYPES", schema = "techdba")
public class TechMapTypes {

	@Id
	@Hidden
	@Column(name = "MAP_TYPE_ID")
	private long maptypeid;

	@Column(name = "VALUE")
	private String value;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "ID")
	private String id;
	
	@Column(name = "IS_ONLINE")
	private String isonline;
	
	@Column(name = "FLAG")
	private String flag;

}