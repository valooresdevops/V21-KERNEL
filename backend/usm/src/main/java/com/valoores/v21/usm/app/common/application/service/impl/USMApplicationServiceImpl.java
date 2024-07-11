package com.valoores.v21.usm.app.common.application.service.impl;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.common.application.model.USMApplication;
import com.valoores.v21.usm.app.common.application.repository.IUSMApplicationRepository;
import com.valoores.v21.usm.app.common.application.service.IUSMApplicationService;
import com.valoores.v21.usm.common.ObjectToJsonRepository;
@Service
public class USMApplicationServiceImpl implements IUSMApplicationService {
	
	@Autowired
	private IUSMApplicationRepository usmApplicationRepository;
	
    @Autowired
	private EntityManager entityManagerR;

	@Override
	public List<USMApplication> getApplicationCombo() {
		return  usmApplicationRepository.getApplicationCombo();

	}
	@Autowired private 
	javax.sql.DataSource dataSource;
	
	@Override
	public List<ObjectNode> getApplicationMenuCombo(String id) {
		Connection conn = DataSourceUtils.getConnection(dataSource);
	    try {
	  DecimalFormat df = new DecimalFormat("000");
      int ids = Integer.parseInt(id);
      String menuCodes = df.format(ids);
      System.out.println("menucodes>>>>>>>>"+menuCodes);
      return ObjectToJsonRepository.getJson(entityManagerR,
    	//	  "select l.menuCode as id,l.menuName as name  from USMMenu l where l.menuCode like '"+menuCodes+"%'  AND menuManaged = '1' AND menuType in (1,8) ORDER BY menuCode ASC");
    		  "select l.menuCode as id,l.menuName as name  from USMMenu l where l.menuCode = '"+menuCodes+ "'or l.menuPCode = '"+menuCodes+"'  AND menuManaged = '1' AND menuType in (1,8) ");
    		  		
	    
	    } finally {
	        DataSourceUtils.releaseConnection(conn, dataSource);
	        
	    }
	} 
	@Override
	public List<ObjectNode> getUSMApplicationRelatedUserId(long id) {
		Connection conn = DataSourceUtils.getConnection(dataSource);
	    try {
//		String sql = " select "
//				+ " s.id as id,s.name as name "
//				+ " from USMMenu u , USMUserMulti m , USMApplication s"
//				+ " where u.menuCode = m.menuCode "
//				+ " and m.userId = " +id
//				+ " and u.menuCode = s.id"
//				+ " and s.id <> 0 " ; //was working when we had a trigger on the usmuser table
	    	
	 //   	String sql="select"+
	  //  	            " m.menuCode as id , m.menuName as name FROM USMMenu m where m.menuPCode=0 and m.menuManaged=1";
//	    	String sql = " select "
//	    			+ " distinct s.id as id,s.name as name "	
//	    			+ " from USMMenu u , USMUserMulti m , USMApplication s"
//	    			+ " where u.menuCode = m.menuCode "
//	    			+ " and m.userId = " +id
//	    			+ " and u.menuCode = s.id"
//	    			+ " and s.id <> 0 " 
//	    			+" or m.menuCode=s.id " ;
	    	
//	    	String sql = " select "+ " distinct s.id as id,s.name as name "	
//	    			+ " from USMMenu u , USMUserMulti m , USMApplication s"
//	    			+ " where u.menuCode = m.menuCode "
//	    			+ " and m.userId = " +id
//	    			+ " and u.menuCode = s.id"
//	    			+ " and s.id <> '0' ";
	    	
	    	String sql = "select  distinct s.APP_CODE as id, s.APP_NAME as \"name\"\r\n" + 
	    			"	    			 from USMDBA.USM_MENU u , USMDBA.usm_user_multi_misc_info m , USMDBA.usm_suite_application s\r\n" + 
	    			"	    			 where u.MENU_CODE = m.MENU_CODE \r\n" + 
	    			"	    			 and m.USER_ID = 1\r\n" + 
	    			"	    			 and TO_NUMBER(u.MENU_CODE,'9999999999999') = s.APP_CODE\r\n" + 
	    			"	    			 and s.APP_CODE <> '0' ";

	    	return ObjectToJsonRepository.getJsonNativeQuery(entityManagerR,sql);
	 
	} finally {
        DataSourceUtils.releaseConnection(conn, dataSource);
        
    }
	}
	@Override
	public List<ObjectNode> getUSMApplicationRelatedRoleId(long id) {
		Connection conn = DataSourceUtils.getConnection(dataSource);
	    try {
	    
//		String sql = " select "
//				+ " s.id as id,s.name as name "
//				+ " from USMMenu u , USMRoleMulti m , USMApplication s"
//				+ " where u.menuCode = m.menuCode "
//				+ " and m.roleId = " +id
//				+ " and u.menuCode = s.id"
//				+ " and s.id <> 0 " ; //was working when we had a trigger on the usmuser table
//	    	String sql="select"+
//    	            " m.menuCode as id , m.menuName as name FROM USMMenu m where m.menuPCode=0 and m.menuManaged=1";
    	//now we need the union between above 2 queries
//	    	String sql= " select "
//    	    +" distinct s.id as id,s.name as name "
//    	    +" from USMMenu u , USMRoleMulti m , USMApplication s "
//    	    + " where u.menuCode = m.menuCode "
//    	    + " and m.roleId = " +id
//    	    + " and u.menuCode = s.id "
//	        + " and s.id <> 0 "
//	        + " or m.menuCode=s.id " ;//was working when having default apps on page loading now no more default apps only those selected and saved in multimiscinfo
	    	
	      	String sql= " select "
	        	    +" s.id as id,s.name as name "
	        	    +" from USMMenu u , USMRoleMulti m , USMApplication s "
	        	    + " where u.menuCode = m.menuCode "
	        	    + " and m.roleId = " +id
	        	    + " and u.menuCode = s.id "
	    	        + " and s.id <> '0' ";
	    	
        return ObjectToJsonRepository.getJson(entityManagerR,sql);
	 
	} finally {
        DataSourceUtils.releaseConnection(conn, dataSource);
        
    }
	}
}
