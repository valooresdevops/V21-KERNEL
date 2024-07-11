package com.valoores.inDisplayApplication.app.formBuilder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.valoores.inDisplayApplication.app.formBuilder.model.AllColumnsModel;
import com.valoores.inDisplayApplication.app.formBuilder.model.AllColumnsModelId;

@Repository
public interface AllColumnsRepository extends JpaRepository<AllColumnsModel,AllColumnsModelId > {
	  
	List<AllColumnsModel> findAllByTableNameAndOwner(String tableName, String tableOwner);

	List<AllColumnsModel> findAllByTableNameAndOwnerAndColumnName(String tableName, String tableOwner, String columnName);
}
