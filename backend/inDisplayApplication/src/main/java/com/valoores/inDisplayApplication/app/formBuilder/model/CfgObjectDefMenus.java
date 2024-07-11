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
public class CfgObjectDefMenus {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_OBJECT_DEF")
    @SequenceGenerator(name = "S_OBJECT_DEF", schema = "SUITEDBA", sequenceName = "S_OBJECT_DEF", allocationSize = 1)
	@Hidden
	@Column(name="object_Id")
	private long id;

	@Column(name="object_Type")
	private long objectType;
	
	@Null
	@Column(name="object_P_Id")
	private Long parentId;
	
	@Column(name="object_Name")
	private String name;
	
	@Column(name="IS_MAIN")
	private String isMain;
	
	@Column(name="created_by")
	private long createdBy;
	
	@Column(name = "UPDATE_DATE")
	private Date updateDate;
	
	@Column(name = "TECH_IS_GRID")
	private String isGrid;
	
	@Column(name = "TECH_IS_QUERY_FORM")
	private String isQueryForm;
	
	@Column(name = "IS_DYNAMIC_REPORT")
	private String isDynamicReport;
	
//	@Column(name = "IS")
//	private String isHidden;
	
	@Column(name = "has_full_display")
	private String isReadOnly;
	
	@Column(name = "HAS_ADVANCED_SEARCH")
	private String isAdvancedSearch;
	
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
}
