package com.valoores.cassandradatacrowd.app.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valoores.cassandradatacrowd.app.dto.CustomResponse;
import com.valoores.cassandradatacrowd.app.service.IQueryBuilderService;
import com.valoores.cassandradatacrowd.common.service.constructQueryService;

/**
 * @author marcelino.a
 */

@RestController
@RequestMapping("/api")
public class QueryBuilderRestController {

  @Autowired
  private IQueryBuilderService queryBuilderService;
  
  @Autowired
  private constructQueryService constructQueryService;
  
  @Autowired
  private CassandraTemplate template;

  @SuppressWarnings("unchecked")
@PostMapping("/queryBuilder")
  public CustomResponse geAllData(@RequestBody String queryBuilder) {
	 
	  CustomResponse resp = CustomResponse.builder().build();

    try {
  	  System.out.println("queryBuilder object >>> "+queryBuilder);



      JSONObject obj = new JSONObject(queryBuilder);

      String attributeName = "";
      String operator = "";
      String value = "";
      String whereCondition = "";
      String id = obj.getString("id");
      String tableName = obj.getString("tableName");
      String keyspace_name = obj.getString("keyspace_name");
      String tableIndex = obj.getString("tableIndex");
   	 
   	 JSONObject lucenefilter = null;
   	 String LuceneQuery="";
	  System.out.println("lucenefilter>>>"+lucenefilter);

//      JSONArray lucenefilter = new JSONArray(obj.getJSONObject("lucenIndex").getJSONArray("attributes"));
//	  System.out.println("lucenefilter>>>"+lucenefilter);

      String type = "";
      String attribute = "";
      @SuppressWarnings("unused")
	String AOIQuery = "";
      Integer cnt = 0;
	  JSONArray finalAOIArray = new JSONArray(); 
	  boolean isPlace = false; 
	  boolean Frstcondition = false; 

	  @SuppressWarnings("unused")
	Integer needAllowFiltering=0;

//	  System.out.println(" queryBuilder object >>> "+queryBuilder);
	  System.out.println("tableName>>>"+tableName);
	  System.out.println("tableName>>>"+keyspace_name);

	  System.out.println(" obj  >>> "+obj);


	  if(tableName.equals("LOCATION_MAIN_DATA")) {
		  tableName="LOC_LOCATION_MAIN_DATA";
	  }
	  
	  
	  String[] tables= tableName.split(",");
	  System.out.println("tables>>>"+tables);
	  Integer tableNotExists=0;
	      for (int l=0;l < tables.length; l++) {
	    	  System.out.println("tables[l]>>>"+tables[l]);

	      //}

	  String cqlsh = "SELECT count(1) FROM system_schema.tables where table_name = '"+tables[l]+ "' allow filtering";
	  Integer tableExists =   template.selectOne(cqlsh, Integer.class);
   	  System.out.println(" tableExists >>> "+tableExists);
	  if(tableExists>0)
	  {
	  }else {
    	  System.out.println("table "+tables[l]+" doesn't exist");
    	  tableNotExists++;
      }
	      }
	   	  System.out.println(" tableNotExists >>> "+tableNotExists);

		  if(tableNotExists<=0) {
			  
		

	  if(tableName.equals("LOCATION_MAIN_DATA")) {
		  tableName="LOC_LOCATION_MAIN_DATA";
	  }
	  
	  
	  
      for (int i = 0; i < obj.getJSONArray("attribute").length(); i++) {

    	  if(i==0)
    	  {
    		  attribute +=  obj.getJSONArray("attribute").get(i) ;
    	  }
    	  else
    	  {
    		  attribute +=  ","  + obj.getJSONArray("attribute").get(i) ;
    	  }
    	  
      }
      //test1 check if there is need for allow filtering
      
//	  System.out.println("attribute>>>"+attribute);
//	  System.out.println("attribute >>>"+attribute.split(","));
//	  String[] attributes=attribute.split(",");
//      for (int m=0;m < attributes.length; m++) {
//    	  System.out.println("attribute>>>"+attributes[m]);
//      	  String cqlsh = "SELECT kind FROM system_schema.columns where keyspace_name = '"+keyspace_name+ "' AND table_name = '"+tableName+ "'  AND column_name ='"+attributes[m]+"'  allow filtering";//    	  Integer tableExists =   template.selectOne(cqlsh, Integer.class);
//    	  String kind =   template.selectOne(cqlsh, String.class);
//       	  System.out.println(" kind >>> "+kind);
//       	  if( kind.equals("clustering") || kind.equals("partition_key")) {
//           	  System.out.println(attributes[m]+" is primary key >>> ");
//
//
//       	  }else {
//           	  System.out.println(" there is need for allow filtering in the query >>> ");
//           	needAllowFiltering++;
//       	  }
//
//    	  
//      }



	  
      
      
      if (obj.length() > 0) {
    	  
    	  //check columns of lucene index
    	  String cqlsh2 = "\r\n" + 
    		  		"SELECT  options FROM system_schema.indexes\r\n" + 
    		  		"WHERE keyspace_name = '"+keyspace_name+"'\r\n" + 
    		  		"  AND table_name = '"+tableName+"'\r\n" + 
    		  		"  AND index_name = '"+tableIndex+"'" + 
    		  		" allow filtering";
			Map<String, Object> resultMap = template.selectOne(cqlsh2, Map.class);
    		  System.out.println("Result Map: " + resultMap);
			Map<String, Object> optionsMap = (Map<String, Object>) resultMap.get("options");
    		  System.out.println("optionsMap: " + optionsMap);
    		  System.out.println("schema: " + optionsMap.get("schema"));
    		  System.out.println("schema type: " + optionsMap.get("schema").getClass().getSimpleName());


    		// Access the 'schema' as a JSON string
    		  String schemaJsonString = (String) optionsMap.get("schema");
    		  System.out.println("Schema JSON String: " + schemaJsonString);
    		  JSONObject x=new JSONObject(schemaJsonString);
    		  System.out.println("x: " + x);
    		  System.out.println("x fields: " + x.get("fields"));
    		  String fieldsString = (String)  x.get("fields").toString();

    	

    	      // Parse JSON string into a Map
    	      ObjectMapper objectMapper = new ObjectMapper();
    	      Map<String, Object> jsonObject = null;
  			jsonObject = objectMapper.readValue(fieldsString, Map.class);
  			
  			

    	
  		  System.out.println("whereCondition typeeee : "+obj.getJSONArray("whereCondition").getClass().getSimpleName());

  		  
  		String filteredArray = filterJSONArray(obj.getJSONArray("whereCondition"));
        System.out.println("filteredArray>>>"+filteredArray.toString());
        System.out.println("after filteredArray  LuceneQuery>>>"+LuceneQuery);
        if(LuceneQuery.isEmpty()) {
            System.out.println("is emtyyyyyyyyyyy");
            LuceneQuery=filteredArray.toString();
        }else {
            LuceneQuery+=","+filteredArray.toString();
        }


  		  
        for (int i = 0; i < obj.getJSONArray("whereCondition").length(); i++) {

          type = obj.getJSONArray("whereCondition").getJSONObject(i).get("value").getClass().getSimpleName();
          attributeName = obj.getJSONArray("whereCondition").getJSONObject(i).getString("attributeName");
          operator = obj.getJSONArray("whereCondition").getJSONObject(i).getString("operator");

          if(attributeName.equals("place"))
          {
        	  cnt++;
          }
          System.out.println(" type >> "+type );

          
            if (type.equals("Integer")) { 
              value = String.valueOf(obj.getJSONArray("whereCondition").getJSONObject(i).getInt("value"));
            } else if (type.equals("Long")) {
              value = String.valueOf(obj.getJSONArray("whereCondition").getJSONObject(i).getLong("value"));
            }else if (type.equals("BigDecimal")) {
                value = String.valueOf(obj.getJSONArray("whereCondition").getJSONObject(i).getBigDecimal("value"));
            }else if (type.equals("JSONArray")) {
                value = String.valueOf(obj.getJSONArray("whereCondition").getJSONObject(i).getJSONArray("value"));
                } else {
              value = obj.getJSONArray("whereCondition").getJSONObject(i).getString("value");
            }
          
          System.out.println(" attribute >> " + i + " = " + attributeName);
          System.out.println(" operator >> " + i + " = " + operator);
          System.out.println(" value >> " + i + " = " + value);
          
          
       

          if(attributeName.equals("place"))
          {
              System.out.println(" is place >> ");

              isPlace = true;
        	  
        	  JSONArray aoiArray = new JSONArray(value); 
        	  for(int h = 0 ; h < aoiArray.length(); h ++ )
        	  {

//        		  getQuery(attributeName, operator, value, type)
//    	      	  JSONArray shapeObj = new JSONArray(value);
                  System.out.println(" isPlace  getJSONObject Coordinates>>"+aoiArray.getJSONObject(h));

            	  
    	      	  String qry  = constructQueryService.constructQuery(aoiArray.getJSONObject(h), "shape");
    	      	  System.out.println("qry"+qry);
    	      	 
//        		  AOIQuery += "  expr(lucene_index," +
//		  	  	            " '{" +
//		  	  	            "   \"filter\":[" +qry+
//	    	  	            "   ]" +
//	    	  	            "}')  ";
            	  
            	  finalAOIArray.put(qry);
        	  }
        	  

          }
          else
          {
        	
      	   
        	  
        	  if(!attributeName.equals("")) {
		          if (i == 0 ) {
		              System.out.println("111111111 >> ");
		              System.out.println("isPlace<><>"+isPlace);
		              
		              
		              
		              // Check if the key exists in the object
		      	      if (jsonObject.containsKey(attributeName)) {
		      	          System.out.println(attributeName + " belongs to the object.");

		      	    	  if(LuceneQuery.isEmpty()) {
		      	            System.out.println("isemptyyyy");
			      	    	  LuceneQuery += "  " + getLuceneQuery(attributeName, operator, value, type);

		      	        }else {
		      	        	System.out.println("nottttemptyyyy"+LuceneQuery);

		      	          if(!operator.equals(">") && !operator.equals("<")) {
		      	        		System.out.println("toavoidcomma3333"+operator);
			      	        	LuceneQuery+=","+getLuceneQuery(attributeName, operator, value, type);

		      	        	}
		      	        }

		      	          System.out.println("LuceneQuery"+LuceneQuery);

		      	      } else {
		      	          System.out.println(attribute + " does not belong to the object.");
				            whereCondition += "  " + getQuery(attributeName, operator, value, type);

		      	      }
		      	      
		      	      

		          } else {
		        	  
		        	  if(isPlace)
		        	  {
			              System.out.println(" 22222 isPlace >> ");

		  	            whereCondition += " " + getQuery(attributeName, operator, value, type);
		  	          isPlace = false;
		        	  }
		        	  else
		        	  {
			              System.out.println(" 33333333 else");
		      	          System.out.println("LuceneQuery before"+LuceneQuery);

			              // Check if the key exists in the object
			      	      if (jsonObject.containsKey(attributeName)) {
//			      	    	LuceneQuery += "," + getLuceneQuery(attributeName, operator, value, type);
			      	          System.out.println(attributeName + " belongs to the object.");
			      	          
			      	        if(!operator.equals(">") && !operator.equals("<")) {
		      	        		System.out.println("toavoidcomma3333"+operator);
			      	        	LuceneQuery+=","+getLuceneQuery(attributeName, operator, value, type);

		      	        	}
			      	          System.out.println("LuceneQuery after"+LuceneQuery);

			      	      } else {
			      	    	System.out.println(attributeName + " does not belong to the object.");
			      	    	System.out.println("Frstcondition"+Frstcondition);

			      	    	  if(Frstcondition==false) {
					  	            whereCondition += " " + getQuery(attributeName, operator, value, type);
					  	          Frstcondition=true;
			      	    	  }else {
					  	            whereCondition += " AND " + getQuery(attributeName, operator, value, type);

			      	    	  }

			      	      }
//			  	            whereCondition += " AND " + getQuery(attributeName, operator, value, type);


		        	  }
		          }
        	  }
	      }
        }

//        if(cnt>0)
//        {
//        	if(!whereCondition.equals(""))
//        	{
//        		whereCondition = " AND " + whereCondition;
//        	}
//        	 whereCondition = "WHERE "+AOIQuery +whereCondition;
//        }
//        else
//        {
//        	 whereCondition = "WHERE "+ whereCondition;
//        }
//        
      }
      System.out.println(" WHERE CONDITION QUERY = " + whereCondition);
      queryBuilderService.GetQueryBuilderResult(attribute,whereCondition, id,finalAOIArray,tableName,tableIndex,LuceneQuery);
	  resp.setStatus("success");
    }else {
    	  resp.setStatus("failed");

    }
	
	      
	      } catch (Exception e) {
  	  resp.setStatus("failed");
      e.printStackTrace();
    }
    return resp;

  }

