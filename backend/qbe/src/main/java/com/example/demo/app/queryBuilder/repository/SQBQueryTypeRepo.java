package com.example.demo.app.queryBuilder.repository;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.app.queryBuilder.model.SQBQueryType;
import com.example.demo.app.queryBuilder.model.queryTypeIdModel;

@Repository
public interface SQBQueryTypeRepo extends JpaRepository <SQBQueryType, queryTypeIdModel>{
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO SQBQueryType (QBE_ID, QUERY_TYPE_CODE, CREATION_DATE) VALUES (:queryID, :queryType, :creationDate)", nativeQuery = true)
	void insertQueryType(Long queryID,Long queryType,Date creationDate);

	
	@Query("SELECT count(*) FROM SQBQueryType WHERE QBE_ID= :queryId")
	int checkIfExists(Long queryId);
	
	@Modifying
	@Query("delete from SQBQueryType a where a.QBE_ID = :id ")
	void deleteQueryTypes(long id);
}
