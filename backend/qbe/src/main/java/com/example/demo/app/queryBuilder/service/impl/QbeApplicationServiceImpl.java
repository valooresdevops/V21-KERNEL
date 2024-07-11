package com.example.demo.app.queryBuilder.service.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.transform.ResultTransformer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.app.queryBuilder.dto.ParamDto;
import com.example.demo.app.queryBuilder.dto.QbeAuthorizedUserDto;
import com.example.demo.app.queryBuilder.dto.QueryDto;
import com.example.demo.app.queryBuilder.model.QbeAuthorizedUserModel;
import com.example.demo.app.queryBuilder.model.QueryDecodeEngineModel;
import com.example.demo.app.queryBuilder.model.SQBQuery;
import com.example.demo.app.queryBuilder.model.SQBQueryDetails;
import com.example.demo.app.queryBuilder.model.SQBQueryType;
import com.example.demo.app.queryBuilder.model.SessionModel;
import com.example.demo.app.queryBuilder.repository.CfgColumnConfigRepo;
import com.example.demo.app.queryBuilder.repository.CfgObjectDefRepo;
import com.example.demo.app.queryBuilder.repository.QbeUserReport;
import com.example.demo.app.queryBuilder.repository.QueryDecodeEngineRepo;
import com.example.demo.app.queryBuilder.repository.qbeAuthorizedUserRepo;
import com.example.demo.app.queryBuilder.repository.SQBQueryDetailsRepo;
import com.example.demo.app.queryBuilder.repository.SQBQueryRepo;
import com.example.demo.app.queryBuilder.repository.SQBQueryTypeRepo;
import com.example.demo.app.queryBuilder.repository.SessionRepo;
import com.example.demo.app.queryBuilder.repository.UsmUserRepo;
import com.example.demo.app.queryBuilder.service.QbeApplicationService;
import com.example.demo.customResponse.CustomResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class QbeApplicationServiceImpl implements QbeApplicationService {
	
	@Autowired
	DataSourceProperties dataSourceProperties;
	
	 @SuppressWarnings("unused")
	@Autowired
	 private JdbcTemplate jdbcTemplate;

	@Autowired
	private EntityManager entityManagerR;
	
	@Resource
	private SQBQueryRepo sqbQueryRepo;
	
	@Resource
	private SQBQueryDetailsRepo sqbQueryDetailsRepo;
	
	@Resource
	private SessionRepo sessionRepo;
	
	@Resource
	private CfgObjectDefRepo cfgObjectDefRepo;
	
	@Resource
	private UsmUserRepo usmUserRepo;
	
	@Resource
	private QbeUserReport qbeUserReport;
	
	@Resource
	private qbeAuthorizedUserRepo qbeAuthorizedUserRepo;
	
	@Resource
	private CfgColumnConfigRepo cfgColumnConfigRepo;
	
	@Resource
	private SQBQueryTypeRepo sqbQueryTypeRepo;
	
	@Resource
	private QueryDecodeEngineRepo queryDecodeEngineRepo;

	@Override
	public List<ObjectNode> getQueries() { 

		return getJson(entityManagerR, "SELECT q.queryId AS QBE_ID," + "  q.QUERY_NAME AS QUERY_NAME,"
				+ "(SELECT r.EMP_NAME from QbeUser r where r.EMP_ID = q.USR_CODE) AS EMP_NAME," + "(case when q.import_flag = 0 then 'QBE' else 'SQB' end ) AS import_flag,"
 + "q.VERSION_NO AS VERSION_NO," + "q.CREATION_DATE AS CREATION_DATE,"
				+ "q.DATA_STORE_ID AS DATA_STORE_ID " + "from SQBQuery q WHERE q.CREATED_BY != '-1'  ORDER BY q.CREATION_DATE desc"); 
	}
	

	
	@SuppressWarnings("deprecation")
	public static List<ObjectNode> getJson(EntityManager entityManagerR, String query) {

		List<ObjectNode> json = new ArrayList<>();

		ObjectMapper mapper = new ObjectMapper();

		entityManagerR.createQuery(query).unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new ResultTransformer() {
					@Override
					public Object transformTuple(Object[] tuple, String[] aliases) {

						ObjectNode node = mapper.createObjectNode();
						for (int i = 0; i < tuple.length; i++) {
							if (tuple[i] != null) {

									node.put(aliases[i], tuple[i].toString());
							}
						}
						json.add(node);
						return json;
					}

					@SuppressWarnings("rawtypes")
					@Override
					public List transformList(List tuples) {
						return tuples;
					}
				}).getResultList();
		return json;

	}

	@SuppressWarnings("deprecation")
	public static List<ObjectNode> getJsonNativeQuery(EntityManager entityManagerR, String query) {

		List<ObjectNode> json = new ArrayList<>();

		ObjectMapper mapper = new ObjectMapper();

		entityManagerR.createNativeQuery(query).unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new ResultTransformer() {
					@Override
					public Object transformTuple(Object[] tuple, String[] aliases) {

						ObjectNode node = mapper.createObjectNode();
						for (int i = 0; i < tuple.length; i++) {
							if (tuple[i] != null) {
								node.put(aliases[i], tuple[i].toString());
							}
						}
						json.add(node);
						return json;
					}

					@SuppressWarnings("rawtypes")
					@Override
					public List transformList(List tuples) {
						return tuples;
					}
				}).getResultList();
		return json;
	}

	public static String decodeBase64(String value) {

		try {
			return new String(java.util.Base64.getDecoder().decode(value));
		} catch (IllegalArgumentException ex) {
			System.out.println("ERROR decoding string '" + value + "':::::::: not a valid Base64 encoded string.");
			throw new RuntimeException(ex);
		}

	}