  public String getQuery(String attribute, String operator, String value, String type) {
    String query = "";

    switch (operator) {
    case "in":

      if (attribute.equals("service_provider_id")) {
    	  
        query = attribute + " " + operator + " (" + value + ")";
        
//      }else if (attribute.equals("AOI")) {
//    	  
//       	  JSONArray shapeObj = new JSONArray(value);
//       	  query = constructQueryService.constructQuery(shapeObj, "shape");
    	  
      }else if (type.equals("String")) {
     	 System.out.println("attribte name  else");

        String[] result = value.split(",");
        String dd = "";

        for (int i = 0; i < result.length; i++) {
          if (i == 0) {
            dd = "'" + result[i] + "'";
          } else {
            dd = dd + ',' + "'" + result[i] + "'";
          }

        }
// 	  query = "{ \"type\": \"contains\", \"field\": "+attribute+", \"values\":  [ "+dd+" ] }";

        query = attribute + " " + operator + " (" + dd + ")";
      }

      break;
    default:
    	
     if(attribute.equals("usage_timeframe")) {
    	 System.out.println("usage_timeframe<><>><><>"+query);

         String Date = value;
         @SuppressWarnings("deprecation")
		Date Dateparse = new Date(Date);
         long Datemillis = Dateparse.getTime();
         @SuppressWarnings("unused")
		long lowerdate=0;
         @SuppressWarnings("unused")
		long Upperdate=0;
//test1
//         if(operator.equals("<")) {
//        	 System.out.println("lower");
//        	 
////        	 query = "{\"type\":\"range\", \"field\": \"usage_timeframe\", \"lower\": "+Datemillis+", \"upper\": 1703714400000, \"include_lower\": true, \"include_upper\": true, \"doc_values\": true}";
////        	 
////        	 System.out.println("query upper lower<><>><><>"+query);
//        	  String Date = value;
//              Date Dateparse = new Date(Date);
//              long Datemillis = Dateparse.getTime();
//        	  lowerdate=Datemillis;
//
//         }else if(operator.equals(">")) {
//        	 
//       	  String Date = value;
//          Date Dateparse = new Date(Date);
//          long Datemillis = Dateparse.getTime();
//          Upperdate=Datemillis;
//    	  
//          System.out.println("upper");
//        	 
//
//
//         }
         
//    	 query = "{\"type\":\"range\", \"field\": \"usage_timeframe\", \"lower\": "+lowerdate+", \"upper\": "+Upperdate+", \"include_lower\": true, \"include_upper\": true, \"doc_values\": true}";
    	 
    	 System.out.println("query upper lower<><>><><>"+query);
       query = attribute + " " + operator + " " + Datemillis + " ";
         //     }else if (attribute.equals("AOI")) {
//    	 
//      	  JSONArray shapeObj = new JSONArray(value);
//      	  query = constructQueryService.constructQuery(shapeObj, "shape");

     } 

     else
     {
    	 
         if (type.equals("String")) {
             query = attribute + " " + operator + " '" + value + "'";
           } else if (type.equals("Integer")) {
             query = attribute + " " + operator + " " + Integer.parseInt(value);
           } else if (type.equals("Long")) {
               query = attribute + " " + operator + " " + Long.parseLong(value);
           } else if (type.equals("BigDecimal")) {
               query = attribute + " " + operator + " " + Double.parseDouble(value);
           } 
     }


    }
    
    System.out.println("query >>>>>>>>>>>>>>>>> "+query );
    
    return query;
  }
  
  

