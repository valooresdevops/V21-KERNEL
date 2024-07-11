package com.valoores.datacrowd.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.valoores.datacrowd.app.model.LOC_TELCO_BTS_CELL_DEF;

@Repository
public interface FixedElementsRepository extends JpaRepository<LOC_TELCO_BTS_CELL_DEF, Integer> {

	@Query("SELECT B.shapeid, B.name, E.type, B.lng, B. lat FROM LOC_TELCO_BTS_CELL_DEF B LEFT JOIN INV_EQUIPMENT E  ON E.equipmentid = B.equipmentid WHERE E.equipmentTypeId IN (3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,14,15,16,17,18,19,20,21,22,23,24,25,26,27) AND  B.shapeid IN (:simulationId) ")
	List<Object> getfixedelementsObject(@Param("simulationId") List<Object> simulationId);

	@Query(value = "SELECT B.BTS_CELL_ID, B.BTS_CELL_NAME, E.EQUIPMENT_MODEL_NAME, B.LOCATION_LONGITUDE, B. LOCATION_LATITUDE FROM LOCDBA.LOC_TELCO_BTS_CELL_DEF B LEFT JOIN INVDBA.INV_EQUIPMENT E  ON E.EQUIPMENT_ID = B.EQUIPMENT_ID WHERE E.EQUIPMENT_TYPE_ID IN (3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,14,15,16,17,18,19,20,21,22,23,24,25,26) AND  B.BTS_CELL_ID  IN (SELECT x.IDSSS FROM techdba.tmp_ins_ds x)\r\n"
			+ " ", nativeQuery = true)
	List<Object> getfixedelementsObject2();

	
}
