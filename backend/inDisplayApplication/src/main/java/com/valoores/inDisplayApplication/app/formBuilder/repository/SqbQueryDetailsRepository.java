package com.valoores.inDisplayApplication.app.formBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.valoores.inDisplayApplication.app.formBuilder.model.SQBQueryDetailsModel;

public interface SqbQueryDetailsRepository extends JpaRepository <SQBQueryDetailsModel, Long>{
	
	@Query("SELECT a.XML_DATA from SQBQueryDetailsModel a where a.QBE_ID = :id ")
	byte[] getQueryBlob(long id);
}
// elie jamil 