package com.valoores.v21.usm.app.logs.invalidlogin.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.valoores.v21.usm.app.logs.invalidlogin.model.InvalidLogs;
@Repository
	public interface InvalidLoginRepository extends PagingAndSortingRepository<InvalidLogs,Long> {

	
	

}
