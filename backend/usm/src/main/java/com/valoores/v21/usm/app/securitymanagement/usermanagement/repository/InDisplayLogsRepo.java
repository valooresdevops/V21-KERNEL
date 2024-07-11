package com.valoores.v21.usm.app.securitymanagement.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.InDisplayLogsModel;


public interface InDisplayLogsRepo extends JpaRepository <InDisplayLogsModel, Long>{

	@Query("SELECT a.changes FROM InDisplayLogsModel a WHERE a.logId = :logId")
	String fetchLogDetails(long logId);
	
}