//////////////////////////// QUERY GRID MODIFICATION  \\\\\\\\\\\\\\\\\\\\\\\\\\
	 @Transactional
	    @Override
	    public CustomResponse addQueryData(QueryDto queryDto) {
		 
		 System.out.println("==============INITIAL ALERT============");
		 
		 	Date date=new Date();
	        @SuppressWarnings("unused")
			CustomResponse resp = CustomResponse.builder().build();
	        SQBQuery qbeQuery = new SQBQuery();
	        SQBQueryDetails qbeQueryDetails = new SQBQueryDetails();
	        String parameters="";
	        String heads="";
	        List<ObjectNode> params=new ArrayList<>();
	        List<ObjectNode> headers=new ArrayList<>();

	      	qbeQuery.setQUERY_NAME(queryDto.getQueryName());
	      	qbeQuery.setUSR_CODE(queryDto.getUserId());
	      	qbeQuery.setCREATED_BY(queryDto.getUserId());
	      	qbeQuery.setDATA_STORE_ID(1);
	      	qbeQuery.setImport_flag(queryDto.getQueryFlag());
	      	qbeQuery.setVERSION_NO(queryDto.getQueryVersion());
	      	qbeQuery.setRELEASE_NO(1);
	      	qbeQuery.setIS_BIG_QUERY(0);
	      	qbeQuery.setIS_FOR_ADVANCED_SEARCH("0");
	      	qbeQuery.setIS_SYSTEM_QUERY("0");
	      	qbeQuery.setCREATION_DATE(date);
	      	qbeQuery.setCOMMENTS(queryDto.getQueryComments());
	      	
	        sqbQueryRepo.save(qbeQuery);
	        
	        System.out.println("FIRST IDDDDD>>>>>>>>>"+qbeQuery.getQueryId());
	        
	        qbeQueryDetails.setQBE_ID(qbeQuery.getQueryId());
	        qbeQueryDetails.setCREATED_BY(queryDto.getUserId());
		    qbeQueryDetails.setCREATION_DATE(date);
		     
		    System.out.println("SERIAL NUMBER>>>>>>>"+queryDto.getSession_serial());
		    try{
		    	
		    params=getParamSession("paramAdd_"+queryDto.getSession_serial());
		    
			  }catch(Exception ex){
		    	
		    }
		    headers=getParamSession("queryHeads_"+queryDto.getSession_serial());
		    
			  String newQuery=queryDto.getQuery();

			 
			@SuppressWarnings("unused")
			List<ObjectNode> newParams=new ArrayList<>();
			
			  	  System.out.println("=========================FIRST ALERT=========================");
	
			  for (ObjectNode node : params) {
		            if (node.has("paramName") && node.has("paramType")) {
		                System.out.println("paramNAMEEEEE>>>>"+node.get("paramName").asText());
		                System.out.println("paramTYPEEEEE>>>>"+node.get("paramType").asText());
		                if(newQuery.contains("["+node.get("paramName").asText()+"]")) {
		                	Random rnd = new Random();
	                	    int number = rnd.nextInt(999999);

	                		String str = newQuery;		        
		    		        String strFind = "["+node.get("paramName").asText()+"]";
		    		        int count = 0, fromIndex = 0;
		    		        
		    		        while ((fromIndex = str.indexOf(strFind, fromIndex)) != -1 ){
		    		 
		    		            count++;
		    		            fromIndex++;
		    		            
		    		        }		          
	                	    
	                	    newQuery=newQuery.replace("["+node.get("paramName").asText()+"]", "#"+String.format("%06d",number)+"#");                		
	                	    if(node.get("paramType").asText().equals("query")) {
	                	    	parameters+="<par fieldType=\""+node.get("paramType").asText()+"\" subQID=\""+node.get("paramDefault").asText()+"\" id=\""+String.format("%06d",number)+"\" linkToGlobal=\"\" obl=\"0\" tUsed=\""+count+"\" type=\""+node.get("paramType").asText()+"\">"
	                	    			 +"<name><![CDATA["+node.get("paramName").asText()+"]]></name>"
	                	    			 +"<val><![CDATA["+node.get("paramName").asText()+"]]></val>"
	                	    			 +"</par>";
	                	    }
	                	    else {
		                	    parameters+="<par fieldType=\""+node.get("paramType").asText()+"\" id=\""+String.format("%06d",number)+"\" linkToGlobal=\"\" obl=\"0\" tUsed=\""+count+"\" type=\""+node.get("paramType").asText()+"\">"
		                	    			 +"<name><![CDATA["+node.get("paramName").asText()+"]]></name>"
		                	    			 +"<val><![CDATA["+node.get("paramName").asText()+"]]></val>"
		                	    			 +"</par>";
	                	    }
		                
		                }
		            }
		        }
			  
		  	  System.out.println("=========================SECOND ALERT=========================");

		  	  for (ObjectNode node : headers) {
		            if (node.has("headerName") && node.has("field")) {
		                System.out.println("Header name>>>>"+node.get("headerName").asText());
		                System.out.println("Header Type>>>>"+node.get("field").asText());
		                	
		          	    heads+="<EltHeads dbName=\""+node.get("headerName").asText()
		          	    		+"\" fieldType=\""+node.get("field").asText()
		          	    		+"\" id=\""+node.get("headerName").asText()
		          	    		+"\" readOnly=\"true\" type=\"S\">"
		          	    		+"<name><![CDATA["+node.get("headerName").asText()+"]]></name>"
		          	    		+"</EltHeads>";
             
		            }
		        }
			  	
		  	  System.out.println("=========================THIRD ALERT=========================");

		     String xmlData="<Qry qId=\""+qbeQuery.getQueryId()+"\" storeId=\""+qbeQuery.getDATA_STORE_ID()+"\">" 
		    		 		+"<name><![CDATA["+queryDto.getQueryName()+"]]></name>"
		    		 		+"<com/><sql><![CDATA["+newQuery+"]]></sql>"
		    		 		+"<LstHeads>"
		    		 		+heads
		    		 		+"</LstHeads>"
		    		 		+"<pars>"
		    		 		+parameters
		    		 		+"</pars>"
		    		 		+"<types/>" 
		    		 		+"<Dependencies/>" 
		    		 		+"<version>" 
		    		 		+"<major>1</major>" 
		    		 		+"<minor>2</minor>" 
		    		 		+"</version>" 
		    		 		+"<LstAtt/>" 
		    		 		+"</Qry>";
		    		 	
		    
		     qbeQueryDetails.setXML_DATA(Base64.encodeBase64(xmlData.getBytes()));
		     
		  	  System.out.println("=========================FOURTH ALERT=========================");

		     
		     sqbQueryDetailsRepo.save(qbeQueryDetails);

	        

	           return CustomResponse.builder().code("0").status("success").Id(qbeQuery.getQueryId())
	                    .description("SAVED SUCCESSFULLY").build();
	       }
	 
	 
	 @Transactional
		public CustomResponse deleteQueryData(long id) {

			SQBQuery sqbQuery = sqbQueryRepo.findByQueryId(id);
			
			CustomResponse resp = CustomResponse.builder().build();

			if (sqbQuery != null) {

				//test1 : delete from oracle
				sqbQueryRepo.deleteAllByQueryId((long)id);
				System.out.println("sqbQuery.getQBE_ID()>>>>>>>>>>"+id);

				resp.setCode("0");
				resp.setStatus("Success");
				resp.setDescription("Query with id = " + id + " deleted successfully!");

			} else {
				resp.setCode("1");
				resp.setStatus("Fail");
				resp.setDescription("Query with id = " + id + " not found!");
			}
			
			return resp;
		}



	 @Transactional
	public CustomResponse deleteQueryDataDetails(long id) {
		SQBQuery sqbQuery = sqbQueryRepo.findByQueryId(id);
		
		CustomResponse resp = CustomResponse.builder().build();

		if (sqbQuery != null) {

			sqbQueryDetailsRepo.deleteAllByQueryId((long)id);
			System.out.println("id>>>>>>>>>>"+id);
	

			resp.setCode("0");
			resp.setStatus("Success");
			resp.setDescription("Query with id = " + id + " deleted successfully!");

		} else {
			resp.setCode("1");
			resp.setStatus("Fail");
			resp.setDescription("Query with id = " + id + " not found!");
		}
		
		return resp;
		}


	 
	 @Override
	 public int updateQuery(QueryDto queryDto) {
	 	

	 	 String parameters="";
	      String heads="";
	      
	      queryDto.setStoreId(1);
	      
	      queryDto.setQueryName(sqbQueryRepo.fetchQueryName(queryDto.getQueryId()));
	      
	     System.out.println("SERIAL NUMBER>>>>>>>"+queryDto.getSession_serial());
	     List<ObjectNode> params=getParamSession("paramAdd_"+queryDto.getSession_serial());

	     List<ObjectNode> headers=getParamSession("queryHeads_"+queryDto.getSession_serial());
	     
	 	  String newQuery=queryDto.getQuery();

	 	 
	 	@SuppressWarnings("unused")
		List<ObjectNode> newParams=new ArrayList<>();
	 	
	 	  	  


	 	  for (ObjectNode node : params) {
	             if (node.has("paramName") && node.has("paramType")) {
	                 System.out.println("paramNAMEEEEE>>>>"+node.get("paramName").asText());
	                 System.out.println("paramTYPEEEEE>>>>"+node.get("paramType").asText());
	                 if(newQuery.contains("["+node.get("paramName").asText()+"]")) {
	                 	Random rnd = new Random();
	             	    int number = rnd.nextInt(999999);

	             		String str = newQuery;		        
	     		        String strFind = "["+node.get("paramName").asText()+"]";
	     		        int count = 0, fromIndex = 0;
	     		        
	     		        while ((fromIndex = str.indexOf(strFind, fromIndex)) != -1 ){
	     		 
	     		            count++;
	     		            fromIndex++;
	     		            
	     		        }		          
	             	    
	             	    newQuery=newQuery.replace("["+node.get("paramName").asText()+"]", "#"+String.format("%06d",number)+"#");                		
	             	   if(node.get("paramType").asText().equals("query")) {
               	    	parameters+="<par fieldType=\""+node.get("paramType").asText()+"\" subQID=\""+node.get("paramDefault").asText()+"\" id=\""+String.format("%06d",number)+"\" linkToGlobal=\"\" obl=\"0\" tUsed=\""+count+"\" type=\""+node.get("paramType").asText()+"\">"
               	    			 +"<name><![CDATA["+node.get("paramName").asText()+"]]></name>"
               	    			 +"<val><![CDATA["+node.get("paramName").asText()+"]]></val>"
               	    			 +"</par>";
               	    }
               	    else {
	                	    parameters+="<par fieldType=\""+node.get("paramType").asText()+"\" id=\""+String.format("%06d",number)+"\" linkToGlobal=\"\" obl=\"0\" tUsed=\""+count+"\" type=\""+node.get("paramType").asText()+"\">"
	                	    			 +"<name><![CDATA["+node.get("paramName").asText()+"]]></name>"
	                	    			 +"<val><![CDATA["+node.get("paramName").asText()+"]]></val>"
	                	    			 +"</par>";
               	    }
	                 
	                 }
	             }
	         }
	 	  
	 	  
	 	  for (ObjectNode node : headers) {
	             if (node.has("headerName") && node.has("field")) {
	                 System.out.println("Header name>>>>"+node.get("headerName").asText());
	                 System.out.println("Header Type>>>>"+node.get("field").asText());
	                 	
	           	    heads+="<EltHeads dbName=\""+node.get("headerName").asText()
	           	    		+"\" fieldType=\""+node.get("field").asText()
	           	    		+"\" id=\""+node.get("headerName").asText()
	           	    		+"\" readOnly=\"true\" type=\"S\">"
	           	    		+"<name><![CDATA["+node.get("headerName").asText()+"]]></name>"
	           	    		+"</EltHeads>";
	      
	             }
	         }
	 	  	

	      String xmlData="<Qry qId=\""+queryDto.getQueryId()+"\" storeId=\""+queryDto.getStoreId()+"\">" 
	     		 		+"<name><![CDATA["+queryDto.getQueryName()+"]]></name>"
	     		 		+"<com/><sql><![CDATA["+newQuery+"]]></sql>"
	     		 		+"<LstHeads>"
	     		 		+heads
	     		 		+"</LstHeads>"
	     		 		+"<pars>"
	     		 		+parameters
	     		 		+"</pars>"
	     		 		+"<types/>" 
	     		 		+"<Dependencies/>" 
	     		 		+"<version>" 
	     		 		+"<major>1</major>" 
	     		 		+"<minor>2</minor>" 
	     		 		+"</version>" 
	     		 		+"<LstAtt/>" 
	     		 		+"</Qry>";
	     		 	
	 	
	      sqbQueryDetailsRepo.updateQueryXml(queryDto.getQueryId(), Base64.encodeBase64(xmlData.getBytes()));
	 	
	 	return 0;
	 }

	 
		@Override
		public List<ObjectNode> decodeQuery(String sessionSerial, long queryId,int userId) {
			byte[] encodedQuery=sqbQueryDetailsRepo.getQueryBlob(queryId);
			String decodedQuery=new String(Base64.decodeBase64(encodedQuery));
			
			String dataStoreId=decodedQuery.substring(decodedQuery.indexOf("storeId=\""+1),decodedQuery.indexOf("\">"));
			dataStoreId=dataStoreId.split("\"")[1];
			
			System.out.println("DataStoreId:>>>>"+dataStoreId);		
			
			String queryName=decodedQuery.substring(decodedQuery.indexOf("<name>")+15,decodedQuery.indexOf("</name>")-3);
			
			System.out.println("Query Name is:>>>>"+queryName);
			
			String query = decodedQuery.substring(decodedQuery.indexOf("<sql>") + 14,decodedQuery.indexOf("</sql>")-3);
			
			System.out.println("Query is>>>>"+query);
			
			
			String headsString=decodedQuery.split("<LstHeads>")[1];
			headsString=headsString.split("</LstHeads>")[0];
			
			System.out.println("Heads String:>>>>>>>"+headsString);
			
			String[] headers=headsString.split("<EltHeads ");
			
			System.out.println("List of Heads>>>"+headers);
			
			String queryHeadsJsonString="[";
			String execHeadsJsonString="[";

			for(int i =1;i<headers.length;i++)
			{
				System.out.println("HEADER>>>"+headers[i]);
				String header = headers[i].split("dbName=\"")[1];
				System.out.println("header "+i+">>>>>>>>>>>"+header);

				header = header.split("\"")[0];
				System.out.println("header "+i+">>>>>>>>>>>"+header);
				
				String field = headers[i].split("fieldType=\"")[1];
				field = field.split("\"")[0];
				System.out.println("field "+i+">>>>>>>>>>>"+field);
				
				queryHeadsJsonString+="{\"headerName\":\""+header+"\",\"field\":\""+field+"\",\"businessName\":\""+header+"\"}";
				execHeadsJsonString+="{\"headerName\":\""+header+"\",\"field\":\""+header+"\"}";

				if(i!=headers.length-1) {
					queryHeadsJsonString+=",";
					execHeadsJsonString+=",";

				}
			}
			queryHeadsJsonString+="]";
			execHeadsJsonString+="]";

			
			String parametersString=decodedQuery.split("<pars>")[1];
			parametersString=parametersString.split("</pars>")[0];
			String[] parameters=parametersString.split("<par ");
			System.out.println("List of Heads>>>"+parameters);

			
			
			String paramsString="[";

			for(int i =1;i<parameters.length;i++)
			{
				System.out.println("PARAMETER>>>"+parameters[i]);
				String paramType = parameters[i].split("fieldType=\"")[1];
				System.out.println("paramName "+i+">>>>>>>>>>>"+paramType);
				paramType = paramType.split("\"")[0];
				System.out.println("paramName "+i+">>>>>>>>>>>"+paramType);
				
				
				
				String paramDefault="1";
				if(paramType.equals("query")) {
					System.out.println("TYPE IS QUERY!!!!!");
				paramDefault=parameters[i].split("subQID=\"")[1];
				paramDefault = paramDefault.split("\"")[0];
				System.out.println("paramQueryDefault>>>>>>>>>>>"+paramDefault);
				}
				
				
				String id = parameters[i].split("id=\"")[1];
				id= id.split("\"")[0];
				System.out.println("id "+i+">>>>>>>>>>>"+id);
				
				
				String paramName = parameters[i].substring(parameters[i].indexOf("<![CDATA[") + 9,parameters[i].indexOf("]"));


				System.out.println("paramName "+i+">>>>>>>>>>>"+paramName);
				if(paramType.equals("query")) {

					
					paramsString+="{\"paramName\":\""+paramName+"\",\"paramType\":\""+paramType+"\",\"paramDefault\":\""+paramDefault+"\"}";

				}else {
					paramsString+="{\"paramName\":\""+paramName+"\",\"paramType\":\""+paramType+"\",\"paramDefault\":\""+Integer.parseInt(paramDefault)+"\"}";

				}

				query=query.replace("#"+id+"#", "["+paramName+"]");
				
				if(i!=parameters.length-1) {
					paramsString+=",";

				}
			}
			paramsString+="]";

			
			System.out.println("The Full Query is:>>>>>>"+query);
			
			JSONArray queryHeads=new JSONArray(new String(queryHeadsJsonString));
			JSONArray execHeads=new JSONArray(new String(execHeadsJsonString));
			JSONArray paramAdd=new JSONArray(new String(paramsString));

//			ParamDto paramDto=new ParamDto();
//			paramDto.setSessionSerial(sessionSerial);
//			paramDto.setUserId(userId);

			
	        setSessionVal("paramAdd_"+sessionSerial,userId,paramAdd,sessionSerial,2);
	        setSessionVal("queryHeads_"+sessionSerial,userId,queryHeads,sessionSerial,2);
	        setSessionVal("execHeads_"+sessionSerial,userId,execHeads,sessionSerial,2);

	        List<ObjectNode> queryData=new ArrayList<>();
	        
	        ObjectMapper objectMapper = new ObjectMapper();
	        ObjectNode objectNode = objectMapper.createObjectNode();

	        objectNode.put("query", query);
	        objectNode.put("queryId", queryId);
	        
	        queryData.add(objectNode);
	        
			return queryData;
		}
