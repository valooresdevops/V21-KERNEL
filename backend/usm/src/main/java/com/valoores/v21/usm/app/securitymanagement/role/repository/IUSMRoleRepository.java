package com.valoores.v21.usm.app.securitymanagement.role.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.valoores.v21.usm.app.common.syslines.model.Syslines;
import com.valoores.v21.usm.app.securitymanagement.role.model.USMRole;

public interface IUSMRoleRepository extends JpaRepository<USMRole, Long> {

	USMRole findById(long id);

	USMRole findByRoleName(String name);

	@Query("select l from Syslines l where  l.heaCode = 6023 and l.id in (2,3,16,17,21,22,35,36)")
	List<Syslines> getBugTypeCombo();

}
