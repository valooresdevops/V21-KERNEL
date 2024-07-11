package com.valoores.v21.usm.app.securitymanagement.usermanagement.userrole.model;

import static com.valoores.v21.usm.utils.Schemas.USMDBA;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.valoores.v21.usm.app.securitymanagement.accessrights.model.USMUser;
import com.valoores.v21.usm.app.securitymanagement.role.model.USMRole;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.userrole.dto.USMUserRoleIds;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usm_user_role", schema = USMDBA)
@IdClass(USMUserRoleIds.class)
@Data
@NoArgsConstructor
public class USMUserRole {
	
	@Id
	@Column(name = "EMP_ID")
	@Hidden
	private long empId;
	
	@Id
	@Column(name = "ROLE_ID")
	private long roleId;
	
	@Column(name="IS_DEFAULT_ROLE")
	private String isDefaultRole;
	
	@ManyToMany
	@JoinColumn(name = "EMP_ID")
	private List<USMUser> usmUser;
	
	@ManyToOne
	@JoinColumn(name = "ROLE_ID", insertable = false, updatable = false)
	private USMRole usmRole;
	
	@Column(name = "CREATION_DATE")
	@Hidden
	private String creationDate;
	
	@Column(name = "CREATED_BY")
	@Hidden
	private String createdBy;
	
	@Column(name = "UPDATE_DATE")
	@Hidden
	private String updateDate;
	
	@Column(name = "UPDATED_BY")
	@Hidden
	private String updatedBy;
	
}
