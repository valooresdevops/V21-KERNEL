package com.valoores.datacrowd.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.valoores.datacrowd.app.model.TECH_REPORT_PARAMS;


@Repository
public interface TechReortParamsRepository  extends JpaRepository<TECH_REPORT_PARAMS, Long> {
	
	
	@Query( "SELECT S.code, S.attribute_name ,S.int_code  FROM TECH_REPORT_PARAMS S")
	public List<Object> getShapelimit();

	

}
