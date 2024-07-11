package com.valoores.v21.usm.app.securitymanagement.role.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class USMRoleDto {

	@Hidden
	private long id;
	private String roleName;
	private Integer roleType;
	private String roleTypeName;
	private String isDualAuthentication;
    private Set<String> bugName = new HashSet<>();    
//	private ArrayList<String> bugName = new ArrayList<String>();
//	private ArrayList<ArrayList> bugType = new ArrayList<ArrayList>();
	private Integer bugType; 
	private Set<String> bugType1 = new HashSet<>();  
	private String creationDate;
	private String createdBy;
	private String userId;
	private String updatedBy;
	private ArrayList<String> ListOfBugName = new ArrayList<String>();


}
