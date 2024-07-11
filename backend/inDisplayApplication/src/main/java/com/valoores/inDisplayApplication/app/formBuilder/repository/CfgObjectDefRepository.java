package com.valoores.inDisplayApplication.app.formBuilder.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.valoores.inDisplayApplication.app.formBuilder.model.CfgObjectDefModel;

@Repository
public interface CfgObjectDefRepository  extends JpaRepository<CfgObjectDefModel, Long>  {
	
	CfgObjectDefModel findByObjectId(long id);
	
	@Query("SELECT D FROM CfgObjectDefModel D WHERE D.objectType = 16 and D.isMain = '1' order by D.objectId desc ")
	List<CfgObjectDefModel> getAllMenus();
	
	@Query("SELECT D FROM CfgObjectDefModel D WHERE D.objectId = :objectId")
	CfgObjectDefModel findByMenuId(long objectId);
	
	@Query("SELECT D FROM CfgObjectDefModel D WHERE D.parentId = :objectId or D.objectId = :objectId")
	List<CfgObjectDefModel> findByObjectPId(long objectId);
	
//	@Query("SELECT D FROM CfgObjectDefModel D , CfgTableObjectifRelModel R WHERE D.objectId = R.objectId and"
//			+ "(D.objectId = :objectId OR D.parentId = :objectId and D.isMain = '0') AND R.tableId <> -1"
//			+ "order by R.orderNo ASC")
//	List<CfgObjectDefModel> getAllTabs(long objectId);
	
	@Query("SELECT D FROM CfgObjectDefModel D WHERE (D.objectId = :objectId OR D.parentId = :objectId and D.isMain = '0')"
			+ " order by D.orderNo ASC")
	List<CfgObjectDefModel> getAllTabs(long objectId);
	
	@Transactional
	@Modifying
	@Query("delete from CfgObjectDefModel a where a.objectId = :objectId or a.parentId = :objectId")
	void deleteByObjectId(long objectId);

	
	@Query("select a.objectId from CfgObjectDefModel a where a.objectId = :objectId or a.parentId = : objectId order by a.objectId DESC")
	List<Long> getObjectIds(long objectId);
	
	
//	@Modifying
//	@Query("update CfgObjectDefModel o.objectType = 16  , "
//			+ " o.menuName = : formBuilderDto.get(0).getMenuName()")
//	void updateTabConf(long objectId, List<FormBuilderDto> formBuilderDto);
	
@Query("select a.menuName from CfgObjectDefModel a where a.objectId = :objectId")
String getObjectName(long objectId);
	
}
 