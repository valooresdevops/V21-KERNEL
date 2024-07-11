package com.valoores.inDisplayApplication.app.formBuilder.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "ALL_OBJECTS", schema = "SYS")
@Immutable
public class AllObjectsModel {
	
	@Id
	@Column(name="OWNER", insertable = false, updatable = false)
	private String owner;
	
	@Column(name="OBJECT_NAME", insertable = false, updatable = false)
	private String objectName;
	
	@Column(name="OBJECT_TYPE", insertable = false, updatable = false)
	private String objectType;
}
