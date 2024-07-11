package com.valoores.inDisplayApplication.app.ScreenBuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.valoores.inDisplayApplication.app.ScreenBuilder.model.ApplicationListModel;

@Repository
public interface ApplicationRepo  extends JpaRepository <ApplicationListModel, Long>{


	@Query("SELECT a.APP_SNAME FROM ApplicationListModel a WHERE a.APP_CODE= :code ")
	String getSName(long code);

	
}
