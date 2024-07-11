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
	@Table(name = "wfm_bp_planning", schema = WFMDBA)
	@Getter
	@Setter

	public class WFMBusinessProcessModel {
		@Id
		@Column(name = "BP_ID")
		private long bpId;
		
		@Column(name = "BP_NAME")
		private String bpName;
		
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
		
		@Column(name = "IS_ACTIVITY_CHAINING")
		private String isActivityChaining;
			
		@Column(name = "IS_SYSTEM")
		private String isSystem;
		
		@Column(name = "PROCESS_DURATION")
		private long processDuration;
		
		@Column(name = "ACTIVITY_COUNT_NO")
		private long activityCountNo;
		
		@Column(name = "ELAPSED_TIME")
		private long elapsedTime;
		
		@Column(name = "BP_STATUS_CODE")
		private long bpStatusCode;
			
		@Column(name = "BP_BDATE")
		private Date bpBDate;
		
		@Column(name = "SETTING_ID")
		private long settingId;
		
		@Column(name = "TECH_BP_INFO")
		private byte[] techBpInfo;
		
		@Column(name = "KPI_THEME_HIERARCHY_ID")
		private long kpiThemeHeirarchyId;
		
		@Column(name = "DATA_SOURCE")
		private long dataSource;
			
	
}
