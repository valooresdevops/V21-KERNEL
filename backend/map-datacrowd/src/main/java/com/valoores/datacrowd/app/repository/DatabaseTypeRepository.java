package com.valoores.datacrowd.app.repository;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.valoores.datacrowd.app.model.DatabaseType;




@Repository
public interface DatabaseTypeRepository extends JpaRepository<DatabaseType, Long>{

	

	@Query(value = "SELECT DB_NAME FROM TECHDBA.TECH_DATA_BASE_TYPE WHERE DB_ID = 1"
			, nativeQuery = true)
	String   getdatabasetype();
	
	

}
