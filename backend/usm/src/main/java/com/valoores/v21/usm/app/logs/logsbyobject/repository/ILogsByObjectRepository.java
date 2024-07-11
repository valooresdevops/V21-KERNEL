package com.valoores.v21.usm.app.logs.logsbyobject.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.valoores.v21.usm.app.common.menucombo.model.USMMenu;

@Repository
public interface ILogsByObjectRepository extends PagingAndSortingRepository<USMMenu, String>{

//	@Query("select t from USMApplication t")
//	List<USMApplication> getApplicationCombo();
	
//	@Query("select a from ObjectMenu a")
//	List<USMMenu> getMenuCombo();
	
}
