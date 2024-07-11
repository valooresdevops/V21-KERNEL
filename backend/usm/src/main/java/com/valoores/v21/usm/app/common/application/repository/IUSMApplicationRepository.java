package com.valoores.v21.usm.app.common.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.valoores.v21.usm.app.common.application.model.USMApplication;

public interface IUSMApplicationRepository extends JpaRepository <USMApplication, Integer> {
	
	
	@Query("select a from USMApplication a")
	List<USMApplication> getApplicationCombo();
	
}
