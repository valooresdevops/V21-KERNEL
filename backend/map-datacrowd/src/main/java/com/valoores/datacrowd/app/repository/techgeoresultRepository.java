package com.valoores.datacrowd.app.repository;

import java.math.BigDecimal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.valoores.datacrowd.app.model.TECH_geo_result;
import com.valoores.datacrowd.app.model.loc_report_config;


@Repository
public interface techgeoresultRepository  extends JpaRepository<TECH_geo_result, Integer> {
	
	
	@Query(value = "select polyline from TECH_geo_result_+ queryId ", nativeQuery = true)
	public String getgeoresult(@Param("queryId") Integer queryId);

//	@Query(value = "SELECT JSON FROM SSDX_TMP.TMP_CYB_GRAPH_RECORDS_118961", nativeQuery = true)
//	public String getGraphRecords(@Param("queryId") String queryId);
	
	
//	@Query(value = "{SELECT JSON  from SSDX_TMP.TMP_CYB_GRAPH_RECORDS_ + queryId}", nativeQuery = true)
//	public List<loc_report_config> setDynQuery(@Param("queryId") Integer queryId, @Param("graph") String graph);
//

	
	
}
