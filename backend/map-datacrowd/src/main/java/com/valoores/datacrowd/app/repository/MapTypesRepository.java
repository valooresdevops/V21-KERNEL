package com.valoores.datacrowd.app.repository;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.valoores.datacrowd.app.model.TechMapTypes;





@Repository
public interface MapTypesRepository extends JpaRepository<TechMapTypes, Long>{

	@SuppressWarnings("rawtypes")
	 @Query( "select  t.maptypeid,t.value,t.name,t.id from TechMapTypes t where isonline = 1 and flag=1")
	 public List getmaptypes() ;

	 @SuppressWarnings("rawtypes")
	 @Query( "select  t.maptypeid,t.value,t.name,t.id from TechMapTypes t where isonline = 0 and flag=1 ")
	 public List getmaptypesOffline() ;


	
	
	
	
	
	
}
