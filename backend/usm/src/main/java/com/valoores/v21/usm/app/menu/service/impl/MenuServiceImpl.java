package com.valoores.v21.usm.app.menu.service.impl;

import java.sql.Connection;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.common.ObjectToJsonRepository;
import com.valoores.v21.usm.app.menu.dto.USMAccessTypeDto;
import com.valoores.v21.usm.app.menu.repository.IUSMMenuRepository;
import com.valoores.v21.usm.app.menu.service.IMenuService;


@Service
public class MenuServiceImpl implements IMenuService{
	
//	@Resource
//	IMenuRepository menuRepository;
	
	@Resource
	IUSMMenuRepository usmMenuRepository;
    
	@Autowired private 
	javax.sql.DataSource dataSource;
	
	@Autowired
	private EntityManager entityManagerR;
	
	@Override
	public List<ObjectNode> getMenusByParentCode(String menuParentCode,String userId) {
		
//		List<USMMenu> menus = usmMenuRepository.findByMenuParentCode(menuParentCode);
		Connection conn = DataSourceUtils.getConnection(dataSource);
	    try {
//		String query =" select m.menuCode as menuCode," + 
//				" 		(select a.accessCode from USMAccessMatrix a ,USMUserMulti u where u.userId = "+ userId +" and  a.accessCode = u.accessCode and u.menuCode = m.menuCode) as isDisplay,	"+
//				"       (select a.appSName from USMMenuApplication a where a.appCode = TO_NUMBER(m.menuCode,'99999999999999999999999999999999999999999999999999') ) as appSname," + 
//				"       m.menuName as menuName," + 
//				"       m.menuVar as menuVariable," + 
//				"       m.menuManaged as menuManaged," + 
//				"       (select count(mm) from USMMenu mm where mm.menuPCode = m.menuCode  and mm.menuManaged = '1' and mm.menuType in (1,8)) as childMenus," + 
//				"       (select mmm.menuPCode" + 
//				"          from USMMenu mmm where  mmm.menuCode = m.menuPCode) as prevParentCode," + 
//				"          m.menuIcon as menuIcon" + 
//				"  from USMMenu m "
//				+ "where  m.menuManaged = '1'"
//				+ "and  m.menuType in (1,8)" + 
//				"  and  m.menuPCode = '" + menuParentCode+"'";
//	String query=	"SELECT "+
//	   " m.menuCode AS menuCode, "+
//	   " (SELECT a.accessCode "+
//	    " FROM USMAccessMatrix a"+
//	    " INNER JOIN USMUserMulti u ON a.accessCode = u.accessCode "+
//	    " WHERE u.userId = "+userId+"  AND u.menuCode = m.menuCode "+
//	    " ) AS isDisplay, "+
//	    "(SELECT a.appSName "+
//	    " FROM USMMenuApplication a "+
//	    "WHERE CAST(a.appCode AS text) = CAST(m.menuCode AS text) "+
//	    " ) AS appSname, "+
//	    "m.menuName AS menuName, "+
//	    "m.menuVar AS menuVariable, "+ 
//	    "m.menuManaged AS menuManaged, "+
//	    "(SELECT COUNT(*) "+
//	    " FROM USMMenu mm "+
//	    " WHERE mm.menuPCode = m.menuCode "+
//	    " AND mm.menuManaged = '1' "+
//	    " AND mm.menuType IN (1,8)) AS childMenus, "+
//	    "(SELECT mmm.menuPCode "+
//	    " FROM USMMenu mmm "+ 
//	    " WHERE mmm.menuCode = m.menuPCode "+
//	    " ) AS prevParentCode, "+
//	    "m.menuIcon AS menuIcon "+
//	    "FROM USMMenu m "+
//	    "WHERE m.menuManaged = '1' "+
//	"AND m.menuType IN (1,8) "+
//	"AND m.menuPCode = '" + menuParentCode+"'";
	    	
	  String query="SELECT "
	      +"  m.menu_Code AS \"menuCode\","
	      +"  (SELECT a.access_Code"
	      +"   FROM USMDBA.USM_Access_Matrix a"
	      +"   INNER JOIN USMDBA.usm_user_multi_misc_info u ON a.access_Code = u.access_Code"
	      + "   WHERE u.user_Id = "+ userId +"  AND u.menu_Code = m.menu_Code "
	      + "  LIMIT 1) AS \"isDisplay\","
	      +"(SELECT a.app_sname"
	      + "  FROM USMDBA.USM_SUITE_Application a "
	      +"   WHERE a.app_Code = CAST(m.menu_Code AS numeric) "
	      +"   LIMIT 1) AS \"appSname\", "
	      +"  m.menu_Name AS \"menuName\", "
	       +" m.menu_variable AS \"menuVariable\", "
	       +" m.menu_Managed AS \"menuManaged\", "
	       +" (SELECT COUNT(*) "
	        +" FROM USMDBA.USM_Menu mm "
	        +" WHERE mm.menu_P_Code = m.menu_Code "
	        +" AND mm.menu_Managed = '1' "
	        +" AND mm.menu_Type IN (1,8)) AS \"childMenus\", "
	       +" (SELECT mmm.menu_P_Code "
	       +"  FROM USMDBA.USM_Menu mmm "
 	        +" WHERE mmm.menu_Code = m.menu_P_Code "
	       +"  LIMIT 1) AS \"prevParentCode\", "
	      +"  m.menu_icon_desc AS \"menuIcon\" "
	   +" FROM USMDBA.USM_Menu m "
	   +" WHERE m.menu_Managed = '1' "
	   +" AND m.menu_Type IN (1,8) "
	   +" AND m.menu_P_Code = '" +menuParentCode+"'";
				System.out.println("TST RUN>>>>>>>>>>>"+ObjectToJsonRepository.getJsonNativeQuery(entityManagerR,query));
		return ObjectToJsonRepository.getJsonNativeQuery(entityManagerR,query)	;
	    } finally {
	        DataSourceUtils.releaseConnection(conn, dataSource);
	        
	    }
	}

