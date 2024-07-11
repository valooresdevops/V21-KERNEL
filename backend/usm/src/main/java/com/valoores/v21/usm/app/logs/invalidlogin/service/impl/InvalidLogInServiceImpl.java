package com.valoores.v21.usm.app.logs.invalidlogin.service.impl;

import java.sql.Connection;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.logs.invalidlogin.service.IInvalidLoginService;
import com.valoores.v21.usm.app.logs.logsbyuser.repository.ILogsByUserRepository;
import com.valoores.v21.usm.common.ObjectToJsonRepository;
@Service

public class InvalidLogInServiceImpl implements IInvalidLoginService {
	@Autowired
	private EntityManager entityManagerR; //to turn object to json
	@Autowired private 
	javax.sql.DataSource dataSource;
	@Resource
	public ILogsByUserRepository logsByUserRepository;
	@Autowired
	private PlatformTransactionManager transactionManager;
	@Override
	public List<ObjectNode> getAllInvalidLogs(String loginDate) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    	org.springframework.transaction.TransactionStatus status = transactionManager.getTransaction(def);
	    try {
		return ObjectToJsonRepository.getJsonNativeQuery(entityManagerR,
//				"select l.Performer as performer," + 
//	    			"       l.Login_Date as loginDate," + 
//	    			"       l.Login_IP as loginIp" + 
//	    			
//	    			"  from InvalidLogs l" +
//	    			
//	    			"    where trunc(l.Login_Date) = to_date('"+loginDate+"','mm-DD-yyyy') ") ;
//				"SELECT l.Performer AS \"performer\"," + 
//		    	"       l.Login_Date AS \"loginDate\"," + 
//		    	"       l.Login_IP AS \"loginIp\"" + 
//		    	"       FROM InvalidLogs l" + 
//		    	"       WHERE DATE_TRUNC('day', l.Login_Date) = TO_DATE('"+loginDate+"', 'MM-DD-YYYY')") ;
				"		SELECT l.invalid_login_user AS \"performer\",  \r\n" + 
				"	    	      l.invalid_login_date AS \"loginDate\", \r\n" + 
				"	    	      l.log_ip AS \"loginIp\" \r\n" + 
				"	    	       FROM USMDBA.USM_INVALID_LOGIN_LOG l  \r\n" + 
				"				    WHERE DATE_TRUNC('day', l.invalid_login_date) = TO_DATE('"+loginDate+"', 'MM-DD-YYYY')");
				
				
				
//	    } finally {
//	        DataSourceUtils.releaseConnection(conn, dataSource);
//	        
//	    }
	    } catch (Exception e) {
			Connection con = DataSourceUtils.getConnection(dataSource);
		    DataSourceUtils.releaseConnection(con, dataSource);
		
			transactionManager.rollback(status); 
			
			throw e;
		}
	}
}
