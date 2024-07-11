package com.valoores.v21.usm.app.common.mdmbsnunitgroup.service.impl;

import java.sql.Connection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.valoores.v21.usm.app.common.mdmbsnunitgroup.model.MdmBsnUnitGroup;
import com.valoores.v21.usm.app.common.mdmbsnunitgroup.repository.IMdmBsnUnitGroupRepository;
import com.valoores.v21.usm.app.common.mdmbsnunitgroup.service.IMdmBsnUnitGroupService;

@Service
public class MdmBsnUnitGroupServiceImpl implements IMdmBsnUnitGroupService {

	@Autowired private 
	javax.sql.DataSource dataSource;

	@Autowired
	private IMdmBsnUnitGroupRepository mdmBsnUnitGroupRepository;
	
	@Override
	public List<MdmBsnUnitGroup> getMdmBsnUnitGroup() {
		Connection conn = DataSourceUtils.getConnection(dataSource);
	    try {
		return mdmBsnUnitGroupRepository.findAll();}
		finally {
	        DataSourceUtils.releaseConnection(conn, dataSource);
		}
	}
}
