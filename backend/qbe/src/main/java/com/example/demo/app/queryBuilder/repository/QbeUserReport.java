package com.example.demo.app.queryBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.app.queryBuilder.model.QbeUserModel;

public interface QbeUserReport extends JpaRepository <QbeUserModel, Long>
	{
		@Query("SELECT a.usrCode FROM QbeUserModel a WHERE a.qbeId = :qbeId")
		long getQbeQueryCreatedBy(long qbeId);
	}