	@Override
	public String isManaged(String menuName) {
		Connection conn = DataSourceUtils.getConnection(dataSource);
	    try {
		String isManaged = "";
		
		isManaged = usmMenuRepository.getIsManagedMenu(menuName);
		
		return isManaged;
	    } finally {
	        DataSourceUtils.releaseConnection(conn, dataSource);
	        
	    }
	}
	
	@Override
	public USMAccessTypeDto getAccessRight(String menuName,Integer userId) {
		Connection conn = DataSourceUtils.getConnection(dataSource);
	    try {
		String accessCode = "";
		USMAccessTypeDto usmAccessType = new USMAccessTypeDto();
		System.out.println("HELOOOOO"+menuName+">>>>>>>>>>"+userId);
		accessCode = usmMenuRepository.getAccessRight(menuName , userId);
		System.out.println("Access>>>>"+accessCode+"<<<<<<COOOODE");
		
		
	if(accessCode != null) {
		if(accessCode.length() != 0)
		{
			for(int i = 0;i < accessCode.length();i++)
			{
				
				if(i == 0)
				{
					usmAccessType.setDisplay(String.valueOf(accessCode.charAt(i))); 
				}
				if(i == 1)
				{
					usmAccessType.setAdd(String.valueOf(accessCode.charAt(i))); 
				}
				if(i == 2)
				{
					usmAccessType.setModify(String.valueOf(accessCode.charAt(i))); 
				}
				if(i == 3)
				{
					usmAccessType.setDelete(String.valueOf(accessCode.charAt(i))); 
				}
				if(i == 4)
				{
					usmAccessType.setPrint(String.valueOf(accessCode.charAt(i))); 
				}
				if(i == 5)
				{
					usmAccessType.setExport(String.valueOf(accessCode.charAt(i))); 
				}
				if(i == 6)
				{
					usmAccessType.setTranslate(String.valueOf(accessCode.charAt(i))); 
				}
			}
		}
		else
		{
			usmAccessType.setDisplay("0"); 
			usmAccessType.setAdd("0"); 
			usmAccessType.setModify("0"); 
			usmAccessType.setDelete("0"); 
			usmAccessType.setPrint("0"); 
			usmAccessType.setExport("0"); 
			usmAccessType.setTranslate("0");
		}
	
		
	}	
	else
	{
		usmAccessType.setDisplay("0"); 
		usmAccessType.setAdd("0"); 
		usmAccessType.setModify("0"); 
		usmAccessType.setDelete("0"); 
		usmAccessType.setPrint("0"); 
		usmAccessType.setExport("0"); 
		usmAccessType.setTranslate("0");
	}
		return usmAccessType;
	    } finally {
	        DataSourceUtils.releaseConnection(conn, dataSource);
	        
	    }
	}
	
}
