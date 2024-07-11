package com.valoores.datacrowd.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.valoores.datacrowd.app.model.tmp_report_coordinates_6_;


@Repository
public interface TmpReportCoordinatesRepository  extends JpaRepository<tmp_report_coordinates_6_, Integer> {
	
	
//
//	@Query(value = "SELECT DEVICE_ID FROM ssdx_eng.tmp_report_coordinates_6_135283", nativeQuery = true)
//	public List getdevices(@Param("queryId") int  queryId);
//
//	
	
}
