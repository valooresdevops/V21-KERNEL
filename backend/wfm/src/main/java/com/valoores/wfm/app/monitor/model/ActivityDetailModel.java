package com.valoores.wfm.app.monitor.model;

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
	@Table(name = "WFM_ACTIVITY_DETAIL", schema = WFMDBA)
	@Getter
	@Setter
	public class ActivityDetailModel {
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="WFM_SEQ_ACT_D_ID")
		@SequenceGenerator(name = "WFM_SEQ_ACT_D_ID", schema = "WFMDBA", sequenceName = "WFM_SEQ_ACT_D_ID", allocationSize = 1)
		@Column(name = "ACTIVITY_D_ID")
		private long activityDId;
		
		@Column(name = "ACTIVITY_ID")
		private long activityId;
		
		@Column(name = "STRUC_DATA_ID")
		private long strucDataId;
			
		@Column(name = "ELEM_DATA_ID")
		private Date elemDataId;
		
		@Column(name = "OPR_TYPE")
		private byte[] oprType;
		
		@Column(name = "IS_CHAR")
		private String isChar;
		
		@Column(name = "TRIGGER_NAME")
		private String triggerName;
		
		@Column(name = "CREATED_BY")
		private long createdBy;
			
		@Column(name = "CREATION_DATE")
		private Date creationDate;
		
		@Column(name = "UPDATED_BY")
		private long updatedBy;
		
		@Column(name = "UPDATE_DATE")
		private Date updateDate;
		
		@Column(name = "VPR_TRIGGER_NAME")
		private String vprTriggerName;
		
		@Column(name = "DAYS_BEFORE_REACHED_DATE")
		private long daysBeforeReachedDate;
			
		@Column(name = "IS_TRIGGER_ENABLED")
		private String isTriggerEnabled;
		
		@Column(name = "PROCESS_ID")
		private String processId;
		
	

}
