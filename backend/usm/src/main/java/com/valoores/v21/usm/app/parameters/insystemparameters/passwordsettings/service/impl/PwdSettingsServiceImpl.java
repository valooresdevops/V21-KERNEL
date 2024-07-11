package com.valoores.v21.usm.app.parameters.insystemparameters.passwordsettings.service.impl;

import java.sql.Connection;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.valoores.v21.usm.app.parameters.insystemparameters.passwordsettings.service.IPwdSettingsService;


@Service
public class PwdSettingsServiceImpl implements IPwdSettingsService {

	@Autowired
	private EntityManager entityManagerR;
	@Autowired
	private PlatformTransactionManager transactionManager;
	@Autowired private 
	javax.sql.DataSource dataSource;
    @Override
	public void updateValues(Integer param_code, String param_value) {
    	//this is how to call a procedure
    	DefaultTransactionDefinition def = new DefaultTransactionDefinition();
    	org.springframework.transaction.TransactionStatus status = transactionManager.getTransaction(def);
		try {
			//this line opens connection with database and closes it when done if put inside the try catch
			//here is the reference: https://stackoverflow.com/questions/9642643/datasourceutils-getconnection-vs-datasource-getconnection
			//another reference: https://stackoverflow.com/questions/47758091/hikaripool-1-connection-is-not-available-request-timed-out-after-30000ms-for
			DataSourceUtils.getConnection(dataSource);
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			
			entityManagerR.createNativeQuery("CALL  ssdx_eng.usm_client_param_def_update(:param_code, :param_value)")
				.setParameter("param_code", param_code)
				.setParameter("param_value", param_value).executeUpdate();
			
		} catch (Exception e) {
			Connection con = DataSourceUtils.getConnection(dataSource);
		    DataSourceUtils.releaseConnection(con, dataSource);
		
			transactionManager.rollback(status); 
			
			throw e;
		}
		   	
}}