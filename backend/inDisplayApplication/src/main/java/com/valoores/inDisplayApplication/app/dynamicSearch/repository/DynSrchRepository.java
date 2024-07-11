package com.valoores.inDisplayApplication.app.dynamicSearch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.valoores.inDisplayApplication.app.dynamicSearch.model.DynamicSearch;

public interface DynSrchRepository extends JpaRepository <DynamicSearch, Long> {

	@Query("SELECT u from DynamicSearch u where u.objectId = :objectId and colName = :colName")
	DynamicSearch findByObjectIdAndColName(long objectId, String colName);

}
