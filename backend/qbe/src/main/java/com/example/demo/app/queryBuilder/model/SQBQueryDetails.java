package com.example.demo.app.queryBuilder.model;

import static com.example.demo.utils.Schemas.QBEDBA;

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
@Table(name = "qbe_user_query_details", schema = QBEDBA)
@Getter
@Setter
public class SQBQueryDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="QBEDBA.QBE_SEQ_QBE_D_ID")
	@SequenceGenerator(name = "QBE_SEQ_QBE_D_ID", schema = "QBEDBA", sequenceName = "QBE_SEQ_QBE_D_ID", allocationSize = 1)
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "QBE_D_ID")
	private long QBE_D_ID;
	
	@Column(name = "QBE_ID")
	private long QBE_ID;
	
	@Column(name = "CREATED_BY")
	private long CREATED_BY;
		
	@Column(name = "CREATION_DATE")
	private Date CREATION_DATE;
	
	@Column(name = "XML_DATA")
	private byte[] XML_DATA;
}

