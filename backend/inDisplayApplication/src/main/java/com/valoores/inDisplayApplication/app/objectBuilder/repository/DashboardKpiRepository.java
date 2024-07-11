package com.valoores.inDisplayApplication.app.objectBuilder.repository;

import org.springframework.stereotype.Repository;

import com.valoores.inDisplayApplication.app.objectBuilder.model.CfgDashboardObjectKpi;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface DashboardKpiRepository extends JpaRepository<CfgDashboardObjectKpi , Long> {
	@Transactional
	@Modifying
	@Query("DELETE FROM CfgDashboardObjectKpi a where a.dashboardKpiId = :id ")
	void deleteById(long id);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM CfgDashboardObjectKpi a where a.kpiId = :id ")
	void deleteByKpiId(long id);
	
}
