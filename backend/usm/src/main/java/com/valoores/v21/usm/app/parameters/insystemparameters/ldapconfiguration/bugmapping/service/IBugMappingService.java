package com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.bugmapping.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.common.mdmbsnunitgroup.model.MdmBsnUnitGroup;
import com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.bugmapping.dto.BugMappingDto;
import com.valoores.v21.usm.backend.CustomResponse;

public interface IBugMappingService {

	CustomResponse addBugMapping(BugMappingDto bugMappingDto);

	List<ObjectNode> getLDAPConfigurationn();

	List<MdmBsnUnitGroup> getMdmBsnUnitGroup();

	CustomResponse deleteBugMapping(long suiteLdapBsngpId);

	List<ObjectNode> getAllBugMapping();
}
