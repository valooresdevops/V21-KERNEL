package com.example.demo.app.queryBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.app.queryBuilder.model.UsmUserModel;

@Repository
public interface UsmUserRepo  extends JpaRepository <UsmUserModel, Long>{

	@Query("select userLogin from UsmUserModel where userId = :userId")
	String getUserName(long userId);
}
