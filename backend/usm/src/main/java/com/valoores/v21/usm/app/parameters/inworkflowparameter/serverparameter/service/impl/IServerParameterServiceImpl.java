package com.valoores.v21.usm.app.parameters.inworkflowparameter.serverparameter.service.impl;

import java.sql.Connection;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.common.ObjectToJsonRepository;
import com.valoores.v21.usm.app.parameters.inworkflowparameter.serverparameter.service.ServerParameterService;

@Service
public class IServerParameterServiceImpl implements ServerParameterService{
	@Autowired
	public EntityManager entityManagerR;
	
	@Autowired private 
	javax.sql.DataSource dataSource;
	
	@Override
	public List<ObjectNode> getAllWorkFlowParameter(String currServerUrl, String currServerName) {
		Connection conn = DataSourceUtils.getConnection(dataSource);
	    try {
		String query = " select s.name as currentServerName, s.appUrl as currentServerIp, s.appPort as wfmServerPort from ServerParameter s where id = 7";
		return ObjectToJsonRepository.getJson(entityManagerR,query);
	    } finally {
	        DataSourceUtils.releaseConnection(conn, dataSource);
	        System.out.println("...");
	    }
	    }
	    
}


