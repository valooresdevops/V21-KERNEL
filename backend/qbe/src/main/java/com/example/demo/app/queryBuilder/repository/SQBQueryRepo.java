package com.example.demo.app.queryBuilder.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.example.demo.app.queryBuilder.model.*;

@Repository
public interface SQBQueryRepo extends JpaRepository <SQBQuery, Long>{
	
	
    SQBQuery findByQueryId(long id);
//	@Query("SELECT a.QBE_ID,a.USR_CODE,a.QUERY_NAME,a.DATA_STORE_ID,a.VERSION_NO,a.import_flag,a.COMMENTS,a.CREATION_DATE,a.CREATED_BY,a.RELEASE_NO,a.IS_BIG_QUERY,a.IS_FOR_ADVANCED_SEARCH,a.IS_SYSTEM_QUERY FROM SQBQuery a WHERE a.QBE_ID=:queryId")
//	SQBQuery findById(long queryId);
	
	@Modifying
	@Query("delete from SQBQuery a where a.queryId = :id ")
	void deleteAllByQueryId(long id);

	@Query("SELECT a.QUERY_NAME FROM SQBQuery a WHERE a.queryId=:queryId")
	String fetchQueryName(long queryId);
	


}
