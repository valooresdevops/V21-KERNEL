package com.valoores.inDisplayApplication.app.formBuilder.model.DynamicRuleBuilder;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;

@Entity
@Table(name = "TECH_DYNAMIC_RULE_BUILDER", schema = "TECHDBA")
@Data
public class RuleBuilderComboModel {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_RULE_ID")
    @SequenceGenerator(name = "S_RULE_ID", schema = "TECHDBA", sequenceName = "S_RULE_ID", allocationSize = 1)
	@Hidden
	@Column(name="RULE_ID")
	private Long id;
	
	@Column(name="OBJECT_ID")
	private long objectId;
	
	@Column(name="RULE_ACTION")
	private long ruleAction;
	
	@Column(name="RULE_DATA")
	private String ruleData;
	
	@Column(name="RULE_DESCRIPTION")
	private String name;
	
	@Column(name="ORDER_NO")
	private String orderNo;
	
	@Column(name="COLUMN_ID")
	private long columnId;
	
	@Column(name="CREATION_DATE")
	private Date creationDate;
	
	@Column(name="CREATED_BY")
	private long createdBy;
	
	@Column(name="UPDATE_DATE")
	private Date updateDate;
	
	@Column(name="UPDATED_BY")
	private long updatedBy;
	
	@Column(name="IS_EXCLUDED")
	private long isExcluded;
}

