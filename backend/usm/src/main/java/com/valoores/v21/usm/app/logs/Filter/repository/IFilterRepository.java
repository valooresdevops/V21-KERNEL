package com.valoores.v21.usm.app.logs.Filter.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.valoores.v21.usm.app.common.logs.model.LogUser;

public interface IFilterRepository extends PagingAndSortingRepository<LogUser, Long>{

}
