package com.valoores.inDisplayApplication.app.formBuilder.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.valoores.inDisplayApplication.app.formBuilder.model.WfmConditionVariableModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.WfmConditionVariablesPrimModel;

public interface WfmConditionVariableRepo  extends JpaRepository <WfmConditionVariableModel, WfmConditionVariablesPrimModel>{
			
	@Modifying
	@Query("DELETE FROM WfmConditionVariableModel WHERE conditionId = :conditionId")
	 void deleteWfmConditionVariables(long conditionId);
}

