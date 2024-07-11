package com.valoores.inDisplayApplication.app.objectBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.valoores.inDisplayApplication.app.objectBuilder.model.CfgObjectKpiModel;

@Repository
public interface CfgObjectKpiRepository extends JpaRepository<CfgObjectKpiModel , Long>{

	@Transactional
	@Modifying
	@Query("DELETE FROM CfgObjectKpiModel a where a.kpiId = :id")
	void deleteById(long id);
	
	@Transactional
	@Modifying
	@Query("update CfgObjectKpiModel u set u.kpiName = :kpiName,u.qbeId = :query ,u.isRatio= :isRatio ,"
			+ " u.mainValue= :mainValue,u.mainLabel= :mainLabel,u.extraLabel= :extraLabel , u.extraValue = :extraValue,"
			+ "u.isPercentage = :isPercentage , u.backgroundColor= :backgroundColor,u.textColor=:textColor, u.chart= :chart,"
			+ " u.grid= :grid , u.report= :report where u.kpiId = :kpiId")
	void updateKpi(long kpiId, String kpiName, long query,String isRatio,
					String mainValue,String mainLabel,String extraLabel,String extraValue,
					String isPercentage,String backgroundColor,String textColor,String chart,
					String grid, String report);
	
	
	@Query("SELECT u.qbeId as query FROM CfgObjectKpiModel u WHERE u.kpiId= :kpiId")
	Integer selectQuery(long kpiId);
	
}
