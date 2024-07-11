package com.valoores.inDisplayApplication.app.ScreenBuilder.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.valoores.inDisplayApplication.app.ScreenBuilder.model.ScreenObjectModel;

@Repository
public interface ScreenObjectRepo extends JpaRepository <ScreenObjectModel, Long>{

	ScreenObjectModel findByObjectId(long id);   

	@Query("SELECT a.MENU_CODE FROM ScreenObjectModel a WHERE a.objectId= :id ")
	String getMenuCode(long id);

	

	@Query("SELECT a.OBJECT_PARAM FROM ScreenObjectModel a WHERE a.objectId= :id ")
	byte[] getEncodedData(long id);
	
	
	@Modifying
	@Query("DELETE FROM ScreenObjectModel a WHERE a.objectId= :id ")
	void deleteScreenObject(long id);
	
	@Query("SELECT a.objectId from ScreenObjectModel a where a.MENU_CODE= :menuCode and a.OBJECT_TYPE=16 and a.IS_MAIN=2")
	BigDecimal getObjectIdFromMenu(String menuCode);
	
	
}


