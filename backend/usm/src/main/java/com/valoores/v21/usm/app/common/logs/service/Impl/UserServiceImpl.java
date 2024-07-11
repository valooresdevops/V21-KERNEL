package com.valoores.v21.usm.app.common.logs.service.Impl;

import java.sql.Connection;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.common.logs.repository.IUserRepository;
import com.valoores.v21.usm.app.common.logs.service.IUserService;
import com.valoores.v21.usm.common.ObjectToJsonRepository;
@Service
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private EntityManager entityManagerR;
	
	@Autowired private 
	javax.sql.DataSource dataSource;

	@Resource
	public IUserRepository userRepository;
	
	@Override
	public List<ObjectNode> getUserCombo() {
		Connection conn = DataSourceUtils.getConnection(dataSource);
	    try {
		String sql="select r.id as id,"+" r.empUserLogin as name"+" from UsmUserMiscInfo r";
		
		return ObjectToJsonRepository.getJson(entityManagerR,sql);}
		finally {
	        DataSourceUtils.releaseConnection(conn, dataSource);
	}
	

	}


}
