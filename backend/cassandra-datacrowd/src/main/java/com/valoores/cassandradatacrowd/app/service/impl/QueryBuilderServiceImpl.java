package com.valoores.cassandradatacrowd.app.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.cassandra.core.CassandraTemplate;

import org.springframework.stereotype.Service;

import com.valoores.cassandradatacrowd.app.dto.CustomResponse;
import com.valoores.cassandradatacrowd.app.model.Location;
import com.valoores.cassandradatacrowd.app.repository.IQueryBuilderRepository;
import com.valoores.cassandradatacrowd.app.service.IQueryBuilderService;

@Service
public class QueryBuilderServiceImpl implements IQueryBuilderService {

  @SuppressWarnings("unused")
@Autowired
  private IQueryBuilderRepository queryBuilderRepo;

  @Autowired
  private CassandraTemplate template;

  @Value("${application.repository.urlPath}")
  private String urlPath;

  @Override
  public CustomResponse GetQueryBuilderResult(String attribute, String whereCondition, String id,JSONArray finalAOIArray,String tableName,String tableIndex,String LuceneQuery ) {
	  
	  
	 System.out.println("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT"); 
	 System.out.println("finalAOIArray><"+finalAOIArray); 
	 System.out.println("LuceneQuery in impl><"+LuceneQuery); 
    CustomResponse resp = CustomResponse.builder().build();
    try {

      String fileName = "/query_builder_" + id + ".txt";
      String fileData = "/query_builder_Data" + id + ".txt";

      String fullPath = urlPath;
      String query = "";
      File directory = new File(fullPath);
      directory.mkdirs();
      FileWriter file_1;
      FileWriter file_2;

      file_1 = new FileWriter(fullPath + fileName);
      file_2 = new FileWriter(fullPath + fileData);
      File file_0 = new File(fullPath + fileData); 

      @SuppressWarnings("resource")
	BufferedWriter buffer_1 = new BufferedWriter(file_1);
      @SuppressWarnings("resource")
	BufferedWriter buffer_2 = new BufferedWriter(file_2);
      String lucenceQry = "";

      lucenceQry=LuceneQuery;

      System.out.println("table Name ==== "+tableName);


      
      
      if(!finalAOIArray.isEmpty())
      {
    	  System.out.println("whereCondition => "+whereCondition);
    	  System.out.println("lucenceQry => "+lucenceQry);

    	  if(!whereCondition.equals(""))
    	  {
    		  whereCondition = "AND "+whereCondition;
    	  }
    		  
    	  
	      for(int i = 0; i < finalAOIArray.length() ; i++)
	      {

	    	  System.out.println(" final query "+i+" = "+finalAOIArray.get(i));
	    	  
//	    	  if(needAllowFiltering>0) {
//	    		  
//	    		  if(!lucenceQry.equals(""))
//	        	  {
//	    			  query = "SELECT " + attribute + " " +
//			        	         "FROM  "+tableName+"  " + 
//			        	         "where expr("+tableIndex+"," +
//			 		  	  	            " '{" +
//					  	  	            "   \"filter\":[" +finalAOIArray.get(i)+","+lucenceQry+
//				    	  	            "   ]" +
//				    	  	            "}') "+
//			        	         " " + whereCondition + "  ALLOW FILTERING";
//	        	  }else {
//	        		  query = "SELECT " + attribute + " " +
//			        	         "FROM  "+tableName+"  " + 
//			        	         "where expr("+tableIndex+"," +
//			 		  	  	            " '{" +
//					  	  	            "   \"filter\":[" +finalAOIArray.get(i)+
//				    	  	            "   ]" +
//				    	  	            "}') "+
//			        	         " " + whereCondition + "  ALLOW FILTERING";
//	        	  }
//	    		  
//		          
//	    	  }else {
	    		  if(!lucenceQry.equals(""))
	        	  {
	        		  if(!whereCondition.equals("")) {

		    			   query = "SELECT " + attribute + " " +
				        	         "FROM  "+tableName+"  " + 
				        	         "where expr("+tableIndex+"," +
				 		  	  	            " '{" +
						  	  	            "   \"filter\":[" +finalAOIArray.get(i)+","+lucenceQry+
					    	  	            "   ]" +
					    	  	            "}') "+
				        	         " " + whereCondition  + "  ALLOW FILTERING"; 
	        		  }else {

		    			   query = "SELECT " + attribute + " " +
				        	         "FROM  "+tableName+"  " + 
				        	         "where expr("+tableIndex+"," +
				 		  	  	            " '{" +
						  	  	            "   \"filter\":[" +finalAOIArray.get(i)+","+lucenceQry+
					    	  	            "   ]" +
					    	  	            "}') "+
				        	         " " + whereCondition; 
	        		  }

	    			  
	    			  
	        	  }else {
	        		  if(!whereCondition.equals("")) {
	        			  query = "SELECT " + attribute + " " +
				        	         "FROM  "+tableName+"  " + 
				        	         "where expr("+tableIndex+"," +
				 		  	  	            " '{" +
						  	  	            "   \"filter\":[" +finalAOIArray.get(i)+
					    	  	            "   ]" +
					    	  	            "}') "+
				        	         " " + whereCondition  + "  ALLOW FILTERING"; 
	        		  }else {
	        			  query = "SELECT " + attribute + " " +
				        	         "FROM  "+tableName+"  " + 
				        	         "where expr("+tableIndex+"," +
				 		  	  	            " '{" +
						  	  	            "   \"filter\":[" +finalAOIArray.get(i)+
					    	  	            "   ]" +
					    	  	            "}') "+
				        	         " " + whereCondition ; 
	        		  }
	        		 
	        	  }
	    	   
	    	  //}
	    	
	           
	           
	           byte[] loc = template.select(query, Location.class).toString().getBytes();

	            	
 	              if (file_0.length() == 0) {
 	  	           buffer_2.write(new String(loc, StandardCharsets.UTF_8));
	              }
	              else
	              {
	 	  	           buffer_2.write("," +new String(loc, StandardCharsets.UTF_8));
	              }
	           buffer_2.flush();
	           
	      }
	      
	
      }
      else
      {
    	  
    	  System.out.println(" final query whereCondition = "+whereCondition);

   
    	  
    	  
    	  if(!lucenceQry.equals(""))
    	  {
    		  if(!whereCondition.equals("") && !whereCondition.equals(" ") )
        	  {
    			  System.out.println("whereCondition 1111  "+whereCondition.length());
    			  query = "SELECT " + attribute + " " +
 	        	         "FROM  "+tableName+"  " + 
 	        	         "where expr("+tableIndex+"," +
 	 		  	  	            " '{" +
 			  	  	            "   \"filter\":[" +lucenceQry+
 		    	  	            "   ]" +
 		    	  	            "}') "+
 	        	         "AND " + whereCondition +"  ALLOW FILTERING"; 
        	  }else {
    			  System.out.println("whereCondition 2222"+whereCondition);

        		  query = "SELECT " + attribute + " " +
  	        	         "FROM  "+tableName+"  " + 
  	        	         "where expr("+tableIndex+"," +
  	 		  	  	            " '{" +
  			  	  	            "   \"filter\":[" +lucenceQry+
  		    	  	            "   ]" +
  		    	  	            "}') "+
  	        	         ""; 
        	  }
//        
    	
    		  
    	  }else {
    	 	  if(!whereCondition.equals(""))
        	  {
//        
        			  whereCondition = "where "+whereCondition +"  ALLOW FILTERING";

        		  
        		
        		  System.out.println(" whereCondition >> 22  "+whereCondition);

        	  }    		   query = "SELECT " + attribute + " " +
    	     	         "FROM "+tableName+"  " +
    	     	          whereCondition +" ";
    	          
    	  }
    	  
    	  
    
    	  
    	  
    	  
    	  System.out.println("query ---- "+query);
    	  
       
          byte[] loc = template.select(query, Location.class).toString().getBytes();
    	  System.out.println("loc ---- "+template.select(query, Location.class).toString());

           if (file_0.length() == 0) {
	           buffer_2.write(new String(loc, StandardCharsets.UTF_8));
          }
          else
          {
	  	           buffer_2.write("," +new String(loc, StandardCharsets.UTF_8));
          }
       buffer_2.flush();
          
      }
      
      
      File f = new File(fullPath, fileData);
      @SuppressWarnings("resource")
	BufferedReader reader = new BufferedReader(new FileReader(f));
	  if(reader.ready())
	    {
	    byte[] contentFile = reader.readLine().toString().getBytes();
	        buffer_1.write("[" + new String(contentFile, StandardCharsets.UTF_8) + "]");
	        buffer_1.flush();
	    }
      
      System.out.println(" final  QUERY >>> "+query);

      System.out.println("Executed querybuilder =" + query);

      
      
      System.out.println(fileName + "created successfully");
    } catch (Exception e) {
      e.printStackTrace();
    }

    return resp;
  }

//  public String getShapeApi(String id) {
//
//    RestTemplate restTemplate = new RestTemplate();
//    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//
//    String object = "{\r\n" +
//      "			\"channelCredentials\": {\r\n" +
//      "				\"User\": \"STPChannel_U1\",\r\n" +
//      "				\"Password\": \"p@ssw0rd01\"\r\n" +
//      "			},\r\n" +
//      "			\"apiChannel\": \"STP Channel\",\r\n" +
//      "			\"shapeId\": " + id + ",\r\n" +
//      "			\"methodId\": \"6\"\r\n" +
//      "		}";
//
//    JSONObject obj = new JSONObject(object);
//    String uri = "https://172.16.30.230:7002/ValooresAPI/ExecutiveMethod/Invoke";
//    HttpHeaders header = new HttpHeaders();
//    ResponseEntity < JSONObject > result = restTemplate.postForEntity(uri, obj, JSONObject.class);
//    System.out.println(" result >> " + result);
//
//    return object;
//  }
  
  

