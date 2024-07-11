package com.valoores.v21.usm.app.common.logs.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.valoores.v21.usm.app.common.logs.model.UsmUserMiscInfo;
@Repository
	public interface IUserRepository extends PagingAndSortingRepository<UsmUserMiscInfo, Long> {

//	@Query("select r.id as id, r.name as name from UserLogs r ")
//	List<UserLogs> getUserCombo();
	

	

}
