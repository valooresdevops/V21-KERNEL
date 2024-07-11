//package com.valoores.datacrowd.common.repository;
//
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.query.Procedure;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import com.valoores.datacrowd.common.model.P_FILL_DATA_FILTERING;
//
//@Repository
//public interface fillDataFilteringRepository  extends JpaRepository<P_FILL_DATA_FILTERING, Long> {
//	
//	@Procedure(procedureName="P_FILL_DATA_FILTERING")
//	   public void callprocedure(@Param("ReportJsonParamId") String ReportJsonParamId,@Param("UserId") Long UserId);
//	
//
//}
