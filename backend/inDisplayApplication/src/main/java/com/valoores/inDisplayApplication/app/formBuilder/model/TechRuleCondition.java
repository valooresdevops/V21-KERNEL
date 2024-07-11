package com.valoores.inDisplayApplication.app.formBuilder.model;
import static com.valoores.inDisplayApplication.utils.Schemas.TECHDBA;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TECH_RULE_DATA", schema = TECHDBA)
@Getter
@Setter
public class TechRuleCondition {

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="S_RULE_DATA_SEQ")
	@SequenceGenerator(name = "S_RULE_DATA_SEQ", schema = "TECHDBA", sequenceName = "S_RULE_DATA_SEQ", allocationSize = 1)
	@Column(name = "MAIN_ID")
	private long mainId;
	
	@Column(name = "RULE_ID")
	private long ruleId;
	
	@Column(name = "CONDITION_ID")
	private long conditionId;
}

