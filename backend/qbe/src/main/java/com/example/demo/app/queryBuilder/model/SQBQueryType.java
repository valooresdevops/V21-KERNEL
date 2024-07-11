package com.example.demo.app.queryBuilder.model;

import static com.example.demo.utils.Schemas.QBEDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@IdClass(queryTypeIdModel.class)
@Table(name = "qbe_query_type", schema = QBEDBA)
@Getter
@Setter

public class SQBQueryType {
	@Id
	@Column(name = "QBE_ID")
	private long QBE_ID;
	
	@Id
	@Column(name = "QUERY_TYPE_CODE")
	private long QUERY_TYPE_CODE;
	
	@Column(name = "CREATION_DATE")
	private Date CREATION_DATE;
	
	@Column(name = "CREATED_BY")
	private long CREATED_BY;
		
	@Column(name = "UPDATE_DATE")
	private Date UPDATE_DATE;
	
	@Column(name = "UPDATED_BY")
	private long UPDATED_BY;
	
}
