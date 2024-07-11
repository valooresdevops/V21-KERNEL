package com.valoores.datacrowd.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.valoores.datacrowd.app.model.LOC_PAR_LOCATION_MAP;


@Repository
public interface LOC_PAR_LOCATION_MAPRepository  extends JpaRepository<LOC_PAR_LOCATION_MAP, Long> {
	
	
	@Query( "SELECT 	S.parID,S.pollimit,S.circlelimit,S.rectanglelimit,S.timelimit,S.maplayouttype,S.description1,S.description2,S.description3,S.description4,S.lastsimulationdesc	\r\n" + 
			"  FROM LOC_PAR_LOCATION_MAP S")
	public List<Object> getShapelimit();


}
