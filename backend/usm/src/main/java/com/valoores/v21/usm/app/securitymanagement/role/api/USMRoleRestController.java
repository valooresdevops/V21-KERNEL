package com.valoores.v21.usm.app.securitymanagement.role.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.common.syslines.model.Syslines;
import com.valoores.v21.usm.app.securitymanagement.role.dto.USMRoleDto;
import com.valoores.v21.usm.app.securitymanagement.role.model.USMRole;
import com.valoores.v21.usm.app.securitymanagement.role.model.USMSanction;
import com.valoores.v21.usm.app.securitymanagement.role.service.IUSMRoleService;
import com.valoores.v21.usm.backend.CustomResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "USM Roles", description = "USM roles exposed APIs")
@RestController
@RequestMapping("/api")
public class USMRoleRestController {

	@Autowired
	private IUSMRoleService usmRoleService;

	@Operation(summary = "Get all USM roles")
	@PostMapping(path = "/usmroles", produces = "application/json")
	public List<ObjectNode> getAllUSMRoles() {
		return usmRoleService.getAllUSMRoles();
	}

	@Operation(summary = "Get all USM Bug Type Combo")
	@GetMapping(path = "/usmBugTypeCombo", produces = "application/json")
	public List<Syslines> getBugTypeCombo() {
		return usmRoleService.getBugTypeCombo();
	}

	@GetMapping("/getRoleBugNameCombo/{id}")
	public List<ObjectNode> getBugNameCombo(@PathVariable("id") String id) {
		return usmRoleService.getBugNameCombo(id);
	}

	@Operation(summary = "Get USM roles provided a ROLE_ID")
	@PostMapping(path = "/usmrole/{id}", produces = "application/json")
	public USMRoleDto findByUSMRoleId(@PathVariable("id") long id) {
		return usmRoleService.getUSMRoleByRoleId(id);
	}

	@Operation(summary = "Get USM roles provided a ROLE_NAME")
	@PostMapping(path = "/usmrole/roleName/{name}", produces = "application/json")
	public USMRole findByUSMRoleName(@PathVariable("name") String name) {
		return usmRoleService.getUSMRoleByRoleName(name);
	}

	@Operation(summary = "Add new USM role")
	@PostMapping("/usmrole/add")
	public CustomResponse addUSMRole(@RequestBody USMRoleDto usmRoleDto) {
		
//		System.out.println("usmRoleDto >> "+usmRoleDto.getRoleName());
//		
//		System.out.println("111111111"); 
//		usmRoleDto.setRoleName( usmRoleDto.getRoleName());
//		System.out.println("22222222");
//		usmRoleDto.setRoleTypeName(usmRoleDto.getRoleTypeName());
//		System.out.println("3333333333");
////		if(usmRoleDto.getBugType()=="" || usmRoleDto.getBugType()=="null")
////		{
////			usmRoleDto.setBugType(null); 
////		} else {
//			usmRoleDto.setBugType(usmRoleDto.getBugType());	
////		}
//		
//		usmRoleDto.setBugName(usmRoleDto.getBugName());	
//		System.out.println("4444444444");
		
     return usmRoleService.addUSMRole(usmRoleDto);
	}

	@Operation(summary = "Update existing USM role provided a ROLE_ID")
	@PutMapping("/usmrole/update/{id}")
	public CustomResponse updateUSMRole(@PathVariable("id") long id, @RequestBody USMRoleDto usmRoleDto) {

		return usmRoleService.updateUSMRole(id, usmRoleDto);
	}

	@Operation(summary = "Delete existing USM role provided a ROLE_ID")
	@DeleteMapping("usmrole/delete/{id}")
	public CustomResponse deleteUSMRole(@PathVariable("id") long id) {
		return usmRoleService.deleteUSMRole(id);
	}
	
	@Operation(summary = "Get Role Sanction List")
	@PostMapping(path = "/usmrole/Sanction/{id}", produces = "application/json")
	public List<ObjectNode> findByUSMRoleName(@PathVariable("id") long id) {
		return usmRoleService.getRoleSanctionList(id);
	}
	
	@PostMapping("/addUSMRoleSnctionLstEntSrc")
	public CustomResponse addUSMRoleSnctionLstEntSrc(@RequestBody USMSanction roleSrc) {
		return usmRoleService.addUSMRoleSnctionLstEntSrc(roleSrc);
	}

	@Operation(summary = "Get Role All Sanction List")
	@PostMapping(path = "/usmrole/AllSanction", produces = "application/json")
	public List<ObjectNode> findByUSMRoleName() {
		return usmRoleService.getAllRoleSanctionList();
	}
}