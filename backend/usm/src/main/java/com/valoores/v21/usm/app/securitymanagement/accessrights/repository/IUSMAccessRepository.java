package com.valoores.v21.usm.app.securitymanagement.accessrights.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.valoores.v21.usm.app.common.menucombo.model.USMMenu;

public interface IUSMAccessRepository extends PagingAndSortingRepository<USMMenu, String> {
//    USMAccessRights findById(long id);
//    List<USMAccessRights> findAllById(long id,String menuvar);
}
