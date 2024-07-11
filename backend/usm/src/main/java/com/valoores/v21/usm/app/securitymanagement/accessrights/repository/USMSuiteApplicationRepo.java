package com.valoores.v21.usm.app.securitymanagement.accessrights.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.valoores.v21.usm.app.securitymanagement.accessrights.model.USMSuiteApplication;



@Repository
public interface USMSuiteApplicationRepo extends JpaRepository <USMSuiteApplication, Long> {

	
	@SuppressWarnings("rawtypes")
    @Query("select appCode as id, appName as name from USMSuiteApplication t")
	List getAppCombo();
	
}
