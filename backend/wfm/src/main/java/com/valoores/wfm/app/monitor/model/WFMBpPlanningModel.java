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

	public class WFMBpPlanningModel {
		@Id
		@Column(name = "BP_PLAN_ID")
		private long bpPlanId;
		
		@Column(name = "BP_ID")
		private long bpId;
		
		@Column(name = "BP_BDATE")
		private Date bpBDate;
			
		@Column(name = "BP_EDATE")
		private Date bpEDate;
		
		@Column(name = "BP_STATUS_CODE")
		private long bpStatusCode;
		
		@Column(name = "CREATION_DATE")
		private Date creationDate;
		
		@Column(name = "CREATED_BY")
		private long createdBy;
		
		@Column(name = "UPDATE_DATE")
		private Date updateDate;
			
		@Column(name = "UPDATED_BY")
		private long updatedBy;
		
		@Column(name = "COMMENTS")
		private String comments;
		
	

}
