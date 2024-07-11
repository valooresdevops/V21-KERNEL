package com.valoores.cassandradatacrowd.app.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.valoores.cassandradatacrowd.app.model.Location;

@Repository
public interface ILocationRepository extends CassandraRepository<Location, String>{
	


}
