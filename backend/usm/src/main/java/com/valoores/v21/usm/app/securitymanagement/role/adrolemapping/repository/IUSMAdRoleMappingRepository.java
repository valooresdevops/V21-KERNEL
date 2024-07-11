package com.valoores.v21.usm.app.securitymanagement.role.adrolemapping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.valoores.v21.usm.app.common.syslines.model.Syslines;
import com.valoores.v21.usm.app.securitymanagement.role.adrolemapping.model.USMAdRoleMapping;
import com.valoores.v21.usm.app.securitymanagement.role.adrolemapping.model.USMAdRoleMappingLdapConfig;

public interface IUSMAdRoleMappingRepository extends JpaRepository<USMAdRoleMapping, Long> {

	USMAdRoleMapping findById(long id);
	
	@Query("select a from USMAdRoleMapping a")
	List<USMAdRoleMapping> getAddMappingCombo();

	@Query("select a from USMAdRoleMappingLdapConfig a")
	List<USMAdRoleMappingLdapConfig> getLdapConfCombo();

	@Query("select l from Syslines l where  l.heaCode = 6023 and l.id in (2,3,16,17,21,22,35,36)")
	List<Syslines> getObjectTypeCombo();
	

}
