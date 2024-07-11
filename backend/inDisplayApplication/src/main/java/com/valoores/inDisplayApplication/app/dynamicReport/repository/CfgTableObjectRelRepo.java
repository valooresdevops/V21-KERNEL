package com.valoores.inDisplayApplication.app.dynamicReport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.valoores.inDisplayApplication.app.dynamicSearch.model.TableObjectModel;

public interface CfgTableObjectRelRepo  extends JpaRepository<TableObjectModel, Long>{

	
	@Query("SELECT tableId FROM TableObjectModel WHERE objectId= :objectId AND orderNo = :orderNo")
	long fetchTableId(long objectId,long orderNo);
}