/////////////////////// QUERY VALIDATION \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	@Override
	public List<ObjectNode> validateQuery(QueryDto queryDto) {
		try {
		  System.out.println("QUERY IS>>>>>>>>>>>"+queryDto.getQuery());
		  String newQuery=queryDto.getQuery();
		  
		  try {
		  List<ObjectNode> params=getParamSession("paramAdd_"+queryDto.getSession_serial());
		  System.out.println("PARAM SESSION CALLED>>>>>>>>"+"paramAdd_"+queryDto.getSession_serial());
		  
		  if(!params.isEmpty()) {
		  
		  for (ObjectNode node : params) {
	            if (node.has("paramName") && node.has("paramType")) {
	                System.out.println("paramNAMEEEEE>>>>"+node.get("paramName").asText());
	                System.out.println("paramTYPEEEEE>>>>"+node.get("paramType").asText());
	                if(newQuery.contains("["+node.get("paramName").asText()+"]")) {
	                	switch(node.get("paramType").asText()) {
	                	case "text":
	                	case "Text":
	                	case "TEXT":
	                		newQuery=newQuery.replace("["+node.get("paramName").asText()+"]", "'1'");
	                		break;
	                	case "number":
	                	case "Number":
	                	case "NUMBER":
	                		newQuery=newQuery.replace("["+node.get("paramName").asText()+"]", "1");
	                		break;
	                	case "date":
	                	case "Date":
	                	case "DATE":
	                		newQuery=newQuery.replace("["+node.get("paramName").asText()+"]","'5/15/2015 8:30:25 AM'" );
	                		break;
	                	case "session":	
	                	case "Session":
	                	case "SESSION":
	                		newQuery=newQuery.replace("["+node.get("paramName").asText()+"]", "'1'");
	                		break;
	                	case "query":	
	                	case "Query":
	                	case "QUERY":
	                		long subQueryId=node.get("paramDefault").asLong();
	                		System.out.println("PARAM QUERY>>>>>>>>>"+node);
	                		System.out.println("ELIE1111>>>>>"+subQueryId);
	                		sqbQueryDetailsRepo.getQueryBlob(subQueryId);
	                		List<ObjectNode> subQuery=decodeSubQuery(queryDto.getSession_serial(),subQueryId,(int)queryDto.getUserId());
	                		System.out.println("ELIE2222>>>>>>"+subQuery);
	                		QueryDto subQueryDto=new QueryDto();
	                		subQueryDto.setSession_serial(queryDto.getSession_serial());
	                		subQueryDto.setUserId(queryDto.getUserId());
	                		String subQueryString=subQuery.get(0).get("query").toString().replaceFirst("\"","");
	                		subQueryString=subQueryString.substring(0,subQueryString.length()-1);
	                		subQueryDto.setQuery(subQueryString);
	                		List<ObjectNode> subQueryValid=validateSubQuery(subQueryDto);
	                		System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS>>>"+subQueryValid.get(0).get("result").toString());
	                		
	                		if(subQueryValid.get(0).get("result").toString().equals("\"Success\"")) {
		                		newQuery=newQuery.replace("["+node.get("paramName").asText()+"]", "SELECT 1 FROM DUAL");
	                		}else {
	                			return subQueryValid;
	                		}
	                		break;
	                	}
	       	
	                }
	            }
	        }
		  }else {
				System.out.println("============BASIC QUERY============== PARAMS IS EMPTY");

		  }
		  }catch(Exception ex){
				System.out.println("============BASIC QUERY============= PARAMS DOEST EXIST");
		  }
		  System.out.println("NEW QUERYYYYYY1>>>>>>>>>"+newQuery);
		  newQuery=newQuery.replace("TRUNC ( '5/15/2015 8:30:25 AM' - 1 )","TRUNC(TO_DATE('5/15/2015 8:30:25 AM', 'MM/DD/YYYY HH:MI:SS AM') - INTERVAL '1' DAY)");
		  System.out.println("NEW QUERYYYYYY2>>>>>>>>>"+newQuery);
		  newQuery=newQuery.replaceAll("\\\\n"," ");

		  //newQuery=newQuery.replaceAll("\"","########");
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		System.out.println("QUERYYYY>>>>>>>"+newQuery);
		  @SuppressWarnings("unused")
		List<ObjectNode> records = getJsonNativeQuery(entityManagerR,newQuery);
		  	System.out.println("0000000000000000000000");
	     @SuppressWarnings("unused")
		List<ObjectNode> headerRecords=new ArrayList<>();
	      @SuppressWarnings("unused")
		ObjectMapper objMapper = new ObjectMapper();
	      String jsonString1="[";
	      String jsonString2="[";
		  Connection con = DriverManager.getConnection(dataSourceProperties.determineUrl(), "postgres", "postgres");
	      Statement stmt = con.createStatement();
	      ResultSet rs = stmt.executeQuery(newQuery);
	      ResultSetMetaData rsMetaData = rs.getMetaData();

	      int count = rsMetaData.getColumnCount();
	      for(int i = 1; i<=count; i++) {
	         jsonString1+="{\"headerName\":\""+rsMetaData.getColumnName(i)+"\",\"field\":\""+rsMetaData.getColumnTypeName(i)+"\",\"businessName\":\""+rsMetaData.getColumnName(i)+"\"}";  
	         jsonString2+="{\"headerName\":\""+rsMetaData.getColumnName(i)+"\",\"field\":\""+rsMetaData.getColumnName(i)+"\"}";  

	         if(i!=count) {
	        	 jsonString1+=",";
	        	 jsonString2+=",";

	         }   
	      }
	      jsonString1+="]";
	      jsonString2+="]";


				System.out.println("jsonString1>>>>>>>>>>>"+jsonString1);
				System.out.println("jsonString2>>>>>>>>>>>"+jsonString2);
				
				
				//jsonString1=jsonString1.replaceAll("########", "\\\\\\\"");
				//	jsonString2=jsonString2.replaceAll("########", "\\\\\\\"");
				
					
				
				
				System.out.println("jsonString1>>>>>>>>>>>"+jsonString1);
				System.out.println("jsonString2>>>>>>>>>>>"+jsonString2);
	
 		JSONArray queryHeadsValue=new JSONArray(new String(jsonString1));
 		JSONArray execHeadsValue=new JSONArray(new String(jsonString2));

 		System.out.println("QUERY HEADS VALUE>>>>>>>>>>>"+queryHeadsValue);
 	

 		
 		
 		setSessionVal("queryHeads_"+queryDto.getSession_serial(),queryDto.getUserId(),queryHeadsValue,queryDto.getSession_serial(),1);
 		setSessionVal("execHeads_"+queryDto.getSession_serial(),queryDto.getUserId(),execHeadsValue,queryDto.getSession_serial(),1);

 		
 		
 		 List<ObjectNode> objectNodeList = new ArrayList<>();

         ObjectMapper objectMapper = new ObjectMapper();

         ObjectNode objectNodeRes = objectMapper.createObjectNode();
         objectNodeRes.put("result", "Success");
         objectNodeList.add(objectNodeRes);

		
 		return objectNodeList;
		
		}catch(Exception ex){
			
			StringWriter sw = new StringWriter();
	        PrintWriter pw = new PrintWriter(sw);
	        ex.printStackTrace(pw);
	        
			List<ObjectNode> objectNodeList = new ArrayList<>();

	         ObjectMapper objectMapper = new ObjectMapper();

	         ObjectNode objectNodeRes = objectMapper.createObjectNode();
	         String res=sw.toString().split("Caused by")[sw.toString().split("Caused by").length-1];
	         
	         objectNodeRes.put("result", res);
	         objectNodeList.add(objectNodeRes);
			
			
			
			System.out.println("FAILED VALIDATION");
			ex.printStackTrace();
			return objectNodeList;
			
		}
		
	}

	@Transactional
	public void updateSessionValQuery(String sessionAttribute,long userId, JSONArray value,String serial,int updatable) {
	 	@SuppressWarnings("unused")
		Date date=new Date();
//	 	if(sessionRepo.sessionExists(sessionAttribute)!=0) {
//	 		JSONArray sessionValue=value;
//	 		System.out.print("ELIEEEEEEE111111111>>>>>>>>>>>>>>>"+sessionValue);
//	 	 	sessionRepo.update(sessionAttribute,Base64.encodeBase64(sessionValue.toString().getBytes()));
//	 	}
	 	if(sessionRepo.sessionExists(sessionAttribute)!=0 && sessionRepo.sessionUpdatable(sessionAttribute)==2) {
	 		byte[] updatedValue=sessionRepo.getAttributeValue(sessionAttribute);
	 		JSONArray sessionValue=new JSONArray(new String(Base64.decodeBase64(updatedValue)));
	 		String sessionValString=new String(Base64.decodeBase64(updatedValue));
	 		System.out.print("ELIEEEEEEE22222222222>>>>>>>>>>>>>>>"+sessionValue);
	 		for(int i=0;i<value.length();i++) {
	 			System.out.println("EELIEEEEEE333333>>>>>>>>>>"+value.get(i).toString());
	 		if(sessionValString.contains(value.get(i).toString())) {
	 			System.out.println("XCXCXCXCXCXC");
	 		}else {
	 			System.out.println("XCXCXCXCXCXC");

	 		sessionValue.put(value.get(i));
	 		}
	 		}
	 	 	sessionRepo.update(sessionAttribute,Base64.encodeBase64(sessionValue.toString().getBytes()));	
	 	}
	 	if(sessionRepo.sessionExists(sessionAttribute)!=0 && sessionRepo.sessionUpdatable(sessionAttribute)==0) {
	 		System.out.println("ERROR: Value neither updatable nor overwritable");
	 	}
	 	
	}	

//////////////////////////SESSION: GET SET MODIFY DELETE\\\\\\\\\\\\\\\\\\\\\\\\\\
	
public String getSessionVal(String sessionAttribute) {
	String jsonString=new String(Base64.decodeBase64(sessionRepo.getAttributeValue(sessionAttribute)));
	System.out.println("JSONNNN STRING>>>>>>"+jsonString);
	return jsonString;
	}

////FOR JSON OBJECT VALUE
public void setSessionVal(String sessionAttribute,long userId, JSONObject value,String serial,int updatable) {
	SessionModel sessionModel=new SessionModel();
 	Date date=new Date();
 	JSONArray sessionValue = new JSONArray();
 	if(sessionRepo.sessionExists(sessionAttribute)!=0) {
 		updateSessionVal(sessionAttribute,userId,value,serial,updatable);
 	}
 	else {
 		sessionValue.put(value);
 		sessionModel.setSESSION_ATTRIBUTE(sessionAttribute);
 	 	sessionModel.setSESSION_SERIAL(serial);
 		sessionModel.setCREATED_BY(userId);
 		sessionModel.setCREATION_DATE(date);
 		sessionModel.setUPDATABLE(updatable);
 		sessionModel.setSESSION_VALUE(Base64.encodeBase64(sessionValue.toString().getBytes()));
 	    sessionRepo.save(sessionModel);
 	}
 	
}	

@Transactional
public void updateSessionVal(String sessionAttribute,long userId, JSONObject value,String serial,int updatable) {
 	@SuppressWarnings("unused")
	Date date=new Date();
 	if(sessionRepo.sessionExists(sessionAttribute)!=0 && sessionRepo.sessionUpdatable(sessionAttribute)==1) {
 		JSONArray sessionValue=new JSONArray();
 		System.out.print("ELIEEEEEEE111111111>>>>>>>>>>>>>>>"+sessionValue);
 		sessionValue.put(value);
 	 	sessionRepo.update(sessionAttribute,Base64.encodeBase64(sessionValue.toString().getBytes()));
 	}

 	if(sessionRepo.sessionExists(sessionAttribute)!=0 && sessionRepo.sessionUpdatable(sessionAttribute)==2) {
 		byte[] updatedValue=sessionRepo.getAttributeValue(sessionAttribute);
 		JSONArray sessionValue=new JSONArray(new String(Base64.decodeBase64(updatedValue)));
 		System.out.print("ELIEEEEEEE22222222222>>>>>>>>>>>>>>>"+sessionValue);
 		sessionValue.put(value);
 	 	sessionRepo.update(sessionAttribute,Base64.encodeBase64(sessionValue.toString().getBytes()));	
 	}
 	
 	if(sessionRepo.sessionExists(sessionAttribute)!=0 && sessionRepo.sessionUpdatable(sessionAttribute)==0) {
 		System.out.println("ERROR: Value neither updatable nor overwritable");
 	}
 	
}	
////FOR JSON ARRAY VALUE
public void setSessionVal(String sessionAttribute,long userId, JSONArray value,String serial,int updatable) {
System.out.println("SETSESSIONVAL TEST>>>>>>>"+sessionAttribute);
	SessionModel sessionModel=new SessionModel();
	Date date=new Date();
	if(sessionRepo.sessionExists(sessionAttribute)!=0) {
		if(sessionAttribute.contains("paramAdd_")) {
			System.out.println("NNNNNNNNN>>>>>>>>>>>"+value);	
			updateSessionValQuery(sessionAttribute,userId,value,serial,updatable);
	 			
			}else {
				updateSessionVal(sessionAttribute,userId,value,serial,updatable);
	 		}
	 		
	}
	else {
		System.out.println("ABCSDFAFG>>>>>>>"+sessionAttribute);
		sessionModel.setSESSION_ATTRIBUTE(sessionAttribute);
	 	sessionModel.setSESSION_SERIAL(serial);
		sessionModel.setCREATED_BY(userId);
		sessionModel.setCREATION_DATE(date);
		sessionModel.setUPDATABLE(updatable);
		sessionModel.setSESSION_VALUE(Base64.encodeBase64(value.toString().getBytes()));
	    sessionRepo.save(sessionModel);
	}

}	


@Transactional
public void updateSessionVal(String sessionAttribute,long userId, JSONArray value,String serial,int updatable) {
 	@SuppressWarnings("unused")
	Date date=new Date();
 	if(sessionRepo.sessionExists(sessionAttribute)!=0) {
 		JSONArray sessionValue=value;
 		System.out.print("ELIEEEEEEE111111111>>>>>>>>>>>>>>>"+sessionValue);
 	 	sessionRepo.update(sessionAttribute,Base64.encodeBase64(sessionValue.toString().getBytes()));
 	}
 	
 	if(sessionRepo.sessionExists(sessionAttribute)!=0 && sessionRepo.sessionUpdatable(sessionAttribute)==0) {
 		System.out.println("ERROR: Value neither updatable nor overwritable");
 	}
 	
}	


@Override
public int deleteSessions(String sessionAttribute) {
		
	sessionRepo.deleteSession("paramAdd_"+sessionAttribute);
	sessionRepo.deleteSession("queryHeads_"+sessionAttribute);
	sessionRepo.deleteSession("execHeads_"+sessionAttribute);

	return 0;
}


///////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

