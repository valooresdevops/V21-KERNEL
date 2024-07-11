package com.valoores.v21.usm.app.logs.logsbyheatmapproc.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.common.ObjectToJsonRepository;
import com.valoores.v21.usm.app.logs.logsbyheatmapproc.service.ILogsByHeatmapprocService;

@Service
public class ILogsByHeatmapprocServiceImpl implements ILogsByHeatmapprocService {
	@Autowired
	private EntityManager entityManagerR;
	@Autowired
	private PlatformTransactionManager transactionManager;
	@Autowired private 
	javax.sql.DataSource dataSource;
    @Override
	public List<ObjectNode> generateHeatMapTable(String startDate, String endDate) {
    	//this is how to call a procedure
    	DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    	org.springframework.transaction.TransactionStatus status = transactionManager.getTransaction(def);
		try {
			//this line opens connection with database and closes it when done if put inside the try catch
			//here is the reference: https://stackoverflow.com/questions/9642643/datasourceutils-getconnection-vs-datasource-getconnection
			//another reference: https://stackoverflow.com/questions/47758091/hikaripool-1-connection-is-not-available-request-timed-out-after-30000ms-for
			DataSourceUtils.getConnection(dataSource);
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			
			entityManagerR.createNativeQuery("CALL  usm_user_log_proc(:startDate, :endDate)")
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate).executeUpdate();
			List<ObjectNode> json = new ArrayList<>();
			json = ObjectToJsonRepository.getJson(entityManagerR, "select l.x as X,l.y as Y,l.values as VALUESS ,l.days as DAYS,l.dates as DATES,l.times as TIMES from HeatMapTable l");
			return json;
		} catch (Exception e) {
			Connection con = DataSourceUtils.getConnection(dataSource);
		    DataSourceUtils.releaseConnection(con, dataSource);
		
			transactionManager.rollback(status); 
			
			throw e;
		}
		   	
}}


