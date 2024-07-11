package com.valoores.v21.usm.app.parameters.insystemparameters.passwordsettings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.valoores.v21.usm.app.parameters.insystemparameters.passwordsettings.model.ClientParam;

@Repository
public interface IPwdSettingsRepository extends JpaRepository<ClientParam, Long> {
	@Modifying
	@Query(value = "CALL usm_client_param_def_update(:param_code, :param_value)", nativeQuery = true)
	void updatePasswordSettings(@Param("param_code") Integer param_code, @Param("param_value") String param_value);
}