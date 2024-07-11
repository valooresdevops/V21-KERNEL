package com.valoores.cassandradatacrowd.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Service;

import com.valoores.cassandradatacrowd.app.dto.CustomResponse;
import com.valoores.cassandradatacrowd.app.model.Location;
import com.valoores.cassandradatacrowd.app.repository.IAuditTrailRepository;
import com.valoores.cassandradatacrowd.app.service.IAuditTrailService;

@Service
public class AuditTrailServiceImpl implements IAuditTrailService {

  @SuppressWarnings("unused")
  @Autowired
  private IAuditTrailRepository auditTrail;

  @Autowired
  private CassandraTemplate template;
  
  @Override
  public CustomResponse addLoggedUser(String insertQuery) {

      CustomResponse resp = CustomResponse.builder().build();

    try {
    	System.out.println(" insertQuery>>>>>>>>>>> "+insertQuery);
    	template.select(insertQuery, Location.class);

    } catch (Exception e) {
      resp.setStatus("failed");
      e.printStackTrace();
    }
    return resp;

  }
}