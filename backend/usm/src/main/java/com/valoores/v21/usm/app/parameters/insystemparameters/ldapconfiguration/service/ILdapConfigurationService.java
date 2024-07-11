package com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.dto.LdapConfigurationDto;
import com.valoores.v21.usm.backend.CustomResponse;

public interface ILdapConfigurationService {

	List<ObjectNode> getAllLdapConfig();

	CustomResponse addLdapConfiguration(LdapConfigurationDto ldapConfigurationDto);

	CustomResponse updateLdapConfiguration(long ldapConfigId, LdapConfigurationDto ldapConfigurationDto);

	CustomResponse deleteLdapConfiguration(long ldapConfigId);

	LdapConfigurationDto getLdapConfigurationByLdapConfigurationId(long id);


}
