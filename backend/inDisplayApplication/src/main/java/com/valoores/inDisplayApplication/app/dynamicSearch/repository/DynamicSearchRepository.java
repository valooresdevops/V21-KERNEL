package com.valoores.inDisplayApplication.app.dynamicSearch.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.valoores.inDisplayApplication.app.dynamicSearch.model.DynamicSearchModel;

public interface DynamicSearchRepository extends JpaRepository <DynamicSearchModel, Long>{
	@Query("SELECT a.XML_DATA from SQBQueryDetails a where a.QBE_ID = :id ")
	byte[] getQueryBlob(long id);

}
