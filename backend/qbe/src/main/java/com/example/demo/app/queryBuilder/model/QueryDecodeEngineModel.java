package com.example.demo.app.queryBuilder.model;
import static com.example.demo.utils.Schemas.TECHDBA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TECH_QBE_ENGINE_DATA", schema = TECHDBA)
@Getter
@Setter
public class QueryDecodeEngineModel {

	@Id
	@Column(name = "QBE_ID")
	private long queryId;
	
	@Column(name = "QUERY_DATA")
	private String query;
	
	@Column(name = "QUERY_NAME")
	private String queryName;
	
	@Column(name = "QUERY_PARAMS")
	private String queryParams;
	
	@Column(name = "EXEC_HEADS")
	private String execHeads;

}

	



