package com.valoores.inDisplayApplication.app.formBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.valoores.inDisplayApplication.app.formBuilder.model.CfgTableConfigModel;

public interface CfgTableConfigRepository extends JpaRepository<CfgTableConfigModel, Long> {
	
	@Transactional
	@Modifying
	@Query("delete from CfgTableConfigModel a where a.tableId in (SELECT tableId FROM CfgTableObjectifRelModel WHERE objectId = :objectId)")
	void deleteByTableId(long objectId);

	@Transactional
	@Modifying
	@Query("delete from CfgTableConfigModel a where a.tableId = :tableId")
	void deleteTableByTableId(long tableId);

	@Query("select concat(t.tableOwner, '.', t.tableName) from CfgTableConfigModel t where t.tableId = :tableId")
	String findTableNameById(long tableId);

	@Query("SELECT t.tableId FROM CfgTableConfigModel t WHERE t.tableName= :tableName AND t.tableOwner= :tableOwner")
	long fetchTableId(String tableName,String tableOwner);

	CfgTableConfigModel findByTableId(long id);

}
