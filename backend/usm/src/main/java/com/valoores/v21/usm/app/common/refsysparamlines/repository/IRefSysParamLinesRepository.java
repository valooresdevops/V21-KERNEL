package com.valoores.v21.usm.app.common.refsysparamlines.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.valoores.v21.usm.app.common.refsysparamlines.model.RefSysParamLines;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.dto.USMSyslinesIds;

public interface IRefSysParamLinesRepository extends JpaRepository  <RefSysParamLines, USMSyslinesIds> {

	@Query("select a from RefSysParamLines a")
	 List<RefSysParamLines> getRefSysParamLines();
}
