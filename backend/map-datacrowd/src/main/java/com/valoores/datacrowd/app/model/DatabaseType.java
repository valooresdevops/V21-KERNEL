package com.valoores.datacrowd.app.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="TECH_DATA_BASE_TYPE",schema="techdba")
public class DatabaseType {
	@Id
	@Column(name="DB_ID")
	private long  DB_ID;
	
	@Column(name="DB_NAME")
	private String  DB_NAME;
	
	
	
}
