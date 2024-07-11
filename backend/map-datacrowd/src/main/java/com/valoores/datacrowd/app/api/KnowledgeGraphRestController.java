package com.valoores.datacrowd.app.api;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.unboundid.util.json.JSONException;
import com.valoores.datacrowd.app.service.DataCrowdService;
import com.valoores.datacrowd.app.service.KnowledgeGraphService;
import com.valoores.datacrowd.backend.CustomResponse;
import com.valoores.datacrowd.common.service.ServiceProviderService;

@RestController
@RequestMapping("/api")
public class KnowledgeGraphRestController {
	
	@Value("${cassandra.ip}")
	private String cassandraIp;
	
	@Autowired
	private KnowledgeGraphService knowledgeGraphService;
	
	
	@Autowired
	private ServiceProviderService serviceproviderservice;
	
	
	@Autowired
	private DataCrowdService datacrowdservice;
	


	@SuppressWarnings("null")
	@PostMapping("/getGraphNodes")
	public byte[] getGraphNodes(@RequestBody String  json) throws JSONException {

		String   res="";
		String   layers;
		Integer   connection ;
		String   queryId;
		String   devices   = "";
		String   FromDate  = "";
		String   toDate    = "";
		Integer   linkType  ;
		String   callingNo = "";
		byte[]	 getGrapgResult = null ;
		
		CustomResponse resp = CustomResponse.builder().build();

		try {
			
		    JSONObject obj = new JSONObject(json);
		    
			devices        = obj.getString("devices");
			FromDate       = obj.getString("fromDatemillis");
			callingNo      = obj.getString("callingNo");
			toDate         = obj.getString("toDatemillis");
			linkType       = obj.getInt("linkType");
			connection 	   = obj.getInt("directIndirect");
			layers         = obj.getString("layer");
			 
			@SuppressWarnings("deprecation")
			Date fromDateparse = new Date(FromDate);
			long fromDatemillis = fromDateparse.getTime();
			String ToDate = toDate;
			@SuppressWarnings("deprecation")
			Date toDateparse = new Date(ToDate);
			long toDatemillis = toDateparse.getTime();
	  
		    resp    = datacrowdservice.inserJsonParam(json);//{"code":"0","id":119212,"status":"Success"}
	      	queryId = Integer.toString((int) resp.getId());
	      	
	      	obj.put("simulationId",queryId );
	      	obj.remove("fromDatemillis");
	      	obj.remove("toDatemillis");
	      	obj.put("fromDatemillis",String.valueOf(fromDatemillis));
	      	obj.put("toDatemillis"  ,String.valueOf(toDatemillis));

	      System.out.println(" fromDatemillis >>  "+fromDatemillis);
	      System.out.println(" fromDatemillis >>  "+toDatemillis);
	      System.out.println(" obj >>  "+obj);
	      	
	      System.out.println(" connection >>  "+connection);

	      if(connection == 1)
	      {
	    	   RestTemplate restTemplate = new RestTemplate();
			   //String uri = cassandraIp+"/api/getKnowledgeGraphOneRecord";
				String uri = "http://10.1.8.112:8888/api/getKnowledgeGraphOneRecord";


			   HttpHeaders headers = new HttpHeaders();
			   headers.setContentType(MediaType.APPLICATION_JSON);
			   HttpEntity<String> entity = new HttpEntity<>(obj.toString(), headers);
			   String url = uri;
			   String response = restTemplate.postForObject(url, entity, String.class);
         	   System.out.println("response  >> " + response);
			  
		              if(response.equals("Success")) 
		               {
		            	  serviceproviderservice.DHloadfile((int)resp.getId(), "graph");
		            	  System.out.println("ids >>> "+queryId);
		            	  
		            	  getGrapgResult = knowledgeGraphService.getGraphRecords(queryId);
		            	  System.out.println("gerGrapgResult  >> " + getGrapgResult);
		            
		    	          res = "Success";

		               }else {
			                 
		            	  res = "NoData";
		            	  getGrapgResult  = res.getBytes();

		    	       }
	    	  
	      }//
	      else 
	      { 
	    	  serviceproviderservice.loadGraphEngine(devices,fromDatemillis,toDatemillis,queryId,linkType,layers,callingNo, ToDate, ToDate);
        	  getGrapgResult = knowledgeGraphService.getGraphRecords(queryId);
  
	       }
		    
		}catch(Exception ex){
			
			ex.printStackTrace();
			res = "Fail";

		}
		
		return getGrapgResult;
		
	}
	
	
	
//	@GetMapping("/getGraphRecords/{queryId}")
//	public String getGraphRecords(@PathVariable String queryId) {
//	    return knowledgeGraphService.getGraphRecords(queryId);
//	}

}
