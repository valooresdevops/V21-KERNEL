package com.valoores.inDisplayApplication.app.formBuilder.repository.DynamicRuleBuilder;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.valoores.inDisplayApplication.app.formBuilder.model.DynamicRuleBuilder.RuleBuilderComboModel;

public interface RuleBuilderComboRep extends JpaRepository<RuleBuilderComboModel , Long>{ 

	@Query("select A from RuleBuilderComboModel A WHERE A.objectId = :objectId and A.columnId = :columnId and A.isExcluded = 1 ORDER BY orderNo ASC")
	List<RuleBuilderComboModel> getDBRCombo(long objectId, long columnId);
}




