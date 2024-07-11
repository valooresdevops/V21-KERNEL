package com.valoores.datacrowd.app.repository;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.valoores.datacrowd.app.model.DatabaseType;




@Repository
public interface DataCrowdRepository extends JpaRepository<DatabaseType, Long>{

	

	
	
}
