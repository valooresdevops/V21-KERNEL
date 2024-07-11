package com.example.demo.app.queryBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.app.queryBuilder.model.SessionModel;

@Repository
public interface SessionRepo extends JpaRepository <SessionModel, Long>{
	
	@Query("SELECT COUNT(1) FROM SessionModel a where a.SESSION_ATTRIBUTE = :sessionAttribute")
	int sessionExists(String sessionAttribute);
	
	@Query("SELECT a.SESSION_VALUE FROM SessionModel a where a.SESSION_ATTRIBUTE = :sessionAttribute")
	byte[] getAttributeValue (String sessionAttribute);
		
	@Query("SELECT a.UPDATABLE FROM SessionModel a where a.SESSION_ATTRIBUTE = :sessionAttribute")
	int sessionUpdatable (String sessionAttribute);
	
	@Transactional
	@Modifying
	@Query("UPDATE SessionModel SET SESSION_VALUE=:sessionValue WHERE SESSION_ATTRIBUTE= :sessionAttribute")
	void update (String sessionAttribute,byte[] sessionValue);
	
	@Transactional
	@Modifying
	@Query("delete from SessionModel a where a.SESSION_ATTRIBUTE = :sessionAttribute ")
	void deleteSession(String sessionAttribute);
}