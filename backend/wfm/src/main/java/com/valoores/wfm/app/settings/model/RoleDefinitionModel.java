package com.valoores.wfm.app.settings.model;

import static com.valoores.wfm.utils.Schemas.USMDBA;

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
	@Table(name = "usm_role_definition", schema = USMDBA)
	@Getter
	@Setter
	public class RoleDefinitionModel {
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="USM_SEQ_ROLE_ID")
		@SequenceGenerator(name = "USM_SEQ_ROLE_ID", schema = "USMDBA", sequenceName = "USM_SEQ_ROLE_ID", allocationSize = 1)
		@Column(name = "ROLE_ID")
		private long roleId;
		
		@Column(name = "ROLE_NAME")
		private String roleName;
		
		@Column(name = "HRE_ID")
		private long hreId;
			
		@Column(name = "OSE_ID")
		private long oseId;
		
		@Column(name = "CREATION_DATE")
		private Date creationDate;
		
		@Column(name = "UPDATE_DATE")
		private Date updateDate;
		
		@Column(name = "CREATED_BY")
		private long createdBy;
		
		@Column(name = "UPDATED_BY")
		private long updatedBy;
		
		@Column(name = "IS_DUAL_AUTHENTICAT_REQUIRED")
		private String isDualAuthenticatRequired;
		
		@Column(name = "IS_BIOMETRIC_AUTHNTICAT_NEEDED")
		private String isBiometricAuthenticatNeeded;
	}



