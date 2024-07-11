package com.valoores.inDisplayApplication.app.objectBuilder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.valoores.inDisplayApplication.app.objectBuilder.model.AppChart;

public interface AppChartRepository extends JpaRepository<AppChart , Long> {

	
	@Transactional
	@Modifying
	@Query("DELETE FROM AppChart a where a.chartId = :id ")
	void deleteById(Long id);
	
	@Transactional
	@Modifying
	@Query("update AppChart u set u.chartName = :fieldsetName , u.objectKpiId =:objectKpiId , u.query =:query , u.chartSize= :chartSize , u.chartHTitle =:chartHTitle , u.chartVTitle= :chartVTitle ,u.showLegend = :showLegend  , u.is3d = :is3d , u.isHorizontal = :isHorizontal , u.chartType = :chartType where u.chartId = :fieldsetId")
	void updateChart(long fieldsetId,
					 String fieldsetName,
					 Integer objectKpiId,
					 int query,
					 Integer chartSize,
					 String chartHTitle,
					 String chartVTitle,
					 String showLegend,
					 String is3d,
					 String isHorizontal,
					 Integer chartType);
	
	
	@Query("SELECT u.query as queryId FROM AppChart u WHERE u.chartId= :chartId")
	Integer selectQuery(long chartId);
	
	@Query("SELECT u.chartType as chartType FROM AppChart u WHERE u.chartId= :chartId")
	Integer selectChartType(long chartId);
	
	
	@Transactional
	@Modifying
	@Query("update AppChart u set u.objectKpiId =:objectKpiId  where u.chartId = :chartId")
	void updateDropdownChart(long chartId, long objectKpiId);
	
	
	
	@Query("SELECT a.chartId FROM AppChart a WHERE a.objectKpiId = :kpiId")
	List<Long> selectChartRelatedKpi(long kpiId);
	
	@Query("SELECT u.is3d as chartType FROM AppChart u WHERE u.chartId= :chartId")
	Integer checkIfIs3d(long chartId);
	

	@Transactional
	@Modifying
	@Query("update AppChart set objectKpiId = null where object_kpi_id= :kpiId")
	void removeDropdownChart(long kpiId);
	/*
	 * @Transactional
	 * 
	 * @Modifying
	 * 
	 * @Query("update AppChartField u set u.isSerie = :isSerie, u.drilldown = :drilldown where u.fieldId = :fieldsetId"
	 * ) void updateChartField(long fieldsetId, boolean isSerie, Integer drilldown,
	 * boolean showLegend,boolean is3d,boolean isHorizontal);
	 */
}
