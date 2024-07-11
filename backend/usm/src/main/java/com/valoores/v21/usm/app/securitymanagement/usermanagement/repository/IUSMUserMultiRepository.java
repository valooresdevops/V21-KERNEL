package com.valoores.v21.usm.app.securitymanagement.usermanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMUserMulti;

@Repository
public interface IUSMUserMultiRepository extends PagingAndSortingRepository<USMUserMulti, Long> {

	void deleteById(long bsnGroupId);

	@Modifying
	@Query("delete from USMUserMulti a where a.userId = :id and a.menuCode is null and a.accessCode is null")
	void deleteByUserId(long id);
	
	@Modifying
	@Query("delete from USMUserMulti a where a.userId = :id and a.menuCode =:menu")
	static void deleteAppByUserId(long id,String menu) {
		
	}
	
	@Modifying
	@Query("delete from USMUserMulti a where a.userId = :id ")
	void deleteAllByUserId(long id);

	@Query("select a from USMUserMulti a where a.userId = :id and a.menuCode is null and a.accessCode is null")
	List<USMUserMulti> getAllUsersWithoutAccessRights(long id);
	
	@Query("select r.name from USMUserRoles r where r.id = (select distinct m.roleId from USMUserMulti m where isDefault = '1' and m.userId = :id ) ") 
	String findRoleName(long id );
	
	@Query("select r.id from USMUserRoles r where r.id = (select distinct m.roleId from USMUserMulti m where isDefault = '1' and m.userId = :id ) ") 
	String findRoleId(long id );
	
	@Modifying
	@Query("UPDATE USMUserMulti set accessCode = :accessCode , "
		                        +" updatedBy = :userLogedId , "
		                        +" updateDate = SYSDATE  "
			                    + " WHERE id = :id")
	void updateAccessRightOnUser(long id, String accessCode , Integer userLogedId);
	
	@Modifying
	@Query("UPDATE USMRoleMulti set accessCode = :accessCode , "
		                        +" updatedBy = :userLogedId , "
		                        +" updateDate = SYSDATE  "
			                    + " WHERE id = :id")
	void updateAccessRightOnRole(long id, String accessCode , Integer userLogedId);

}
   