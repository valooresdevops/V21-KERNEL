package com.valoores.inDisplayApplication.app.formBuilder.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "qbe_user_query", schema = "qbedba")
@Data
public class QbeUserQueryModel {

	@Id
	@Column(name="QBE_ID")
	private long id;
	
	
	@Column(name="QUERY_NAME")
	private String name;
	
	@Column(name="CREATION_DATE")
	private String creationDate;
	
	@Column(name="CREATED_BY")
	private long createdBy;
	
}
