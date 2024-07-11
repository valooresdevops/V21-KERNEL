package com.valoores.inDisplayApplication.app.formBuilder.repository.DynamicRuleBuilder;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.valoores.inDisplayApplication.app.formBuilder.model.DynamicRuleBuilder.TechDynamicRuleBuilder;

public interface TechDynamicRuleBuilderRepo extends JpaRepository<TechDynamicRuleBuilder , Long>{
	
	@Query("select A from TechDynamicRuleBuilder A WHERE A.objectId = :objectId and A.columnId = :columnId ORDER BY orderNo ASC")
	List<TechDynamicRuleBuilder> getDBRGrid(long objectId, long columnId);
	
	@Query("select A from TechDynamicRuleBuilder A WHERE A.objectId = :objectId and A.ruleAction = :ruleAction and A.columnId = :columnId ORDER BY orderNo ASC")
	List<TechDynamicRuleBuilder> getDBRGridByRuleActionAndObjectId(long objectId, long ruleAction, long columnId);
	
	@Query("select A from TechDynamicRuleBuilder A WHERE A.ruleId = :ruleId")
	List<TechDynamicRuleBuilder> getDBRInfo(long ruleId);
	
	TechDynamicRuleBuilder findByRuleId(long id);

	@Transactional
	@Modifying
	@Query("DELETE from TechDynamicRuleBuilder WHERE ruleId = :ruleId")
	void deletedById(long ruleId);
	
	@Query("select A from TechDynamicRuleBuilder A WHERE A.objectId = :objectId and A.isExcluded = 1 and A.ruleAction = :ruleAction and A.ruleId = :ruleId ORDER BY orderNo ASC")
	List<TechDynamicRuleBuilder> getDBRGridByRuleActionAndRuleId(long objectId, long ruleAction, long ruleId);
	
}


