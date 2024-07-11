package com.valoores.inDisplayApplication.app.objectBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.valoores.inDisplayApplication.app.objectBuilder.model.AppChartField;

public interface AppChartFieldRepository extends JpaRepository<AppChartField , Long> {
	
	@Transactional
	@Modifying
	@Query("delete from AppChartField a where a.chartId = :id")
	void deleteById(long id);
	
	@Transactional
	@Modifying
	@Query("update AppChartField u set u.fieldName = :fieldName,u.queryFieldName = :queryFieldName ,u.fieldColor= :fieldColor,"
			+ " u.isSerie= :isSerie,u.serieType= :serieType,u.drilldown= :drilldown , u.drilldownType = :drilldownType  where u.chartId = :chartId")
	void updateChartField(long chartId,String fieldName,  String queryFieldName,String  fieldColor,String isSerie,
			Integer serieType,String drilldown,Integer drilldownType);
	
	

    
//	@Query(value = "INSERT INTO AppChartField u(u.fieldName,u.queryFieldName,u.fieldColor,u.isSerie,u.serieType,u.drilldown,u.drilldownType) VALUES (:fieldName ,:queryFieldName ,:fieldColor ,:isSerie ,:serieType ,:drilldown ,:drilldownType)", nativeQuery = true)
//	void insertChartField(String fieldName,  String queryFieldName,String  fieldColor,String isSerie,Integer serieType,String drilldown,Integer drilldownType);

}
