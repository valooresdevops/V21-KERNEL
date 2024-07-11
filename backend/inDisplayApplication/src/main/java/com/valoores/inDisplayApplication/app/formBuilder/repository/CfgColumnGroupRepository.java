package com.valoores.inDisplayApplication.app.formBuilder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.valoores.inDisplayApplication.app.formBuilder.model.CfgColumnGroupModel;

public interface CfgColumnGroupRepository extends JpaRepository<CfgColumnGroupModel, Long>{
	CfgColumnGroupModel findById(long id);

//	@Query("SELECT A.id as id, A.name as name FROM CfgColumnGroupModel A , CfgFieldsetObjectModel O where A.id = O.fieldSetId and O.objectId = :objectId ")
//	List<CfgColumnGroupModel> getAllFieldSetsCombo(long objectId);
	
	@Query("SELECT A FROM CfgColumnGroupModel A, CfgFieldsetObjectModel O where A.id = O.fieldSetId and O.objectId = :objectId order by A.orderNb ASC ")
	List<CfgColumnGroupModel> getAllFieldSetsCombo(long objectId);
	
	@Query("SELECT A FROM CfgColumnGroupModel A, CfgFieldsetObjectModel O where A.id = O.fieldSetId and O.objectId = :objectId and O.fieldSetId = :fieldsetId ")
	List<CfgColumnGroupModel> getFieldSetData(long objectId, long fieldsetId);
	
	@Transactional
	@Modifying
	@Query("update CfgColumnGroupModel u set u.name = :fieldsetName, u.orderNb = :orderNo where u.id = :fieldsetId")
	void updateFieldset(long fieldsetId, String fieldsetName, long orderNo);

	
	@Transactional
	@Modifying
	@Query("delete from CfgColumnGroupModel a where a.id in :fieldSetIds")
	void deleteByFieldSetId(long fieldSetIds);
	
}
