package com.valoores.inDisplayApplication.app.formBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.valoores.inDisplayApplication.app.formBuilder.model.WfmConditionActivityCondDModel;

public interface WfmConditionActivityCondDRepo  extends JpaRepository <WfmConditionActivityCondDModel, Long>{
	
	@Modifying
	@Query("DELETE FROM WfmConditionActivityCondDModel WHERE conditionId = :conditionId")
	void deleteWfmConditionActivityCond(long conditionId);
	
}