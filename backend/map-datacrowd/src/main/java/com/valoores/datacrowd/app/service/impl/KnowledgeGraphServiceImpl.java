package com.valoores.datacrowd.app.service.impl;

import java.io.IOException;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import com.valoores.datacrowd.app.model.loc_report_config;
import com.valoores.datacrowd.app.repository.KnowledgeGraphRepository;
import com.valoores.datacrowd.app.service.KnowledgeGraphService;


@Service
public class KnowledgeGraphServiceImpl implements KnowledgeGraphService{

	@Autowired
	private  KnowledgeGraphRepository knowledgeGraphRepository;
	
	@Autowired
	private  KnowledgeGraphService knowledgegraphservice;
	
//	@Autowired 
//	private EntityManager entitymanager;

	    @PersistenceContext
	    private EntityManager entityManager;
	    
	    @Autowired
	    private JdbcTemplate jdbcTemplate;

	@Override
	public List<loc_report_config> callCybLoadFile(Integer queryId, String graph) {
		return knowledgeGraphRepository.callCybLoadFile(queryId, graph);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public byte[]  getGraphRecords(String queryId) throws IOException  {
		try {
		
        String tableName = " SSDX_TMP.TMP_CYB_GRAPH_RECORDS_" + queryId;
        String sql = "SELECT json FROM " + tableName;
        return jdbcTemplate.queryForObject(sql, new RowMapper<String>() {
	     	
        	@Override
        	public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        	    Clob clob = rs.getClob("JSON");
        	    System.out.println("JSON clob >> " + clob);
        	    if (clob != null) {
        	        String json = clob.getSubString(1, (int) clob.length());
        	        if (json.length() == 0) {
        	            return null;
        	        } else {
        	            return json;
        	        }
        	    } else {
        	        return "noData";
        	    }
        	}
        	      	
        }).getBytes();
        
           
		}catch (Exception e) {
			e.printStackTrace();
		}        
        return null   ;
    }
	
}
