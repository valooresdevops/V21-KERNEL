package com.valoores.datacrowd.app.repository;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.valoores.datacrowd.app.model.loc_report_config;




@Repository
public interface SimulationRepository extends JpaRepository<loc_report_config, Long>{

	

    List<loc_report_config> findByCreatedByOrderByExecutionDateDesc(long createdBy);
    loc_report_config findFirstSimulationIdByCreatedByOrderBySimulationIdDesc(int createdBy);

	
}
