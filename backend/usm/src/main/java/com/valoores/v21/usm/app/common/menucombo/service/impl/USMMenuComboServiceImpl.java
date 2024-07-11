package com.valoores.v21.usm.app.common.menucombo.service.impl;

import java.sql.Connection;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.common.menucombo.model.USMMenu;
import com.valoores.v21.usm.app.common.menucombo.repository.IUSMMenuComboRepository;
import com.valoores.v21.usm.app.common.menucombo.service.IUSMMenuComboService;
import com.valoores.v21.usm.common.ObjectToJsonRepository;
@Service
public class USMMenuComboServiceImpl implements IUSMMenuComboService {
	@Autowired
	private EntityManager entityManagerR;
	@Autowired
	private PlatformTransactionManager transactionManager;
	@Autowired private 
	javax.sql.DataSource dataSource;
	
	@Autowired
	private IUSMMenuComboRepository usmmenuComboRepository;
	@Override
	public List<ObjectNode> getMenuCombo(String id) {
		System.out.println("test52 >>>>>>>>>>>>>>>>>> " + id);
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    	org.springframework.transaction.TransactionStatus status = transactionManager.getTransaction(def);
		try {
		return ObjectToJsonRepository.getJson(entityManagerR,"select s.menuCode as id, s.menuName as name from USMMenu s where s.menuPCode = '" + id + "'");
	}
		catch (Exception e) {
			Connection con = DataSourceUtils.getConnection(dataSource);
		    DataSourceUtils.releaseConnection(con, dataSource);
		
			transactionManager.rollback(status); 
		throw e;
	}
	}	
	@Override
	public List<USMMenu> getMenuName() {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    	org.springframework.transaction.TransactionStatus status = transactionManager.getTransaction(def);
		try {
		return usmmenuComboRepository.getMenuName();}
		catch (Exception e) {
			Connection con = DataSourceUtils.getConnection(dataSource);
		    DataSourceUtils.releaseConnection(con, dataSource);
		
			transactionManager.rollback(status); 
		throw e;
	}
	}
	@Override
	public List<ObjectNode> getMenuCombowithSubMenu(String menuCode) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    	org.springframework.transaction.TransactionStatus status = transactionManager.getTransaction(def);
		try {
		String sql = "SELECT M.id as id, M.name as name" + 
				"  FROM ObjectMenu M" + 
				" WHERE M.id LIKE '0"+menuCode+"%'" + 
				"   AND M.id <> 0" +  menuCode +
				"   AND M.menuType IN (1, 8)" +
				" ORDER BY M.id ASC";
	System.out.println("sql isss ---- > " +  sql);
	return ObjectToJsonRepository.getJson(entityManagerR,sql);}
		catch (Exception e) {
			Connection con = DataSourceUtils.getConnection(dataSource);
		    DataSourceUtils.releaseConnection(con, dataSource);
		
			transactionManager.rollback(status); 
		throw e;
	}	
		
	}

//	@Override
//	public List<ObjectNode> getMenuCombowithSubMenu(String id) {
//		
//		System.out.println("id issssss " + id);
//		String sql = "SELECT M.id as id, M.name as name" + 
//					"  FROM ObjectMenu M" + 
//					" WHERE M.id LIKE '0"+id+"%'" + 
//					"   AND M.id <> 0" +  id +
//					"   AND M.menuType IN (1, 8)" +
//					" ORDER BY M.id ASC";
//		System.out.println("sql isss ---- > " +  sql);
//		return ObjectToJsonRepository.getJson(entityManagerR,sql);
//		
//	}


}
