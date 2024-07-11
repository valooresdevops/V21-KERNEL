package com.valoores.inDisplayApplication.app.dynamicReport.model;

import static com.valoores.inDisplayApplication.utils.Schemas.SUITEDBA;

import java.util.Date;

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
@Table(name = "cfg_report_column_filter", schema = SUITEDBA)
@Getter
@Setter
public class CfgReportColumnFilterModel {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="S_REPORT_COLUMN_FILTER")
	@SequenceGenerator(name = "S_REPORT_COLUMN_FILTER", schema = "SUITEDBA", sequenceName = "S_REPORT_COLUMN_FILTER", allocationSize = 1)
	@Column(name = "REPORT_COLUMN_FILTER_ID")
	private long reportColumnFilterId;
	
	@Column(name = "REPORT_DYNAMIC_CONFIG_ID")
	private long reportDynamicConfigId;
	
	@Column(name = "COLUMN_ID")
	private long columnId;
	
	@Column(name = "CONDITION_TYPE_CODE")
	private long conditionTypeCode;
		
	@Column(name = "CONDITION_VALUE")
	private String conditionValue;

	@Column(name = "CREATION_DATE")
	private Date creationDate;
	
	@Column(name = "CREATED_BY")
	private long createdBy;
	
	@Column(name = "MAX_CONDITION_VALUE")
	private String maxConditionValue;
	
	@Column(name = "BOOLEAN_OPERATOR")
	private long booleanOperator;
	
	@Column(name = "UPDATE_DATE")
	private Date updateDate;
	
	@Column(name = "UPDATED_BY")
	private long updatedBy;
	
	@Column(name = "PERIOD_TYPE")
	private long periodType;
	
	@Column(name = "PARAMETER_DATE_TYPE_CODE")
	private long parameterDateTypeCode;
	
	@Column(name = "PERIOD_NO")
	private long periodNo;
	
	@Column(name = "TECH_IS_DYNAMIC_DATE")
	private String techIsDynamicDate;
	
	@Column(name = "TECH_QUERY_CONDITION_COLUMN")
	private String techQueryConditionColumn;
	
	@Column(name = "ORDER_NO")
	private long orderNo;
}
