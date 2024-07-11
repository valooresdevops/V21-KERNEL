package com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.bugmapping.api;

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
import com.valoores.v21.usm.app.common.mdmbsnunitgroup.model.MdmBsnUnitGroup;
import com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.bugmapping.dto.BugMappingDto;
import com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.bugmapping.service.IBugMappingService;
import com.valoores.v21.usm.backend.CustomResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Bug Mapping", description = "Bug Mapping  APIs.")
@RestController
@RequestMapping("/api")
public class BugMappingRestController {

	@Autowired
	private IBugMappingService bugMappingService;

	@Operation(summary = "Get all BUG Mapping")
	@GetMapping(path = "/bugmapping", produces = "application/json")
	public List<ObjectNode> getAllBugMapping() {
		return bugMappingService.getAllBugMapping();
	}

	@Operation(summary = "Add New BUG Mapping")
	@PostMapping("/bugmapping/add")
	public CustomResponse addBugMapping(@RequestBody BugMappingDto bugMappingDto) {
		return bugMappingService.addBugMapping(bugMappingDto);
	}

	@GetMapping("/LDAPConfigurationn")
	public List<ObjectNode> getLDAPConfigurationn() {
		return bugMappingService.getLDAPConfigurationn();
	}

	@GetMapping("/getMdmBsnUnitGroupp")
	public List<MdmBsnUnitGroup> getMdmBsnUnitGroup() {
		return bugMappingService.getMdmBsnUnitGroup();
	}

	@Operation(summary = "Delete BUG Mapping")
	@DeleteMapping("/bugmapping/delete/{suiteLdapBsngpId}")
	public CustomResponse deleteBugMapping(@PathVariable("suiteLdapBsngpId") long suiteLdapBsngpId) {
		return bugMappingService.deleteBugMapping(suiteLdapBsngpId);
	}

}
