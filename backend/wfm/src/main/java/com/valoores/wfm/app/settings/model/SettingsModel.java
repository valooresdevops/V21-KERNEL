package com.valoores.wfm.app.settings.model;

import static com.valoores.wfm.utils.Schemas.WFMDBA;

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

public class SettingsModel {
	@Entity
	@Table(name = "qbe_user_query_details", schema = WFMDBA)
	@Getter
	@Setter
	public class SQBQueryDetailsModel {
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="QBE_SEQ_QBE_D_ID")
		@SequenceGenerator(name = "QBE_SEQ_QBE_D_ID", schema = "WFMDBA", sequenceName = "QBE_SEQ_QBE_D_ID", allocationSize = 1)
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
}
