package com.valoores.v21.usm.app.securitymanagement.role.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.valoores.v21.usm.app.securitymanagement.role.model.USMRoleMulti;

@Repository
public interface IUSMRoleMultiRepository extends JpaRepository<USMRoleMulti, Long>{
	@Modifying
	@Query("delete from USMRoleMulti a where a.roleId = :id and a.menuCode is null")
	void deleteByRoleId(long id);
	
	@Modifying
	@Query("delete from USMRoleMulti a where a.roleId = :id and a.menuCode =:menu")
	static void deleteAppByRoleId(long id,String menu) {
		
	}
	
	@Modifying
	@Query("delete from USMRoleMulti a where a.roleId = :id ")
	void deleteAllByRoleId(long id);

	@Query("select a from USMRoleMulti a where a.roleId = :id and a.menuCode is null and a.accessCode is null")
	List<USMRoleMulti> findByRoleId(long id);
	
	@Modifying
	@Query("UPDATE USMRoleMulti set accessCode = :accessCode , updatedBy = :userLogedId ,  updateDate = SYSDATE WHERE multiId = :id")
	void updateAccessRightOnRole(long id, String accessCode, Integer userLogedId);
	
	
}
