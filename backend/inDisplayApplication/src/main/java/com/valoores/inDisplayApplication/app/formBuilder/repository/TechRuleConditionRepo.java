package com.valoores.inDisplayApplication.app.formBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.valoores.inDisplayApplication.app.formBuilder.model.TechRuleCondition;

public interface TechRuleConditionRepo extends JpaRepository <TechRuleCondition, Long>{


	@Query("SELECT conditionId FROM TechRuleCondition WHERE ruleId = :ruleCode")
	long fetchConditionId(long ruleCode);
	
	@Query("SELECT count(1) FROM TechRuleCondition WHERE ruleId = :ruleCode")
	int checkIfExists(long ruleCode);
	
	@Modifying
	@Query("DELETE FROM TechRuleCondition WHERE ruleId = :ruleCode")
	void deleteFromTechRule(long ruleCode);
	
	@Modifying
	@Query("DELETE FROM TechRuleCondition WHERE conditionId = :conditionId")
	void deleteFromTechRuleByCond(long conditionId);
	
	
	@Query("SELECT mainId FROM TechRuleCondition WHERE ruleId = :ruleCode")
	long fetchRuleMainId(long ruleCode);
	
}
