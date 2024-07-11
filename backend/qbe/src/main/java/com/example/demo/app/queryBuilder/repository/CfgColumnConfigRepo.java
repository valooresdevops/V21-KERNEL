package com.example.demo.app.queryBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.app.queryBuilder.model.CfgColumnConfigModel;

@Repository
public interface CfgColumnConfigRepo extends JpaRepository <CfgColumnConfigModel, Long>{
	
	@Query("SELECT COUNT(1) FROM CfgColumnConfigModel a where a.query = :id OR a.qbeReadOnly = :id OR a.defaultValue = :id OR a.dependencyDefaultValue = :id")
	int qbeExitsts(long id);
	

}