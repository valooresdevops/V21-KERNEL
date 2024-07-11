package com.valoores.v21.usm.app.logs.logsbyheatmapproc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.valoores.v21.usm.app.logs.logsbyheatmapproc.model.HeatMapTable;

@Repository
public interface ILogsByHeatmapProcRepository extends JpaRepository<HeatMapTable, Long> {
	 @Procedure(name = "usm_user_log_proc")
	    void usmUserLogProc(String startDate, String endDate);
	}
