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

	@Entity
	@Table(name = "wfm_automatic_escalation", schema = WFMDBA)
	@Getter
	@Setter
	public class AutomaticEscalationModel {
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="WFM_SEQ_AUTO_ESCALATION_ID")
		@SequenceGenerator(name = "WFM_SEQ_AUTO_ESCALATION_ID", schema = "WFMDBA", sequenceName = "WFM_SEQ_AUTO_ESCALATION_ID", allocationSize = 1)
		@Column(name = "AUTO_ESCALATION_ID")
		private long autoEscalationId;
		
		@Column(name = "ESCALATION_TYPE")
		private long escalationType;
		
		@Column(name = "PARENT_ESCALATOR_CODE")
		private long parentEscalatorCode;
			
		@Column(name = "CHILD_ESCALATOR_CODE")
		private long childEscalatorCode;
		
		@Column(name = "BEGIN_DATE")
		private Date beginDate;
		
		@Column(name = "END_DATE")
		private Date endDate;
		
		@Column(name = "CREATION_DATE")
		private Date creationDate;
		
		@Column(name = "CREATED_BY")
		private long createdBy;
		
		@Column(name = "UPDATE_DATE")
		private Date updateDate;
		
		@Column(name = "UPDATED_BY")
		private long updatedBy;
		
		@Column(name = "ESCALATION_OBJECT")
		private long escalationObject;
		


}


