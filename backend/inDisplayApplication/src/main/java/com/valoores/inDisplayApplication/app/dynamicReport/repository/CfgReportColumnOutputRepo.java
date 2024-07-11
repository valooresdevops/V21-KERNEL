package com.valoores.inDisplayApplication.app.dynamicReport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.valoores.inDisplayApplication.app.dynamicReport.model.CfgReportColumnOutputIdModel;
import com.valoores.inDisplayApplication.app.dynamicReport.model.CfgReportColumnOutputModel;

public interface CfgReportColumnOutputRepo extends JpaRepository<CfgReportColumnOutputModel, CfgReportColumnOutputIdModel>{

	@Modifying
	@Query("DELETE FROM CfgReportColumnOutputModel WHERE reportDynamicConfigId = :reportId")
	void deleteAllByReportId(long reportId);
	

}
