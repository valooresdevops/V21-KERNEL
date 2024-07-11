package com.valoores.datacrowd.app.repository;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.valoores.datacrowd.app.model.REF_SYS_LINES;




@Repository
public interface RefSysLineRepository extends JpaRepository<REF_SYS_LINES, Long>{

	@Query( "select  t.lincode from REF_SYS_LINES t where t.heacode =13002 And  t.linname = :Shapetype")
	public int getlinecode(String Shapetype);



	
	
}
