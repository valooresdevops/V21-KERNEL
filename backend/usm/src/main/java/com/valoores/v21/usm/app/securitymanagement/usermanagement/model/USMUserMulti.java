package com.valoores.v21.usm.app.securitymanagement.usermanagement.model;

import static com.valoores.v21.usm.utils.Schemas.USMDBA;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.valoores.v21.usm.app.securitymanagement.accessrights.model.USMUser;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usm_user_multi_misc_info", schema = USMDBA)
@NoArgsConstructor
@Getter
@Setter
public class USMUserMulti {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "s_user_multi_misc_info")
	@SequenceGenerator(name = "s_user_multi_misc_info", schema = "USMDBA", sequenceName = "s_user_multi_misc_info", allocationSize = 1)
	@Column(name = "USER_MULTI_MISC_INFO_ID")
	@Hidden
	private long id;
	
	@Column(name = "USER_ID")
	private long userId;
	
	@Column(name = "BSN_GROUP_ID")
	private Integer bsnGroupId;
	
	@Column(name = "STRUC_DATA_ID")
	private Integer strucDataId;
	
	@Column(name = "BSN_GROUP_TYPE_CODE")
	private Integer bsnGroupTypeId;

	@Column(name = "ROLE_ID")
	private Integer roleId;

	@Column(name = "IS_DEFAULT")
	private String isDefault;
	
	@Column(name = "CREATION_DATE")
	@Hidden
	private Date creationDate;
	
	@Column(name = "CREATED_BY")
	@Hidden
	private long createdBy;
	
	@Column(name = "UPDATE_DATE")
	@Hidden
	private Date updateDate;
	
	@Column(name = "UPDATED_BY")
	@Hidden
	private Long updatedBy;
	
	@Column(name = "ACCESS_CODE")
	private String accessCode;
	
	@Column(name = "MENU_CODE")
	private String menuCode;
	
  @ManyToOne
  @JsonIgnore
  @JoinColumn(name="USER_ID", nullable=false ,insertable=false, updatable=false)
  private USMUser usmUser; 
	
//  @ManyToOne
//  @JsonIgnore
//  @JoinColumn(name="ACCESS_CODE",insertable=false, updatable=false)
//  private USMAccessMatrix usmAccessMatrix; 
//	
//  @ManyToOne
//  @JsonIgnore
//  @JoinColumn(name="MENU_CODE",insertable=false, updatable=false)
//  private USMAccessRights usmAccessRights; 
	
}
