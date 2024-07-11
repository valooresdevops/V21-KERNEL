package com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.model.LdapConfiguration;

public interface ILdapConfigurationRepository extends JpaRepository<LdapConfiguration, Long> {

	LdapConfiguration findByLdapConfigId(long ldapConfigId);

	LdapConfiguration findByldapConfigId(long ldapConfigId);

	@Modifying
	@Query("UPDATE LdapConfiguration SET isLdapConfigActive = 0 , updateDate = SYSDATE , updatedBy = :userId WHERE isLdapConfigset  = 1 ")
	void setUnActive();

}
