package com.example.demo.app.reportbuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.app.reportbuilder.model.ReportsModel;

public interface BirtRepository extends JpaRepository<ReportsModel, Long>
{
	ReportsModel findById(long id);
	
	
	@Modifying
	@Query("UPDATE ReportsModel a SET a.REPORT_NAME= :name, a.REPORT_DESC= :desc, a.MEDIA_FILE= :file where a.id = :id ")
	void updateReport(long id,String name,String desc,byte[] file);
	
	
	@Modifying
	@Query("delete from ReportsModel a where a.id = :id ")
	void deleteReport(long id);

	@Query("SELECT a.MEDIA_FILE from ReportsModel a where a.id = :id ")
	byte[] getMediaFile(long id);
	
}
