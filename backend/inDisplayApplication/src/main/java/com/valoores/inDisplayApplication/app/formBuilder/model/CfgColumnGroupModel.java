package com.valoores.inDisplayApplication.app.formBuilder.model;

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
@Table(name = "CFG_COLUMN_GROUP ", schema = "SUITEDBA")
@Data
public class CfgColumnGroupModel {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_COLUMN_GROUP")
    @SequenceGenerator(name = "S_COLUMN_GROUP", schema = "SUITEDBA", sequenceName = "S_COLUMN_GROUP", allocationSize = 1)
	@Hidden
	@Column(name="COLUMN_GROUP_ID")
	private long id;

	@Column(name="COLUMN_GROUP_CODE")
	private long groupCode;
	
	@Column(name="COLUMN_GROUP_DESC")
	private String name;

	@Column(name="ORDER_NO")
	private long orderNb;
	
	@Column(name="CREATED_BY")
	private long createdBy;
	
	@Column(name="CREATION_DATE")
	private Date creationDate;
	
	@Column(name="IS_READ_ONLY")
	private String isreadOnly;
	
	@Column(name="IS_ADVANCED_SEARCH_APPLIED")
	private String advancedSearch;
	
	@Column(name="IS_HIDDEN")
	private String isHidden;
}