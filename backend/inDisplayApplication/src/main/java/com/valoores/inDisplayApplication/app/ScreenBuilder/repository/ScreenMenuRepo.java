package com.valoores.inDisplayApplication.app.ScreenBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.valoores.inDisplayApplication.app.ScreenBuilder.model.ScreenMenuModel;

@Repository
public interface ScreenMenuRepo  extends JpaRepository <ScreenMenuModel, String>{

	
	@Query("SELECT MAX(a.MENU_CODE) FROM ScreenMenuModel a WHERE a.MENU_P_CODE= :code ")
	String getNewMenuCode(String code);

//	@Query("SELECT CONCAT(MAX(a.MENU_CODE), '2')  FROM ScreenMenuModel a WHERE a.MENU_P_CODE = :code")
//	String getNewMenuCode(@Param("code") String code);




	
	
	@Modifying
	@Query("delete from ScreenMenuModel a where a.MENU_CODE = :menuCode")
	void deleteScreenMenu(String menuCode);
	
	@Query("SELECT a.MENU_CODE FROM ScreenMenuModel a where MENU_VARIABLE= :menuVariable")
	String getMenuCode(String menuVariable);

}
