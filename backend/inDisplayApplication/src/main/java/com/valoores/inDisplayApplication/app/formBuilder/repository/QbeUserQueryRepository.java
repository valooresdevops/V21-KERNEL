package com.valoores.inDisplayApplication.app.formBuilder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.valoores.inDisplayApplication.app.formBuilder.model.QbeUserQueryModel;

public interface QbeUserQueryRepository extends JpaRepository<QbeUserQueryModel, Long> {
	@Query("SELECT D FROM QbeUserQueryModel D where D.createdBy <> -1 order by creationDate DESC")
	List<QbeUserQueryModel> getSourceQuery();
	
	@Query("SELECT D.name FROM QbeUserQueryModel D where D.id = :qbeId ")
	String getQbeSpecName(long qbeId);
}
