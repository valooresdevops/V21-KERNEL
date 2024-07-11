package com.example.demo.app.queryBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.app.queryBuilder.model.CfgObjectDefModel;

@Repository
public interface CfgObjectDefRepo extends JpaRepository <CfgObjectDefModel, Long>{
	
	@Query("SELECT COUNT(1) FROM CfgObjectDefModel a where a.canAdd = :id OR a.canModify= :id OR a.canDelete= :id OR a.condition = :id OR a.isSave = :id OR a.sourceQuery = :id")
	int qbeExists(long id);
	

}