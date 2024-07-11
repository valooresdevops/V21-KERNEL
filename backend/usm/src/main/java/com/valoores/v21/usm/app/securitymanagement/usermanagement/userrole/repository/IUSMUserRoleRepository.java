package com.valoores.v21.usm.app.securitymanagement.usermanagement.userrole.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.valoores.v21.usm.app.securitymanagement.usermanagement.userrole.dto.USMUserRoleIds;
import com.valoores.v21.usm.app.securitymanagement.usermanagement.userrole.model.USMUserRole;

@Repository
public interface IUSMUserRoleRepository extends PagingAndSortingRepository<USMUserRole, USMUserRoleIds> {

	List<USMUserRole> findAllByEmpId(long empId);
	
}
  