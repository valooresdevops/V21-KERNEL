package com.example.demo.app.queryBuilder.api;

import java.util.List;



//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.demo.app.queryBuilder.service.QbeApplicationService;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//
//import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.app.queryBuilder.dto.ParamDto;
import com.example.demo.app.queryBuilder.dto.QbeAuthorizedUserDto;
import com.example.demo.app.queryBuilder.dto.QueryDto;
import com.example.demo.app.queryBuilder.service.QbeApplicationService;
import com.example.demo.customResponse.CustomResponse;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.swagger.v3.oas.annotations.Operation;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class QbeRestController {
	
	@Autowired
	private QbeApplicationService qbeservice;

//	@Autowired
//	private HttpServletRequest request;
	
    @Operation(summary = "Get all Queries")
    @PostMapping(path = "/getAllQueries", produces = "application/json")
	public List<ObjectNode> getQueries(){
 
		return qbeservice.getQueries(); 

    }
	

	@PostMapping("/addQueryData")
	public CustomResponse addQueryData(@RequestBody QueryDto queryDto ) {

		System.out.println("queryDTO11111>>>>>>>>>>>>> "+queryDto.getQueryName());
		System.out.println("queryDTO22222>>>>>>>>>>>>> "+queryDto.getQuery());
		System.out.println("queryDTO33333>>>>>>>>>>>>> "+queryDto.getUserId());
		if(queryDto.getQueryComments()=="") {
			queryDto.setQueryComments("None");
		}
		System.out.println("queryDTO44444>>>>>>>>>>>>> "+queryDto.getQueryComments());
		
		System.out.println("queryDTO55555>>>>>>>>>>>>> "+queryDto.getQueryVersion());
		System.out.println("queryDTO44444>>>>>>>>>>>>> "+queryDto.getQueryComments());
		System.out.println("queryFlag>>>>>>>>>>>>> "+queryDto.getQueryFlag());


		
		return qbeservice.addQueryData(queryDto);
	}
	
	 @Operation(summary = "Delete an existing Query")
	    @DeleteMapping("/deleteQueryData/{id}")
	    public CustomResponse deleteQueryData(@PathVariable("id") Integer id) {
		 
		 	int queryCountInDisplay=qbeservice.checkQueryUsage(id);
		 
		 	if(queryCountInDisplay==0) {
		 	
	        @SuppressWarnings("unused")
			CustomResponse respt = qbeservice.deleteQueryDataDetails(id);

	        CustomResponse resp = qbeservice.deleteQueryData(id);
	        return resp;
	        
		 	}else return null;
	    }
	 
	 
	 @PostMapping("/validateQuery")
		public List<ObjectNode> validateQuery(@RequestBody QueryDto queryDto ) {

			System.out.println("<<<<<<<<<<<<<<<<QUERY Validation>>>>>>>>>>>>>>>>>>>>> "+queryDto.getQuery());
			
			System.out.println("<<<<<<<<<<<<<<<<Session Serial>>>>>>>>>>>>>>>>>>>>> "+queryDto.getSession_serial());
		
			return qbeservice.validateQuery(queryDto);
		}
	 
	 @PostMapping("/validateSubQuery")
		public List<ObjectNode> validateSubQuery(@RequestBody QueryDto queryDto ) {

			System.out.println("<<<<<<<<<<<<<<<<QUERY Validation>>>>>>>>>>>>>>>>>>>>> "+queryDto.getQuery());
			
			System.out.println("<<<<<<<<<<<<<<<<Session Serial>>>>>>>>>>>>>>>>>>>>> "+queryDto.getSession_serial());
		
			return qbeservice.validateQuery(queryDto);
		}
	 
	 @GetMapping("/decodeQuery/{sessionSerial}/{queryId}/{userId}")
		public List<ObjectNode> decodeQuery(@PathVariable("sessionSerial") String sessionSerial,@PathVariable("queryId") long queryId,@PathVariable("userId") int userId) {

			return qbeservice.decodeQuery(sessionSerial,queryId,userId);
		}
	 
	 @GetMapping("/decodeSubQuery/{sessionSerial}/{queryId}/{userId}")
		public List<ObjectNode> decodeSubQuery(@PathVariable("sessionSerial") String sessionSerial,@PathVariable("queryId") long queryId,@PathVariable("userId") int userId) {

			return qbeservice.decodeQuery(sessionSerial,queryId,userId);
		}
	 
	 @GetMapping("/getSubQueries/{sessionSerial}/{queryId}/{userId}")
		public List<ObjectNode> getSubQueries(@PathVariable("sessionSerial") String sessionSerial,@PathVariable("queryId") long queryId,@PathVariable("userId") int userId) {

			return qbeservice.getSubQueries(sessionSerial,queryId,userId);
		}
	 
	 @PostMapping("/updateQuery")
		public int updateQuery(@RequestBody QueryDto queryDto ) {

		
			return qbeservice.updateQuery(queryDto);
		}
	
///////////////////////////PARAMETERS\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    

	    @PostMapping("/addParamSession")
		public int addParamSession(@RequestBody ParamDto paramDto) {
	    	
	    	System.out.println("<<<<<<<<<<<<<<<<PARAM User Id >>>>>>>>>>>>>>>>>>>>> "+paramDto.getUserId());
			System.out.println("<<<<<<<<<<<<<<<<PARAM Name >>>>>>>>>>>>>>>>>>>>> "+paramDto.getParamName());
			System.out.println("<<<<<<<<<<<<<<<<PARAM Type >>>>>>>>>>>>>>>>>>>>> "+paramDto.getParamType());
			System.out.println("<<<<<<<<<<<<<<:::::SERIAL::::: >>>>>>>>>>>>>>>>>>>>> "+paramDto.getSessionSerial());

			return qbeservice.addParamSession(paramDto);
	    }
	 
	    @Operation(summary = "Get Session Query Params")
	    @PostMapping(path = "/getParamSession/{serialAttribute}", produces = "application/json")
		public List<ObjectNode> getParamSession(@PathVariable("serialAttribute") String serialAttribute){
	    	
			return qbeservice.getParamSession(serialAttribute);
	    }
	 
	    @Operation(summary = "Get Session Params Combo")
	    @GetMapping(path = "/getParamCombo/{serialAttribute}", produces = "application/json")
		public List<ObjectNode> getParamCombo(@PathVariable("serialAttribute") String serialAttribute){
	    	
			return qbeservice.getParamCombo(serialAttribute);
	    }
	    
	    
	    @Operation(summary = "Get Param Types")
	    @GetMapping(path = "/getParamTypes", produces = "application/json")
		public List<ObjectNode> getParamTypes(){
	    	
			return qbeservice.getParamTypes();
	    }
	    
	    
	    @PostMapping("/updateParamSession")
		public int updateParamSession(@RequestBody ParamDto paramDto) {
	    	
			return qbeservice.updateParamSession(paramDto);
	    }
	    
	    
	    @Operation(summary = "Delete a Parameter in Parameters")
	    @DeleteMapping("/deleteParameter/{node}/{sessionAttribute}")
	    public int deleteParameter(@PathVariable("node") String node,@PathVariable("sessionAttribute") String sessionAttribute) {

	        return qbeservice.deleteParameter(node,sessionAttribute);
	        
	    }
	    
	    @Operation(summary = "Delete Sessions queryAdd, execHeads, queryHeads")
	    @DeleteMapping("/deleteSessions/{sessionAttribute}")
	    public int deleteSessions(@PathVariable("sessionAttribute") String sessionAttribute) {

	        return qbeservice.deleteSessions(sessionAttribute);
	        
	    }
///////////////////////////////HEADERS\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	    
	    @Operation(summary = "Get Headers Data ")
	    @PostMapping(path = "/getHeaderData/{serialAttribute}", produces = "application/json")
		public List<ObjectNode> getHeaderData(@PathVariable("serialAttribute") String serialAttribute){
	    	
			return qbeservice.getHeaderData(serialAttribute);
	    }
	    
	    
////////////////////////// QUERY EXECUTION \\\\\\\\\\\\\\\\\\\\\\\\\\
	    
	    @Operation(summary = "Query Execution Headers ")
	    @GetMapping(path = "/fetchDynamicHeaderData/{serialAttribute}", produces = "application/json")
		public List<ObjectNode> fetchDynamicHeaderData(@PathVariable("serialAttribute") String serialAttribute) {

			System.out.println("<<<<<<<<<<<<<<<<QUERY Result Headers>>>>>>>>>>>>>>>>>>>>> "+serialAttribute);
							
			return qbeservice.fetchDynamicHeaderData(serialAttribute);
		}
		  
		  
		  
	    @PostMapping("/fetchDynamicData/{query}")
		public List<ObjectNode> fetchDynamicData(@PathVariable("query") byte[] query){
	    	
			System.out.println("<<<<<<<<<<<<<<<<QUERY Result Data>>>>>>>>>>>>>>>>>>>>> "+query);
			return qbeservice.fetchDynamicData(query);
	    }

	    @PostMapping("/fetchDynamicDataQueryForm/")
	 		public List<ObjectNode> fetchDynamicDataQueryForm(@RequestBody QueryDto queryDto){
	 	    	
	 	//		System.out.println("<<<<<<<<<<<<<<<<QUERY Result Data>>>>>>>>>>>>>>>>>>>>> "+query);
	 			return qbeservice.fetchDynamicDataQueryForm(queryDto);
	 	    }

/////////////////////////////QUERY EXECUTION FROM INDISPLAY\\\\\\\\\\\\\\\\\\\\\\\
	    
		 @PostMapping("/execQueryInDisplay")
			public List<List<ObjectNode>> execQueryInDisplay(@RequestBody QueryDto queryDto ) {
			 
			 System.out.println("queryId>>>>>>>>>>>>>>>>>"+queryDto.getQueryId());
			 System.out.println("queryObject>>>>>>>>>>>>>"+queryDto.getQueryObject());
			
				return qbeservice.execQueryInDisplay(queryDto);
			}
		
		 
		 
		 
		 
		 
		  @PostMapping("/getParamsName/{queryId}")
			public String getParamsData(@PathVariable("queryId") long queryId){
		    	
				return qbeservice.getParamsName(queryId);
		    }
		 
	/////////////////////////QUERY EXPORT\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		  

		  @GetMapping("/exportQuery/{queryId}")
			public List<ObjectNode> exportQuery(@PathVariable("queryId") long queryId){
		    	
				return qbeservice.exportQuery(queryId);
		    }
		  
		 
			@PostMapping("/importQuery")
			public int importQuery(@RequestBody QueryDto queryDto ) {

				System.out.println("queryDTO11111>>>>>>>>>>>>> "+queryDto.getQueryName());
				System.out.println("queryDTO22222>>>>>>>>>>>>> "+queryDto.getQuery());
				System.out.println("queryDTO33333>>>>>>>>>>>>> "+queryDto.getUserId());
				if(queryDto.getQueryComments()=="") {
					queryDto.setQueryComments("None");
				}
				System.out.println("queryDTO44444>>>>>>>>>>>>> "+queryDto.getQueryComments());
				
				System.out.println("queryDTO55555>>>>>>>>>>>>> "+queryDto.getQueryVersion());
				System.out.println("queryDTO44444>>>>>>>>>>>>> "+queryDto.getQueryComments());
				System.out.println("queryFlag>>>>>>>>>>>>> "+queryDto.getQueryFlag());
				System.out.println("queryData>>>>>>>>>>>>> "+queryDto.getFile());


				
				return qbeservice.importQuery(queryDto);
			}
			
			@GetMapping("/importSubQuery")
			public long importSubQuery(@RequestBody QueryDto queryDto) {

						
				return qbeservice.importSubQuery(queryDto);
			}
			
			@PostMapping("/getlookupQueries")
			public List<ObjectNode> getlookupQueries() {
				return qbeservice.getlookupQueries();
			}
			
			///////////V-QUERY-FORM\\\\\\\\\\\\\\\\
			
			@GetMapping("/getQueryParams/{queryId}")
			public List<ObjectNode> getQueryParams(@PathVariable("queryId") long queryId){
				return qbeservice.getQueryParams(queryId);
			}
		 
			@GetMapping("/getQueryParamsLookup/{queryId}")
			public List<ObjectNode> getQueryParamsLookup(@PathVariable("queryId") long queryId){
				return qbeservice.getQueryParamsLookup(queryId);
			}
			
//			@GetMapping("/getQueryParamAllData/{queryId}")
//			public List<List<ObjectNode>> getQueryParamAllData(@PathVariable("queryId") long queryId){
//				return qbeservice.getQueryParamAllData(queryId);
//			}
			
			@PostMapping("/getQueryList")
			public List<ObjectNode> getQueryList(){
				return qbeservice.getQueryList();
			}
			
			@PostMapping("/insertQueryType/{queryId}/{queryTypeString}")
			public int insertQueryType(@PathVariable("queryId") Long queryId, @PathVariable("queryTypeString") String queryTypeString) {
				return qbeservice.insertQueryType(queryId, queryTypeString);
			}
			
			@GetMapping("/checkQueryTypeExists/{queryId}")
			public List<ObjectNode> checkQueryTypeExists(@PathVariable("queryId") Long queryId) {
				return qbeservice.checkQueryTypeExists(queryId);
			}
			
			@PostMapping("/updateQueryType/{queryId}/{queryTypeString}")
			public int updateQueryType(@PathVariable("queryId") Long queryId, @PathVariable("queryTypeString") String queryTypeString) {
				return qbeservice.updateQueryType(queryId, queryTypeString);
			}
			
			@PostMapping("/getSecurityUserQuery/{queryId}")
			public List<ObjectNode> getSecurityUserQuery(@PathVariable("queryId") Long queryId) {
				return qbeservice.getSecurityUserQuery(queryId);
			}
			@PostMapping("/getQueryListUsers/{qbeId}")
			public List<ObjectNode> getQueryListUsers(@PathVariable("qbeId") Long qbeId){
				return qbeservice.getQueryListUsers(qbeId);
			}
			
			
			@PostMapping("/addQbeAuthorizedUsers")
			public int addQbeAuthorizedUsers(@RequestBody QbeAuthorizedUserDto qbeAuthorizedUserDto){
				return qbeservice.addQbeAuthorizedUsers(qbeAuthorizedUserDto);
			}
			
			@DeleteMapping("/deleteQbeAuthorizedUsers/{qbeId}/{usrCode}")
			public void deleteQbeAuthorizedUsers(@PathVariable("qbeId") long qbeId, @PathVariable("usrCode") String usrCode) {
				qbeservice.deleteQbeAuthorizedUsers(qbeId,usrCode);
			}
			
			@DeleteMapping("/deleteQbeSecurityChanges/{qbeId}")
			public void deleteQbeSecurityChanges(@PathVariable("qbeId") long qbeId) {
				qbeservice.deleteQbeSecurityChanges(qbeId);
			}
			
			@PostMapping("/getQbeQueryCreatedBy/{qbeId}")
			public List<ObjectNode> getQbeQueryCreatedBy(@PathVariable("qbeId") long qbeId){
				return qbeservice.getQbeQueryCreatedBy(qbeId);
			}

			
			@GetMapping("/getAllQueryDataEngine")
			public void getAllQueryDataEngine(){
				qbeservice.getAllQueryDataEngine();
			}
			@GetMapping("/getBackQueriesEngine")
			public void getBackQueriesEngine(){
				qbeservice.getBackQueriesEngine();
			}
}
