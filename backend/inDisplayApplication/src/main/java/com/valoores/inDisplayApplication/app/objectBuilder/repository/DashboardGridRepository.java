package com.valoores.inDisplayApplication.app.objectBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.valoores.inDisplayApplication.app.objectBuilder.model.CfgDashboardGridModel;

@Repository
public interface DashboardGridRepository extends JpaRepository<CfgDashboardGridModel , Long> {
	@Transactional
	@Modifying
	@Query("DELETE FROM CfgDashboardGridModel a where a.dashboardGridId = :id ")
	void deleteById(long id);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM CfgDashboardGridModel a where a.gridId = :id ")
	void deleteByGridId(long id);
}
