package com.valoores.wfm.app.monitor.model;

import static com.valoores.wfm.utils.Schemas.WFMDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;



	@Entity
	@Table(name = "wfm_bp_activity_document", schema = WFMDBA)
	@Getter
	@Setter

	public class WFMBpActivityDocumentModel {
		@Id
		@Column(name = "BP_ACTIVITY_DOC_ID")
		private long bpActivityDocId;
		
		@Column(name = "BP_ACTIVITY_ID")
		private long bpActivityId;
		
		@Column(name = "DOC_TYPE")
		private long docType;
			
		@Column(name = "DOC_FORMAT")
		private long docFormat;
		
		@Column(name = "DOC_CODE")
		private long docCode;
		
		@Column(name = "USE_INTEGRATION_SETTINGS")
		private String useIntegrationSettings;
		
		@Column(name = "CREATION_DATE")
		private Date creationDate;
		
		@Column(name = "UPDATE_DATE")
		private Date updateDate;
		
		@Column(name = "CREATED_BY")
		private long createdBy;
			
		@Column(name = "UPDATED_BY")
		private long updatedBy;
		
		@Column(name = "DOC_DISPLAY_NAME")
		private String docDisplayName;
		
		@Column(name = "DOC_EXTENSION")
		private long docExtension;
		
		@Column(name = "DOC_FILE")
		private byte[] docFile;
		
}
