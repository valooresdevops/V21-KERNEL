package com.valoores.v21.usm.app.securitymanagement.role.roleusers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.securitymanagement.role.roleusers.service.IUSMRoleUsersService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "USM Role Users", description = "USM role users exposed APIs")
@RestController
@RequestMapping("/api")
public class USMRoleUsersRestController {
  
	@Autowired
	private IUSMRoleUsersService usmRoleUsersService;

	@PostMapping("/usmroleusers/{id}")
	public List<ObjectNode> getRoleUsers(@PathVariable("id") String roleId) {
		return usmRoleUsersService.getRoleUsers(roleId);
	}

}
