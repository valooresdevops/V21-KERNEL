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
@Table(name = "USM_ROLE_MISC_INFO", schema = USMDBA)
@Getter
@Setter
@NoArgsConstructor
public class USMRole {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_SEQ_ID")
	@SequenceGenerator(name = "ROLE_SEQ_ID", schema = "USMDBA", sequenceName = "S_ROLE_MISC_INFO", allocationSize = 1)
	@Column(name = "ROLE_ID")
	private long id;
	
//	@ManyToMany(mappedBy = "usmRoles")
//	@JsonIgnore
//	private Set<USMUser> usmUsers;
	
//    @OneToMany(mappedBy="usmRole")
//    private Set<USMUserMulti> usmUserMulti;

	@Column(name = "ROLE_NAME")
	private String roleName;
	
	@Column(name = "IS_DUAL_AUTHENTICAT_REQUIRED")
	private String isDualAuthentication;
	
	@Column(name = "ROLE_TYPE")
	private Integer roleType;
	
	@Column(name = "CREATION_DATE")
	@Transient
	private Date creationDate;
	
	@Column(name = "CREATED_BY")
	@Hidden
	private String createdBy;
	
	@Column(name = "UPDATED_BY")
	private String updatedBy;
	
	@Column(name = "UPDATE_DATE")
	private Date updateDate;
}