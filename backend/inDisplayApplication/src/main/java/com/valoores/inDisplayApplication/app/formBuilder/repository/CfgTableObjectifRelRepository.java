package com.valoores.inDisplayApplication.app.formBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.valoores.inDisplayApplication.app.formBuilder.model.CfgTableObjectifRelModel;

@Repository
public interface CfgTableObjectifRelRepository extends JpaRepository<CfgTableObjectifRelModel, CfgTableObjectifRelModel>{

	@Transactional
	@Modifying
	@Query("delete from CfgTableObjectifRelModel a where a.objectId = :objectId")
	void deleteFormBuilder(long objectId);
	
	@Transactional
	@Modifying
	@Query("delete from CfgTableObjectifRelModel a where a.objectId = :objectId and a.tableId = :tableId")
	void deleteFormTable(long objectId, long tableId);
	
	@Query("SELECT COUNT(a) FROM CfgTableObjectifRelModel a WHERE a.objectId = :objectId AND a.tableId = :tableId")
	int findByObjectIdAndTableId(long objectId, long tableId);

		

	@Query("SELECT a.orderNo FROM CfgTableObjectifRelModel a WHERE a.objectId = :objectId AND a.tableId = :tableId")
	long getTableOrder(long objectId, long tableId);

	

	@Query("SELECT a.objectId FROM CfgTableObjectifRelModel a WHERE a.tableId = :tableId")
	long getObjectId(long tableId);

}
