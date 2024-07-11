package com.valoores.inDisplayApplication.app.dynamicReport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.valoores.inDisplayApplication.app.dynamicReport.model.CfgReportColumnFilterModel;

public interface CfgReportColumnFilterRepo extends JpaRepository<CfgReportColumnFilterModel, Long>{

	@Query("SELECT COUNT(*) FROM CfgReportColumnFilterModel WHERE reportDynamicConfigId= :reportId AND columnId = :columnId")
	int checkIfFilterExists(long reportId,long columnId);
	
	
	@Modifying
	@Query("UPDATE CfgReportColumnFilterModel SET conditionValue= :columnValue WHERE columnId= :columnId AND reportDynamicConfigId = :reportId")
	void updateColumnFilter(long reportId,long columnId,String columnValue);
	
}
