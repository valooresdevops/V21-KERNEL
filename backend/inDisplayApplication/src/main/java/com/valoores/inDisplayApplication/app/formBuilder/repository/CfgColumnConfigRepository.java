package com.valoores.inDisplayApplication.app.formBuilder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.valoores.inDisplayApplication.app.formBuilder.model.CfgColumnConfigModel;

public interface CfgColumnConfigRepository extends JpaRepository<CfgColumnConfigModel , Long>{

	@Query("select A from CfgColumnConfigModel A WHERE A.tableId = :tableId ")
	List<CfgColumnConfigModel> getAllColumns(long tableId); 
	
	@Query("select A.columnName from CfgColumnConfigModel A WHERE A.columnId = :columnId")
	String findColNameByColumnId(long columnId);
	
	@Query("select A.query from CfgColumnConfigModel A WHERE A.columnId = :columnId")
	long getQbeId(long columnId);
	
	@Transactional
	@Modifying
	@Query("delete from CfgColumnConfigModel a where a.tableId in ( SELECT tableId FROM CfgTableObjectifRelModel WHERE objectId = :objectId )")
	void deleteByColumnId(long objectId);
	
	
	@Transactional
	@Modifying
	@Query("delete from CfgColumnConfigModel a where a.tableId = :tableId and columnId = :columnId ")
	void deleteByColumnId(long tableId,long columnId);
	
	@Transactional
	@Modifying
	@Query("delete from CfgColumnConfigModel a where a.tableId = :tableId  ")
	void deleteColumnsByTableId(long tableId);
	
	
	//rony changes 
	CfgColumnConfigModel findByColumnId(long id);
	
	@Transactional
	@Modifying
	@Query("UPDATE CfgColumnConfigModel A set A.columnName = :buttonName, A.groupId = :fieldset, A.orderNo = :order, A.blobFile = :byteArray,A.updatedBy = :updatedBy where A.columnId = :buttonId")
	void updateFormButton(long buttonId,String buttonName, long order, long fieldset,byte[] byteArray,long updatedBy);

	List<CfgColumnConfigModel> findByGroupId(long fieldSetId);
	
	@Transactional
	@Modifying
	@Query("UPDATE CfgColumnConfigModel A set A.orderNo = :orderNo where A.columnId = :columnId")
	void updateFieldOrder(long columnId, long orderNo);
}


