package com.valoores.v21.usm.app.securitymanagement.role.roleusers.service.impl;

import java.sql.Connection;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.common.ObjectToJsonRepository;
import com.valoores.v21.usm.app.securitymanagement.role.roleusers.service.IUSMRoleUsersService;

@Service
public class USMRoleUsersServiceImpl implements IUSMRoleUsersService {

	@Autowired
	private EntityManager entityManagerR;

	@Autowired private 
	javax.sql.DataSource dataSource;
	
	@Override
	public List<ObjectNode> getRoleUsers(String roleId) {
		Connection conn = DataSourceUtils.getConnection(dataSource);
		try {
		String sqlCond = "";
		Integer idParam = Integer.parseInt(roleId);
		if (roleId != "")
			sqlCond = " and um.roleId = " + idParam;

		return ObjectToJsonRepository.getJson(entityManagerR,
				"select distinct uu.id as userId , uu.username as userName , uu.firstName as firstName , uu.lastName as lastName  "
						+ "from USMUser uu," + "USMUserMulti um" + " where uu.id = um.userId " + sqlCond + "");
		} finally {
	        DataSourceUtils.releaseConnection(conn, dataSource);
	        
	    }
	}
}