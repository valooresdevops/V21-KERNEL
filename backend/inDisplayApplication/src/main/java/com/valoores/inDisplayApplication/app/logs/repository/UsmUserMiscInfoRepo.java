package com.valoores.inDisplayApplication.app.logs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.valoores.inDisplayApplication.app.logs.model.UsmUserMiscInfoModel;

@Repository
public interface UsmUserMiscInfoRepo extends JpaRepository <UsmUserMiscInfoModel, Long>{
	

		@Query("select userLogin from UsmUserMiscInfoModel where userId = :userId")
		String getUserName(long userId);
	
}