  public String getQuery(String attribute, String operator, String value, String type, Long Uppervalue,Long Lowervalue) {
    String query = "";

    switch (operator) {
    case "in":
    	
      String[] result = value.split(",");
      String dd = "";

      for (int i = 0; i < result.length; i++) {
    	    if (i == 0) {
    	        dd = "\"" + result[i] + "\"";
    	    } else {
    	        dd = dd + ',' + "\"" + result[i] + "\"";
    	    }
    	}
	  System.out.println("attribute in><><>< " + attribute);

	  query = "{ \"type\": \"contains\", \"field\":\""+attribute+"\", \"values\":  [ "+dd+" ] }";
	  System.out.println(" query >> " + query);



      break;
      
    case "<>":
  	  System.out.println("attribute in><><>< " + attribute);

//  	  query = "{ \"type\": \"contains\", \"field\": "+attribute+", \"values\":  [ "+value+" ] }";
 	 query = "{\"type\":\"range\", \"field\":\""+attribute+"\", \"lower\": "+Lowervalue+", \"upper\": "+Uppervalue+", \"include_lower\": true, \"include_upper\": true, \"doc_values\": true}";

    default:
    	

   }
    
    System.out.println("query >>>>>>>>>>>>>>>>> "+query );
    
    return query;
  }
  

}