package com.valoores.inDisplayApplication.app.objectBuilder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.inDisplayApplication.app.objectBuilder.model.AppGrid;
@Repository
public interface AppGridRepository extends JpaRepository<AppGrid , Long> {

	@Transactional
	@Modifying
	@Query("delete from AppGrid a where a.gridId = :id")
	void deleteById(long id);
	
	@Transactional
	@Modifying
	@Query("update AppGrid u set u.gridName = :gridName ,u.query = :query  ,u.objectKpiId  = :objectKpiId  where u.gridId = :gridId")
	void updateGrid(long gridId, Integer query,long objectKpiId, String gridName);
	
	@Query("SELECT u.query as queryId FROM AppGrid u WHERE u.gridId= :gridId")
	Integer selectQuery(long gridId);

	
	
	@Query("SELECT a.gridId FROM AppGrid a WHERE a.objectKpiId = :kpiId")
	long  selectGridRelatedKpi(long kpiId);
	
	
	@Query("SELECT a.gridId FROM AppGrid a WHERE a.objectKpiId = :kpiId")
	List<ObjectNode> selectGridblabal(long kpiId);
	@Transactional
	@Modifying
	@Query("update AppGrid u set u.objectKpiId  = :objectKpiId  where u.gridId = :gridId")
	void updateDropdownGrid(long gridId, long objectKpiId);

	@Transactional
	@Modifying
	@Query("update AppGrid set objectKpiId = null where object_kpi_id= :kpiId")
	void removeDropdownGrid(long kpiId);
	
}
