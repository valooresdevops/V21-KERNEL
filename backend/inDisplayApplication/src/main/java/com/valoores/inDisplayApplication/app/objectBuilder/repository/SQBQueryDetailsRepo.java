package com.valoores.inDisplayApplication.app.objectBuilder.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.valoores.inDisplayApplication.app.objectBuilder.model.SQBQueryDetails;

@Repository
public interface SQBQueryDetailsRepo extends JpaRepository <SQBQueryDetails, Long>{
	
	@Modifying
	@Query("delete from SQBQueryDetails a where a.QBE_ID = :id ")
	void deleteAllByQueryId(long id);


	@Query("SELECT a.XML_DATA from SQBQueryDetails a where a.QBE_ID = :id ")
	byte[] getQueryBlob(long id);
	
	
	@Transactional
	@Modifying
	@Query("UPDATE SQBQueryDetails SET XML_DATA=:data WHERE QBE_ID=:queryId")
	void updateQueryXml (long queryId,byte[] data);

}
