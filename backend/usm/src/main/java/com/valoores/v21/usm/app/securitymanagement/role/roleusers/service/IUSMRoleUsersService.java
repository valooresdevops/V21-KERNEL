package com.valoores.v21.usm.app.securitymanagement.role.roleusers.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface IUSMRoleUsersService {

	List<ObjectNode> getRoleUsers(String roleId);
}
