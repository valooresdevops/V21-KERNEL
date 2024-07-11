package com.valoores.inDisplayApplication.app.formBuilder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.valoores.inDisplayApplication.app.formBuilder.model.CfgObjectDefMenus;

@Repository
public interface CfgObjectDefMenusRepository extends JpaRepository<CfgObjectDefMenus, Long> {

	@Query("SELECT D FROM CfgObjectDefMenus D WHERE D.objectType = 16 and D.isMain = '1' order by D.id desc ")
	List<CfgObjectDefMenus> getMenusButton(); 
	
}
