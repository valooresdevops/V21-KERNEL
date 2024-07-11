package com.valoores.inDisplayApplication.app.dynamicSearch.model;

import static com.valoores.inDisplayApplication.utils.Schemas.TECHDBA;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Table(name = "TECH_DYNAMIC_SEARCH", schema = TECHDBA)
@Data
public class DynamicSearch {
	
	
	@GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "PK_DYN_SRCH_ID")
    @SequenceGenerator(name = "PK_DYN_SRCH_ID", schema = "TECHDBA", sequenceName = "PK_DYN_SRCH_ID", allocationSize = 1)
	@Id
	@Column(name = "DYN_SRCH_ID")
	private long dynSrchId;
	
	@Column(name = "TABLE_NAME")
	private String tableName;
	
	@Column(name = "COL_NAME")
	private String colName;
	
	@Column(name = "COL_TYPE")
	private String colType;
	
	@Column(name = "COL_APP_NAME")
	private String colAppName;
	
	@Column(name = "PROPERTY")
	private String property;
	
	@Column(name = "CMB_SQL")
	private String cmbSQL;

	@Column(name = "IS_MANDATORY")
	private Integer isMandatory;

	@Column(name = "IS_DEFAULT")
	private Integer isDefault;

	@Column(name = "DEFAULT_VALUES")
	private String defaultValues;

	@Column(name = "CREATED_BY")
	private Integer createdBy;

	@CreationTimestamp
	@Column(name = "CREATION_DATE")
	private Date creationDate;

	@Column(name = "OBJECT_ID")
	private long objectId;

}

