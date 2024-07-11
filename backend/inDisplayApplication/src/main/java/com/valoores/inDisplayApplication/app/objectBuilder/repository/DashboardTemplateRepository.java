package com.valoores.inDisplayApplication.app.objectBuilder.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.valoores.inDisplayApplication.app.objectBuilder.model.DashboardTemplates;

@Repository
public interface DashboardTemplateRepository extends JpaRepository<DashboardTemplates , Long> {
	@Transactional
	@Modifying
	@Query("DELETE FROM DashboardTemplates a where a.templateID = :id ")
	void deleteById(long id);
	
	
	@Transactional
	@Modifying
	@Query("UPDATE DashboardTemplates u SET u.templateName = :templateName WHERE u.templateID = :templateID")
	void updateDashboardTemplate(long templateID,String templateName);
	
	@Transactional
	@Modifying
	@Query("UPDATE DashboardTemplates u SET u.PREF_DATA = :PREF_DATA WHERE u.templateID = :templateID")
	void updateDashboardTemplatePrefData(long templateID,String PREF_DATA);
	
	@Query("SELECT a.PREF_DATA FROM DashboardTemplates a WHERE a.templateID = :templateId")
	String getPrefData(Long templateId);
			
}
