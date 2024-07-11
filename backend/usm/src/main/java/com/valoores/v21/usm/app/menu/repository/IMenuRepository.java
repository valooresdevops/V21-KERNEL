//package com.valoores.v21.usm.app.menu.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import com.valoores.v21.usm.app.menu.model.Menu;
//
//@Repository
//public interface IMenuRepository extends JpaRepository<Menu, String> {
//	
//
//
//	@Query("select count(a) from USMMenu a where a.menuVar like :menuName and a.menuManaged = 1")
//	String getIsManagedMenu(String menuName);
//	
//
//}