  @SuppressWarnings("unchecked")
public Integer checkColumnInluceneIndex(String keyspace_name,String tableName, String tableIndex,String attribute){
	  Integer needLuceneIndex=0;
	  String cqlsh2 = "\r\n" + 
		  		"SELECT  options FROM system_schema.indexes\r\n" + 
		  		"WHERE keyspace_name = '"+keyspace_name+"'\r\n" + 
		  		"  AND table_name = '"+tableName+"'\r\n" + 
		  		"  AND index_name = '"+tableIndex+"'" + 
		  		" allow filtering";
		  Map<String, Object> resultMap = template.selectOne(cqlsh2, Map.class);
		  System.out.println("Result Map: " + resultMap);
		  Map<String, Object> optionsMap = (Map<String, Object>) resultMap.get("options");
		  System.out.println("optionsMap: " + optionsMap);
		  System.out.println("schema: " + optionsMap.get("schema"));
		  System.out.println("schema type: " + optionsMap.get("schema").getClass().getSimpleName());


		// Access the 'schema' as a JSON string
		  String schemaJsonString = (String) optionsMap.get("schema");
		  System.out.println("Schema JSON String: " + schemaJsonString);
		  JSONObject x=new JSONObject(schemaJsonString);
		  System.out.println("x: " + x);
		  System.out.println("x fields: " + x.get("fields"));
		  String fieldsString = (String)  x.get("fields").toString();

	

	      // Parse JSON string into a Map
	      ObjectMapper objectMapper = new ObjectMapper();
	      Map<String, Object> jsonObject = null;
		try {
			jsonObject = objectMapper.readValue(fieldsString, Map.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	      // Check if the key exists in the object
	      if (jsonObject.containsKey(attribute)) {
	          System.out.println(attribute + " belongs to the object.");
	          needLuceneIndex++;
	      } else {
	          System.out.println(attribute + " does not belong to the object.");
	      }
	      
		return needLuceneIndex;
  }
  
  

  public String getLuceneQuery(String attribute, String operator, String value, String type) {
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
//      
//    case "<>":
//  	  System.out.println("attribute in><><>< " + attribute);
//
////  	  query = "{ \"type\": \"contains\", \"field\": "+attribute+", \"values\":  [ "+value+" ] }";
//// 	 query = "{\"type\":\"range\", \"field\":\""+attribute+"\", \"lower\": "+Lowervalue+", \"upper\": "+Uppervalue+", \"include_lower\": true, \"include_upper\": true, \"doc_values\": true}";
//
   default:
    	

   }
    
    System.out.println("query >>>>>>>>>>>>>>>>> "+query );
    
    return query;
  }
  
  private String filterJSONArray(JSONArray jsonArray) throws JsonMappingException, JsonProcessingException {
	  System.out.println("jsonArray<>"+jsonArray);

	 
	  
//	  String jsonString = "[{\"attributeName\":\"device_id\",\"value\":\"a78752ab-4b78-44fd-a904-951adf370e4f\",\"operator\":\"in\"},{\"attributeName\":\"usage_timeframe\",\"value\":\"01/09/2023\",\"operator\":\"<\"},{\"attributeName\":\"usage_timeframe\",\"value\":\"01/09/2024\",\"operator\":\">\"}]";
	  String jsonString=jsonArray.toString();
      ObjectMapper objectMapper = new ObjectMapper();

      // Deserialize JSON array into a list of JsonNode objects
      List<JsonNode> nodeList = objectMapper.readValue(jsonString, objectMapper.getTypeFactory().constructCollectionType(List.class, JsonNode.class));

      // Filter the list based on the "< or >" operator
      List<JsonNode> filteredList = nodeList.stream()
              .filter(node -> node.has("attributeName") && "<".equals(node.get("operator").asText()) ||">".equals(node.get("operator").asText())).collect(Collectors.toList());

      // Print the filtered list
      filteredList.forEach(System.out::println);
      
      
      @SuppressWarnings("unused")
	String lowerDate = "";
      @SuppressWarnings("unused")
	String upperDate = "";
      String queryfinal="";

      Map<String, Map<String, String>> combinedQueriesMap = new HashMap<>();

      for (JsonNode node : filteredList) {
          String attributeName = node.get("attributeName").asText();
          String operator = node.get("operator").asText();
          String value = node.get("value").asText();

          combinedQueriesMap.compute(attributeName, (key, existingQuery) -> {
              if (existingQuery == null) {
                  existingQuery = new HashMap<>();
                  existingQuery.put("type", "range");
                  existingQuery.put("field", attributeName);
                  existingQuery.put("doc_values", "true");
              }

              if ("<".equals(operator)) {
                  existingQuery.put("lower", value);
                  existingQuery.put("include_lower", "true");
              } else if (">".equals(operator)) {
                  existingQuery.put("upper", value);
                  existingQuery.put("include_upper", "true");
              }

              return existingQuery;
          });
      }

      List<String> combinedQueries = combinedQueriesMap.values().stream()
              .map(queryAttributes -> "{" +
                      queryAttributes.entrySet().stream()
                              .map(entry -> "\"" + entry.getKey() + "\": \"" + entry.getValue() + "\"")
                              .collect(Collectors.joining(", ")) +
                      "}")
              .collect(Collectors.toList());

      String combinedQueryResult = String.join(",", combinedQueries);
      System.out.println("Combined Queries: " + combinedQueryResult);
      
	return queryfinal;

  }
  
 

}