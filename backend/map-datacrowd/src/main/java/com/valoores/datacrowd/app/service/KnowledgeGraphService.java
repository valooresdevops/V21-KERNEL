package com.valoores.datacrowd.app.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.valoores.datacrowd.app.model.loc_report_config;

@Service
public interface KnowledgeGraphService {
	
	public List<loc_report_config> callCybLoadFile(Integer queryId, String graph);

	 public byte[] getGraphRecords(String queryId) throws IOException;

//	 byte[] run(String args);

//	Connection getConnection() throws SQLException;
  
}
