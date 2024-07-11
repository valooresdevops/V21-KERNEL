package com.valoores.datacrowd.app.repository;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.valoores.datacrowd.app.model.techLast_simul;



 
@Repository
public interface LastSimulationRepository extends JpaRepository<techLast_simul, Long>{
	@Transactional
	@Modifying
	@Query( "DELETE FROM techLast_simul t where t.CreatedBy =:createdBy ")
	public void deletelastSimualtion(int createdBy);


	 @Query( "select  t.simulid,t.CreatedBy from techLast_simul t where t.CreatedBy =:createdBy")
	 public Object getLastSimualtionID(int createdBy) ;

	
}
