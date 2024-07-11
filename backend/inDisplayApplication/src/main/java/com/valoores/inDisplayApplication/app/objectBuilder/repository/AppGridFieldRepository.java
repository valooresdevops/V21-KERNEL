package com.valoores.inDisplayApplication.app.objectBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.valoores.inDisplayApplication.app.objectBuilder.model.AppGridField;

@Repository
public interface AppGridFieldRepository extends JpaRepository<AppGridField , Long>{
	
	@Transactional
	@Modifying
	@Query("delete from AppGridField a where a.gridId = :id")
	void deleteById(long id);
	
	
	  @Transactional
	  @Modifying
	  @Query("update AppGridField u set u.fieldName = :fieldName  ,u.qryFieldName = :qryFieldName ,u.drilldown= :drilldown  where u.gridId = :gridId"
	  ) void updateGridField(long gridId, String fieldName,String qryFieldName,Integer drilldown);
	  
		
	 
}
