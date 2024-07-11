package com.example.demo.app.queryBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.app.queryBuilder.model.QbeAuthorizedUserModel;

@Repository
public interface qbeAuthorizedUserRepo extends JpaRepository <QbeAuthorizedUserModel, com.example.demo.app.queryBuilder.model.QbeAuthorizedUserIdModel> {

	@Transactional
	@Modifying
	@Query("DELETE from QbeAuthorizedUserModel WHERE qbeId= :qbeId and usrCode= :usrCode")
	void deleteUsers(long qbeId,long usrCode);
	
	@Transactional
	@Modifying
	@Query("DELETE from QbeAuthorizedUserModel WHERE qbeId = :qbeId")
	void deleteQbeUsers(long qbeId);
	
	@Query("SELECT count(1) from QbeAuthorizedUserModel where qbeId = :qbeId")
	int getCountQbeUsers (long qbeId);
}
