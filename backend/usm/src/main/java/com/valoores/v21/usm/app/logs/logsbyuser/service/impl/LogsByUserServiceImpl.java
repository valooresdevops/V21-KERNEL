package com.valoores.v21.usm.app.logs.logsbyuser.service.impl;

import java.sql.Connection;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.common.ObjectToJsonRepository;
import com.valoores.v21.usm.app.logs.logsbyuser.repository.ILogsByUserRepository;
import com.valoores.v21.usm.app.logs.logsbyuser.service.ILogsByUserService;
//import com.valoores.v21.usm.common.objecttojson.ObjectToJsonRepository;
@Service
public class LogsByUserServiceImpl implements ILogsByUserService {
	@Autowired
	private EntityManager entityManagerR;
	
	@Autowired private 
	javax.sql.DataSource dataSource;
	
	@Resource
	public ILogsByUserRepository logsByUserRepository;
	
	@Override
	public List<ObjectNode> getAllLogsByUser(String userLogs, String application, String loginDate) {
		Connection conn = DataSourceUtils.getConnection(dataSource);
	    try { String sql =		"select trunc(l.loginDate) as loginDate," + 
	    			"       sa.name as application," + 
	    			"       trunc(l.logoutDate) as logoutDate," + 
	    			"       l.logIp as ip," + 
	    			"       ld.operationDescription as operationDescription," + 
	    			"		(SELECT s.name FROM Syslines s WHERE s.id = ld.operationType AND s.heaCode = 424) as operationType,"     +
	    			"		trunc(ld.operationDate) as operationDate"    + 
	     			"  from LogUser l," +
	    			"       USMApplication sa," +  
	    			"       LogsDetails ld," + 
	    			"       UsmUserMiscInfo r" + 
	    			"    where ld.logId = l.logId" +
	    			"      and r.appCode = sa.id" + 
	    			" 	   and r.id = l.empId" + 
	    			"      and r.id =" + userLogs + 
	    			"      and trunc(l.loginDate) = to_date('"+loginDate+"','DD-mm-yyyy')";
	    
	    	if(!application.equals("-1")) {
				sql = sql + "      and sa.id =" + application ;
			}		
	    		
	    			return ObjectToJsonRepository.getJson(entityManagerR,sql);
	    			
	    } finally {
	        DataSourceUtils.releaseConnection(conn, dataSource);
	        
	    }
	}

}
