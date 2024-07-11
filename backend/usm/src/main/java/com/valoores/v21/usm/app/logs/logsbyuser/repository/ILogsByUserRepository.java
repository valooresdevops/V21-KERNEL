package com.valoores.v21.usm.app.logs.logsbyuser.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.valoores.v21.usm.app.common.application.model.USMApplication;
import com.valoores.v21.usm.app.common.logs.model.UsmUserMiscInfo;
@Repository
	public interface ILogsByUserRepository extends PagingAndSortingRepository<UsmUserMiscInfo, Long> {

//	@Query("select r from LogsUser r ")
//	List<UserLogs> getUserCombo();
	
	@Query("select a from USMApplication a")
	List<USMApplication> getApplicationCombo();
	

}
