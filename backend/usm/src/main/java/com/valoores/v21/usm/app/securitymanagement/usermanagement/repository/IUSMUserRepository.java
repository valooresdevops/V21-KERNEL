package com.valoores.v21.usm.app.securitymanagement.usermanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.valoores.v21.usm.app.securitymanagement.accessrights.model.USMUser;
import com.valoores.v21.usm.app.securitymanagement.role.model.USMRoleMulti;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMCurrency;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMLanguage;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMMdmBsnUnitGroup;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMRefSysParamLines;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMStatus;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMSyslines;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMUserRoles;

@Repository
public interface IUSMUserRepository extends PagingAndSortingRepository<USMUser, Long> {
	
	USMUser findById(long id);
	USMUser findByUsername(String username);
	List<USMUser> findByCreationDate(String creationDate);
	List<USMUser> findByUsernameContaining(String username);
	
	@Query("SELECT u.id, u.creationDate from USMUser u where u.id = :id")
	Object getIdAndCreationDate(long id);
	
	@Query("select a from USMStatus a where a.name in ('Active','Inactive','Deleted') AND statusCode is null")
	List<USMStatus> getStatusCombo();
	
	@Query("select l from USMLanguage l")
	List<USMLanguage> getLanguageCombo();
	
	@Query("select l from USMSyslines l where  l.heaCode = 6023 and l.id in (2,3,16,17,21,22,35,36)")
	List<USMSyslines> getBugTypeCombo();
	
	@Query("select r from USMRefSysParamLines r where  r.heaCode = 5 ")
	List<USMRefSysParamLines> getCivilStatusCombo();
	
	@Query("select c from USMCurrency c ")
	List<USMCurrency> getCurrencyCombo();
	
	@Query("select b from USMMdmBsnUnitGroup b where bsnGroupId = :id ")
	USMMdmBsnUnitGroup getBugInfoById(long id);
	
	@Query("select r from USMUserRoles r ")
	List<USMUserRoles> getRoleCombo();
	

	
	
	void deleteById(Integer id);
	
	@Query("select r from USMUserRoles r where r.id in :ids ")
	List<USMUserRoles> getRoleCombo(@Param("ids") List<Long> ids);
	
	@Query("select l from USMRoleMulti l where l.roleId in :ids and l.menuCode is null")
	List<USMRoleMulti> getBugIds(@Param("ids") List<Long> ids);
	
	
	
}
  