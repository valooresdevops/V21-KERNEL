package com.valoores.datacrowd.app.repository;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.valoores.datacrowd.app.model.tmp_ins_ds;




@Repository
public interface tmp_ins_dsRepository extends JpaRepository<tmp_ins_ds, Long>{
	@Transactional
	@Modifying
	@Query( "DELETE FROM tmp_ins_ds")
	public void deleteIDS();


	

	
}
