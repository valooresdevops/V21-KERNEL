package com.valoores.inDisplayApplication.app.formBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.valoores.inDisplayApplication.app.formBuilder.model.QueryFormButtonModel;


public interface QueryFormButtonRepo extends JpaRepository <QueryFormButtonModel, Long>{

	@Query("SELECT count(1) FROM QueryFormButtonModel WHERE objectId = :objectId")
	long checkIfExists(long objectId);
	
	@Query("SELECT buttonList FROM QueryFormButtonModel WHERE objectId = :objectId")
	String getButtonList(long objectId);
	
	
//	@Query("SELECT REPLACE(REPLACE(REPLACE(buttonList,'[\"',''),'\"]',''),'\",\"',',') as id from QueryFormButtonModel WHERE objectId= :objectId")
//	String getButtonListJSON(long objectId);
	
	@Query("SELECT buttonList as id from QueryFormButtonModel WHERE objectId= :objectId")
	String getButtonListJSON(long objectId);
	
	
	@Transactional
	@Modifying
	@Query("DELETE FROM QueryFormButtonModel WHERE objectId = :objectId")
	void deleteQueryForm(long objectId);
	
}
