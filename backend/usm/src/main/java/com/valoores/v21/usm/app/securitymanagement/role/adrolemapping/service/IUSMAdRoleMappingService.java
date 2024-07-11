package com.valoores.v21.usm.app.securitymanagement.role.adrolemapping.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.common.syslines.model.Syslines;
import com.valoores.v21.usm.app.securitymanagement.role.adrolemapping.dto.USMAdRoleMappingDto;
import com.valoores.v21.usm.app.securitymanagement.role.adrolemapping.dto.USMAdRoleMappingLdapConfiDto;
import com.valoores.v21.usm.backend.CustomResponse;

public interface IUSMAdRoleMappingService {

	public CustomResponse deleteUSMAdRoleMapping(long id);

	public CustomResponse addUSMAdRoleMapping(USMAdRoleMappingDto adRoleMappingDto);

	List<ObjectNode> getRoleMappingNameId(String id);

	public List<USMAdRoleMappingLdapConfiDto> getLdapConfCombo();

	public List<Syslines> getObjectTypeCombo();

	List<ObjectNode> getLDAPConfiguration();

}
	