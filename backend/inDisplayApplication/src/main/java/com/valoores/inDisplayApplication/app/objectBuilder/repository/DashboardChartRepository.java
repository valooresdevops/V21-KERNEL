package com.valoores.inDisplayApplication.app.objectBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.valoores.inDisplayApplication.app.objectBuilder.model.CfgDashboardChartModel;

@Repository
public interface DashboardChartRepository extends JpaRepository<CfgDashboardChartModel , Long> {
	@Transactional
	@Modifying
	@Query("DELETE FROM CfgDashboardChartModel a where a.dashboardChartId = :id ")
	void deleteById(long id);
	
	
	@Transactional
	@Modifying
	@Query("DELETE FROM CfgDashboardChartModel a where a.chartId = :id ")
	void deleteByChartId(long id);
}
