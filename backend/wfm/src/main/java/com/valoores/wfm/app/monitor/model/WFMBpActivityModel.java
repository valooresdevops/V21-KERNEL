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

	public class WFMBpActivityModel {
		@Id
		@Column(name = "BP_ACTIVITY_ID")
		private long bpActivityId;
		
		@Column(name = "BP_ID")
		private long bpId;
		
		@Column(name = "ACTIVITY_ID")
		private long activityId;
			
		@Column(name = "BP_ACTIVITY_NAME")
		private String bpActivityName;
		
		@Column(name = "BP_ACTIVITY_DURATION")
		private long bpActivityDuration;
		
		@Column(name = "COMMENTS")
		private String comments;
		
		@Column(name = "CREATION_DATE")
		private Date creationDate;
		
		@Column(name = "CREATED_BY")
		private long createdBy;
		
		@Column(name = "UPDATE_DATE")
		private Date updateDate;
		
		@Column(name = "UPDATED_BY")
		private long updatedBy;
		
		@Column(name = "X_COORDINATE")
		private long xCoordinate;
		
		@Column(name = "Y_COORDINATE")
		private long yCoordinate;
		
		@Column(name = "IS_VALID_NEED")
		private String isValidNeed;
		
		@Column(name = "DURATION_UOT")
		private long durationUot;
			
		@Column(name = "IS_SHOWN_ON_DASHBOARD")
		private String isShownOnDashboard;
		
		@Column(name = "BP_ACTIVITY_PRIORITY")
		private long bpActivityPriority;
		
		@Column(name = "AUTO_VAL_REJ_EXEC")
		private long autoValRejExec;
		
		@Column(name = "AUTO_VAL_REJ_EXEC_TIME_LAPS")
		private long autoValRejExecTimeLaps;
		
		@Column(name = "MANUAL_ACTIVITY_DESCRIPTION")
		private String manualActivityDescription;
		
		@Column(name = "IS_SHARED")
		private String isShared;
			
		@Column(name = "MSG_TYPE")
		private long msgType;
		
		@Column(name = "TEMPLATE_ID")
		private long templateId;
		
		@Column(name = "MSG_SUBJECT")
		private String msgSubject;
		
		@Column(name = "QBE_ID")
		private long qbeId;
		
		@Column(name = "IS_AUTO_COMPLETION")
		private String isAutoCompletion;
		
		@Column(name = "ACTIVITY_DURATION")
		private long activityDuration;
			
		@Column(name = "IS_SYSTEM")
		private String isSystem;
		
		@Column(name = "TECH_IS_CURRENTLY_EXECUTED")
		private String techIsCurrentlyExecuted;
		
		@Column(name = "EXECUTION_MSG_DESC")
		private String executionMsgDesc;
		
		
		
}
