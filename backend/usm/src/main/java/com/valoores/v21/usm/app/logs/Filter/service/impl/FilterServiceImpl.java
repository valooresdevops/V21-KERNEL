package com.valoores.v21.usm.app.logs.Filter.service.impl;

import java.sql.Connection;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.logs.Filter.repository.IFilterRepository;
import com.valoores.v21.usm.app.logs.Filter.service.IFilterService;
import com.valoores.v21.usm.common.ObjectToJsonRepository;

@Service
public class FilterServiceImpl implements IFilterService {

	@Autowired
	private EntityManager entityManagerR;
	
	@Resource
	public IFilterRepository filterServiceRepository;
	@Autowired private 
	javax.sql.DataSource dataSource;
	@Override
	public List<ObjectNode> getApplicationEvent(String application, String fromDate, String toDate) {

		Connection conn = DataSourceUtils.getConnection(dataSource);
	    try {
	
//		String sql = "select DISTINCT l.logId as logId,"+
//	                                  "he.id as empId,"+
//	                                  "he.empUserLogin as empUserLogin,"+
//	                                  "he.name as empName,"+
//	                                  "l.loginDate as loginDate,"+
//	                                  "l.logoutDate as logoutDate ,"+
//	                                  "l.logoutIncidence as logoutIncidence, "+
//	                                  "l.logIp as logIp,"+
//	                                  "s.name as logoutIncidenceName, "+
//	                                  " (select COUNT(uuld.logDId) from LogsDetails uuld, LogUser l" + 
//	  					            " where uuld.logId = l.logId and rownum = 1) as actionsCount, "+
//	  					          " (SELECT COUNT(uule.id) FROM userLogExeption uule, LogUser l " +
//	  					        "WHERE uule.logId = l.logId AND rownum = 1)  as exceptionsCount"+
//              
//                " from LogUser l, Syslines s, UsmUserMiscInfo he"+
//                " WHERE he.id = l.empId and s.id = l.logoutIncidence and s.heaCode = 425 "+ 
//                 
//                 "and l.logId in (select ldd.logId from LogsDetails ldd where ldd.operationProcess like concat('" + application+"%'))" ;
//		String sql = "SELECT DISTINCT  " + 
//				"  l.logId as \"logId\",   " + 
//				"  he.id as \"empId\",   " + 
//				"  he.empUserLogin as \"empUserLogin\",   " + 
//				"  he.name as \"empName\",   " + 
//				"  l.loginDate as \"loginDate\",   " + 
//				"  l.logoutDate as \"logoutDate\",   " + 
//				"  l.logoutIncidence as \"logoutIncidence\",    " + 
//				"  l.logIp as \"logIp\",   " + 
//				"  s.name as \"logoutIncidenceName\",    " + 
//				"  (SELECT COUNT(uuld.logDId) FROM LogsDetails uuld, LogUser l WHERE uuld.logId = l.logId LIMIT 1) as \"actionsCount\",    " + 
//				"  (SELECT COUNT(uule.id) FROM userLogExeption uule, LogUser l WHERE uule.logId = l.logId LIMIT 1)  as \"exceptionsCount\"   " + 
//				"FROM  " + 
//				"  LogUser l,  " + 
//				"  Syslines s,  " + 
//				"  UsmUserMiscInfo he   " + 
//				"WHERE  " + 
//				"  he.id = l.empId  " + 
//				"  AND s.id = l.logoutIncidence  " + 
//				"  AND s.heaCode = 425     " + 
//				"  AND l.logId IN (SELECT ldd.logId FROM LogsDetails ldd WHERE ldd.operationProcess LIKE '    application%')";

	    	String sql = "SELECT DISTINCT \r\n" + 
	    			"  l.LOG_ID as \"logId\",  \r\n" + 
	    			"  he.USER_ID as \"empId\",  \r\n" + 
	    			"  he.USER_LOGIN as \"empUserLogin\",  \r\n" + 
	    			"  he.USER_FULL_NAME as \"empName\",  \r\n" + 
	    			"  l.LOGIN_DATE as \"loginDate\",  \r\n" + 
	    			"  l.LOGOUT_DATE as \"logoutDate\",  \r\n" + 
	    			"  l.logout_incidence as \"logoutIncidence\",   \r\n" + 
	    			"  l.LOG_IP as \"logIp\",  \r\n" + 
	    			"  s.LIN_NAME as \"logoutIncidenceName\",   \r\n" + 
	    			"  (SELECT COUNT(uuld.LOG_D_ID) FROM USMDBA.USM_USER_LOG_DETAILS uuld, USMDBA.USM_USER_LOG l WHERE uuld.LOG_ID = l.LOG_ID LIMIT 1) as \"actionsCount\",   \r\n" + 
	    			"  (SELECT COUNT(uule.LOG_EXCEP_ID) FROM USMDBA.usm_user_log_exception uule, USMDBA.USM_USER_LOG l WHERE uule.LOG_ID = l.LOG_ID LIMIT 1)  as \"exceptionsCount\"  \r\n" + 
	    			"FROM \r\n" + 
	    			"  USMDBA.USM_USER_LOG l, \r\n" + 
	    			"  SDEDBA.REF_SYS_LINES s, \r\n" + 
	    			"  USMDBA.USM_USER_MISC_INFO he  \r\n" + 
	    			"WHERE \r\n" + 
	    			"  he.USER_ID = l.EMP_ID \r\n" + 
	    			"  AND s.LIN_CODE = l.logout_incidence \r\n" + 
	    			"  AND s.HEA_CODE = 425    \r\n" + 
	    			"  AND l.LOG_ID IN (SELECT ldd.LOG_ID FROM USMDBA.USM_USER_LOG_DETAILS ldd WHERE ldd.OPERATION_PROCESS LIKE '    application%')";
					
		
	if(!fromDate.equals("-1")) {
//		sql = sql + " AND trunc(l.loginDate) >= to_date('"+ fromDate +"','dd-MM-yyyy')";
		sql = sql + " AND DATE_TRUNC('day', l.LOGIN_DATE) >= TO_DATE('"+ fromDate +"','DD-MM-YYYY')";
	}
	if(!toDate.equals("-1")) {
//		sql = sql + " AND trunc(l.logoutDate) <= to_date('"+ toDate +"','dd-MM-yyyy')";
		sql = sql + " AND DATE_TRUNC('day', l.LOGOUT_DATE) <= TO_DATE('"+ toDate +"','DD-MM-YYYY')";
	}
	
	
	System.out.println("sql >>>>>>>>>>>>>  " + sql);
	
	return ObjectToJsonRepository.getJsonNativeQuery(entityManagerR,sql);
	
	} finally {
        DataSourceUtils.releaseConnection(conn, dataSource);
        
    }
	}
}
