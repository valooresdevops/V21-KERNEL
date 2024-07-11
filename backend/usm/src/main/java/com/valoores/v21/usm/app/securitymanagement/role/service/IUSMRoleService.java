package com.valoores.v21.usm.app.securitymanagement.role.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.common.syslines.model.Syslines;
import com.valoores.v21.usm.app.securitymanagement.role.dto.USMRoleDto;
import com.valoores.v21.usm.app.securitymanagement.role.model.USMRole;
import com.valoores.v21.usm.app.securitymanagement.role.model.USMSanction;
import com.valoores.v21.usm.backend.CustomResponse;

public interface IUSMRoleService {

	public CustomResponse updateUSMRole(long id, USMRoleDto theDto);

	public CustomResponse deleteUSMRole(long id);

	public CustomResponse addUSMRole(USMRoleDto theDto);

	public List<ObjectNode> getAllUSMRoles();

	public USMRoleDto getUSMRoleByRoleId(long id);

	public USMRole getUSMRoleByRoleName(String name);

	public List<Syslines> getBugTypeCombo();
	
	List<ObjectNode> getBugNameCombo(String id);

	public List<ObjectNode> getRoleSanctionList(long id);

	public CustomResponse addUSMRoleSnctionLstEntSrc(USMSanction roleSrc);

	public List<ObjectNode> getAllRoleSanctionList();

}
