package com.valoores.inDisplayApplication.app.ScreenBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.valoores.inDisplayApplication.app.ScreenBuilder.model.ScreenMenuModel;
import com.valoores.inDisplayApplication.app.ScreenBuilder.model.ScreenUserAccessModel;

@Repository
public interface ScreenUserAccessRepo  extends JpaRepository <ScreenUserAccessModel, Integer>{





	
	
	@Modifying
	@Query("delete from ScreenUserAccessModel a where a.MENU_CODE = :menuCode")
	void deleteScreenUserAcess(String menuCode);
	
	

}
