package com.valoores.inDisplayApplication.app.formBuilder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.valoores.inDisplayApplication.app.formBuilder.model.CfgFieldsetObjectModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.CfgFieldsetObjectModelId;

@Repository
public interface CfgFieldsetRepository extends JpaRepository<CfgFieldsetObjectModel, CfgFieldsetObjectModelId>{

	
	@Transactional
	@Modifying
	@Query("delete from CfgFieldsetObjectModel a where a.objectId = :objectId")
	void deleteByObjectId(long objectId);

	
	
	@Transactional
	@Modifying
	@Query("delete from CfgFieldsetObjectModel a where a.fieldSetId = :fieldSetId")
	void deleteByFieldSetId(long fieldSetId);

	@Query("select a.fieldSetId from CfgFieldsetObjectModel a where a.objectId = :objectId")
	List<Long> getFieldSetIds(long objectId);
}
