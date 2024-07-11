package com.valoores.inDisplayApplication.app.formBuilder.model;

import java.util.Date;

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
@Table(name = "CFG_OBJECT_DEF", schema = "SUITEDBA")
@Data
public class CfgObjectDefModel {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_OBJECT_DEF")
    @SequenceGenerator(name = "S_OBJECT_DEF", schema = "SUITEDBA", sequenceName = "S_OBJECT_DEF", allocationSize = 1)
	@Hidden
	@Column(name="object_Id")
	private long objectId;

	@Column(name="object_Type")
	private long objectType;
	
	@Null
	@Column(name="object_P_Id")
	private Long parentId;
	
	@Column(name="object_Name")
	private String menuName;
	
	@Column(name="IS_MAIN")
	private long isMain;
	
	@Column(name="ORDER_NO")
	private long orderNo;
	
	@Column(name="created_by")
	private long createdBy;
	
	@Column(name = "UPDATE_DATE")
	private Date updateDate;
	
	@Column(name = "TECH_IS_GRID")
	private long isGrid;
	
	@Column(name = "TECH_IS_QUERY_FORM")
	private String isQueryForm;
//	@Column(name = "IS")
//	private String isHidden;
	
	@Column(name = "IS_DYNAMIC_REPORT")
	private String isDynamicReport;
	
	@Column(name = "has_full_display")
	private String isReadOnly;
	
	@Column(name = "HAS_ADVANCED_SEARCH")
	private long isAdvancedSearch;
	
	@Null
	@Column(name = "QBE_ID")
	private Long sourceQuery;
	
	@Null
	@Column(name = "ADD_MODE_QBE_ID")
	private Long canAdd;
	
	@Null
	@Column(name = "MODIFY_MODE_QBE_ID")
	private Long canModify;
	
	@Null
	@Column(name = "DELETE_MODE_QBE_ID")
	private Long canDelete;
	
	@Null
	@Column(name = "HIDE_CONDITION_QBE_ID") 
	private Long condition;
	
	@Null
	@Column(name = "SAVE_CONDITION_QBE_ID")
	private Long isSave;
	
	@Null
	@Column(name = "READONLY_QBE_ID")
	private Long readOnlyQbeId;
	
	@Null
	@Column(name = "ADVANCED_SEARCH_PROCEDURE_NAME")
	private String advancedSearchProcedureName;
}