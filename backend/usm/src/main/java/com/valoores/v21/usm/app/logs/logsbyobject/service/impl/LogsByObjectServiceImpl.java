package com.valoores.v21.usm.app.logs.logsbyobject.service.impl;

import java.sql.Connection;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.common.ObjectToJsonRepository;
import com.valoores.v21.usm.app.logs.logsbyobject.repository.ILogsByObjectRepository;
import com.valoores.v21.usm.app.logs.logsbyobject.service.ILogsByObjectService;
//import com.valoores.v21.usm.common.objecttojson.ObjectToJsonRepository;
@Service

public class LogsByObjectServiceImpl implements ILogsByObjectService {
	
	@Autowired
	private EntityManager entityManagerR;
	
	@Resource
	public ILogsByObjectRepository logsByObjectRepository;
    
//	@Override
//	public List<USMApplication> getApplicationCombo() {
//		return logsByObjectRepository.getApplicationCombo();
//	}

//	@Override
//	public List<ObjectNode> getMenuCombo(String id) {
//		return ObjectToJsonRepository.getJson(entityManagerR,"select s.id as id, s.name as name from ObjectMenu s where s.menuPCode = " + id);
//	}
	@Autowired private 
	javax.sql.DataSource dataSource;
	
	@Override
	public List<ObjectNode> getAllLogsByObject(String application,String menuName,String loginDate, String logoutDate)  {
		Connection conn = DataSourceUtils.getConnection(dataSource);
		try {
		    	String sql = "SELECT r.empUserLogin as empName, m.menuPath as menuPath," +
						"            s.name as operationtype," + 
						"           ld.operationDescription as operationHint," + 
						"           trunc(ld.operationDate) as operationDate" + 
						"      FROM USMApplication sa," + 
						"           UsmUserMiscInfo r," + 
						"           LogsDetails ld," + 
						"           LogUser l," + 
						"           USMMenu m, Syslines s" +
						"       WHERE ld.logId = l.logId" + 
						"       AND r.appCode = sa.id" + 
						"       AND s.id = ld.operationType "+
						"       AND s.heaCode = 424"+
						"       AND r.id = l.empId" + 
						"       AND sa.id =" + application;
			if(!loginDate.equals("-1")) {
				sql = sql + " AND trunc(l.loginDate) = to_date('"+loginDate+"','dd-mm-yyyy')";
			}
			if(!logoutDate.equals("-1")) {
				sql = sql + " AND trunc(l.logoutDate) = to_date('"+logoutDate+"','dd-mm-yyyy')";
			}
			if(!menuName.equals("-1")) {
				sql = sql + " AND m.menuCode =" + menuName;
			}
			
			System.out.println("sql >>>>>>>>>>>>>  " + sql);
			System.out.println("loginDate >>>>>>>>>>>>>  " + loginDate);
			System.out.println("logoutDate >>>>>>>>>>>>>  " + logoutDate);
			System.out.println("application >>>>>>>>>>>>>  " + application);
			System.out.println("menuName >>>>>>>>>>>>>  " + menuName);
			return ObjectToJsonRepository.getJson(entityManagerR,sql);
			
		} finally {
	        DataSourceUtils.releaseConnection(conn, dataSource);
	        
	    }
		}}