/////////////////////////////////PARAMETERS \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	@Override
	public int addParamSession(ParamDto paramDto) {
			List<ObjectNode> list = new ArrayList<>();
			@SuppressWarnings("unused")
			ObjectMapper objMapper = new ObjectMapper();
			
			if(sessionRepo.sessionExists("paramAdd_"+paramDto.getSessionSerial())!=0) {
			
				list=getParamSession("paramAdd_"+paramDto.getSessionSerial());
				
				for (ObjectNode node : list) {
	            	System.out.println(">>>>>>>"+node.get("paramName").asText()+"<<<<<<<<"+paramDto.getParamName());

					if (((node.get("paramName").asText()).toString()).equals((paramDto.getParamName()).toString())) {
		            	return 1;
		            }
				}
			
		
			}
			
			JSONArray allparams=new JSONArray();
			JSONObject data = new JSONObject();
			
	        String paramName = paramDto.getParamName();
	        String paramType = paramDto.getParamType();
	        String paramDefault=paramDto.getParamDefault();

	        data.put("paramName", paramName);
	        data.put("paramType", paramType);
	        data.put("paramDefault", paramDefault);
	        
	        allparams.put(data);
	        
	        setSessionVal("paramAdd_"+paramDto.getSessionSerial(),paramDto.getUserId(),data,paramDto.getSessionSerial(),2);
	        
	    	return 0;
	}


	@Override
	public List<ObjectNode> getParamSession(String serialAttribute) {
		String jsonString=getSessionVal(serialAttribute);
		ObjectMapper objMapper = new ObjectMapper();
		List<ObjectNode> list = new ArrayList<>();
		JsonNode jsonArray;
		try {
			jsonArray = objMapper.readTree(jsonString);
		
		
		for (JsonNode element : jsonArray) {
			ObjectNode object = objMapper.treeToValue(element, ObjectNode.class);
			
			if(jsonString.contains("paramType")) {
			if(object.get("paramType").toString().equals("\"SESSION\"")) {
			String testVal=object.get("paramType").toString();
			System.out.println("TESTRUN>>>>>>>"+object.get("paramType"));
			System.out.println("TESTRUN>>>>>>>"+testVal);
			object.put("paramType","text");
			}
			if(object.get("paramType").toString().equals("\"VARCHAR2\"")) {
				String testVal=object.get("paramType").toString();
				System.out.println("TESTRUN>>>>>>>"+object.get("paramType"));
				System.out.println("TESTRUN>>>>>>>"+testVal);
				object.put("paramType","text");
			}
			}
		    list.add(object);
		
		}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		System.out.println("LIST>>>>>>>>>>"+list);

		return list;
	}
	
	@Override
	public List<ObjectNode> getParamCombo(String serialAttribute) {
		String jsonString=getSessionVal(serialAttribute);
		ObjectMapper objMapper = new ObjectMapper();
		List<ObjectNode> list = new ArrayList<>();
		List<ObjectNode> paramNameList = new ArrayList<>();

		JsonNode jsonArray;
		try {
			jsonArray = objMapper.readTree(jsonString);
		
		
			for (JsonNode element : jsonArray) {
				ObjectNode object = objMapper.treeToValue(element, ObjectNode.class);
			    list.add(object);
			}
				
        for (ObjectNode node : list) {
            if (node.has("paramName")) {
                ObjectNode newNode = objMapper.createObjectNode();
                newNode.put("id", node.get("paramName").asText());
                newNode.put("name", node.get("paramName").asText());
                paramNameList.add(newNode);
            }
        }
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		System.out.println("paramNameList>>>>>>>>>>>>>>>>>>"+paramNameList);

		return paramNameList;
	}


	@Override
	public List<ObjectNode> getParamTypes() {
		return getJson(entityManagerR, "SELECT lower(a.LIN_NAME) AS id,lower(a.LIN_NAME) as name FROM ParameterTypes a WHERE a.HEA_CODE=714 AND (a.LIN_CODE=1 OR a.LIN_CODE=2 OR a.LIN_CODE=3 OR LIN_CODE=5)"); 
	}
	//OR a.LIN_CODE=8
	
	@Override
	public int updateParamSession(ParamDto paramDto) {
			
 		JSONArray allparameters=new JSONArray(new String(paramDto.getAllParameters()));
 		System.out.println("ALL PARAMETERS>>>>>>>>>>>>>>>>>"+allparameters);
	        updateSessionVal("paramAdd_"+paramDto.getSessionSerial(),paramDto.getUserId(),allparameters,paramDto.getSessionSerial(),1);
	        
	    	return 0;
	}
	
	
	@Override
	public int deleteParameter(String node, String sessionAttribute) {
	    List<ObjectNode> params=getParamSession("paramAdd_"+sessionAttribute);
		System.out.println("params before delete>>>"+params);
		ObjectNode upForDeletion=null;
		  for (ObjectNode n : params) {
			  	if(n.get("paramName").asText().equals(node)) {
			  	
			  		upForDeletion=n;
			  	
			  	}
		  }
		params.remove(upForDeletion);
		
		String newParams=new String(params.toString());
		  
		System.out.println("params after delete>>>"+params);
		
		sessionRepo.update("paramAdd_"+sessionAttribute,Base64.encodeBase64(newParams.getBytes()));
		return 0;
	}
	

	
///////////////////////////////////// HEADERS \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	@Override
	public List<ObjectNode> getHeaderData(String serialAttribute) {
		String jsonString=getSessionVal(serialAttribute);
		ObjectMapper objMapper = new ObjectMapper();
		List<ObjectNode> list = new ArrayList<>();
		JsonNode jsonArray;
		try {
			jsonArray = objMapper.readTree(jsonString);
			
		
		for (JsonNode element : jsonArray) {
			ObjectNode object = objMapper.treeToValue(element, ObjectNode.class);
		    list.add(object);
		}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		System.out.println("LIST>>>>>>>>>>"+list);

		return list;
	}

 
/////////////////////////////// QUERY EXECUTION \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\


	@Override
	public List<ObjectNode> fetchDynamicHeaderData(String serialAttribute) {
	
		String jsonString=getSessionVal(serialAttribute);
		ObjectMapper objMapper = new ObjectMapper();
		List<ObjectNode> list = new ArrayList<>();
		JsonNode jsonArray;
		try {
			jsonArray = objMapper.readTree(jsonString);
		
		
		for (JsonNode element : jsonArray) {
			ObjectNode object = objMapper.treeToValue(element, ObjectNode.class);
		    list.add(object);
		}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		System.out.println("HEADER LIST>>>>>>>>>>>>>>"+list);

		return list;
	}
	
	
	
	@Override
	public List<ObjectNode> fetchDynamicData(byte[] query) { 
		
		System.out.println("Query encoded>>>>>>"+query);
		
		String currentQuery=new String(Base64.decodeBase64(query));
		//currentQuery=currentQuery.replace("",);
		//.replaceAll("\"","########")
		List<ObjectNode> records = getJsonNativeQuery(entityManagerR,currentQuery);
		String recordsString=records.toString();
		//.replaceAll("########","\\\\\\\"")
		ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Parse the JSON string into a JsonNode
            JsonNode jsonNode = objectMapper.readTree(recordsString);

            // Convert the JsonNode into a List<ObjectNode>
            records = new ArrayList<>();
            if (jsonNode.isArray()) {
                for (JsonNode node : jsonNode) {
                    // Convert each element to ObjectNode
                    ObjectNode objectNode = (ObjectNode) node;
                    records.add(objectNode);
                }
            }

            // Print the result
            for (ObjectNode objectNode : records) {
                System.out.println(objectNode);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
		
		System.out.println("Executed Query Data>>>>>>"+records);
		return records; 
	}

	
	  @Override
  	public List<ObjectNode> fetchDynamicDataQueryForm(QueryDto queryDto) { 
  		
  		System.out.println("Query encoded>>>>>>"+queryDto.getQuery());
  		
  		String currentQuery=new String(Base64.decodeBase64(queryDto.getQuery()));
  		//currentQuery=currentQuery.replace("",);
  		//.replaceAll("\"","########")
  		List<ObjectNode> records = getJsonNativeQuery(entityManagerR,currentQuery);
  		String recordsString=records.toString();
  		//.replaceAll("########","\\\\\\\"")
  		ObjectMapper objectMapper = new ObjectMapper();

          try {
              // Parse the JSON string into a JsonNode
              JsonNode jsonNode = objectMapper.readTree(recordsString);

              // Convert the JsonNode into a List<ObjectNode>
              records = new ArrayList<>();
              if (jsonNode.isArray()) {
                  for (JsonNode node : jsonNode) {
                      // Convert each element to ObjectNode
                      ObjectNode objectNode = (ObjectNode) node;
                      records.add(objectNode);
                  }
              }

              // Print the result
              for (ObjectNode objectNode : records) {
                  System.out.println(objectNode);
              }

          } catch (JsonProcessingException e) {
              e.printStackTrace();
          }
  		
  		System.out.println("Executed Query Data>>>>>>"+records);
  		return records; 
  	}


	@Override
	public List<List<ObjectNode>> execQueryInDisplay(QueryDto queryDto) {
			
		System.out.println("QQQQQQQQ>>>>>>>>>>>>>>>>>>"+queryDto.getQueryId());
			//JSONObject json = new JSONObject(new String(queryDto.getQueryObject()));
			//JSONArray jsonHeaders=json.names();
									
			byte[] encodedQuery=sqbQueryDetailsRepo.getQueryBlob(queryDto.getQueryId());
			
			
			String decodedQuery=new String(Base64.decodeBase64(encodedQuery));
							

			
			String query = decodedQuery.substring(decodedQuery.indexOf("<sql>") + 14,decodedQuery.indexOf("</sql>")-3);
			
			System.out.println("Query is>>>>>>>>>>>>>>>>>>>>>"+query);

			String headsString=decodedQuery.split("<LstHeads>")[1];
			headsString=headsString.split("</LstHeads>")[0];
					
			String[] headers=headsString.split("<EltHeads ");
			
			String execHeadsJsonString="[";

			for(int i =1;i<headers.length;i++)
			{
				String header = headers[i].split("dbName=\"")[1];

				header = header.split("\"")[0];
		
				execHeadsJsonString+="{\"headerName\":\""+header+"\",\"field\":\""+header+"\"}";

				if(i!=headers.length-1) {
					execHeadsJsonString+=",";

				}
			}
			execHeadsJsonString+="]";
		
			String parametersString=decodedQuery.split("<pars>")[1];
			parametersString=parametersString.split("</pars>")[0];
			String[] parameters=parametersString.split("<par ");

			for(int i =1;i<parameters.length;i++)
			{
				System.out.println("HEADER>>>"+parameters[i]);
				String paramType = parameters[i].split("fieldType=\"")[1];
				System.out.println("paramName "+i+">>>>>>>>>>>"+paramType);

				paramType = paramType.split("\"")[0];
				System.out.println("paramName "+i+">>>>>>>>>>>"+paramType);
				
				String id = parameters[i].split("id=\"")[1];
				id= id.split("\"")[0];
				System.out.println("id "+i+">>>>>>>>>>>"+id);
		
				String paramName = parameters[i].substring(parameters[i].indexOf("<![CDATA[") + 9,parameters[i].indexOf("]"));

				query=query.replace("#"+id+"#", "["+paramName+"]");
			}

			@SuppressWarnings("unused")
			JSONArray execHeads=new JSONArray(new String(execHeadsJsonString));
		
//			if(jsonHeaders!=null) {
//				for(int i=0;i<jsonHeaders.length();i++) {
//
//					query=query.replace("["+jsonHeaders.getString(i)+"]","'"+json.get(jsonHeaders.getString(i)).toString()+"'");
//	
//				}
//			}

			ObjectMapper objMapper = new ObjectMapper();
		 	List<ObjectNode> headerList = new ArrayList<>();
			JsonNode jsonArray;
			try {
				jsonArray = objMapper.readTree(execHeadsJsonString);
			
			
			for (JsonNode element : jsonArray) {
				ObjectNode object = objMapper.treeToValue(element, ObjectNode.class);
				headerList.add(object);
			}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
			System.out.println("RAW QUERY>>>>>>>>>>>"+query);

			JSONArray parametersList=new JSONArray(new String (queryDto.getParameters()));
			
			for(int i=0;i<parametersList.length();i++){
				query=query.replace("["+parametersList.getJSONObject(i).getString("paramName")+"]",parametersList.getJSONObject(i).getString("paramValue"));
			}
			
			System.out.println("NEW QUERY>>>>>>>>>>>>"+query);

			
			List<ObjectNode> dataList=getJsonNativeQuery(entityManagerR,query);

			List<List<ObjectNode>> alldata=new ArrayList<>();
			alldata.add(headerList);
			alldata.add(dataList);
			System.out.println("allData>>>>>>>>>>>"+alldata);
			return alldata;
	}


	
	
	@Override
	public String getParamsName(long queryId) {
		byte[] encodedQuery=sqbQueryDetailsRepo.getQueryBlob(queryId);
		String decodedQuery=new String(Base64.decodeBase64(encodedQuery));
		
	
		String parametersString=decodedQuery.split("<pars>")[1];
		parametersString=parametersString.split("</pars>")[0];
		String[] parameters=parametersString.split("<par ");
		System.out.println("List of Heads>>>"+parameters);
		
		String paramsString="[";

		for(int i =1;i<parameters.length;i++)
		{
			System.out.println("HEADER>>>"+parameters[i]);
			String paramType = parameters[i].split("fieldType=\"")[1];
			System.out.println("paramName "+i+">>>>>>>>>>>"+paramType);

			paramType = paramType.split("\"")[0];
			System.out.println("paramName "+i+">>>>>>>>>>>"+paramType);
			
			String id = parameters[i].split("id=\"")[1];
			id= id.split("\"")[0];
			System.out.println("id "+i+">>>>>>>>>>>"+id);
			
			
			String paramName = parameters[i].substring(parameters[i].indexOf("<![CDATA[") + 9,parameters[i].indexOf("]"));

			System.out.println("paramName "+i+">>>>>>>>>>>"+paramName);
			
			paramsString+="{\"paramName\":\""+paramName+"\",\"paramType\":\""+paramType+"\",\"paramDefault\":\""+1+"\"}";
			
			if(i!=parameters.length-1) {
				paramsString+=",";

			}
		}
		paramsString+="]";

		
        
		return paramsString;
	}



	@Override
	public int checkQueryUsage(long id) {

		int exists;
		exists=cfgObjectDefRepo.qbeExists(id);
		exists+=cfgColumnConfigRepo.qbeExitsts(id);
		
		return exists;
	}



	@Override
	public List<ObjectNode> exportQuery(long queryId) {
		try{
		String queryName=sqbQueryRepo.fetchQueryName(queryId);
		byte[] query=sqbQueryDetailsRepo.getQueryBlob(queryId);
		System.out.println("QUERY NAME EXPORT>>>>>>"+queryName);
		System.out.println("QUERY DATA EXPORT>>>>>>"+query);

		 List<ObjectNode> queryData=new ArrayList<>();
	        
	        ObjectMapper objectMapper = new ObjectMapper();
	        ObjectNode objectNode = objectMapper.createObjectNode();

	        objectNode.put("queryName", queryName);
	        objectNode.put("query", query);
	        
	        queryData.add(objectNode);
			return queryData;

		}catch(Exception ex) {
			return null;
		}
		
	}


	public static String replaceFirstOccurrence(String original, String target, String replacement) {
        int index = original.indexOf(target);
        if (index != -1) {
            return original.substring(0, index) + replacement + original.substring(index + target.length());
        }
        return original;
    }
	
	@Override
	public int importQuery(QueryDto queryDto) {
		System.out.println(queryDto.getFile());
		  String decodedQuery= new String(Base64.decodeBase64(queryDto.getFile())).replaceAll("\n","");
		  String regex = "\\" + "<query>" + ".*?\\" + "</query>";
		  decodedQuery=decodedQuery.replaceAll(regex,"");
		  String regex2 = "\\" + "</val><Qry" + ".*?\\" + "</Qry>";
		  decodedQuery=decodedQuery.replaceAll(regex2,"");

		  
	        System.out.println("decodedQuery>>>> " + decodedQuery);

	        
	        int count = 0;
	        int index = 0;
	        List<String> paramIds=new ArrayList<>();
	        while ((index = decodedQuery.indexOf("<par", index)) != -1) {
	            count++;
	            index += "<par".length();
	            
	        }
	        int z=1;
	        String[] decodedSubQueryParam=decodedQuery.split("<par");

	        while(count>1) {
	        
	        if(decodedSubQueryParam[z].contains("type=\"QUERY\"")) {
	        String decodedSubQueryParam2=decodedSubQueryParam[z].split(">")[0];
	        String paramToRemove="<par"+decodedSubQueryParam2+">";
	        System.out.println("PARAM TO REMOVE>>>>>>>>>"+paramToRemove);
	        decodedQuery=decodedQuery.replaceAll(paramToRemove,"");
	        String paramid=decodedSubQueryParam2.split("id=\"")[1].split("\"")[0];
	        paramIds.add(paramid);
	        }
	        count--;
	        z++;
	        }
	        System.out.println("PAARAMIDSSSS>>>>"+paramIds);

	        Collections.reverse(paramIds);

	        System.out.println("PAARAMIDSSSS>>>>"+paramIds);
	        
	        int count2 = 0;
	        int index2 = 0;
	        while ((index2 = decodedQuery.indexOf("<Qry", index2)) != -1) {
	            count2++;
	            index2 += "<Qry".length();
	            
	        }
	        
	        int j=1;
	        while(count2>1) {
		        System.out.println("PAARAMIDSSSS>>>>"+paramIds);

		        String[] decodedSubQueryAr=decodedQuery.split("<Qry");
		        String decodedSubQuery="<Qry"+decodedSubQueryAr[decodedSubQueryAr.length-1].split("</Qry>")[0]+"</Qry>";
		        QueryDto subQueryDto=queryDto;
		        subQueryDto.setFile(decodedSubQuery);
		        long subQueryImported=importSubQuery(subQueryDto);
		        if(subQueryImported==-1) {
		        	return 0;
		        }
		        else {
			        System.out.println("COUNT2222222>>>>>>"+count2);
			        System.out.println("SUB QUERY NUM>>>>>>>>>>>"+decodedSubQueryAr.length);

			    System.out.println("SUB QUERY IS>>>>>>>>>>"+decodedSubQuery);
			    System.out.println("PARAMIDDDD>>>>"+paramIds.get(j-1));
		        System.out.println("Test QUERY11111>>>>>>>>"+decodedQuery);

			    String param = "<par fieldType=\"query\" subQID=\"" + subQueryImported + "\" id=\"" + paramIds.get(j-1) + "\" linkToGlobal=\"\" obl=\"0\" tUsed=\"1\" type=\"query\">";
			    
			    System.out.println("PARAM TO BE REPLACED WITH>>>>"+param);
			    //decodedQuery = decodedQuery.replace(decodedSubQuery, param);
			    decodedQuery=replaceFirstOccurrence(decodedQuery,decodedSubQuery,param);
		        System.out.println("Test QUERY22222>>>>>>>>"+decodedQuery);
		        int c3 = 0;
		        int i3 = 0;
		        while ((i3 = decodedQuery.indexOf("<Qry", i3)) != -1) {
		        	c3++;
		            i3 += "<Qry".length();
		            
		        }
		        if(c3==1) {
		        	break;
		        }
		        }
	        j++;
	        count2--;
	        }
	        
	        System.out.println("XML QUERY>>>>>>>>"+decodedQuery);
			String dataStoreId=decodedQuery.substring(decodedQuery.indexOf("storeId=\""+1),decodedQuery.indexOf("\">"));
			dataStoreId=dataStoreId.split("\"")[1];
			
			
			String queryName=decodedQuery.substring(decodedQuery.indexOf("<name>")+15,decodedQuery.indexOf("</name>")-3);
			
			System.out.println("Query Name is:>>>>"+queryName);
			
			String query = decodedQuery.substring(decodedQuery.indexOf("<sql>") + 14,decodedQuery.indexOf("</sql>")-3);
			
			System.out.println("Query is>>>>"+query);
			
			
			String headsString=decodedQuery.split("<LstHeads>")[1];
			headsString=headsString.split("</LstHeads>")[0];
			
			
			String[] headers=headsString.split("<EltHeads ");
			
			
			String queryHeadsJsonString="[";
			String execHeadsJsonString="[";

			for(int i =1;i<headers.length;i++)
			{
				System.out.println("HEADER>>>"+headers[i]);
				String header = headers[i].split("dbName=\"")[1];
				System.out.println("header "+i+">>>>>>>>>>>"+header);

				header = header.split("\"")[0];
				System.out.println("header "+i+">>>>>>>>>>>"+header);
				
				String field = headers[i].split("fieldType=\"")[1];
				field = field.split("\"")[0];
				System.out.println("field "+i+">>>>>>>>>>>"+field);
				
				queryHeadsJsonString+="{\"headerName\":\""+header+"\",\"field\":\""+field+"\",\"businessName\":\""+header+"\"}";
				execHeadsJsonString+="{\"headerName\":\""+header+"\",\"field\":\""+header+"\"}";

				if(i!=headers.length-1) {
					queryHeadsJsonString+=",";
					execHeadsJsonString+=",";

				}
			}
			queryHeadsJsonString+="]";
			execHeadsJsonString+="]";

			if(decodedQuery.contains("<pars/>")) {
				decodedQuery=decodedQuery.replaceAll("<pars/>", "<pars></pars>");
			}
			
			
			String parametersString=decodedQuery.split("<pars>")[1];
			parametersString=parametersString.split("</pars>")[0];
			String[] parameters=parametersString.split("<par ");
			System.out.println("List of Heads>>>"+parameters);

			
			
			String paramsString="[";

			for(int i =1;i<parameters.length;i++)
			{
				System.out.println("PARAMETER>>>"+parameters[i]);
				String paramType = parameters[i].split("fieldType=\"")[1];
				System.out.println("paramName "+i+">>>>>>>>>>>"+paramType);
				paramType = paramType.split("\"")[0];
				System.out.println("paramName "+i+">>>>>>>>>>>"+paramType);
				
				
				
				String paramDefault="1";
				if(paramType.equals("query")) {
					System.out.println("TYPE IS QUERY!!!!!");
				paramDefault=parameters[i].split("subQID=\"")[1];
				paramDefault = paramDefault.split("\"")[0];
				System.out.println("paramQueryDefault>>>>>>>>>>>"+paramDefault);
				}
				
				
				String id = parameters[i].split("id=\"")[1];
				id= id.split("\"")[0];
				System.out.println("id "+i+">>>>>>>>>>>"+id);
				
				
				String paramName = parameters[i].substring(parameters[i].indexOf("<![CDATA[") + 9,parameters[i].indexOf("]"));


				System.out.println("paramName "+i+">>>>>>>>>>>"+paramName);
				if(paramType.equals("query")) {

					
					paramsString+="{\"paramName\":\""+paramName+"\",\"paramType\":\""+paramType+"\",\"paramDefault\":\""+paramDefault+"\"}";

				}else {
					paramsString+="{\"paramName\":\""+paramName+"\",\"paramType\":\""+paramType+"\",\"paramDefault\":\""+Integer.parseInt(paramDefault)+"\"}";

				}

				query=query.replace("#"+id+"#", "["+paramName+"]");
				
				if(i!=parameters.length-1) {
					paramsString+=",";

				}
			}
			paramsString+="]";

			
			System.out.println("The Full Query is:>>>>>>"+query);
			
			JSONArray queryHeads=new JSONArray(new String(queryHeadsJsonString));
			JSONArray execHeads=new JSONArray(new String(execHeadsJsonString));
			JSONArray paramAdd=new JSONArray(new String(paramsString));

//			ParamDto paramDto=new ParamDto();
//			paramDto.setSessionSerial(sessionSerial);
//			paramDto.setUserId(userId);

			
	        setSessionVal("paramAdd_"+queryDto.getSession_serial(),queryDto.getUserId(),paramAdd,queryDto.getSession_serial(),2);
	        setSessionVal("queryHeads_"+queryDto.getSession_serial(),queryDto.getUserId(),queryHeads,queryDto.getSession_serial(),2);
	        setSessionVal("execHeads_"+queryDto.getSession_serial(),queryDto.getUserId(),execHeads,queryDto.getSession_serial(),2);

	        List<ObjectNode> queryData=new ArrayList<>();
	        
	        ObjectMapper objectMapper = new ObjectMapper();
	        ObjectNode objectNode = objectMapper.createObjectNode();

	        objectNode.put("query", query);
	        
	        queryData.add(objectNode);
	        System.out.println("QUERYYYYYY>>>>>>>>>>>"+queryData);
	        
	        queryDto.setQuery(query);
	        queryDto.setQueryName(queryName);
	        queryDto.setQueryFlag(1);
	        
	        List<ObjectNode> isQueryValid=validateQuery(queryDto);
	        String isQueryValidString=isQueryValid.get(0).get("result").textValue();
	        System.out.println("RESSS>>>>>>>>>>>>>>"+isQueryValidString);
	        
	        if(isQueryValidString.equals("Success")) {
	        	
	        	Date date=new Date();
		        @SuppressWarnings("unused")
				CustomResponse resp = CustomResponse.builder().build();
		        SQBQuery qbeQuery = new SQBQuery();
		        SQBQueryDetails qbeQueryDetails = new SQBQueryDetails();
//		        String parameters="";
//		        String heads="";
//		        List<ObjectNode> params=new ArrayList<>();
//		        List<ObjectNode> headers=new ArrayList<>();

		      	qbeQuery.setQUERY_NAME(queryDto.getQueryName());
		      	qbeQuery.setUSR_CODE(queryDto.getUserId());
		      	qbeQuery.setCREATED_BY(queryDto.getUserId());
		      	qbeQuery.setDATA_STORE_ID(1);
		      	qbeQuery.setImport_flag(queryDto.getQueryFlag());
		      	qbeQuery.setVERSION_NO(queryDto.getQueryVersion());
		      	qbeQuery.setRELEASE_NO(1);
		      	qbeQuery.setIS_BIG_QUERY(0);
		      	qbeQuery.setIS_FOR_ADVANCED_SEARCH("0");
		      	qbeQuery.setIS_SYSTEM_QUERY("0");
		      	qbeQuery.setCREATION_DATE(date);
		      	qbeQuery.setCOMMENTS(queryDto.getQueryComments());
		      	
		        sqbQueryRepo.save(qbeQuery);
	        	
		        qbeQueryDetails.setQBE_ID(qbeQuery.getQueryId());
		        qbeQueryDetails.setCREATED_BY(queryDto.getUserId());
			    qbeQueryDetails.setCREATION_DATE(date);
			    qbeQueryDetails.setCREATION_DATE(date);

			     qbeQueryDetails.setXML_DATA(Base64.encodeBase64(decodedQuery.getBytes()));
			     
			     
			     sqbQueryDetailsRepo.save(qbeQueryDetails);
		         return 1;
	        }else {
	        	return 0; 
	        }	        
	}
	
	
	
	
	
	@Override
	public long importSubQuery(QueryDto queryDto) {
		System.out.println(queryDto.getFile());
		  String decodedQuery= queryDto.getFile();
	        System.out.println("decodedQuery>>>> " + decodedQuery);
	        
	        
	   
			String dataStoreId=decodedQuery.substring(decodedQuery.indexOf("storeId=\""+1),decodedQuery.indexOf("\">"));
			dataStoreId=dataStoreId.split("\"")[1];
			
			System.out.println("DataStoreId:>>>>"+dataStoreId);		
			
			String queryName=decodedQuery.substring(decodedQuery.indexOf("<name>")+15,decodedQuery.indexOf("</name>")-3);
			
			System.out.println("Query Name is:>>>>"+queryName);
			
			String query = decodedQuery.substring(decodedQuery.indexOf("<sql>") + 14,decodedQuery.indexOf("</sql>")-3);
			
			System.out.println("Query is>>>>"+query);
			
			
			String headsString=decodedQuery.split("<LstHeads>")[1];
			headsString=headsString.split("</LstHeads>")[0];
			
			System.out.println("Heads String:>>>>>>>"+headsString);
			
			String[] headers=headsString.split("<EltHeads ");
			
			System.out.println("List of Heads>>>"+headers);
			
			String queryHeadsJsonString="[";
			String execHeadsJsonString="[";

//			for(int i =1;i<headers.length;i++)
//			{
//				System.out.println("HEADER>>>"+headers[i]);
//				String header = headers[i].split("dbName=\"")[1];
//				System.out.println("header "+i+">>>>>>>>>>>"+header);
//
//				header = header.split("\"")[0];
//				System.out.println("header "+i+">>>>>>>>>>>"+header);
//				
//				String field = headers[i].split("fieldType=\"")[1];
//				field = field.split("\"")[0];
//				System.out.println("field "+i+">>>>>>>>>>>"+field);
//				
//				queryHeadsJsonString+="{\"headerName\":\""+header+"\",\"field\":\""+field+"\",\"businessName\":\""+header+"\"}";
//				execHeadsJsonString+="{\"headerName\":\""+header+"\",\"field\":\""+header+"\"}";
//
//				if(i!=headers.length-1) {
//					queryHeadsJsonString+=",";
//					execHeadsJsonString+=",";
//
//				}
//			}
			queryHeadsJsonString+="]";
			execHeadsJsonString+="]";

			if(decodedQuery.contains("<pars/>")) {
				decodedQuery=decodedQuery.replaceAll("<pars/>", "<pars></pars>");
			}
			
			
			String parametersString=decodedQuery.split("<pars>")[1];
			parametersString=parametersString.split("</pars>")[0];
			String[] parameters=parametersString.split("<par ");
			System.out.println("List of Heads>>>"+parameters);

			
			
			String paramsString="[";

			for(int i =1;i<parameters.length;i++)
			{
				System.out.println("HEADER>>>"+parameters[i]);
				String paramType = parameters[i].split("fieldType=\"")[1];
				System.out.println("paramName "+i+">>>>>>>>>>>"+paramType);

				paramType = paramType.split("\"")[0];
				System.out.println("paramName "+i+">>>>>>>>>>>"+paramType);
				
				String id = parameters[i].split("id=\"")[1];
				id= id.split("\"")[0];
				System.out.println("id "+i+">>>>>>>>>>>"+id);
				
				
				String paramName = parameters[i].substring(parameters[i].indexOf("<![CDATA[") + 9,parameters[i].indexOf("]"));

//				String paramName = parameters[i].split("<![CDATA[")[1];
//				paramName= paramName.split("]")[0];
				System.out.println("paramName "+i+">>>>>>>>>>>"+paramName);
				
				paramsString+="{\"paramName\":\""+paramName+"\",\"paramType\":\""+paramType+"\",\"paramDefault\":\""+1+"\"}";

				query=query.replace("#"+id+"#", "["+paramName+"]");
				
				if(i!=parameters.length-1) {
					paramsString+=",";

				}
			}
			paramsString+="]";

			
			System.out.println("The Full Query is:>>>>>>"+query);
			
			@SuppressWarnings("unused")
			JSONArray queryHeads=new JSONArray(new String(queryHeadsJsonString));
			@SuppressWarnings("unused")
			JSONArray execHeads=new JSONArray(new String(execHeadsJsonString));
			JSONArray paramAdd=new JSONArray(new String(paramsString));

//			ParamDto paramDto=new ParamDto();
//			paramDto.setSessionSerial(sessionSerial);
//			paramDto.setUserId(userId);

			
	        setSessionVal("paramAdd_"+queryDto.getSession_serial(),queryDto.getUserId(),paramAdd,queryDto.getSession_serial(),2);
	    //    setSessionVal("queryHeads_"+queryDto.getSession_serial(),queryDto.getUserId(),queryHeads,queryDto.getSession_serial(),2);
	    //    setSessionVal("execHeads_"+queryDto.getSession_serial(),queryDto.getUserId(),execHeads,queryDto.getSession_serial(),2);

	        List<ObjectNode> queryData=new ArrayList<>();
	        
	        ObjectMapper objectMapper = new ObjectMapper();
	        ObjectNode objectNode = objectMapper.createObjectNode();
	        query=query.replaceAll("\t", " ");
	        objectNode.put("query", query);
	        
	        queryData.add(objectNode);
	        System.out.println("QUERYYYYYY>>>>>>>>>>>"+queryData);
	        
	        queryDto.setQuery(query);
	        queryDto.setQueryName(queryName);
	        queryDto.setQueryFlag(1);
	        
	        List<ObjectNode> isQueryValid=validateSubQuery(queryDto);
	        String isQueryValidString=isQueryValid.get(0).get("result").textValue();
	        System.out.println("RESSS>>>>>>>>>>>>>>"+isQueryValidString);
	        
	        if(isQueryValidString.equals("Success")) {
	        	
	        	Date date=new Date();
		        @SuppressWarnings("unused")
				CustomResponse resp = CustomResponse.builder().build();
		        SQBQuery qbeQuery = new SQBQuery();
		        SQBQueryDetails qbeQueryDetails = new SQBQueryDetails();
//		        String parameters="";
//		        String heads="";
//		        List<ObjectNode> params=new ArrayList<>();
//		        List<ObjectNode> headers=new ArrayList<>();

		      	qbeQuery.setQUERY_NAME(queryDto.getQueryName());
		      	qbeQuery.setUSR_CODE(queryDto.getUserId());
		      	qbeQuery.setCREATED_BY(queryDto.getUserId());
		      	qbeQuery.setDATA_STORE_ID(1);
		      	qbeQuery.setImport_flag(queryDto.getQueryFlag());
		      	qbeQuery.setVERSION_NO(queryDto.getQueryVersion());
		      	qbeQuery.setRELEASE_NO(1);
		      	qbeQuery.setIS_BIG_QUERY(0);
		      	qbeQuery.setIS_FOR_ADVANCED_SEARCH("0");
		      	qbeQuery.setIS_SYSTEM_QUERY("0");
		      	qbeQuery.setCREATION_DATE(date);
		      	qbeQuery.setCOMMENTS(queryDto.getQueryComments());
		      	
		        sqbQueryRepo.save(qbeQuery);
	        	
		        qbeQueryDetails.setQBE_ID(qbeQuery.getQueryId());
		        qbeQueryDetails.setCREATED_BY(queryDto.getUserId());
			    qbeQueryDetails.setCREATION_DATE(date);
			    qbeQueryDetails.setCREATION_DATE(date);

			     qbeQueryDetails.setXML_DATA(Base64.encodeBase64(decodedQuery.getBytes()));
			     
			     
			     sqbQueryDetailsRepo.save(qbeQueryDetails);
			     
			     

		         return qbeQuery.getQueryId();
	        }else {
	        	return -1; 
	        }	        
	}

	
	@Override
	public List<ObjectNode> getlookupQueries() {
		return getJson(entityManagerR, "SELECT q.queryId AS id," + "  q.QUERY_NAME AS name FROM SQBQuery q");
	}

	@Override
	public List<ObjectNode> validateSubQuery(QueryDto queryDto) {
		try {
		  System.out.println("QUERY IS>>>>>>>>>>>"+queryDto.getQuery());
		  String newQuery=queryDto.getQuery();
		  
		  try {
		  List<ObjectNode> params=getParamSession("paramAdd_"+queryDto.getSession_serial());

		  
		  if(!params.isEmpty()) {
		  
		  for (ObjectNode node : params) {
	            if (node.has("paramName") && node.has("paramType")) {
	                System.out.println("paramNAMEEEEE>>>>"+node.get("paramName").asText());
	                System.out.println("paramTYPEEEEE>>>>"+node.get("paramType").asText());
	                if(newQuery.contains("["+node.get("paramName").asText()+"]")) {
	                	switch(node.get("paramType").asText()) {
	                	case "text":
	                	case "Text":
	                	case "TEXT":
	                		newQuery=newQuery.replace("["+node.get("paramName").asText()+"]", "'1'");
	                		break;
	                	case "number":
	                	case "Number":
	                	case "NUMBER":
	                		newQuery=newQuery.replace("["+node.get("paramName").asText()+"]", "1");
	                		break;
	                	case "date":
	                	case "Date":
	                	case "DATE":
	                		newQuery=newQuery.replace("["+node.get("paramName").asText()+"]","'5/15/2015 8:30:25 AM'" );
	                		break;
	                	case "session":	
	                	case "Session":
	                	case "SESSION":
	                		newQuery=newQuery.replace("["+node.get("paramName").asText()+"]", "'1'");
	                		break;
	                	case "query":	
	                	case "Query":
	                	case "QUERY":
	                		long subQueryId=node.get("paramDefault").asLong();
	                		System.out.println("ELIE1111>>>>>"+subQueryId);
	                		sqbQueryDetailsRepo.getQueryBlob(subQueryId);
	                		List<ObjectNode> subQuery=decodeSubQuery(queryDto.getSession_serial(),subQueryId,(int)queryDto.getUserId());
	                		System.out.println("ELIE2222>>>>>>"+subQuery);
	                		QueryDto subQueryDto=new QueryDto();
	                		subQueryDto.setSession_serial(queryDto.getSession_serial());
	                		subQueryDto.setUserId(queryDto.getUserId());
	                		String subQueryString=subQuery.get(0).get("query").toString().replaceFirst("\"","");
	                		subQueryString=subQueryString.substring(0,subQueryString.length()-1);
	                		subQueryDto.setQuery(subQueryString);
	                		List<ObjectNode> subQueryValid=validateSubQuery(subQueryDto);
	                		System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS>>>"+subQueryValid.get(0).get("result").toString());
	                		
	                		if(subQueryValid.get(0).get("result").toString().equals("\"Success\"")) {
		                		newQuery=newQuery.replace("["+node.get("paramName").asText()+"]", "SELECT 1 FROM DUAL");
	                		}else {
	                			return subQueryValid;
	                		}
	                		break;
	                	}
	       	
	                }
	            }
	        }
		  }else {
				System.out.println("============BASIC QUERY============== PARAMS IS EMPTY");

		  }
		  }catch(Exception ex){
				System.out.println("============BASIC QUERY============= PARAMS DOEST EXIST");
		  }
		  System.out.println("NEW QUERYYYYYY>>>>>>>>>"+newQuery);
		  newQuery=newQuery.replaceAll("\\\\n"," ");
		  newQuery=newQuery.replaceAll("\\\\t"," ");

		  @SuppressWarnings("unused")
		List<ObjectNode> records = getJsonNativeQuery(entityManagerR,newQuery);
		  	System.out.println("0000000000000000000000");
	     @SuppressWarnings("unused")
		List<ObjectNode> headerRecords=new ArrayList<>();
	      @SuppressWarnings("unused")
		ObjectMapper objMapper = new ObjectMapper();
	      String jsonString1="[";
	      String jsonString2="[";
		  Connection con = DriverManager.getConnection(dataSourceProperties.determineUrl(), "qbedba", "qbedba");
	      Statement stmt = con.createStatement();
	      ResultSet rs = stmt.executeQuery(newQuery);
	      ResultSetMetaData rsMetaData = rs.getMetaData();

	      int count = rsMetaData.getColumnCount();
	      for(int i = 1; i<=count; i++) {
	         jsonString1+="{\"headerName\":\""+rsMetaData.getColumnName(i)+"\",\"field\":\""+rsMetaData.getColumnTypeName(i)+"\",\"businessName\":\""+rsMetaData.getColumnName(i)+"\"}";  
	         jsonString2+="{\"headerName\":\""+rsMetaData.getColumnName(i)+"\",\"field\":\""+rsMetaData.getColumnName(i)+"\"}";  

	         if(i!=count) {
	        	 jsonString1+=",";
	        	 jsonString2+=",";

	         }   
	      }
	      jsonString1+="]";
	      jsonString2+="]";


System.out.println("jsonString1>>>>>>>>>>>"+jsonString1);
System.out.println("jsonString2>>>>>>>>>>>"+jsonString2);


//jsonString1=jsonString1.replaceAll("########", "\\\\\\\"");
//	jsonString2=jsonString2.replaceAll("########", "\\\\\\\"");

	


System.out.println("jsonString1>>>>>>>>>>>"+jsonString1);
System.out.println("jsonString2>>>>>>>>>>>"+jsonString2);
	
 		JSONArray queryHeadsValue=new JSONArray(new String(jsonString1));
 		@SuppressWarnings("unused")
		JSONArray execHeadsValue=new JSONArray(new String(jsonString2));

 		System.out.println("QUERY HEADS VALUE>>>>>>>>>>>"+queryHeadsValue);
 	

 		
 		
 		//setSessionVal("queryHeads_"+queryDto.getSession_serial(),queryDto.getUserId(),queryHeadsValue,queryDto.getSession_serial(),1);
 		//setSessionVal("execHeads_"+queryDto.getSession_serial(),queryDto.getUserId(),execHeadsValue,queryDto.getSession_serial(),1);

 		
 		
 		 List<ObjectNode> objectNodeList = new ArrayList<>();

         ObjectMapper objectMapper = new ObjectMapper();

         ObjectNode objectNodeRes = objectMapper.createObjectNode();
         objectNodeRes.put("result", "Success");
         objectNodeList.add(objectNodeRes);

		
 		return objectNodeList;
		
		}catch(Exception ex){
			
			StringWriter sw = new StringWriter();
	        PrintWriter pw = new PrintWriter(sw);
	        ex.printStackTrace(pw);
	        
			List<ObjectNode> objectNodeList = new ArrayList<>();

	         ObjectMapper objectMapper = new ObjectMapper();

	         ObjectNode objectNodeRes = objectMapper.createObjectNode();
	         String res=sw.toString().split("Caused by")[sw.toString().split("Caused by").length-1];
	         
	         objectNodeRes.put("result", res);
	         objectNodeList.add(objectNodeRes);
			
			
			
			System.out.println("FAILED VALIDATION");
			ex.printStackTrace();
			return objectNodeList;
			
		}
		
	}

	@Override
	public List<ObjectNode> decodeSubQuery(String sessionSerial, long queryId,int userId) {
		byte[] encodedQuery=sqbQueryDetailsRepo.getQueryBlob(queryId);
		String decodedQuery=new String(Base64.decodeBase64(encodedQuery));
		
		String dataStoreId=decodedQuery.substring(decodedQuery.indexOf("storeId=\""+1),decodedQuery.indexOf("\">"));
		dataStoreId=dataStoreId.split("\"")[1];
		
		System.out.println("DataStoreId:>>>>"+dataStoreId);		
		
		String queryName=decodedQuery.substring(decodedQuery.indexOf("<name>")+15,decodedQuery.indexOf("</name>")-3);
		
		System.out.println("Query Name is:>>>>"+queryName);
		
		String query = decodedQuery.substring(decodedQuery.indexOf("<sql>") + 14,decodedQuery.indexOf("</sql>")-3);
		
		System.out.println("Query is>>>>"+query);
		
		
		String headsString=decodedQuery.split("<LstHeads>")[1];
		headsString=headsString.split("</LstHeads>")[0];
		
		System.out.println("Heads String:>>>>>>>"+headsString);
		
		String[] headers=headsString.split("<EltHeads ");
		
		System.out.println("List of Heads>>>"+headers);
		
		String queryHeadsJsonString="[";
		String execHeadsJsonString="[";

		for(int i =1;i<headers.length;i++)
		{
			System.out.println("HEADER>>>"+headers[i]);
			String header = headers[i].split("dbName=\"")[1];
			System.out.println("header "+i+">>>>>>>>>>>"+header);

			header = header.split("\"")[0];
			System.out.println("header "+i+">>>>>>>>>>>"+header);
			
			String field = headers[i].split("fieldType=\"")[1];
			field = field.split("\"")[0];
			System.out.println("field "+i+">>>>>>>>>>>"+field);
			
			queryHeadsJsonString+="{\"headerName\":\""+header+"\",\"field\":\""+field+"\",\"businessName\":\""+header+"\"}";
			execHeadsJsonString+="{\"headerName\":\""+header+"\",\"field\":\""+header+"\"}";

			if(i!=headers.length-1) {
				queryHeadsJsonString+=",";
				execHeadsJsonString+=",";

			}
		}
		queryHeadsJsonString+="]";
		execHeadsJsonString+="]";

		
		String parametersString=decodedQuery.split("<pars>")[1];
		parametersString=parametersString.split("</pars>")[0];
		String[] parameters=parametersString.split("<par ");
		System.out.println("List of Heads>>>"+parameters);

		
		
		String paramsString="[";

		for(int i =1;i<parameters.length;i++)
		{
			System.out.println("HEADER>>>"+parameters[i]);
			String paramType = parameters[i].split("fieldType=\"")[1];
			System.out.println("paramName "+i+">>>>>>>>>>>"+paramType);

			paramType = paramType.split("\"")[0];
			System.out.println("paramName "+i+">>>>>>>>>>>"+paramType);
			
			
			
			String paramDefault="1";
			if(paramType.equals("query")) {
				System.out.println("TYPE IS QUERY!!!!!");
			paramDefault=parameters[i].split("subQID=\"")[1];
			paramDefault = paramDefault.split("\"")[0];
			System.out.println("paramQueryDefault>>>>>>>>>>>"+paramDefault);
			}
			
			
			
			
			
			String id = parameters[i].split("id=\"")[1];
			id= id.split("\"")[0];
			System.out.println("id "+i+">>>>>>>>>>>"+id);
			
			
			String paramName = parameters[i].substring(parameters[i].indexOf("<![CDATA[") + 9,parameters[i].indexOf("]"));

//			String paramName = parameters[i].split("<![CDATA[")[1];
//			paramName= paramName.split("]")[0];
			System.out.println("paramName "+i+">>>>>>>>>>>"+paramName);
			
			if(paramType.equals("query")) {

				
				paramsString+="{\"paramName\":\""+paramName+"\",\"paramType\":\""+paramType+"\",\"paramDefault\":\""+paramDefault+"\"}";

			}else {
				paramsString+="{\"paramName\":\""+paramName+"\",\"paramType\":\""+paramType+"\",\"paramDefault\":\""+Integer.parseInt(paramDefault)+"\"}";

			}
			query=query.replace("#"+id+"#", "["+paramName+"]");
			
			if(i!=parameters.length-1) {
				paramsString+=",";

			}
		}
		paramsString+="]";

		
		System.out.println("The Full Query is:>>>>>>"+query);
		
		@SuppressWarnings("unused")
		JSONArray queryHeads=new JSONArray(new String(queryHeadsJsonString));
		@SuppressWarnings("unused")
		JSONArray execHeads=new JSONArray(new String(execHeadsJsonString));
		JSONArray paramAdd=new JSONArray(new String(paramsString));

//		ParamDto paramDto=new ParamDto();
//		paramDto.setSessionSerial(sessionSerial);
//		paramDto.setUserId(userId);

		
        setSessionVal("paramAdd_"+sessionSerial,userId,paramAdd,sessionSerial,2);
       // setSessionVal("queryHeads_"+sessionSerial,userId,queryHeads,sessionSerial,2);
      //  setSessionVal("execHeads_"+sessionSerial,userId,execHeads,sessionSerial,2);

        List<ObjectNode> queryData=new ArrayList<>();
        
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("query", query);
        objectNode.put("queryId", queryId);
        
        queryData.add(objectNode);
        
		return queryData;
	}

	
	
	
	
	
	
	
	@Override
	public List<ObjectNode> getSubQueries(String sessionSerial, long queryId,int userId) {
		byte[] encodedQuery=sqbQueryDetailsRepo.getQueryBlob(queryId);
		String decodedQuery=new String(Base64.decodeBase64(encodedQuery));
		
		String dataStoreId=decodedQuery.substring(decodedQuery.indexOf("storeId=\""+1),decodedQuery.indexOf("\">"));
		dataStoreId=dataStoreId.split("\"")[1];
		
		System.out.println("DataStoreId:>>>>"+dataStoreId);		
		
		String queryName=decodedQuery.substring(decodedQuery.indexOf("<name>")+15,decodedQuery.indexOf("</name>")-3);
		
		System.out.println("Query Name is:>>>>"+queryName);
		
		String query = decodedQuery.substring(decodedQuery.indexOf("<sql>") + 14,decodedQuery.indexOf("</sql>")-3);
		
		System.out.println("Query is>>>>"+query);
		
		
		String headsString=decodedQuery.split("<LstHeads>")[1];
		headsString=headsString.split("</LstHeads>")[0];
		
		System.out.println("Heads String:>>>>>>>"+headsString);
		
		String[] headers=headsString.split("<EltHeads ");
		
		System.out.println("List of Heads>>>"+headers);
		
		@SuppressWarnings("unused")
		String queryHeadsJsonString="[";
		@SuppressWarnings("unused")
		String execHeadsJsonString="[";

		for(int i =1;i<headers.length;i++)
		{
			System.out.println("HEADER>>>"+headers[i]);
			String header = headers[i].split("dbName=\"")[1];
			System.out.println("header "+i+">>>>>>>>>>>"+header);

			header = header.split("\"")[0];
			System.out.println("header "+i+">>>>>>>>>>>"+header);
			
			String field = headers[i].split("fieldType=\"")[1];
			field = field.split("\"")[0];
			System.out.println("field "+i+">>>>>>>>>>>"+field);
			
			queryHeadsJsonString+="{\"headerName\":\""+header+"\",\"field\":\""+field+"\",\"businessName\":\""+header+"\"}";
			execHeadsJsonString+="{\"headerName\":\""+header+"\",\"field\":\""+header+"\"}";

			if(i!=headers.length-1) {
				queryHeadsJsonString+=",";
				execHeadsJsonString+=",";

			}
		}
		queryHeadsJsonString+="]";
		execHeadsJsonString+="]";

		
		String parametersString=decodedQuery.split("<pars>")[1];
		parametersString=parametersString.split("</pars>")[0];
		String[] parameters=parametersString.split("<par ");
		System.out.println("List of Heads>>>"+parameters);

		
		
		@SuppressWarnings("unused")
		String paramsString="[";

		for(int i =1;i<parameters.length;i++)
		{
			System.out.println("HEADER>>>"+parameters[i]);
			String paramType = parameters[i].split("fieldType=\"")[1];
			System.out.println("paramName "+i+">>>>>>>>>>>"+paramType);

			paramType = paramType.split("\"")[0];
			System.out.println("paramName "+i+">>>>>>>>>>>"+paramType);
			
			
			
			String paramDefault="1";
			if(paramType.equals("query")) {
				System.out.println("TYPE IS QUERY!!!!!");
			paramDefault=parameters[i].split("subQID=\"")[1];
			paramDefault = paramDefault.split("\"")[0];
			System.out.println("paramQueryDefault>>>>>>>>>>>"+paramDefault);
			}
			
			
			
			
			
			String id = parameters[i].split("id=\"")[1];
			id= id.split("\"")[0];
			System.out.println("id "+i+">>>>>>>>>>>"+id);
			
			
			String paramName = parameters[i].substring(parameters[i].indexOf("<![CDATA[") + 9,parameters[i].indexOf("]"));

//			String paramName = parameters[i].split("<![CDATA[")[1];
//			paramName= paramName.split("]")[0];
			System.out.println("paramName "+i+">>>>>>>>>>>"+paramName);
			
			if(paramType.equals("query")) {

				
				paramsString+="{\"paramName\":\""+paramName+"\",\"paramType\":\""+paramType+"\",\"paramDefault\":\""+paramDefault+"\"}";

			}else {
				paramsString+="{\"paramName\":\""+paramName+"\",\"paramType\":\""+paramType+"\",\"paramDefault\":\""+Integer.parseInt(paramDefault)+"\"}";

			}
			query=query.replace("#"+id+"#", "["+paramName+"]");
			
			if(i!=parameters.length-1) {
				paramsString+=",";

			}
		}
		paramsString+="]";

		
		System.out.println("The Full Query is:>>>>>>"+query);
		
//		JSONArray queryHeads=new JSONArray(new String(queryHeadsJsonString));
//		JSONArray execHeads=new JSONArray(new String(execHeadsJsonString));
//		JSONArray paramAdd=new JSONArray(new String(paramsString));

//		ParamDto paramDto=new ParamDto();
//		paramDto.setSessionSerial(sessionSerial);
//		paramDto.setUserId(userId);

		
      //  setSessionVal("paramAdd_"+sessionSerial,userId,paramAdd,sessionSerial,2);
       // setSessionVal("queryHeads_"+sessionSerial,userId,queryHeads,sessionSerial,2);
      //  setSessionVal("execHeads_"+sessionSerial,userId,execHeads,sessionSerial,2);

        List<ObjectNode> queryData=new ArrayList<>();
        
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("query", query);
        objectNode.put("queryId", queryId);
        
        queryData.add(objectNode);
        
		return queryData;
	}

	
	
	@Override
	public List<ObjectNode> getQueryParams(long queryId) {
		byte[] encodedQuery=sqbQueryDetailsRepo.getQueryBlob(queryId);
		String decodedQuery=new String(Base64.decodeBase64(encodedQuery));
		
		//String dataStoreId=decodedQuery.substring(decodedQuery.indexOf("storeId=\""+1),decodedQuery.indexOf("\">"));
		//dataStoreId=dataStoreId.split("\"")[1];
		
		//System.out.println("DataStoreId:>>>>"+dataStoreId);		
		
		String queryName=decodedQuery.substring(decodedQuery.indexOf("<name>")+15,decodedQuery.indexOf("</name>")-3);
		
		System.out.println("Query Name is:>>>>"+queryName);
		
		String query = decodedQuery.substring(decodedQuery.indexOf("<sql>") + 14,decodedQuery.indexOf("</sql>")-3);
		
		System.out.println("Query is>>>>"+query);
		
		
		String headsString=decodedQuery.split("<LstHeads>")[1];
		headsString=headsString.split("</LstHeads>")[0];
		
		System.out.println("Heads String:>>>>>>>"+headsString);
		
		String[] headers=headsString.split("<EltHeads ");
		
		System.out.println("List of Heads>>>"+headers);
		
		@SuppressWarnings("unused")
		String queryHeadsJsonString="[";
		String execHeadsJsonString="[";

		for(int i =1;i<headers.length;i++)
		{
			System.out.println("HEADER>>>"+headers[i]);
			String header = headers[i].split("dbName=\"")[1];
			System.out.println("header "+i+">>>>>>>>>>>"+header);

			header = header.split("\"")[0];
			System.out.println("header "+i+">>>>>>>>>>>"+header);
			
			String field = headers[i].split("fieldType=\"")[1];
			field = field.split("\"")[0];
			System.out.println("field "+i+">>>>>>>>>>>"+field);
			
			queryHeadsJsonString+="{\"headerName\":\""+header+"\",\"field\":\""+field+"\",\"businessName\":\""+header+"\"}";
			execHeadsJsonString+="{\"headerName\":\""+header+"\",\"field\":\""+header+"\"}";

			if(i!=headers.length-1) {
				queryHeadsJsonString+=",";
				execHeadsJsonString+=",";

			}
		}
		queryHeadsJsonString+="]";
		execHeadsJsonString+="]";

		
		String parametersString=decodedQuery.split("<pars>")[1];
		parametersString=parametersString.split("</pars>")[0];
		String[] parameters=parametersString.split("<par ");
		System.out.println("List of Heads>>>"+parameters);

		
		
		String paramsString="[";
		
		for(int i =1;i<parameters.length;i++)
		{
			System.out.println("PARAMETER>>>"+parameters[i]);
			String paramType = parameters[i].split("fieldType=\"")[1];
			System.out.println("paramName "+i+">>>>>>>>>>>"+paramType);
			paramType = paramType.split("\"")[0];
			System.out.println("paramName "+i+">>>>>>>>>>>"+paramType);
			
			
			
			String paramDefault="1";
			if(paramType.equals("query")) {
				System.out.println("TYPE IS QUERY!!!!!");
			paramDefault=parameters[i].split("subQID=\"")[1];
			paramDefault = paramDefault.split("\"")[0];
			System.out.println("paramQueryDefault>>>>>>>>>>>"+paramDefault);
			}
			
			
			String id = parameters[i].split("id=\"")[1];
			id= id.split("\"")[0];
			System.out.println("id "+i+">>>>>>>>>>>"+id);
			
			
			String paramName = parameters[i].substring(parameters[i].indexOf("<![CDATA[") + 9,parameters[i].indexOf("]"));


			System.out.println("paramName "+i+">>>>>>>>>>>"+paramName);
			if(paramType.equals("query")) {

				
				paramsString+="{\"paramName\":\""+paramName+"\",\"paramType\":\""+paramType+"\",\"paramDefault\":\""+paramDefault+"\",\"paramId\":\""+id+"\"}";

			}else {
				paramsString+="{\"paramName\":\""+paramName+"\",\"paramType\":\""+paramType+"\",\"paramDefault\":\""+Integer.parseInt(paramDefault)+"\",\"paramId\":\""+id+"\"}";

			}

			query=query.replace("#"+id+"#", "["+paramName+"]");
			
			if(i!=parameters.length-1) {
				paramsString+=",";

			}
		}
		paramsString+="]";

		
		System.out.println("The Full Query is:>>>>>>"+query);
//		
//		JSONArray queryHeads=new JSONArray(new String(queryHeadsJsonString));
//		JSONArray execHeads=new JSONArray(new String(execHeadsJsonString));
//		JSONArray paramAdd=new JSONArray(new String(paramsString));

		
//        setSessionVal("paramAdd_"+sessionSerial,userId,paramAdd,sessionSerial,2);
//        setSessionVal("queryHeads_"+sessionSerial,userId,queryHeads,sessionSerial,2);
//        setSessionVal("execHeads_"+sessionSerial,userId,execHeads,sessionSerial,2);

        List<ObjectNode> queryData=new ArrayList<>();
        
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("query", query);
        objectNode.put("queryId", queryId);
        objectNode.put("queryParams", paramsString);
        objectNode.put("queryName", queryName);
        objectNode.put("execHeads",execHeadsJsonString);
        queryData.add(objectNode);
        
		return queryData;
	}

	
	
	
	
	
	@Override
	public List<ObjectNode> getQueryParamsLookup(long queryId) {
		byte[] encodedQuery=sqbQueryDetailsRepo.getQueryBlob(queryId);
		String decodedQuery=new String(Base64.decodeBase64(encodedQuery));
		List<ObjectNode> decodedSubQueryLookup=new ArrayList<>();

		String dataStoreId=decodedQuery.substring(decodedQuery.indexOf("storeId=\""+1),decodedQuery.indexOf("\">"));
		dataStoreId=dataStoreId.split("\"")[1];
				
		String queryName=decodedQuery.substring(decodedQuery.indexOf("<name>")+15,decodedQuery.indexOf("</name>")-3);
		
		System.out.println("Query Name is:>>>>"+queryName);
		
		String query = decodedQuery.substring(decodedQuery.indexOf("<sql>") + 14,decodedQuery.indexOf("</sql>")-3);
		
		System.out.println("Query is>>>>"+query);
		
		
		String headsString=decodedQuery.split("<LstHeads>")[1];
		headsString=headsString.split("</LstHeads>")[0];
		
		System.out.println("Heads String:>>>>>>>"+headsString);
		
		String[] headers=headsString.split("<EltHeads ");
		
		System.out.println("List of Heads>>>"+headers);
		
		String execHeadsJsonString="[";

		for(int i =1;i<headers.length;i++)
		{
			System.out.println("HEADER>>>"+headers[i]);
			String header = headers[i].split("dbName=\"")[1];
			System.out.println("header "+i+">>>>>>>>>>>"+header);

			header = header.split("\"")[0];
			System.out.println("header "+i+">>>>>>>>>>>"+header);
			
			String field = headers[i].split("fieldType=\"")[1];
			field = field.split("\"")[0];
			System.out.println("field "+i+">>>>>>>>>>>"+field);
			
			execHeadsJsonString+="{\"headerName\":\""+header+"\",\"field\":\""+header+"\"}";

			if(i!=headers.length-1) {
				execHeadsJsonString+=",";

			}
		}
		execHeadsJsonString+="]";

		
		String parametersString=decodedQuery.split("<pars>")[1];
		parametersString=parametersString.split("</pars>")[0];
		String[] parameters=parametersString.split("<par ");
		System.out.println("List of Heads>>>"+parameters);

		
		
		String paramsString="[";
		
		int countQuery=0;
		for(int i =1;i<parameters.length;i++) {
			String paramType = parameters[i].split("fieldType=\"")[1];
			paramType = paramType.split("\"")[0];
			if(!paramType.equals("query")) {
				countQuery++;
			}
		}
		
		for(int i =1;i<parameters.length;i++)
		{
			System.out.println("PARAMETERRR>>>"+parameters[i]);
			String paramType = parameters[i].split("fieldType=\"")[1];
			paramType = paramType.split("\"")[0];
			System.out.println("paramName "+i+">>>>>>>>>>>"+paramType);
			
			
			
			String paramDefault="1";
			if(paramType.equals("query")) {
				System.out.println("TYPE IS QUERY!!!!!");
			paramDefault=parameters[i].split("subQID=\"")[1];
			paramDefault = paramDefault.split("\"")[0];
			System.out.println("paramQueryDefault>>>>>>>>>>>"+paramDefault);
			
			decodedSubQueryLookup=getQueryParamsLookup(Long.parseLong(paramDefault));
			}
			
		
			String id = parameters[i].split("id=\"")[1];
			id= id.split("\"")[0];
			
			
			String paramName = parameters[i].substring(parameters[i].indexOf("<![CDATA[") + 9,parameters[i].indexOf("]"));

			System.out.println("paramName "+i+">>>>>>>>>>>"+paramName);
			
			if(paramType.equals("query")) {

				query=query.replace("#"+id+"#",decodedSubQueryLookup.get(0).get("query").asText());
				
			}else {
				paramsString+="{\"paramName\":\""+paramName+"\",\"paramType\":\""+paramType+"\",\"paramDefault\":\""+Integer.parseInt(paramDefault)+"\"}";
				
				if(i!=parameters.length-1 && countQuery>1) {
					paramsString+=",";
					countQuery--;
				}
				
			}
			query=query.replace("#"+id+"#", "["+paramName+"]");
			
			
		}
		
		if(decodedSubQueryLookup.size()!=0 && !decodedSubQueryLookup.get(0).get("queryParams").toString().equals("\"[]\"")) {
				paramsString+=decodedSubQueryLookup.get(0).get("queryParams").toString();
				System.out.println("PARAMS STRINGGG>>>>>"+paramsString);
		}
		
		paramsString+="]";

	
		System.out.println("The Full Query is:>>>>>>"+query);
		
    //    setSessionVal("paramAdd_"+sessionSerial,userId,paramAdd,sessionSerial,2);
  
        List<ObjectNode> queryData=new ArrayList<>();
        
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("query", query);
        objectNode.put("queryId", queryId);
        objectNode.put("queryParams", paramsString);
        objectNode.put("execHeads",execHeadsJsonString);
        
        queryData.add(objectNode);
        
		return queryData;
	}


	
	@Override
	public List<ObjectNode> getQueryList() { 

	//	return getJson(entityManagerR, "SELECT q.queryId as id, q.QUERY_NAME AS name from SQBQuery q where q.CREATION_DATE>TO_DATE('05-09-2023','DD-MM-YYYY') order by CREATION_DATE desc"); 
	return getJson(entityManagerR,"select q.queryId as id,q.QUERY_NAME as name from SQBQuery q where queryId in (select QBE_ID from SQBQueryType where QUERY_TYPE_CODE='3') and q.queryId like '5%' and q.CREATION_DATE>TO_DATE('05-09-2023','DD-MM-YYYY') order by CREATION_DATE desc");

	}
	@Override
    public int insertQueryType(Long queryId, String queryTypeString) {
		try {
			Date now1 = new Date();
			
			SQBQueryType sqbQueryType=new SQBQueryType();
			String [] test = queryTypeString.split(",");
			for(int i=0; i<test.length; i++) {
				Long queryType = Long.parseLong(test[i]);

				sqbQueryType.setQBE_ID(queryId);
				sqbQueryType.setQUERY_TYPE_CODE(queryType);
				sqbQueryType.setCREATION_DATE(now1);
				sqbQueryTypeRepo.save(sqbQueryType);	
				
			}
			
		return 1;
		
		}catch(Exception ex){
			return 0;
		}
    }
	
	
	@Override
    public List<ObjectNode> checkQueryTypeExists(Long queryId) {		

			List<ObjectNode> selectedTypes=getJson(entityManagerR,"SELECT a.QUERY_TYPE_CODE as queryTypeCode FROM SQBQueryType a WHERE a.QBE_ID="+queryId);

			return selectedTypes;
	
    }
	
	@Transactional
	@Override
    public int updateQueryType(Long queryId, String queryTypeString) {
		System.out.println("UPDATE MODe");
		try {
			sqbQueryTypeRepo.deleteQueryTypes(queryId);	
			
			if(queryTypeString!="none") {
			 @SuppressWarnings("unused")
			int typeUpdated=insertQueryType(queryId,queryTypeString);
			}
			
		return 1;
		
		}catch(Exception ex){
			return 0;
		}
    }
	
	@Override
	public List<ObjectNode> getSecurityUserQuery(Long queryId){
		try{
				String sql = "SELECT a.userId as USERID, a.userLogin as USERNAME " + 
							 "FROM UsmUserModel a " + 
							 "JOIN QbeAuthorizedUserModel b ON b.usrCode = a.userId " + 
							 "where b.qbeId="+queryId;

				List<ObjectNode> records = getJson(entityManagerR,sql);

				return records;
				} catch (Exception ex) {
						return null; 
				}
		
	}
	
	@Override
	public List<ObjectNode> getQueryListUsers(long qbeId){
		
		String sql="SELECT a.userId as id,a.userLogin as name FROM UsmUserModel a WHERE userId NOT IN(select usrCode from QbeAuthorizedUserModel where qbeId ="+qbeId+")"; 

		return getJson(entityManagerR,sql);
	}
	 @Transactional
	    public int addQbeAuthorizedUsers(QbeAuthorizedUserDto qbeAuthorizedUserDto) {
	        long qbeId = qbeAuthorizedUserDto.getQbeId();
	        String usrCodes = qbeAuthorizedUserDto.getUsrCode();
	        long createdBy = qbeAuthorizedUserDto.getCreatedBy();
	        byte hasUpdate = 1;
	        Date now = new Date();
	        String[] usrCodeArray = usrCodes.split(",");
	        for (String usrCodeStr : usrCodeArray) {
	            try {
	                long usrCode = Long.parseLong(usrCodeStr);
	                QbeAuthorizedUserModel user = new QbeAuthorizedUserModel();
	                user.setQbeId(qbeId);
	                user.setUsrCode(usrCode);
	                user.setCreationDate(now);
	                user.setCreatedBy(createdBy);
	                user.setHasUpdate(hasUpdate);
	                qbeAuthorizedUserRepo.save(user);
	            } catch (NumberFormatException e) {
	                e.printStackTrace(); // Log the error for debugging
	                return 0; // or handle the error as appropriate
	            }
	        }

	        return 1;
	    }
	 @Transactional
	 public void deleteQbeAuthorizedUsers(long qbeId, String usrCode) {
	        String[] usrCodeArray = usrCode.split(",");
	        for (String usrCodeStr : usrCodeArray) {
                long usrCodee = Long.parseLong(usrCodeStr);
                qbeAuthorizedUserRepo.deleteUsers(qbeId, usrCodee);
	        }

		 
	 }
	 
	 @Transactional
	 public void deleteQbeSecurityChanges(long qbeId)
	 {
		 qbeAuthorizedUserRepo.deleteQbeUsers(qbeId);  
	 }
	 
	 @Override
	 public List<ObjectNode> getQbeQueryCreatedBy(long qbeId)
	 {
		  ObjectMapper mapper = new ObjectMapper();
		  ObjectNode userNode = mapper.createObjectNode();
		 long createdBy = qbeUserReport.getQbeQueryCreatedBy(qbeId);
		 String userName = usmUserRepo.getUserName(createdBy);
		 int count = qbeAuthorizedUserRepo.getCountQbeUsers(qbeId);
		 if(count == 0) {
			 userNode.put("type","Public");
			 userNode.put("count",count);

		 }else if(count == 1) {
			 userNode.put("type","Private");
			 userNode.put("count",count);

			 
		 }else {
			 userNode.put("type","Selection");
			 userNode.put("count",count);

		 }
		   
		    userNode.put("userName", userName);

		    List<ObjectNode> result = new ArrayList<>();
		    result.add(userNode);


		    return result;
		}
	
 /////(EXPORT)this engine was used to set a table with query data to be transformed to postgres////
 @Override
		public void getAllQueryDataEngine() {
			List<QueryDecodeEngineModel> allQueryEngineData=new ArrayList<>();
			
			List<ObjectNode> allQueryEngineIds=getJson(entityManagerR,"SELECT a.QBE_ID as queryId FROM SQBQueryDetails a WHERE a.QBE_ID>=51132 AND a.QBE_ID!=51600 AND a.QBE_ID!=53128 order by a.QBE_ID asc");
			
			for(int j=0;j<allQueryEngineIds.size();j++) {
				System.out.println("QUERY ID>>>>>>>>>>>>"+allQueryEngineIds.get(j).get("queryId"));
				long queryId=allQueryEngineIds.get(j).get("queryId").asLong();
			
			
			byte[] encodedQuery=sqbQueryDetailsRepo.getQueryBlob(queryId);
			String decodedQuery=new String(Base64.decodeBase64(encodedQuery));
			
			//String dataStoreId=decodedQuery.substring(decodedQuery.indexOf("storeId=\""+1),decodedQuery.indexOf("\">"));
			//dataStoreId=dataStoreId.split("\"")[1];
			
			//System.out.println("DataStoreId:>>>>"+dataStoreId);		
			
			String queryName=decodedQuery.substring(decodedQuery.indexOf("<name>")+15,decodedQuery.indexOf("</name>")-3);
			
			//System.out.println("Query Name is:>>>>"+queryName);
			
			String query = decodedQuery.substring(decodedQuery.indexOf("<sql>") + 14,decodedQuery.indexOf("</sql>")-3);
			
			//System.out.println("Query is>>>>"+query);
			
			
			String headsString=decodedQuery.split("<LstHeads>")[1];
			headsString=headsString.split("</LstHeads>")[0];
			
			//System.out.println("Heads String:>>>>>>>"+headsString);
			
			String[] headers=headsString.split("<EltHeads ");
			
			//System.out.println("List of Heads>>>"+headers);
			
			String queryHeadsJsonString="[";
			String execHeadsJsonString="[";

			for(int i =1;i<headers.length;i++)
			{
				//System.out.println("HEADER>>>"+headers[i]);
				String header = headers[i].split("dbName=\"")[1];
				//System.out.println("header "+i+">>>>>>>>>>>"+header);

				header = header.split("\"")[0];
			//	System.out.println("header "+i+">>>>>>>>>>>"+header);
				
				String field = headers[i].split("fieldType=\"")[1];
				field = field.split("\"")[0];
			//	System.out.println("field "+i+">>>>>>>>>>>"+field);
				
				queryHeadsJsonString+="{\"headerName\":\""+header+"\",\"field\":\""+field+"\",\"businessName\":\""+header+"\"}";
				execHeadsJsonString+="{\"headerName\":\""+header+"\",\"field\":\""+header+"\"}";

				if(i!=headers.length-1) {
					queryHeadsJsonString+=",";
					execHeadsJsonString+=",";

				}
			}
			queryHeadsJsonString+="]";
			execHeadsJsonString+="]";

			
			String parametersString=decodedQuery.split("<pars>")[1];
			parametersString=parametersString.split("</pars>")[0];
			String[] parameters=parametersString.split("<par ");
			//System.out.println("List of Heads>>>"+parameters);

			
			
			String paramsString="[";
			
			for(int i =1;i<parameters.length;i++)
			{
				//System.out.println("PARAMETER>>>"+parameters[i]);
				String paramType = parameters[i].split("fieldType=\"")[1];
				//System.out.println("paramName "+i+">>>>>>>>>>>"+paramType);
				paramType = paramType.split("\"")[0];
				//System.out.println("paramName "+i+">>>>>>>>>>>"+paramType);
				
				
				
				String paramDefault="1";
				if(paramType.equals("query")) {
					//System.out.println("TYPE IS QUERY!!!!!");
				paramDefault=parameters[i].split("subQID=\"")[1];
				paramDefault = paramDefault.split("\"")[0];
				//System.out.println("paramQueryDefault>>>>>>>>>>>"+paramDefault);
				}
				
				
				String id = parameters[i].split("id=\"")[1];
				id= id.split("\"")[0];
				//System.out.println("id "+i+">>>>>>>>>>>"+id);
				
				
				String paramName = parameters[i].substring(parameters[i].indexOf("<![CDATA[") + 9,parameters[i].indexOf("]"));


				//System.out.println("paramName "+i+">>>>>>>>>>>"+paramName);
				if(paramType.equals("query")) {

					
					paramsString+="{\"paramName\":\""+paramName+"\",\"paramType\":\""+paramType+"\",\"paramDefault\":\""+paramDefault+"\",\"paramId\":\""+id+"\"}";

				}else {
					paramsString+="{\"paramName\":\""+paramName+"\",\"paramType\":\""+paramType+"\",\"paramDefault\":\""+Integer.parseInt(paramDefault)+"\",\"paramId\":\""+id+"\"}";

				}

				query=query.replace("#"+id+"#", "["+paramName+"]");
				
				if(i!=parameters.length-1) {
					paramsString+=",";

				}
			}
			paramsString+="]";

			
			QueryDecodeEngineModel queryObject=new QueryDecodeEngineModel();
			queryObject.setQueryId(queryId);
			queryObject.setQueryName(queryName);
			queryObject.setExecHeads(execHeadsJsonString);
			queryObject.setQueryParams(paramsString);
			queryObject.setQuery(query);
	        
			allQueryEngineData.add(queryObject);
			}
			queryDecodeEngineRepo.saveAll(allQueryEngineData);
		}
	 

		////////////this engine is recompiled to send the queries to postgres

		@Override

		public void getBackQueriesEngine() {

			

		}

		
}
	

		

