package com.valoores.datacrowd.app.repository;




import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.valoores.datacrowd.app.model.LOC_REPORT_CONFIG_DSTAT;




@Repository
public interface SimulationRepositorydstat extends JpaRepository<LOC_REPORT_CONFIG_DSTAT, Long>{

	@Transactional
    void deleteBysimulationId(Integer simulid);
  
	  @Transactional
	    @Modifying
	    @Query("DELETE FROM LOC_REPORT_CONFIG_DSTAT u WHERE u.simulationId = :simulationId")
	    void deleteBySimulationId(@Param("simulationId") Integer simulationId);


}
