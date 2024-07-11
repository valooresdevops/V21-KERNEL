package com.valoores.v21.usm.app.securitymanagement.role.model;

import static com.valoores.v21.usm.utils.Schemas.USMDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USM_ROLE_MULTI_MISC_INFO", schema = USMDBA)
@Getter
@Setter
@NoArgsConstructor
public class USMRoleMulti {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_MULTI_SEQ_ID")
	@SequenceGenerator(name = "ROLE_MULTI_SEQ_ID", schema = "USMDBA", sequenceName = "S_ROLE_MULTI_MISC_INFO", allocationSize = 1)
	@Column(name="ROLE_MULTI_MISC_INFO_ID")
	private long multiId;
	
	@Column(name = "ACCESS_CODE")
	private String accessCode;
	
	@Column(name = "MENU_CODE")
	private String menuCode;
	
	@Column(name = "CREATION_DATE")
	@Transient
	private Date creationDate;
	
	@Column(name = "CREATED_BY")
	@Hidden
	private long createdBy;
	
	@Column(name = "role_id")
	private long roleId;
	
	@Column(name = "bsn_group_id")
	private Integer bugId;
	
	@Column(name = "bsn_group_type_code")
	private Integer bugTypeId ;
	
	@Column(name = "UPDATE_DATE")
	@Hidden
	private Date updateDate;
	
	@Column(name = "UPDATED_BY")
	@Hidden
	private long updatedBy;
}
