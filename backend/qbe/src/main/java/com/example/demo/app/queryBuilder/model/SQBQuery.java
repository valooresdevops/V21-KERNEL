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
@Table(name = "qbe_user_query", schema = QBEDBA)
@Getter
@Setter
public class SQBQuery {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="QBEDBA.QBE_SEQ_QBE_ID")
	@SequenceGenerator(name = "QBE_SEQ_QBE_ID", schema = "QBEDBA", sequenceName = "QBE_SEQ_QBE_ID", allocationSize = 1)
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "QBE_ID")
	private long queryId;
	
	@Column(name = "USR_CODE")
	private long USR_CODE;
	
	@Column(name = "QUERY_NAME")
	private String QUERY_NAME;
	
	@Column(name = "DATA_STORE_ID")
	private int DATA_STORE_ID;
	
	@Column(name = "VERSION_NO")
	private Integer VERSION_NO;
	
	@Column(name = "import_flag")
	private Integer import_flag;
	
	@Column(name = "COMMENTS")
	private String COMMENTS;
	
	@Column(name = "CREATION_DATE")
	private Date CREATION_DATE;
	
	@Column(name = "CREATED_BY")
	private long CREATED_BY;
	
	@Column(name = "RELEASE_NO")
	private Integer RELEASE_NO;
	
	@Column(name = "IS_BIG_QUERY")
	private Integer IS_BIG_QUERY;
	
	@Column(name = "IS_FOR_ADVANCED_SEARCH")
	private String IS_FOR_ADVANCED_SEARCH;
	
	@Column(name = "IS_SYSTEM_QUERY")
	private String IS_SYSTEM_QUERY;

}

