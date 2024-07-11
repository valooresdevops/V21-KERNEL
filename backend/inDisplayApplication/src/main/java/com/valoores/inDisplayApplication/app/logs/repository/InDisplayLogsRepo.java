package com.valoores.inDisplayApplication.app.logs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.valoores.inDisplayApplication.app.logs.model.InDisplayLogsModel;


public interface InDisplayLogsRepo extends JpaRepository <InDisplayLogsModel, Long>{

}
