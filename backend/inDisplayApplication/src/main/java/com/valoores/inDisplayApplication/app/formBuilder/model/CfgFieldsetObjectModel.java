package com.valoores.inDisplayApplication.app.formBuilder.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "CFG_COLUMN_GROUP_OBJECT ", schema = "SUITEDBA")
@Data
@IdClass(CfgFieldsetObjectModelId.class)
public class CfgFieldsetObjectModel   implements Serializable {

	@Id
	@Column(name="OBJECT_ID")
	private long objectId;
	
	@Id
	@Column(name="COLUMN_GROUP_ID")
	private long fieldSetId;
	
	@Column(name="CREATED_BY")
	private long createdBy;
	
	@Column(name="CREATION_DATE")
	private Date creationDate;
}
