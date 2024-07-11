package com.valoores.inDisplayApplication.app.common.syslines.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.valoores.inDisplayApplication.app.common.syslines.model.Syslines;
import com.valoores.inDisplayApplication.app.common.syslines.model.SyslinesIds;

@Repository
public interface ISyslinesRepository extends JpaRepository <Syslines, SyslinesIds> {
	
	@Query("SELECT A FROM Syslines A where A.heaCode = :heaCode")
	List<Syslines> getSysLinesData(Integer heaCode);
	
	@Query("SELECT A FROM Syslines A WHERE A.heaCode = :heaCode AND A.id IN :Ids")
	List<Syslines> getSysLinesDataWithIds(Integer heaCode, List<Integer> Ids);
}
