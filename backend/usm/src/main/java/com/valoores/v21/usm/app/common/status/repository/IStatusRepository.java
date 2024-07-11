package com.valoores.v21.usm.app.common.status.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.valoores.v21.usm.app.common.status.model.Status;


public interface IStatusRepository extends JpaRepository<Status, Long> {
	@Query("select a from Status a")
	List<Status> getStatus();
}
