package com.example.demo.app.reportbuilder.model;
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
@Table(name ="XIB_REPORT", schema =QBEDBA)
@Getter
@Setter
public class ReportsModel {
	
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="XIB_SEQ_REPORT_ID")
		@SequenceGenerator(name = "XIB_SEQ_REPORT_ID", schema = "QBEDBA", sequenceName = "XIB_SEQ_REPORT_ID", allocationSize = 1)
		@Column(name = "REPORT_ID")
		private long id;
		
		@Column(name = "REPORT_NAME")
		private String REPORT_NAME;
		
		@Column(name = "REPORT_DESC")
		private String REPORT_DESC;
		
		@Column(name = "CREATION_DATE")
		private Date CREATION_DATE;
		
		@Column(name = "MEDIA_FILE")
		private byte[] MEDIA_FILE;
		
		@Column(name = "CREATED_BY")
		private long CREATED_BY;

		@Column(name = "DATA_SOURCE")
		private long DATA_SOURCE;

}
