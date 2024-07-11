package com.valoores.cassandra.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Getter
@Setter
@NoArgsConstructor
@EntityScan
@Table(name="loc_location_rule_definition",schema="locdba")
public class Test {

	
	@Id
	@Column(name="LOCATION_RULE_ID")
	private int  LOCATION_RULE_ID;
	
	@Column(name="LOCATION_RULE_NAME")
	private String  LOCATION_RULE_NAME;
	
	@Column(name="LOCATION_RULE_DESC")
	private String  LOCATION_RULE_DESC;
	
	
	
	
}
