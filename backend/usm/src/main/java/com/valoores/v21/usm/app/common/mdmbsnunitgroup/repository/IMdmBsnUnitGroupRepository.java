package com.valoores.v21.usm.app.common.mdmbsnunitgroup.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.valoores.v21.usm.app.common.mdmbsnunitgroup.model.MdmBsnUnitGroup;

public interface IMdmBsnUnitGroupRepository extends JpaRepository  <MdmBsnUnitGroup, Long> {
	

	@Query("select a from MdmBsnUnitGroup a")
	 List<MdmBsnUnitGroup> getMdmBsnUnitGroup();

}
