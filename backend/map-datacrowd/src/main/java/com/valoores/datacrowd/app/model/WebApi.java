package com.valoores.datacrowd.app.model;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="WEB_API_METHOD_LOG_DETAILS",schema="SSDX_ENG")
public class WebApi {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_API_METHOD_LOG_DET")
	@SequenceGenerator(name = "S_API_METHOD_LOG_DET", schema = "SSDX_ENG", sequenceName = "S_API_METHOD_LOG_DET", allocationSize = 1)
	@Column(name="METHOD_LOG_ID")
	private int  methodlogId;
	
	@Column(name="METHOD_ID")
	private int  methodId;
	
	@Column(name="EXCUTION_BDATE",nullable = false, columnDefinition = "TIMESTAMP DEFAULT SYSDATE")
	private Date  excutionBdate;
	
	@Column(name="EXECUTION_EDATE")
	private Date  executionEdate;
	
	@Lob
	@Column(name="EXCUTION_RESULT")
	private String  excutionResult;
	
	@Lob
	@Column(name="EXECUTION_PARAM")
	private String  executionParam;
	
	
	
	
	
	
}
