package com.valoores.datacrowd.app.repository;




import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.valoores.datacrowd.app.model.LOC_REPORT_CONFIG_USER;




@Repository
public interface SimulationRepositoryUser extends JpaRepository<LOC_REPORT_CONFIG_USER, Long>{

    void deleteBysimulationId(Integer statusId);

	  @Transactional
	    @Modifying
	    @Query("DELETE FROM LOC_REPORT_CONFIG_USER u WHERE u.simulationId = :simulationId")
	    void deleteBySimulationId(@Param("simulationId") Integer simulationId);


}
