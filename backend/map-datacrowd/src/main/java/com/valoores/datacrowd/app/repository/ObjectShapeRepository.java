package com.valoores.datacrowd.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.valoores.datacrowd.app.model.LOC_LOCATION_MAP_OBJECT_SHAPE;





@Repository
public interface ObjectShapeRepository extends JpaRepository<LOC_LOCATION_MAP_OBJECT_SHAPE, Integer>{

	 @Query( "select  t.objectShapeValue from LOC_LOCATION_MAP_OBJECT_SHAPE t where t.shapeid =:simulationId ")
	 public byte[] getSelectedShape(int simulationId) ;



	
		
	  public boolean existsByshapeName(String shapeName);
	
	
	
	
}
