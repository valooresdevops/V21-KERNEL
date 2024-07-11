package com.valoores.v21.usm.app.securitymanagement.role.adrolemapping.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.common.syslines.model.Syslines;
import com.valoores.v21.usm.app.securitymanagement.role.adrolemapping.dto.USMAdRoleMappingDto;
import com.valoores.v21.usm.app.securitymanagement.role.adrolemapping.dto.USMAdRoleMappingLdapConfiDto;
import com.valoores.v21.usm.app.securitymanagement.role.adrolemapping.service.IUSMAdRoleMappingService;
import com.valoores.v21.usm.backend.CustomResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "USM Add Role Mapping", description = "USM add role mapping exposed APIs")
@RestController
@RequestMapping("/api")
public class USMAdRoleMappingRestController {

	@Autowired
	private IUSMAdRoleMappingService usmAdRoleMapping;

	@PostMapping("/USMRoleMapping/{id}")
	public List<ObjectNode> getRoleMappingNameId(@PathVariable("id") String id) {
		return usmAdRoleMapping.getRoleMappingNameId(id);
	}

	@PostMapping("/LDAPConfiguration")
	public List<ObjectNode> getLDAPConfiguration() {
		return usmAdRoleMapping.getLDAPConfiguration();
	}

	@GetMapping("/getAddRoleMappingLdapConfCombo")
	public List<USMAdRoleMappingLdapConfiDto> getLdapConfCombo() {
		return usmAdRoleMapping.getLdapConfCombo();
	}

	@GetMapping(path = "/usmObjectTypeCombo", produces = "application/json")
	public List<Syslines> getObjectTypeCombo() {
		return usmAdRoleMapping.getObjectTypeCombo();
	}

	@Operation(summary = "Add new USM role mapping")
	@PostMapping("/usmadd/add")
	public CustomResponse addUSMAdRoleMapping(@RequestBody USMAdRoleMappingDto adRoleMappingDto) {
		return usmAdRoleMapping.addUSMAdRoleMapping(adRoleMappingDto);
	}

	@Operation(summary = "Delete existing USM role mapping provided a ROLE_ID")
	@DeleteMapping("usmdelete/delete/{id}")
	public CustomResponse deleteUSMAdRoleMapping(@PathVariable("id") long id) {
		return usmAdRoleMapping.deleteUSMAdRoleMapping(id);
	}

}
