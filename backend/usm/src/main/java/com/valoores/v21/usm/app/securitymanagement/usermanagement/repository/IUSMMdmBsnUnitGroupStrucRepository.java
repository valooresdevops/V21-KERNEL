package com.valoores.v21.usm.app.securitymanagement.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.valoores.v21.usm.app.securitymanagement.usermanagement.model.USMMdmBsnUnitGroupStruc;

public interface IUSMMdmBsnUnitGroupStrucRepository extends JpaRepository<USMMdmBsnUnitGroupStruc, Long> {
   
	@Query("select l.strucBsnGroupTypeId from USMMdmBsnUnitGroupStruc l where strucBsnGroupId = :id")
	Integer getBugType(long id);
	

}
