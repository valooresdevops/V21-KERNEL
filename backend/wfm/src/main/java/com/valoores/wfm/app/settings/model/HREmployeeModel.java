package com.valoores.wfm.app.settings.model;

import static com.valoores.wfm.utils.Schemas.HRDBA;

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
	@Table(name = "hr_employee", schema = HRDBA)
	@Getter
	@Setter
	public class HREmployeeModel {
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="HR_SEQ_EMP_ID")
		@SequenceGenerator(name = "HR_SEQ_EMP_ID", schema = "HRDBA", sequenceName = "HR_SEQ_EMP_ID", allocationSize = 1)
		@Column(name = "EMP_ID")
		private long empId;
		
		@Column(name = "CONTRACT_TYPE_ID")
		private long contractTypeId;
		
		@Column(name = "EMP_CIVILITY")
		private long empCivility;
			
		@Column(name = "EMP_ORIGIN")
		private long empOrigin;
		
		@Column(name = "ESTABLISHMENT_ID")
		private long establishmentId;
		
		@Column(name = "FAMILY_SITUATION_ID")
		private long familySituationId;
		
		@Column(name = "EMP_FNAME")
		private String empFname;
		
		@Column(name = "HANDICAP_LEVEL_ID")
		private long handicapLevelId;
		
		@Column(name = "HAS_COMPUTER_KNOWLEDGE")
		private String hasComputerKnowledge;
		
		@Column(name = "HAS_LOAN")
		private String hasLoan;
		
		@Column(name = "IS_AVAILABLE")
		private String isAvailable;
		
		@Column(name = "IS_EMP_CURNT_PURSUING_DEGREE")
		private String isEmpCurntPursuingDegree;
		
		@Column(name = "IS_INTERNAL")
		private String isInternal;
		
		@Column(name = "EMP_NAME")
		private String empName;
		
		@Column(name = "NATIONALITY_ID") 
		private long nationalityId;
		
		@Column(name = "PERMIT_ID")
		private long permitId;
		
		@Column(name = "MANAGER_EMP_ID")
		private long managerEmpId;
		
		@Column(name = "CREATED_BY")
		private long createdBy;
		
		@Column(name = "EMP_FNAME2")
		private String empFname2;
		
		@Column(name = "EMP_FNAME3")
		private String empFname3;
			
		@Column(name = "EMP_MAIDEN_NAME")
		private String empMaidenName;
		
		@Column(name = "EMP_CUR_STATUS")
		private long empCurStatus;
		
		@Column(name = "EMP_MATRICIDE")
		private String empMatricide;
		
		@Column(name = "EMP_DOB")
		private Date empDob;
		
		@Column(name = "EMP_BIRTH_LOCATION")
		private String empBirthLocation;
		
		@Column(name = "EMP_SEX")
		private String empSex;
		
		@Column(name = "CONTRACT_BDATE")
		private Date contractBdate;
		
		@Column(name = "CONTRACT_EDATE")
		private Date cotractEdate;
		
		@Column(name = "HIRING_DATE")
		private Date hiringDate;
		
		@Column(name = "HIERARCHICAL_LEVEL")
		private String heirarchicalLevel;
		
		@Column(name = "SENIORITY_DATE")
		private Date seniorityDate;
		
		@Column(name = "EMP_PHOTO")
		private String empPhoto;
		
		@Column(name = "CREATION_DATE")
		private Date creationDate;
		
		@Column(name = "UPDATE_DATE")
		private Date updateDate;
		
		@Column(name = "UPDATED_BY")
		private long updatedBy;
		
		@Column(name = "NIF_CODE")
		private String nifCode;
		
		@Column(name = "EMP_CUR_STATUS_BDATE")
		private Date empCurStatusBdate;
		
		@Column(name = "EMP_IMAGE")
		private byte[] empImage;
		
		@Column(name = "EMP_INTERNAL_CODE")
		private String empInternalCode;
		
		@Column(name = "LAST_EXTRACTION_DATE")
		private Date lastExtractionDate;
		
		@Column(name = "PERSON_ID")
		private long personId;
		
		@Column(name = "LEGACY_EMP_NAME")
		private String legacyEmpName;
		
		@Column(name = "EMP_FULL_NAME")
		private String empFullName;
	
}
