
package com.valoores.cassandradatacrowd.common.service;
  
import org.json.JSONObject;
import org.springframework.stereotype.Service;
  
  
 @Service
 public interface constructQueryService {
  
 String constructQuery(JSONObject jsonArray,String type);
  
  
  }
 