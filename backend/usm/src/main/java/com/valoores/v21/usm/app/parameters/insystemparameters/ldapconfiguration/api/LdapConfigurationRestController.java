package com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.dto.LdapConfigurationDto;
import com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.service.ILdapConfigurationService;
import com.valoores.v21.usm.backend.CustomResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Ldap Configuration", description = "Ldap configuration  APIs.")
@RestController
@RequestMapping("/api")
public class LdapConfigurationRestController {

	@Autowired
	private ILdapConfigurationService ldapConfigurationService;

	@Operation(summary = "Get all ldap configuration")
	@PostMapping(path = "/ldapconfiguration", produces = "application/json")
	public List<ObjectNode> getAllLdapConfig() {
		return ldapConfigurationService.getAllLdapConfig();
	}

	@Operation(summary = "Get Ldap Configuration using Ldap Configuration ID")
	@PostMapping(path = "/ldapconfigurations/{ldapConfigId}", produces = "application/json")
	public LdapConfigurationDto findByLdapConfigurationId(@PathVariable("ldapConfigId") long ldapConfigId) {
		return ldapConfigurationService.getLdapConfigurationByLdapConfigurationId(ldapConfigId);
	}

	@Operation(summary = "Add new ldap configuration")
	@PostMapping("/ldapconfiguration/add")
	public CustomResponse addLdapConfiguration(@RequestBody LdapConfigurationDto ldapConfigurationDto) {
		return ldapConfigurationService.addLdapConfiguration(ldapConfigurationDto);
	}

	@Operation(summary = "Update ldap configuration")
	@PutMapping("/ldapconfiguration/update/{ldapConfigId}")
	public CustomResponse updateLdapConfiguration(@PathVariable("ldapConfigId") long ldapConfigId,
			@RequestBody LdapConfigurationDto ldapConfigurationDto) {

		return ldapConfigurationService.updateLdapConfiguration(ldapConfigId, ldapConfigurationDto);
	}

	@Operation(summary = "Delete ldap configuration")
	@DeleteMapping("/ldapconfiguration/delete/{ldapConfigId}")
	public CustomResponse deleteLdapConfiguration(@PathVariable("ldapConfigId") long ldapConfigId) {
		return ldapConfigurationService.deleteLdapConfiguration(ldapConfigId);
	}

}
