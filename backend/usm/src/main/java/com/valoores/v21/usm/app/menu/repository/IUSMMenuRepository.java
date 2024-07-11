package com.valoores.v21.usm.app.menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.valoores.v21.usm.app.common.menucombo.model.USMMenu;

@Repository
public interface IUSMMenuRepository extends JpaRepository<USMMenu, String> {
	
	@Query("select l from USMMenu l where menuCode like :id% ")
	List<USMMenu> getApplicationMenuCombo(String id);
	
	@Query("select l.menuCode from USMMenu l where l.menuCode = :id")
	String getMenuCode(Integer id);

	String findByMenuCode(String id);
	
	@Query("select count(a) from USMMenu a where a.menuVar like :menuName and a.menuManaged = '1'")
	String getIsManagedMenu(String menuName);
	
	
	@Query("select a.menuCode from USMMenu a where a.menuName like :menuName")
	USMMenu findByMenuName(String menuName);
	
//	@Query("select a.accessCode from USMUserMulti a where a.userId = :userId and a.menuCode = (select m.menuCode from USMMenu m where m.menuVar = :menuName) ")
//	String getAccessRight(String menuName ,long userId);}
	
	@Query("select DISTINCT a.accessCode from USMUserMulti a where a.userId = :userId and a.menuCode = (select m.menuCode from USMMenu m where m.menuVar = :menuName) ")
	String getAccessRight(String menuName ,long userId);
}
