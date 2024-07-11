package com.valoores.inDisplayApplication.app.formBuilder.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Null;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;
@Entity
@Table(name = "CFG_COLUMN_CONFIG", schema = "SUITEDBA")
@Data
public class CfgColumnConfigModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_column_config")
    @SequenceGenerator(name = "s_column_config", schema = "SUITEDBA", sequenceName = "s_column_config", allocationSize = 1)
	@Hidden
	@Column(name="COLUMN_ID")
	private long columnId;

	@Column(name="COLUMN_NAME")
	private String columnName;
	
	@Column(name="COLUMN_GROUP_ID")
	private long groupId;
	
	@Null
	@Column(name="COLUMN_TYPE_CODE")
	private long columnType;
	
	@Null
	@Column(name="COLUMN_LENGTH")
	private long columnLength;
	
	@Null
	@Column(name="IS_MANDATORY")
	private String isMandatory;
	
	@Null
	@Column(name="IS_EXECUTION_SUSPENDED")
	private String isSuspended;
	
	@Null
	@Column(name="HAS_MULTIPLE_VALUE")
	private String isMultiple;
	
	@Column(name="ORDER_NO")
	private long orderNo;
	
	@Null
	@Column(name="COLUMN_DESC")
	private String columnDescription;

	@Null
	@Column(name="QBE_ID")
	private Long query;

	@Null
	@Column(name="CELL_RENDERING")
	private String sizeField;
	
	@Column(name="CREATED_BY")
	private long createdBy;
	
	@Column(name="TABLE_ID")
	private long tableId;
	
	@Null
	@Column(name="IS_SAVED")
	private String isExcluded;
	
	@Null
	@Column(name="IS_HYPERLINK")
	private String isLink;
	
	@Null
	@Column(name="READONLY_QBE_ID")
	private Long qbeReadOnly;
	
	@Null
	@Column(name="DEFAULT_QBE_ID")
	private Long defaultValue;
	
	@Column(name="COLUMN_P_ID")
	private Long columnPId;
	
	@Column(name="JAVA_METHOD_FILE")
	private byte[] blobFile;
	
	@Column(name="OBJECT_ID")
	private Long menus;
	
	@Column(name="DEPENDENT_QBE_ID")
	private Long dependencyDefaultValue;
	
	@Null
	@Column(name="INPUT_LAN_ID")
	private Integer languageId;
	
	@Null
	@Column(name="MANDATORY_QBE_ID")
	private Long mandatoryQuery;
	
	@Null
	@Column(name="IS_HIDDEN")
	private String isGridHidden;
	
	@Null
	@Column(name="IS_GRID_FORM_INSERT")
	private String isEditable;
	
	
	@Column(name="is_list")
	private String isList;

	@Column(name="UPDATED_BY")
	private Long updatedBy;

	
}
