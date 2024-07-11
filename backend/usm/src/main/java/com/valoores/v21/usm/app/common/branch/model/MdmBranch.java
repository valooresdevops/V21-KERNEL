package com.valoores.v21.usm.app.common.branch.model;

import static com.valoores.v21.usm.utils.Schemas.MDMDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "MDM_BRANCH", schema = MDMDBA)
@Getter
@Setter
public class MdmBranch {
	@Id
	@Column(name = "BRANCH_ID")
	private long branchId;
	
	@Column(name = "BRANCH_NAME")
	private String branchName;
	
	@Column(name = "BRANCH_INTERNAL_CODE")
	private long branchInternalCode;
	
	@Column(name = "SOC_ID")
	private long socId;
	
	@Column(name = "BRANCH_SNAME")
	private String branchSName;
	
	@Column(name = "MANAGER_NAME")
	private String managerName;
	
	@Column(name = "STATUS_CODE")
	private long statusCode;
	
	@Column(name = "STATUS_BDATE")
	private Date statusBDate;
	
	@Column(name = "BANK_ID")
	private long bankId;
	
	@Column(name = "CENTRAL_BANK_BRANCH_CODE")
	private long centralBankBranchCode;
	
	@Column(name = "MANAGER_ASSISTANT_NAME")
	private String managerAssistantName;
	
	@Column(name = "HEAD_TELLER_NAME")
	private String headTellerName;
	
	@Column(name = "PERSONAL_REPRESENTATIVE_NAME")
	private String personalRepresentativeName;
	
	@Column(name = "PHONE_NO")
	private String phoneNo;
	
	@Column(name = "BRANCH_TYPE_CODE")
	private long branchTypeCode;
	
	@Hidden
	@JsonIgnore
	@Column(name = "CREATION_DATE")
	private Date creationDate;
	
	@Hidden
	@JsonIgnore
	@Column(name = "CREATED_BY")
	private long createdBy;
	
	@Hidden
	@JsonIgnore
	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	@Hidden
	@JsonIgnore
	@Column(name = "UPDATED_BY")
	private long updatedBy;
}