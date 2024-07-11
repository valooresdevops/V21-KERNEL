package com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.bugmapping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.valoores.v21.usm.app.parameters.insystemparameters.ldapconfiguration.bugmapping.model.BugMapping;

public interface IBugMappingRepository extends JpaRepository<BugMapping, Long> {

}
