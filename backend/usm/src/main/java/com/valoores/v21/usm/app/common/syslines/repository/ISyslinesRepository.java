package com.valoores.v21.usm.app.common.syslines.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.valoores.v21.usm.app.common.syslines.model.Syslines;
import com.valoores.v21.usm.app.common.syslines.model.SyslinesIds;

@Repository
public interface ISyslinesRepository extends JpaRepository <Syslines, SyslinesIds> {
	
	
	List<Syslines> findByHeaCode(Integer heaCode);
	
	
	@Query("SELECT A.name FROM Syslines A where A.id = :roleType and A.heaCode = :heaCode ")
	String findNameByIdAndHeaCode(Integer roleType,Integer heaCode);

	

	@Query("SELECT A FROM Syslines A where A.id in (3,4) and A.heaCode = 208 ")
	List<Syslines> getSyslinesPeriodCombo();
	
}
