package com.valoores.v21.usm.app.common.menucombo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.valoores.v21.usm.app.common.menucombo.model.USMMenu;

public interface IUSMMenuComboRepository extends PagingAndSortingRepository<USMMenu, String>{
	
	@Query("select a from USMMenu a")
	List<USMMenu> getMenuCombo(Integer menuCode);
	
	@Query("select b from USMMenu b")
	List<USMMenu> getMenuName();

}
