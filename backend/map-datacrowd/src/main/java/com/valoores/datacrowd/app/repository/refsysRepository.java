package com.valoores.datacrowd.app.repository;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.valoores.datacrowd.app.model.refsys13000;




@Repository
public interface refsysRepository extends JpaRepository<refsys13000, Long>{

	@Query( "select  t.linname from refsys13000 t where t.lincode = :reportType")
	public String getTypeName(int reportType);


	@SuppressWarnings("rawtypes")
	@Query( "SELECT S.lincode AS ID, S.linname AS NAME FROM refsys13000 S WHERE S.lincode NOT IN (4, 5) ORDER BY ID ASC")
	public List getSimulationTypes();
	
}
