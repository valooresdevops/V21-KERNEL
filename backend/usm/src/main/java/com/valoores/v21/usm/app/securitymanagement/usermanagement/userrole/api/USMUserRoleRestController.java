package com.valoores.v21.usm.app.securitymanagement.usermanagement.userrole.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valoores.v21.usm.app.securitymanagement.usermanagement.userrole.dto.USMUserRoleDto;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.userrole.service.IUSMUserRoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "USM UserRole", description = "USM UserRole' exposed APIs")
@RestController
@RequestMapping("/api")
public class USMUserRoleRestController {

	@Autowired
	private IUSMUserRoleService usmUserRoleService;
	
	@Operation(summary = "Get all USM users Role")
	@GetMapping(path = "/usmUsersRole/{empId}", produces = "application/json")
	public List<USMUserRoleDto> getAllUSMUsers(@PathVariable long empId) {
		return usmUserRoleService.getAllUSMUsersRole(empId);
		
	}
	
}
