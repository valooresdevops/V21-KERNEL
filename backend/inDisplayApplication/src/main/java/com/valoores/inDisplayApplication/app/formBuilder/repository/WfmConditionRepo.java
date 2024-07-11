package com.valoores.inDisplayApplication.app.formBuilder.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.valoores.inDisplayApplication.app.formBuilder.model.WfmConditionModel;

public interface WfmConditionRepo  extends JpaRepository <WfmConditionModel, Long>{
	
	@Query("SELECT qbeId FROM WfmConditionModel WHERE conditionId = :conditionId")
	long fetchQbeIdCond(long conditionId);
	
	@Modifying
	@Query("UPDATE WfmConditionModel SET qbeId = :qbeId, updateDate = :date, updatedBy = :userId WHERE conditionId= :conditionId")
	 void updateWfmCondition(long qbeId,Date date,long userId,long conditionId);
	
	@Modifying
	@Query("DELETE FROM WfmConditionModel WHERE conditionId= :conditionId")
	 void deleteWfmCondition(long conditionId);
	
}
