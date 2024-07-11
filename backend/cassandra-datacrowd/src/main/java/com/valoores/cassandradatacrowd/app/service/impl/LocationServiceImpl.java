package com.valoores.cassandradatacrowd.app.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Service;

import com.valoores.cassandradatacrowd.app.dto.CustomResponse;
import com.valoores.cassandradatacrowd.app.model.CDRLocation;
import com.valoores.cassandradatacrowd.app.model.Location;
import com.valoores.cassandradatacrowd.app.service.ILocationService;

@Service
public class LocationServiceImpl implements ILocationService {

  @Autowired
  private CassandraTemplate template;

  @Value("${application.repository.urlPath}")
  private String urlPath;
  
  String schema = "datacrowd";
  @Override
  public byte[] FilteringData(String query,String query_1, String simulationId, String whereCondition,JSONArray  ObjShapesArrayByDate, JSONArray ObjArray ,String simulationType,String provider,long fromDatemillis,long toDatemillis,String allDevices, String dataType,String callingNo,String imsiId,JSONArray countryCode,JSONArray arrayOfDate,String SubRegion,String Region,HttpServletRequest request)
  {

		CustomResponse resp = CustomResponse.builder().build();
		String queryLog = "";
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date   date = new Date();
		String userId = "";
		String devices = "";
		@SuppressWarnings("unused")
		String finalDevices = "";
		String serviceProvider = "";
		String getProviderFilter = "";
		String conditionProviderFilter = "";
		byte[] finalresult= null;

		if (!dataType.equals("")) {
			String[] result0 = dataType.split(",");

			for (int i = 0; i < result0.length; i++) {
				if (i == 0) {
					serviceProvider = "'" + result0[i] + "'";
				} else {
					serviceProvider = serviceProvider + ',' + "'" + result0[i] + "'";
				}
			}
		}
        
		if (!allDevices.equals("")) {
			String[] result = allDevices.split(",");

			for (int i = 0; i < result.length; i++) {
				if (i == 0) {
					devices = "'" + result[i] + "'";
				} else {
					devices = devices + ',' + "'" + result[i] + "'";
				}
			}
		}

    try {
    	

    
    	
			if (request.getSession().getAttribute("userName") != null) {
				userId = request.getSession().getAttribute("userName").toString();
			} else {
				userId = "no user";
			}
        
	  @SuppressWarnings("unused")
	String table    = "";  
	  @SuppressWarnings("unused")
	String columns  = "";
      String fileData = "/data_crowd_data_" + simulationId + ".txt";
      String fileName = "/data_crowd_" + simulationId + ".txt";
      String fullPath = urlPath;
      
      File file_0 = new File(fullPath+fileData); 
      System.out.println("file_0  "+file_0);
      Integer ArrayLength = ObjArray.length();
      System.out.println("ArrayLength  "+ArrayLength);
      System.out.println("ObjArray  "+ObjArray);

      File directory = new File(fullPath);
      directory.mkdirs();
      FileWriter file_1;
      FileWriter file_2;

      file_1 = new FileWriter(fullPath + fileName);
      file_2 = new FileWriter(fullPath + fileData);

      @SuppressWarnings("resource")
	BufferedWriter buffer_1 = new BufferedWriter(file_1);
      @SuppressWarnings("resource")
	BufferedWriter buffer_2 = new BufferedWriter(file_2);
      
      File f1 = new File(fullPath, fileData);
      @SuppressWarnings("resource")
	BufferedReader reader1 = new BufferedReader(new FileReader(f1));
	  if(reader1.ready())
	    {
	    byte[] contentFile = reader1.readLine().toString().getBytes();
	        buffer_1.write("[" + new String(contentFile, StandardCharsets.UTF_8) + "]");
	        buffer_1.flush();
	    }

      System.out.println(" provider = ' "+provider+"'");
      System.out.println(" arrayOfDate = ' "+arrayOfDate+"'");
      System.out.println(" arrayOfDate.length() = ' "+arrayOfDate.length()+"'");

      
      
      
    	
      
      //arrayOfDate
//      
//	  String[] SubRegionArray = SubRegion.split(",");
//      String[] RegionArray = Region.split(",");
	  @SuppressWarnings("unused")
	String[] SubRegionArray =null;
      @SuppressWarnings("unused")
	String[] RegionArray = null;
      JSONArray country_code=null;
      String country_codeArray=null;

//      for(int l =0  ; l < RegionArray.length ; l++)
//      {
//    	  
//    	  
//    	  for(int k =0  ; k < SubRegionArray.length ; k++)
//    	  {
//    		  
      
      for(int o =0  ; o < arrayOfDate.length() ; o++)
      {
    	  System.out.println(" simulationType >>>> "+simulationType);

    	  System.out.println(" SubRegion >>>> "+SubRegion);
    	  System.out.println(" Region >>>> "+Region);
    	  
    	  System.out.println("  year "+ String.valueOf(arrayOfDate.getJSONObject(o).getInt("year")));
    	  System.out.println("  month "+ arrayOfDate.getJSONObject(o).getJSONArray("month"));
    	  
    	  
    	  
    	  String year = String.valueOf(arrayOfDate.getJSONObject(o).getInt("year"));
    	  JSONArray month = arrayOfDate.getJSONObject(o).getJSONArray("month");
    	  


    	  if(!year.equals(""))
    	  {
        	  for(int p = 0 ; p < month.length() ; p ++ )
        	  {
        		  
//        		  String monthValue = String.format("%02d",month.get(p));
        		  
        		  String monthValue =month.get(p).toString();
        		  System.out.println(" year ==  "+ year);
        		  System.out.println(" month ==>"+ monthValue);
        		  
        		  
        		  String tableName = "geo_data";
        		  String indexName = "geo_index";
        		  String tablesCriteria="";
        		  String indexesCriteria="";
        		  
        		  String tableCriteria = tableName+"_"+year + "_" + monthValue+"_";
//        		  +RegionArray[l]+"_"+SubRegionArray[k];
        		  String indexCriteria = indexName+"_"+year + "_" + monthValue+"_";
//                  +RegionArray[l]+"_"+SubRegionArray[k];
        		  System.out.println(" tableCriteria ==  "+ tableCriteria);


//        		  String cqlsh = "SELECT count(1) FROM system_schema.tables where table_name = '"+tableCriteria+ "' allow filtering";
//        		  
//        		  Integer tableExists =   template.selectOne(cqlsh, Integer.class);
//        		  System.out.println(" tableExists   = = "+tableExists);
//        		  tableExists=1;
//        		  if(tableExists > 0)
//        		  {
        			  
	  System.out.println("tableExists in >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	  
      if(provider.contains("3") || provider.equals("") ) // geo
      {
    	  String callingNo0 = "";
	  	  String callingNo1 = "";
				if (!callingNo.equals("")) {
					String[] result = callingNo.split(",");

					for (int i = 0; i < result.length; i++) {
						if (i == 0) {
							callingNo0 = "" + result[i] + "";
						} else {
							callingNo0 = callingNo0 + ',' + "" + result[i] + "";
						}
						callingNo1 = "AND location_name in (" + callingNo0 + ")";
					}
				}
		   
    	    if(simulationType.equals("3") )
    	      {
    	 	 	if(!whereCondition.equals("")){
					devices = devicesElements(devices);
							whereCondition = " {" +
				    	                " \"type\": \"contains\"," +
				    	                " \"field\": \"device_id\"," +
				    	                " \"values\":  [ " +devices + " ]" +
				    	                " },";
						}else {
							whereCondition ="";

						}
    	    	
    	    	String   dates = "{"  
    	           + " \"type\": \"range\"," 
    	           + " \"field\": \"usage_timeframe\"," 
  	               + " \"lower\": " + fromDatemillis + "," 
  	               + " \"upper\": " + toDatemillis + "," 
    	           + " \"include_lower\": true," 
    	           + " \"include_upper\": true," 
    	           + " \"doc_values\": true" 
    	           + " },"; 
    	      	  System.out.println(" DTP GEO ");
    	      	  
    	      	  System.out.println(" whereCondition 5555555   "+ whereCondition);

    	    	  if (ArrayLength > 0) {
    	  	        for (int i = 0; i < ArrayLength; i++) {
    	  	        	

						  System.out.println("countryjson in filtering data>>>"+ ObjArray.getJSONObject(i).getJSONArray("countryjson"));  
			   	    	     
			                 JSONArray countryjson = new JSONArray(ObjArray.getJSONObject(i).getJSONArray("countryjson"));

			   	    	     	for (int j = 0; j < countryjson.length(); j++) {
			            	    JSONArray innerArray = countryjson.getJSONArray(j);
			            	    System.out.println(" innerArray  "+ innerArray);
			
			            	    for (int k = 0; k < innerArray.length(); k++) {
//			            	        System.out.println(" inn i = "+ k +"  "+innerArray.getString(k) + " ");
//			            	        String[] elementSubRegion = {innerArray.getString(0)};
			            	        if(k==0) {
//			            	        	SubRegion=SubRegion+','+innerArray.getString(k);
			            	        	SubRegion=innerArray.getString(k);
			
			            	        }else  if(k==1) {
//			            	        	Region=Region+','+innerArray.getString(k);
		            	        	     Region=innerArray.getString(k);

			
			            	        }else if(k==2) {
			            	            JSONArray countryjson2 = new JSONArray(innerArray.getJSONArray(k));
						                 System.out.println("countryjson2>>"+countryjson2);
			            	        	country_code=countryjson2;
			            	        }
			
			            	        
			            	        
			            	     
//			            	        SubRegion.add(elementSubRegion);
					   	           

			            	        
//			            	        String[] elementRegion = {innerArray.getString(1)};
//			            	        Region.add(elementRegion);
			            	        
			            	    }
			            		System.out.println("SubRegion  final  "+ SubRegion);
				   	         	System.out.println("Region final "+ Region);
				   	         	System.out.println("country_code final "+ country_code);

				   	     	System.out.println("table cirteria    "+ tableCriteria);
	            	       String  updatedtableCriteria=tableCriteria+Region+"_"+SubRegion;
	            	       String  updatedindexCriteria=indexCriteria+Region+"_"+SubRegion;
			   	           	System.out.println("updated tableCriteria final  "+ updatedtableCriteria);

			   	         if(tablesCriteria.equals("")) {
				   	         tablesCriteria=updatedtableCriteria;
			   	        	 
			   	         }else {
			   	        	tablesCriteria=tablesCriteria+','+updatedtableCriteria;
			   	         }
			   	         
			   	      if(indexesCriteria.equals("")) {
			   	    	indexesCriteria=updatedindexCriteria;
			   	        	 
			   	         }else {
			   	        	indexesCriteria=indexesCriteria+','+updatedindexCriteria;
			   	         }
			   	         
			   	         
			   	           	System.out.println("tablesCriteria before final  "+ tablesCriteria);
			   	           	System.out.println("indexesCriteria before final  "+ indexesCriteria);
			   	           	
			   	           	System.out.println("getUniqueStringsBetweenCommas tablesCriteria final  "+ getUniqueStringsBetweenCommas(tablesCriteria));
			   	           	System.out.println("getUniqueStringsBetweenCommas indexesCriteria final  "+ getUniqueStringsBetweenCommas(indexesCriteria));
		   	             	System.out.println("convertSetToString tablesCriteria final  "+ convertSetToString(getUniqueStringsBetweenCommas(tablesCriteria)));
		   	         tablesCriteria=convertSetToString(getUniqueStringsBetweenCommas(tablesCriteria));
		   	      indexesCriteria=convertSetToString(getUniqueStringsBetweenCommas(indexesCriteria));
		   	    	System.out.println("tablesCriteria  final  "+ calculateCommaSeparatedStringLength(tablesCriteria));


//			   	         tablesCriteria=getUniqueStringsBetweenCommas(tablesCriteria).toString();
//			   	      indexesCriteria=getUniqueStringsBetweenCommas(indexesCriteria).toString();
//
//			   	         
//			   	    	System.out.println("tablesCriteria  final  "+ tablesCriteria);
//		   	           	System.out.println("indexesCriteria  final  "+ indexesCriteria);
			            	}

    	  	        	
    	  	        	
			   	    		  int nbrofTbl=calculateCommaSeparatedStringLength(tablesCriteria);
					   	         System.out.println("nbrofTbl"+nbrofTbl);

				   	    	String FilteringQuery="";
				   	    	   if(nbrofTbl > 1) {
				   	         // Calculate the length of the modified string
				   	         // Split the input string by commas
				   	         String[] tables = tablesCriteria.split(",");
				   	         String[] indexes = indexesCriteria.split(",");
				   	    	  System.out.println(" tables >>> "+tables);
				   	    	  System.out.println(" indexes >>> "+indexes);
				   	      for (int l=0;l < tables.length; l++) {
				   	    	  
				   	    	  System.out.println(" tables[l] >>> "+tables[l]);
				   	    	  System.out.println(" indexes[l] >>> "+indexes[l]);

				   	    	  
			        		  String cqlsh = "SELECT count(1) FROM system_schema.tables where table_name = '"+tables[l]+ "' allow filtering";
			        		  Integer tableExists =   template.selectOne(cqlsh, Integer.class);
				   	    	  System.out.println(" tableExists >>> "+tableExists);
			        		  if(tableExists>0)
			        		  {
			        			  FilteringQuery = "SELECT location_main_data_id  ,country_code, data_category, device_carrier_name, device_hit_count, device_id,\r\n" 
			    	  	        		  	+ " device_manufacturer_brand, device_model, location_accuracy, location_altitude, location_cou_code_num,\r\n" 
			    	    	  	          	+ " location_density, location_latitude, location_longitude, location_name, service_provider_id, usage_date,\r\n"
			    	    	  	          	+ " usage_timeframe, usage_timeline,0+0 as called_no,0+0 as call_bdate,0+0 as call_edate,0+0 as calling_no,"+ObjArray.getJSONObject(i).getInt("shapeId") +"+ 0+0  as SHAPE_ID"
			    	    	  	            + " FROM  "+schema+"."+tables[l] 
			    	    	  	            + " WHERE expr("+indexes[l]+"_place_country," 
			    	    	  	            + " '{" 
			    	    	  	            + " \"filter\":[" 
			    	    	  	            + dates
			    	    	  	            + whereCondition
			    	    	  	            + ObjArray.getJSONObject(i).getString("query") + query +
					    	    	          " ,{\"type\": \"contains\",\"field\": \"country_code\",\"values\":"+country_code+"}"
			    	    	  	            +" ]" +"}')" ;
//			    	    	  	             + whereCondition + " " +callingNo1+ "   allow filtering";
			    	  	          
			    	  	          byte[] FilteringResult = template.select(FilteringQuery, Location.class).toString().getBytes();
			    	  	          
			    	  	          
			    	  	        FilteringQuery = FilteringQuery.replace("'", "~");
			    	  	          System.out.println(" circle query >> " + FilteringQuery);

			    	  	          queryLog = "insert into "+schema+".query_log\r\n" + 
			    	  	       		   "(username, operation_date, log_ip, query, status)\r\n" + 
			    	  	       		   "values\r\n" + 
			    	  	       		   "('"+userId+"','"+formatter.format(date)+"','','"+FilteringQuery+"',1)";
			    	  	          
			    	  	          
			    	  	       System.out.println(" logs query >>> "+queryLog);
			    	  	       template.select(queryLog, Location.class);
			    	  	          
			    	  	            if (i == 0) {
			    	  	              buffer_2.write(new String(FilteringResult, StandardCharsets.UTF_8));
			    	  	            } else {
			    	  	              buffer_2.write("," + new String(FilteringResult, StandardCharsets.UTF_8));
			    	  	            }

			    	  	          buffer_2.flush(); 
			        		  }  
			        		  
				   	      	  }
				   	      
				   	    	   }
				   	    	   else{
				   	    		  System.out.println(" tablesCriteria >>> "+tablesCriteria);

				        		  String cqlsh = "SELECT count(1) FROM system_schema.tables where table_name = '"+tablesCriteria+ "' allow filtering";
				        		  Integer tableExists =   template.selectOne(cqlsh, Integer.class);
					   	    	  System.out.println(" tableExists >>> "+tableExists);

				        		  if(tableExists>0)
				        		  {
				        			   FilteringQuery = "SELECT location_main_data_id  ,country_code, data_category, device_carrier_name, device_hit_count, device_id,\r\n" 
				    	  	        		  	+ " device_manufacturer_brand, device_model, location_accuracy, location_altitude, location_cou_code_num,\r\n" 
				    	    	  	          	+ " location_density, location_latitude, location_longitude, location_name, service_provider_id, usage_date,\r\n"
				    	    	  	          	+ " usage_timeframe, usage_timeline,0+0 as called_no,0+0 as call_bdate,0+0 as call_edate,0+0 as calling_no,"+ObjArray.getJSONObject(i).getInt("shapeId") +"+ 0+0  as SHAPE_ID"
				    	    	  	            + " FROM  "+schema+"."+tablesCriteria 
				    	    	  	            + " WHERE expr("+indexesCriteria+"_place_country," 
				    	    	  	            + " '{" 
				    	    	  	            + " \"filter\":[" 
				    	    	  	            + dates
				    	    	  	            + whereCondition
				    	    	  	            + ObjArray.getJSONObject(i).getString("query") + query +
						    	    	          " ,{\"type\": \"contains\",\"field\": \"country_code\",\"values\":"+country_code+"}"
				    	    	  	            +" ]" +"}')" ;
//				    	    	  	             + whereCondition + " " +callingNo1+ "   allow filtering";
				    	  	          
				    	  	          byte[] FilteringResult = template.select(FilteringQuery, Location.class).toString().getBytes();
				    	  	          
				    	  	          
				    	  	        FilteringQuery = FilteringQuery.replace("'", "~");
				    	  	          System.out.println(" circle query >> " + FilteringQuery);

				    	  	          queryLog = "insert into "+schema+".query_log\r\n" + 
				    	  	       		   "(username, operation_date, log_ip, query, status)\r\n" + 
				    	  	       		   "values\r\n" + 
				    	  	       		   "('"+userId+"','"+formatter.format(date)+"','','"+FilteringQuery+"',1)";
				    	  	          
				    	  	          
				    	  	       System.out.println(" logs query >>> "+queryLog);
				    	  	       template.select(queryLog, Location.class);
				    	  	          
				    	  	            if (i == 0) {
				    	  	              buffer_2.write(new String(FilteringResult, StandardCharsets.UTF_8));
				    	  	            } else {
				    	  	              buffer_2.write("," + new String(FilteringResult, StandardCharsets.UTF_8));
				    	  	            }

				    	  	          buffer_2.flush();
				        			  
				        		  }
			        		  }
    	  	        	
    	  	        
    	  	        }
    	  	      }
    	      }
    	      else if(simulationType.equals("1"))
    	      {
    	    	  
      	      	System.out.println(" Activity Scan GEO");
      	      	String serviceProviderFilter   = "";
      	      	String serviceProviderFilter_1 = "";
      	      	
					if (!dataType.equals("")) {
						String[] result0 = dataType.split(",");

						for (int i = 0; i < result0.length; i++) {
							if (i == 0) {
								getProviderFilter = "" + result0[i] + "";
							} else {
								getProviderFilter = getProviderFilter + ',' + "" + result0[i] + "";
							}

						}

							serviceProviderFilter_1 = " { \"type\": \"contains\","
									+ " \"field\": \"service_provider_id\"," + " \"values\":[ "
									+ getProviderFilter + " ]" + "},";
					}
					
					
					
					if(!whereCondition.equals("")){
						devices = devicesElements(devices);
								whereCondition = " {" +
					    	                " \"type\": \"contains\"," +
					    	                " \"field\": \"device_id\"," +
					    	                " \"values\":  [ " +devices + " ]" +
					    	                " },";
							}else {
								whereCondition ="";

							}
      	      	
    	    	  if (!query.equals("") ) {
    	    		  String   dates = "{" 
    	    	         + " \"type\": \"range\"," 
    	    	         + " \"field\": \"usage_timeframe\"," 
    	    	         + " \"lower\": " + fromDatemillis + "," 
    	    	         + " \"upper\": " + toDatemillis + ","
    	    	         + " \"include_lower\": true," 
    	    	         + " \"include_upper\": true," 
    	    	         + " \"doc_values\": true"
    	    	         + " },"; 
    	    		  
    	      	      System.out.println(" all shappes rectangle and polygine >>>>>>> >>>>>>>>0000>>>>");

    	    	        String queryCassandra = "SELECT location_main_data_id  ,country_code, data_category, device_carrier_name, device_hit_count, device_id," 
    	    	          + " device_manufacturer_brand, device_model, location_accuracy, location_altitude, location_cou_code_num," 
    	    	          + " location_density, location_latitude, location_longitude, location_name, service_provider_id, usage_date, " 
    	    	          + "usage_timeframe, usage_timeline,0+0 as called_no,0+0 as call_bdate,0+0 as call_edate,0+0 as calling_no"
    	    	          + " FROM "+schema+"."+tableCriteria
    	    	          + " WHERE expr("+indexCriteria+"_place_country," 
    	    	          + " '{" 
    	    	          + "   \"filter\":[" 
    	    	          + serviceProviderFilter_1
    	    	          + dates
    	    	          + whereCondition
    	    	          +  query 
    	    	          +" ,{\"type\": \"contains\",\"field\": \"country_code\",\"values\":"+country_code+"}"

    	    	          + "   ]" + "}') ";
//    	    	           + whereCondition+ " " +serviceProviderFilter+ " " +callingNo1+ "  allow filtering";

    	    	        System.out.println(" query all shapes  >>" +queryCassandra);
    	    	        
    	    	        byte[] loc = template.select(queryCassandra, Location.class).toString().getBytes();
    	    	        
    	    	        System.out.println(" query poyline records  >>>>>>>>>>>>>>>>" +new String(loc, StandardCharsets.UTF_8));

    	    	  	  if(reader1.ready())
    	    		    {
            	  	        buffer_2.write("," +new String(loc, StandardCharsets.UTF_8));
    	    		    }
    	    	  	  else
    	    	  	  {
            	  	        buffer_2.write(new String(loc, StandardCharsets.UTF_8));
    	    	  	  }
    	    	  	  
    	                System.out.println("buffer_2  3  "+ buffer_2);

    	    	        buffer_2.flush();
    	    	        
    	    	        queryCassandra = queryCassandra.replace("'", "~");

    	    	        queryLog = "insert into "+schema+".query_log\r\n" + 
    	    	        		   "(username, operation_date, log_ip, query, status)\r\n" + 
    	    	        		   "values\r\n" + 
    	    	        		   "('"+userId+"','"+formatter.format(date)+"','','"+queryCassandra+"',1)";
    	    	        System.out.println(" logs query >>> "+queryLog);
    	    	        template.select(queryLog, Location.class);
    	    	        
    	    	      }
    	    	  //zaher here
    	    	  if(ObjShapesArrayByDate.length() > 0 )
    	    	  {
    	    		  System.out.println(" shapes with dates >>>>>>>>>>>>>>>>>>>> "+ObjShapesArrayByDate);
   	    	        for (int i = 0; i < ObjShapesArrayByDate.length(); i++) {
   	    	        	
   	    	        	
   	    	        	
		   	    	         String selectedStartDate =   ObjShapesArrayByDate.getJSONObject(i).getString("selectedStartDate");
			   	    	      @SuppressWarnings("deprecation")
							Date fromDateparse = new Date(selectedStartDate);
			   	    	      @SuppressWarnings("unused")
							long selectedStartDatemillis = fromDateparse.getTime();
			
			   	    	      String selectedEndDate =  ObjShapesArrayByDate.getJSONObject(i).getString("selectedEndDate");
			   	    	      @SuppressWarnings("deprecation")
							Date toDateparse = new Date(selectedEndDate);
			   	    	      @SuppressWarnings("unused")
							long selectedEndDatemillis = toDateparse.getTime();
			   	    	     System.out.println("countryjson in filtering data>>>"+ ObjShapesArrayByDate.getJSONObject(i).getJSONArray("countryjson"));  
			   	    	     
			                 JSONArray countryjson = new JSONArray(ObjShapesArrayByDate.getJSONObject(i).getJSONArray("countryjson"));

			   	    	     	for (int j = 0; j < countryjson.length(); j++) {
			            	    JSONArray innerArray = countryjson.getJSONArray(j);
			            	    System.out.println(" innerArray  "+ innerArray);
			
			            	    for (int k = 0; k < innerArray.length(); k++) {
//			            	        System.out.println(" inn i = "+ k +"  "+innerArray.getString(k) + " ");
//			            	        String[] elementSubRegion = {innerArray.getString(0)};
			            	        if(k==0) {
//			            	        	SubRegion=SubRegion+','+innerArray.getString(k);
			            	        	SubRegion=innerArray.getString(k);
			
			            	        }else  if(k==1) {
//			            	        	Region=Region+','+innerArray.getString(k);
		            	        	     Region=innerArray.getString(k);

			
			            	        }else if(k==2) {
			            	            JSONArray countryjson2 = new JSONArray(innerArray.getJSONArray(k));
						                 System.out.println("countryjson2>>"+countryjson2);
			            	        	country_code=countryjson2;
			            	        }
			
			            	        
			            	        
			            	     
//			            	        SubRegion.add(elementSubRegion);
					   	           

			            	        
//			            	        String[] elementRegion = {innerArray.getString(1)};
//			            	        Region.add(elementRegion);
			            	        
			            	    }
			            		System.out.println("SubRegion  final  "+ SubRegion);
				   	         	System.out.println("Region final "+ Region);
				   	         	System.out.println("country_code final "+ country_code);

				   	     	System.out.println("table cirteria    "+ tableCriteria);
	            	       String  updatedtableCriteria=tableCriteria+Region+"_"+SubRegion;
	            	       String  updatedindexCriteria=indexCriteria+Region+"_"+SubRegion;
			   	           	System.out.println("updated tableCriteria final  "+ updatedtableCriteria);

			   	         if(tablesCriteria.equals("")) {
				   	         tablesCriteria=updatedtableCriteria;
			   	        	 
			   	         }else {
			   	        	tablesCriteria=tablesCriteria+','+updatedtableCriteria;
			   	         }
			   	         
			   	      if(indexesCriteria.equals("")) {
			   	    	indexesCriteria=updatedindexCriteria;
			   	        	 
			   	         }else {
			   	        	indexesCriteria=indexesCriteria+','+updatedindexCriteria;
			   	         }
			   	         
			   	         
			   	           	System.out.println("tablesCriteria before final  "+ tablesCriteria);
			   	           	System.out.println("indexesCriteria before final  "+ indexesCriteria);
			   	           	
			   	           	System.out.println("getUniqueStringsBetweenCommas tablesCriteria final  "+ getUniqueStringsBetweenCommas(tablesCriteria));
			   	           	System.out.println("getUniqueStringsBetweenCommas tablesCriteria final  "+ getUniqueStringsBetweenCommas(indexesCriteria));
		   	             	System.out.println("convertSetToString tablesCriteria final  "+ convertSetToString(getUniqueStringsBetweenCommas(tablesCriteria)));
		   	         tablesCriteria=convertSetToString(getUniqueStringsBetweenCommas(tablesCriteria));
		   	      indexesCriteria=convertSetToString(getUniqueStringsBetweenCommas(indexesCriteria));
		   	    	System.out.println("tablesCriteria  final  "+ calculateCommaSeparatedStringLength(tablesCriteria));


//			   	         tablesCriteria=getUniqueStringsBetweenCommas(tablesCriteria).toString();
//			   	      indexesCriteria=getUniqueStringsBetweenCommas(indexesCriteria).toString();
//
//			   	         
//			   	    	System.out.println("tablesCriteria  final  "+ tablesCriteria);
//		   	           	System.out.println("indexesCriteria  final  "+ indexesCriteria);
			            	}

			   	    	     
			   	    	     
			   	    	     
			   	    	     
			   	    	     
			   	    	   String   dates = "{" 
			   	    	          + " \"type\": \"range\"," 
			   	    			  + " \"field\": \"usage_timeframe\"," 
			   	    			  + " \"lower\": " + fromDatemillis + "," 
			   	    			  + " \"upper\": " + toDatemillis + "," 
			   	    			  + " \"include_lower\": true," 
			   	    			  + " \"include_upper\": true," 
			   	    			  + " \"doc_values\": true" 
			   	    			  + " },"; 
			   	    	   
			   	    	  int nbrofTbl=calculateCommaSeparatedStringLength(tablesCriteria);
				   	         System.out.println("nbrofTbl"+nbrofTbl);

			   	    	String queryCassandra_2="";
			   	    	   if(nbrofTbl > 1) {
			   	         // Calculate the length of the modified string
			   	         // Split the input string by commas
			   	         String[] tables = tablesCriteria.split(",");
			   	         String[] indexes = indexesCriteria.split(",");
			   	    	  System.out.println(" tables >>> "+tables);

			   	      for (int l=0;l < tables.length; l++) {
			   	    	  
			   	    	  System.out.println(" tables[l] >>> "+tables[l]);

			   	    	  
		        		  String cqlsh = "SELECT count(1) FROM system_schema.tables where table_name = '"+tables[l]+ "' allow filtering";
		        		  Integer tableExists =   template.selectOne(cqlsh, Integer.class);
			   	    	  System.out.println(" tableExists >>> "+tableExists);
		        		  if(tableExists>0)
		        		  {
//			              System.out.println(word);
			              
//			              for (String index : indexes ) {
//				              System.out.println(index);

				              
				              queryCassandra_2 = "SELECT location_main_data_id  ,country_code, data_category, device_carrier_name, device_hit_count, device_id,"
			    	    	          + " device_manufacturer_brand, device_model, location_accuracy, location_altitude, location_cou_code_num," 
			    	    	          + " location_density, location_latitude, location_longitude, location_name, service_provider_id, usage_date, " 
			    	    	          + " usage_timeframe, usage_timeline,0+0 as called_no,0+0 as call_bdate,0+0 as call_edate,0+0 as calling_no "
			    	    	          + " FROM "+schema+"."+tables[l] 
			    	    	          + " WHERE expr("+indexes[l]+"_place_country," 
			    	    	          + " '{" 
			    	    	          + "   \"filter\":[" 
			    	    	          + serviceProviderFilter_1
			    	    	          + whereCondition
			    	    	          + dates
			    	    	          + ObjShapesArrayByDate.getJSONObject(i).getString("coordinates") +
			    	    	          " ,{\"type\": \"contains\",\"field\": \"country_code\",\"values\":"+country_code+"}"
			    	    	         + "   ]"
			    	    	          +" }') ";

//			              }
			              System.out.println("queryCassandra_2 "+queryCassandra_2);
    	    	        System.out.println(" query all shapes by datesss >>" +queryCassandra_2);
	    	    	        
	    	    	        byte[] loc = template.select(queryCassandra_2, Location.class).toString().getBytes();
	    	    	        
	    	    	        
	  	  	    	  	  if(reader1.ready())
	  	  	    		    {
	  	          	  	        buffer_2.write("," +new String(loc, StandardCharsets.UTF_8));
	  	  	    		    }
	  	  	    	  	  else
	  	  	    	  	  {
	  	          	  	        buffer_2.write(new String(loc, StandardCharsets.UTF_8));
	  	  	    	  	  }  	    	      
	  	  	    	  	  buffer_2.flush();
	    	    	        
	  	  	    	  queryCassandra_2 = queryCassandra_2.replace("'", "~");
	
	    	    	        queryLog = "insert into "+schema+".query_log\r\n" + 
	    	    	        		   "(username, operation_date, log_ip, query, status)\r\n" + 
	    	    	        		   "values\r\n" + 
	    	    	        		   "('"+userId+"','"+formatter.format(date)+"','','"+queryCassandra_2+"',1)";
	    	    	        System.out.println(" logs query >>> "+queryLog);
	    	    	        template.select(queryLog, Location.class);
			              
		        		  }
			          }
			   	       
			   	    	   }else {
			   	    		   
					   	    	  System.out.println(" tableCriteria >>> "+tablesCriteria);

		        		  String cqlsh = "SELECT count(1) FROM system_schema.tables where table_name = '"+tablesCriteria+ "' allow filtering";
		        		  Integer tableExists =   template.selectOne(cqlsh, Integer.class);
			   	    	  System.out.println(" tableExists >>> "+tableExists);

		        		  if(tableExists>0)
		        		  {
	   	    		   
			   	    		  queryCassandra_2 = "SELECT location_main_data_id  ,country_code, data_category, device_carrier_name, device_hit_count, device_id,"
			    	    	          + " device_manufacturer_brand, device_model, location_accuracy, location_altitude, location_cou_code_num," 
			    	    	          + " location_density, location_latitude, location_longitude, location_name, service_provider_id, usage_date, " 
			    	    	          + " usage_timeframe, usage_timeline,0+0 as called_no,0+0 as call_bdate,0+0 as call_edate,0+0 as calling_no "
			    	    	          + " FROM "+schema+"."+tablesCriteria 
			    	    	          + " WHERE expr("+indexesCriteria+"_place_country," 
			    	    	          + " '{" 
			    	    	          + "   \"filter\":[" 
			    	    	          + serviceProviderFilter_1 
			    	    	          + whereCondition
			    	    	          + dates
			    	    	          + ObjShapesArrayByDate.getJSONObject(i).getString("coordinates") +
			    	    	          " ,{\"type\": \"contains\",\"field\": \"country_code\",\"values\":"+country_code+"}]"
			    	    	          +" }') ";
			   	    		   
		    	    	        System.out.println(" query all shapes by datesss >>" +queryCassandra_2);
		    	    	        
		    	    	        byte[] loc = template.select(queryCassandra_2, Location.class).toString().getBytes();
		    	    	        
		    	    	        
	  	  	    	  	  if(reader1.ready())
	  	  	    		    {
	  	          	  	        buffer_2.write("," +new String(loc, StandardCharsets.UTF_8));
	  	  	    		    } 
	  	  	    	  	  else
	  	  	    	  	  {
	  	          	  	        buffer_2.write(new String(loc, StandardCharsets.UTF_8));
	  	  	    	  	  }  	    	      
		  	  	    	  	  buffer_2.flush();
		    	    	        
		  	  	    	  queryCassandra_2 = queryCassandra_2.replace("'", "~");
		
		    	    	        queryLog = "insert into "+schema+".query_log\r\n" + 
		    	    	        		   "(username, operation_date, log_ip, query, status)\r\n" + 
		    	    	        		   "values\r\n" + 
		    	    	        		   "('"+userId+"','"+formatter.format(date)+"','','"+queryCassandra_2+"',1)";
		    	    	        System.out.println(" logs query >>> "+queryLog);
		    	    	        template.select(queryLog, Location.class);
		    	    	        
		        		  		}
			   	    	   }
			   	    	   
	    	    	  }
    	    	  }
    	    	  
    	    	  if (!query_1.equals("") ) {
    	    		  
    	    		 String   dates = "{" 
    	    		 + " \"type\": \"range\"," 
    	    		 + " \"field\": \"usage_timeframe\"," 
    	    		 + " \"lower\": " + fromDatemillis + "," 
    	    		 + " \"upper\": " + toDatemillis + "," 
    	    		 + " \"include_lower\": true," 
    	    		 + " \"include_upper\": true," 
    	    		 + " \"doc_values\": true" 
    	    		 + " },"; 
    	    		 
    	      	      System.out.println(" polyines >>>>>>>>>>>>>>>> >>>>>>>>1111111111111>>>>");

  	    	        String queryCassandra_1 = "SELECT location_main_data_id  ,country_code, data_category, device_carrier_name, device_hit_count, device_id," 
  	    	        + " device_manufacturer_brand, device_model, location_accuracy, location_altitude, location_cou_code_num," 
  	    	        + " location_density, location_latitude, location_longitude, location_name, service_provider_id, usage_date, " 
  	    	        + " usage_timeframe, usage_timeline,0+0 as called_no,0+0 as call_bdate,0+0 as call_edate,0+0 as calling_no "
  	    	        + " FROM "+schema+"."+tableCriteria 
  	    	        + " WHERE expr("+indexCriteria+"," 
  	    	        + " '{" 
  	    	        + "  \"filter\":[" 
  	    	        + serviceProviderFilter_1
  	    	        + dates
  	    	        + whereCondition
  	    	        + query_1 
  	    	        + "   ]" 
  	    	        + "}') ";

//  	    	        +"}')  "+ whereCondition+ " " +serviceProviderFilter+ " " +callingNo1+ "  allow filtering";

  	    	        System.out.println(" query all shapes  >>" +queryCassandra_1);
  	    	        
  	    	        byte[] loc = template.select(queryCassandra_1, Location.class).toString().getBytes();
  	    	        System.out.println(" query all shapes polyline ss >> >>" +new String(loc, StandardCharsets.UTF_8));

  	    	        
	  	    	  	  if(reader1.ready())
	  	    		    {
	          	  	        buffer_2.write("," +new String(loc, StandardCharsets.UTF_8));
	  	    		    }
	  	    	  	  else
	  	    	  	  {
	          	  	        buffer_2.write(new String(loc, StandardCharsets.UTF_8));
	  	    	  	  }  	    	      
	  	    	  	  buffer_2.flush();
  	    	        
  	    	      queryCassandra_1 = queryCassandra_1.replace("'", "~");

  	    	        queryLog = "insert into "+schema+".query_log\r\n" + 
  	    	        		   "(username, operation_date, log_ip, query, status)\r\n" + 
  	    	        		   "values\r\n" + 
  	    	        		   "('"+userId+"','"+formatter.format(date)+"','','"+queryCassandra_1+"',1)";
  	    	        System.out.println(" logs query >>> "+queryLog);
  	    	        template.select(queryLog, Location.class);
  	    	        
  	    	      }
    	    	  
    	    	  System.out.println();
    	    	      
    	    	      if (ArrayLength > 0) {
    	    	        for (int i = 0; i < ArrayLength; i++) {
    	    	        	
							System.out.println("first  fromDatemillis >> circle >> =" + fromDatemillis);
							System.out.println(" first fromDatemillis >> circle >> =" + toDatemillis);
							System.out.println(" first selectedStartDate >>  >> ="+ObjArray.getJSONObject(i).getString("selectedStartDate"));
    	    	        	
    	    	        	if(!ObjArray.getJSONObject(i).getString("selectedStartDate").equals(""))
    	    	        	{
    	    	        		
								System.out.println(" selectedStartDate >. " + ObjArray.getJSONObject(i).getString("selectedStartDate"));
								System.out.println(" selecte\tEndtDate >. " + ObjArray.getJSONObject(i).getString("selectedEndDate"));
						
								String selectedStartDate = ObjArray.getJSONObject(i).getString("selectedStartDate");
								@SuppressWarnings("deprecation")
								Date fromDateparse = new Date(selectedStartDate);
								long selectedStartDatemillis = fromDateparse.getTime();
								fromDatemillis = selectedStartDatemillis;
								String selectedEndDate = ObjArray.getJSONObject(i).getString("selectedEndDate");
								@SuppressWarnings("deprecation")
								Date toDateparse = new Date(selectedEndDate);
								long selectedEndDatemillis = toDateparse.getTime();
								toDatemillis = selectedEndDatemillis;

								System.out.println(" fromDatemillis >> circle >> =" + fromDatemillis);
								System.out.println(" fromDatemillis >> circle >> =" + toDatemillis);
								
								  System.out.println("countryjson in filtering data>>>"+ ObjArray.getJSONObject(i).getJSONArray("countryjson"));  
					   	    	     
					                 JSONArray countryjson = new JSONArray(ObjArray.getJSONObject(i).getJSONArray("countryjson"));

					   	    	     	for (int j = 0; j < countryjson.length(); j++) {
					            	    JSONArray innerArray = countryjson.getJSONArray(j);
					            	    System.out.println(" innerArray  "+ innerArray);
					
					            	    for (int k = 0; k < innerArray.length(); k++) {
//					            	        System.out.println(" inn i = "+ k +"  "+innerArray.getString(k) + " ");
//					            	        String[] elementSubRegion = {innerArray.getString(0)};
					            	        if(k==0) {
//					            	        	SubRegion=SubRegion+','+innerArray.getString(k);
					            	        	SubRegion=innerArray.getString(k);
					
					            	        }else  if(k==1) {
//					            	        	Region=Region+','+innerArray.getString(k);
				            	        	     Region=innerArray.getString(k);

					
					            	        }else if(k==2) {
								                 JSONArray countryjson2 = new JSONArray(innerArray.getJSONArray(k));
								                 System.out.println("countryjson2"+countryjson2);

					            	        	country_code=countryjson2;
					            	        }
					
					            	        
					            	        
					            	     
//					            	        SubRegion.add(elementSubRegion);
							   	           

					            	        
//					            	        String[] elementRegion = {innerArray.getString(1)};
//					            	        Region.add(elementRegion);
					            	        
					            	    }
					            		System.out.println("SubRegion  final  "+ SubRegion);
						   	         	System.out.println("Region final "+ Region);
						   	         	System.out.println("country_code final "+ country_code);

						   	     	System.out.println("table cirteria    "+ tableCriteria);
			            	       String  updatedtableCriteria=tableCriteria+Region+"_"+SubRegion;
			            	       String  updatedindexCriteria=indexCriteria+Region+"_"+SubRegion;
					   	           	System.out.println("updated tableCriteria final  "+ updatedtableCriteria);

					   	         if(tablesCriteria.equals("")) {
						   	         tablesCriteria=updatedtableCriteria;
					   	        	 
					   	         }else {
					   	        	tablesCriteria=tablesCriteria+','+updatedtableCriteria;
					   	         }
					   	         
					   	      if(indexesCriteria.equals("")) {
					   	    	indexesCriteria=updatedindexCriteria;
					   	        	 
					   	         }else {
					   	        	indexesCriteria=indexesCriteria+','+updatedindexCriteria;
					   	         }
					   	         
					   	         
					   	           	System.out.println("tablesCriteria before final  "+ tablesCriteria);
					   	           	System.out.println("indexesCriteria before final  "+ indexesCriteria);
					   	           	
					   	           	System.out.println("getUniqueStringsBetweenCommas tablesCriteria final  "+ getUniqueStringsBetweenCommas(tablesCriteria));
					   	           	System.out.println("getUniqueStringsBetweenCommas tablesCriteria final  "+ getUniqueStringsBetweenCommas(indexesCriteria));
				   	             	System.out.println("convertSetToString tablesCriteria final  "+ convertSetToString(getUniqueStringsBetweenCommas(tablesCriteria)));
				   	         tablesCriteria=convertSetToString(getUniqueStringsBetweenCommas(tablesCriteria));
				   	      indexesCriteria=convertSetToString(getUniqueStringsBetweenCommas(indexesCriteria));
				   	    	System.out.println("tablesCriteria  final  "+ calculateCommaSeparatedStringLength(tablesCriteria));


//					   	         tablesCriteria=getUniqueStringsBetweenCommas(tablesCriteria).toString();
//					   	      indexesCriteria=getUniqueStringsBetweenCommas(indexesCriteria).toString();
		//
//					   	         
//					   	    	System.out.println("tablesCriteria  final  "+ tablesCriteria);
//				   	           	System.out.println("indexesCriteria  final  "+ indexesCriteria);
					            	}

								
								
								
    				   	    	
    	    	        	}
    	    	        	
							System.out.println(" lst fromDatemillis >> circle >> =" + fromDatemillis);
							System.out.println(" lst fromDatemillis >> circle >> =" + toDatemillis);

					      
						
							
		   	  String   dates = " {" 
		   			  + " \"type\": \"range\","  
		   			  + " \"field\": \"usage_timeframe\"," 
		   			  + " \"lower\": " + fromDatemillis + "," 
		   			  + " \"upper\": " + toDatemillis + "," 
		   			  + " \"include_lower\": true," 
		   			  + " \"include_upper\": true," 
		   			  + " \"doc_values\": true" 
		   			  + " },";
		   	  
		   	  System.out.println(" serviceProviderFilter  ii "+ serviceProviderFilter);
		   	  System.out.println(" whereCondition ii  "+whereCondition);
		   	  System.out.println(" callingNo1  ii "+callingNo1);
		   	  
		   	  //zaher circle 
		   	  
		   	  int nbrofTbl=calculateCommaSeparatedStringLength(tablesCriteria);
	   	         System.out.println("nbrofTbl"+nbrofTbl);

	    	String  circleFilteringQuery="";
	    	   if(nbrofTbl > 1) {
	    		   // Split the input string by commas
		   	         String[] tables = tablesCriteria.split(",");
		   	         String[] indexes = indexesCriteria.split(",");
		   	    	  System.out.println(" tables >>> "+tables);

		   	      for (int l=0;l < tables.length; l++) {
		   	    	  
		   	    	  System.out.println(" tables[l] >>> "+tables[l]);

		   	    	  
	        		  String cqlsh = "SELECT count(1) FROM system_schema.tables where table_name = '"+tables[l]+ "' allow filtering";
	        		  Integer tableExists =   template.selectOne(cqlsh, Integer.class);
		   	    	  System.out.println(" tableExists >>> "+tableExists);
	        		  if(tableExists>0)
	        		  {
	        			   circleFilteringQuery = "SELECT location_main_data_id  ,country_code, data_category, device_carrier_name, device_hit_count, device_id," 
	         	    	            + " device_manufacturer_brand, device_model, location_accuracy, location_altitude, location_cou_code_num," 
	         	    	            + " location_density, location_latitude, location_longitude, location_name, service_provider_id, usage_date, " 
	         	    	            + "usage_timeframe, usage_timeline,0+0 as called_no,0+0 as call_bdate,0+0 as call_edate,0+0 as calling_no,"+ObjArray.getJSONObject(i).getInt("shapeId") +"+ 0+0 as SHAPE_ID  "
	         	    	            + " FROM "+schema+"."+tables[l] 
	         	    	            + " WHERE expr("+indexes[l]+"_place_country," 
	         	    	            + " '{" 
	         	    	            + "   \"filter\":[" 
	         	    	            +  serviceProviderFilter_1 
	     							+  dates
	     							+  whereCondition
	     							+ " { type: \"geo_distance\"," 
	     							+ " field: \"place\"," 
	     							+ " latitude: " + ObjArray.getJSONObject(i).getString("latCircleCenter") + ","
	     							+ " longitude: " + ObjArray.getJSONObject(i).getString("lngCircleCenter") + "," 
	     							+ " max_distance: \"" + ObjArray.getJSONObject(i).getString("radius") + " m\"}"
			    	    	        + " ,{\"type\": \"contains\",\"field\": \"country_code\",\"values\":"+country_code+"}"

	     							+ "   ]" 
	     							+ "}') ";
//	     							+ "}') "+ whereCondition +  " " +serviceProviderFilter+ " " +callingNo1+ "  allow filtering";

	         	    	      
	         	    	          System.out.println(" queryyy   "+ circleFilteringQuery);
	         	    	          byte[] circleFilteringResult = template.select(circleFilteringQuery, Location.class).toString().getBytes();
	         	    	          
	         	    	          
	         	    	          circleFilteringQuery = circleFilteringQuery.replace("'", "~");
	         	    	          System.out.println(" circle query >> " + circleFilteringQuery);

	         	    	          queryLog = "insert into "+schema+".query_log\r\n" + 
	         	    	       		   "(username, operation_date, log_ip, query, status)\r\n" + 
	         	    	       		   "values\r\n" + 
	         	    	       		   "('"+userId+"','"+formatter.format(date)+"','','"+circleFilteringQuery+"',1)";
	         	    	          
	         	    	          
	         	    	       System.out.println(" logs query >>> "+queryLog);
	         	    	       template.select(queryLog, Location.class);
	         	    	          
	         	    	       System.out.println(" ObjShapesArrayByDate = "+ObjShapesArrayByDate.length());
	         	    	       System.out.println(" query = "+query);
	         	    	       
	         	    	      File f = new File(fullPath, fileData);
	         	    	     @SuppressWarnings("resource")
							BufferedReader reader = new BufferedReader(new FileReader(f));
	         	    	       System.out.println(" query = "+reader.ready());

//	         	    	       System.out.println(" circleFilteringResult >>>>>>>>>> "+new String(circleFilteringResult));
	         	    	        System.out.println(" ");
	         	    	          if (query.equals("") && query_1.equals("") && ObjShapesArrayByDate.length() == 0) {
//	         	    	            if (i == 0) {
//	         	    	              buffer_2.write(new String(circleFilteringResult, StandardCharsets.UTF_8));
//	         	    	            } else {
//	         	    	              buffer_2.write("," + new String(circleFilteringResult, StandardCharsets.UTF_8));
//	         	    	            }	         	    	        	  
         	    	        	  
	         	  	  	    	  if(reader.ready())
	      	  	  	    		    {
	      	  	          	  	        buffer_2.write("," +new String(circleFilteringResult, StandardCharsets.UTF_8));
	      	  	  	    		    }
         	  	  	    	  	else
	      	  	  	    	        {
	      	  	          	  	        buffer_2.write(new String(circleFilteringResult, StandardCharsets.UTF_8));
	      	  	  	    	  	    }  
	         	    	        	  
	         	    	          } else {
	         	    	            buffer_2.write("," + new String(circleFilteringResult, StandardCharsets.UTF_8));

	         	    	          }

	         	    	       buffer_2.flush();
	        			  
	        		  }
	        		  
		   	      }
	    		   
	    		   
	    	   }else {
	  	    	  System.out.println(" tableCriteria >>> "+tablesCriteria);

        		  String cqlsh = "SELECT count(1) FROM system_schema.tables where table_name = '"+tablesCriteria+ "' allow filtering";
        		  Integer tableExists =   template.selectOne(cqlsh, Integer.class);
	   	    	  System.out.println(" tableExists >>> "+tableExists);

	   	    	  
	   	    	  
        		  if(tableExists>0)
        		  {
        		          circleFilteringQuery = "SELECT location_main_data_id  ,country_code, data_category, device_carrier_name, device_hit_count, device_id," 
         	    	            + " device_manufacturer_brand, device_model, location_accuracy, location_altitude, location_cou_code_num," 
         	    	            + " location_density, location_latitude, location_longitude, location_name, service_provider_id, usage_date, " 
         	    	            + "usage_timeframe, usage_timeline,0+0 as called_no,0+0 as call_bdate,0+0 as call_edate,0+0 as calling_no,"+ObjArray.getJSONObject(i).getInt("shapeId") +"+ 0+0 as SHAPE_ID  "
         	    	            + " FROM "+schema+"."+tablesCriteria 
         	    	            + " WHERE expr("+indexesCriteria+"_place_country," 
         	    	            + " '{" 
         	    	            + "   \"filter\":[" 
         	    	            +  serviceProviderFilter_1 
     							+  dates
     							+  whereCondition
     							+ " { type: \"geo_distance\"," 
     							+ " field: \"place\"," 
     							+ " latitude: " + ObjArray.getJSONObject(i).getString("latCircleCenter") + ","
     							+ " longitude: " + ObjArray.getJSONObject(i).getString("lngCircleCenter") + "," 
     							+ " max_distance: \"" + ObjArray.getJSONObject(i).getString("radius") + " m\"}" 
		    	    	        +" ,{\"type\": \"contains\",\"field\": \"country_code\",\"values\":"+country_code+"}"

     							+ "   ]" 
     							+ "}') ";
//     							+ "}') "+ whereCondition +  " " +serviceProviderFilter+ " " +callingNo1+ "  allow filtering";

         	    	      
         	    	          System.out.println(" circleFilteringQuery>>>>"+ circleFilteringQuery);
         	    	          byte[] circleFilteringResult = template.select(circleFilteringQuery, Location.class).toString().getBytes();
         	    	          
         	    	          
         	    	          circleFilteringQuery = circleFilteringQuery.replace("'", "~");
         	    	          System.out.println(" circle query >> " + circleFilteringQuery);

         	    	          queryLog = "insert into "+schema+".query_log\r\n" + 
         	    	       		   "(username, operation_date, log_ip, query, status)\r\n" + 
         	    	       		   "values\r\n" + 
         	    	       		   "('"+userId+"','"+formatter.format(date)+"','','"+circleFilteringQuery+"',1)";
         	    	          
         	    	          
         	    	       System.out.println(" logs query >>> "+queryLog);
         	    	       template.select(queryLog, Location.class);
         	    	       
      	    	          
      	    	       System.out.println(" ObjShapesArrayByDate = "+ObjShapesArrayByDate.length());
      	    	       System.out.println(" query = "+query);
      	    	       
      	    	      File f = new File(fullPath, fileData);
      	    	     @SuppressWarnings("resource")
					BufferedReader reader = new BufferedReader(new FileReader(f));
      	    	       System.out.println(" reader.ready() = "+reader.ready());
         	    	          
//         	    	       System.out.println(" circleFilteringResult >>>>>>>>>> "+new String(circleFilteringResult));
         	    	       
  	    	          if (query.equals("") && query_1.equals("") && ObjShapesArrayByDate.length() == 0) {

 	    	        	  
     	  	  	    	  if(reader.ready())
  	  	  	    		    {
  	  	          	  	        buffer_2.write("," +new String(circleFilteringResult, StandardCharsets.UTF_8));
  	  	  	    		    }
 	  	  	    	  	else
  	  	  	    	        {
  	  	          	  	        buffer_2.write(new String(circleFilteringResult, StandardCharsets.UTF_8));
  	  	  	    	  	    }
  	    	        	  
         	    	          } else {
         	    	            buffer_2.write("," + new String(circleFilteringResult, StandardCharsets.UTF_8));

         	    	          }

         	    	          buffer_2.flush();
             	    	       System.out.println(" buffer_2= "+buffer_2);

        			  
        		  }
	    	   }
		   	  
		   	  
    	    	 
    	    	        }
    	    	      }
    	      }else if(simulationType.equals("2"))
    	      { String serviceProviderFilter_1 = null ;
    	      
    	      System.out.println("country code in dh "+countryCode);
    	      
    	     	for (int j = 0; j < countryCode.length(); j++) {
            	    JSONArray innerArray = countryCode.getJSONArray(j);
            	    System.out.println(" innerArraydh  "+ innerArray);

            	    for (int k = 0; k < innerArray.length(); k++) {
//            	        System.out.println(" inn i = "+ k +"  "+innerArray.getString(k) + " ");
//            	        String[] elementSubRegion = {innerArray.getString(0)};
            	        if(k==0) {
//            	        	SubRegion=SubRegion+','+innerArray.getString(k);
            	        	SubRegion=innerArray.getString(k);

            	        }else  if(k==1) {
//            	        	Region=Region+','+innerArray.getString(k);
        	        	     Region=innerArray.getString(k);


            	        }else if(k==2) {
            	            JSONArray countryjson2 = new JSONArray(innerArray.getJSONArray(k));
			                 System.out.println("countryjson2>>"+countryjson2);
           	        	country_code=countryjson2;
		                 System.out.println("country_codeArray>>"+country_codeArray);
		                 if(country_codeArray ==null || country_codeArray.equals("")) {
			                 System.out.println("country_codeArray null>>");
			                 country_codeArray=country_code.toString();
			                 System.out.println("country_codeArray <<>>"+country_codeArray);


		                 }else {
			                 country_codeArray=country_codeArray+','+country_code.toString();
			                 System.out.println("country_codeArray <<>>"+country_codeArray);


		                 }
            	        }

            	        
            	        
            	     
//            	        SubRegion.add(elementSubRegion);
		   	           

            	        
//            	        String[] elementRegion = {innerArray.getString(1)};
//            	        Region.add(elementRegion);
            	        
            	    }
            		System.out.println("SubRegion  final  "+ SubRegion);
	   	         	System.out.println("Region final "+ Region);
	   	         	System.out.println("country_code final "+ country_code);

	   	     	System.out.println("table cirteria    "+ tableCriteria);
    	       String  updatedtableCriteria=tableCriteria+Region+"_"+SubRegion;
    	       String  updatedindexCriteria=indexCriteria+Region+"_"+SubRegion;
   	           	System.out.println("updated tableCriteria final  "+ updatedtableCriteria);

   	         if(tablesCriteria.equals("")) {
	   	         tablesCriteria=updatedtableCriteria;
   	        	 
   	         }else {
   	        	tablesCriteria=tablesCriteria+','+updatedtableCriteria;
   	         }
   	         
   	      if(indexesCriteria.equals("")) {
   	    	indexesCriteria=updatedindexCriteria;
   	        	 
   	         }else {
   	        	indexesCriteria=indexesCriteria+','+updatedindexCriteria;
   	         }
   	         
   	         
   	           	System.out.println("tablesCriteria before final  "+ tablesCriteria);
   	           	System.out.println("indexesCriteria before final  "+ indexesCriteria);
   	           	
   	           	System.out.println("getUniqueStringsBetweenCommas tablesCriteria final  "+ getUniqueStringsBetweenCommas(tablesCriteria));
   	           	System.out.println("getUniqueStringsBetweenCommas indexesCriteria final  "+ getUniqueStringsBetweenCommas(indexesCriteria));
	             	System.out.println("convertSetToString tablesCriteria final  "+ convertSetToString(getUniqueStringsBetweenCommas(tablesCriteria)));
	         tablesCriteria=convertSetToString(getUniqueStringsBetweenCommas(tablesCriteria));
	      indexesCriteria=convertSetToString(getUniqueStringsBetweenCommas(indexesCriteria));
	    	System.out.println("tablesCriteria  final  "+ calculateCommaSeparatedStringLength(tablesCriteria));


//   	         tablesCriteria=getUniqueStringsBetweenCommas(tablesCriteria).toString();
//   	      indexesCriteria=getUniqueStringsBetweenCommas(indexesCriteria).toString();
//
//   	         
//   	    	System.out.println("tablesCriteria  final  "+ tablesCriteria);
//	           	System.out.println("indexesCriteria  final  "+ indexesCriteria);
            	}

	        	
	        	
   	    	
    	      
    	    	  if(!dataType.equals(""))
    	      	    {
    	      	    	  String[] result0 = dataType.split(",");
    	          	 	 
    	       	 	    for (int i = 0; i < result0.length; i++) {
    	       	 	      if (i == 0) {
    	       	 	    	getProviderFilter = "" + result0[i] + "";
    	       	 	      } else {
    	       	 	    	getProviderFilter = getProviderFilter + ',' + "" + result0[i] + "";
    	       	 	      }

    	       	 	    }
    	       	 	    
    	       	 	    
						if (!whereCondition.equals("")) {
							devices = devicesElements(devices);
							whereCondition = " ,{" + " \"type\": \"contains\"," + " \"field\": \"device_id\","
									+ " \"values\":  [ " + devices + " ]" + " }";
						} else {
							whereCondition = "";

						}
    	       	 	
    	       	 	    
    	      	    	 serviceProviderFilter_1 = " {" 
    	      	    	 + " \"type\": \"contains\"," 
    	      	    	 + " \"field\": \"service_provider_id\"," 
    	                 + " \"values\":  [ " +getProviderFilter + " ]" 
    	                 + " },";
    	      	    }
    	    	  
					System.out.println(" DTP GEO cdr ");
					System.out.println(" whereCondition  " + whereCondition);
					System.out.println(" callingNo1  " + callingNo1);
      	      	 String   dates = "{" 
      	      	         + " \"type\": \"range\"," 
      	      			 + " \"field\": \"usage_timeframe\"," 
      	      			 + " \"lower\": " + fromDatemillis + "," 
      	      			 + " \"upper\": " + toDatemillis + "," 
      	      			 + " \"include_lower\": true," 
      	      			 + " \"include_upper\": true," 
      	      			 + " \"doc_values\": true" 
      	      			 + "  }"; 
      	      	 
      	      	 System.out.println(" callingNo1  "+callingNo1);
      	      	 
      	      	 
      	      	 //dhhhh
      	      	 
      	 	  int nbrofTbl=calculateCommaSeparatedStringLength(tablesCriteria);
	   	         System.out.println("nbrofTbl"+nbrofTbl);

	    	String queryCassandra="";
	    	   if(nbrofTbl > 1) {
	    	      // Calculate the length of the modified string
		   	         // Split the input string by commas
		   	         String[] tables = tablesCriteria.split(",");
		   	         String[] indexes = indexesCriteria.split(",");
		   	         String[] countryarraysss=country_codeArray.split("],");
		   	    	  System.out.println(" tables >>> "+tables);
		   	    	  System.out.println(" indexes  dh>>> "+indexes);
		   	    	  System.out.println(" countryarraysss >>> "+countryarraysss);

		   	      for (int l=0;l < tables.length; l++) {
		   	    	  
		   	    	  System.out.println(" tables[l] >>> "+tables[l]);
		   	    	  System.out.println(" indexes[l] >>> "+indexes[l]);
		   	    	  System.out.println(" countryarraysss[l] >>> "+countryarraysss[l]);


		   	    	  
	        		  String cqlsh = "SELECT count(1) FROM system_schema.tables where table_name = '"+tables[l]+ "' allow filtering";
	        		  Integer tableExists =   template.selectOne(cqlsh, Integer.class);
		   	    	  System.out.println(" tableExists >>> "+tableExists);
	        		  if(tableExists>0)
	        		  {
	        			  if (!whereCondition.equals("")  || !callingNo1.equals("")) {
	      	    	        
	 						 queryCassandra = "SELECT location_main_data_id  ,country_code, data_category, device_carrier_name, device_hit_count, device_id,"
	 								  + " device_manufacturer_brand, device_model, location_accuracy, location_altitude, location_cou_code_num," 
	     	    	    	          + " location_density, location_latitude, location_longitude, location_name, service_provider_id, usage_date, " 
	     	    	    	          + " usage_timeframe, usage_timeline,0+0 as called_no,0+0 as  call_bdate, 0+0 as call_edate, 0+0 as calling_no "
	     	    	    	          + " FROM "+schema+"."+tables[l] 
	     	    	    	          + " WHERE expr("+indexes[l]+"_place_country," 
	     	    	    	          + " '{" 
	     	    	    	          + " \"filter\":[" 
	                     	    	  + serviceProviderFilter_1  
	     	    	    	          + dates 
	     	    	    	          + whereCondition
			    	    	          +" ,{\"type\": \"contains\",\"field\": \"country_code\",\"values\":"+countryarraysss[l]+"]}"

	     	    	    	          + "  ]" 
	     	    	    	          + "}') " ;

	     	    		     System.out.println(" DH query geo >> "+queryCassandra);
	     	    		      
	         	    	      File f = new File(fullPath, fileData);
	         	    	     @SuppressWarnings("resource")
							BufferedReader reader = new BufferedReader(new FileReader(f));
	         	    	       System.out.println(" reader.ready()111 = "+reader.ready()); 
	         	    	      byte[] loc = template.select(queryCassandra, Location.class).toString().getBytes();
		 	      	  	        
	         	    	      if(reader.ready())
		  	  	    		    {
		  	          	  	        buffer_2.write("," +new String(loc, StandardCharsets.UTF_8));
		  	  	    		    }
		  	  	    	  	  else
		  	  	    	  	  {
		  	          	  	        buffer_2.write(new String(loc, StandardCharsets.UTF_8));
		  	  	    	  	  }   
	    	    		     
	         	    	       
	     	    	        
	     	    	        buffer_2.flush();
	     	    	        
	     	    	        queryCassandra = queryCassandra.replace("'", "~");

	     	    	        queryLog = "insert into "+schema+".query_log\r\n" + 
	     	    	        		   "(username, operation_date, log_ip, query, status)\r\n" + 
	     	    	        		   "values\r\n" + 
	     	    	        		   "('"+userId+"','"+formatter.format(date)+"','','"+queryCassandra+"',1)";
	     	    	        System.out.println(" logs query >>> "+queryLog);
	     	    	        template.select(queryLog, Location.class);
	     	    	      }
	        			  
	        			  
	        			  }
	        		  }
	    	   }else {
	    			  if (!whereCondition.equals("")  || !callingNo1.equals("")) {
	    	    	        
	    				  String cqlsh = "SELECT count(1) FROM system_schema.tables where table_name = '"+tablesCriteria+ "' allow filtering";
		        		  Integer tableExists =   template.selectOne(cqlsh, Integer.class);
			   	    	  System.out.println(" tableExists >>> "+tableExists);
		        		  if(tableExists>0)
		        		  {
		        			  queryCassandra = "SELECT location_main_data_id  ,country_code, data_category, device_carrier_name, device_hit_count, device_id,"
									  + " device_manufacturer_brand, device_model, location_accuracy, location_altitude, location_cou_code_num," 
	    	    	    	          + " location_density, location_latitude, location_longitude, location_name, service_provider_id, usage_date, " 
	    	    	    	          + " usage_timeframe, usage_timeline,0+0 as called_no,0+0 as  call_bdate, 0+0 as call_edate, 0+0 as calling_no "
	    	    	    	          + " FROM "+schema+"."+tablesCriteria 
	    	    	    	          + " WHERE expr("+indexesCriteria+"_place_country," 
	    	    	    	          + " '{" 
	    	    	    	          + " \"filter\":[" 
	                    	    	  + serviceProviderFilter_1  
	    	    	    	          + dates 
	    	    	    	          + whereCondition
					    	    	  +" ,{\"type\": \"contains\",\"field\": \"country_code\",\"values\":"+country_code+"}"
	    	    	    	          + "  ]" 
	    	    	    	          + "}') " ;

	    	    		     System.out.println(" DH query geo >> "+queryCassandra);
	    	    	        
	    	    		     File f = new File(fullPath, fileData);
	         	    	     @SuppressWarnings("resource")
							BufferedReader reader = new BufferedReader(new FileReader(f));
	         	    	       System.out.println(" reader.ready()22 = "+reader.ready()); 
	         	    	      byte[] loc = template.select(queryCassandra, Location.class).toString().getBytes();
			      	  	    
	         	    	       
	       	  	    	  	  if(reader.ready())
		  	  	    		    {
		  	          	  	        buffer_2.write("," +new String(loc, StandardCharsets.UTF_8));
		  	  	    		    }
		  	  	    	  	  else
		  	  	    	  	  {
		  	          	  	        buffer_2.write(new String(loc, StandardCharsets.UTF_8));
		  	  	    	  	  }   
	    	    		     
	    	    	       
//	       	  	    	   buffer_2.write(new String(loc, StandardCharsets.UTF_8));
	    	    	       buffer_2.flush();
	    	    	        
	    	    	        
	    	    	        queryCassandra = queryCassandra.replace("'", "~");

	    	    	        queryLog = "insert into "+schema+".query_log\r\n" + 
	    	    	        		   "(username, operation_date, log_ip, query, status)\r\n" + 
	    	    	        		   "values\r\n" + 
	    	    	        		   "('"+userId+"','"+formatter.format(date)+"','','"+queryCassandra+"',1)";
	    	    	        System.out.println(" logs query >>> "+queryLog);
	    	    	        template.select(queryLog, Location.class);
		        			  
		        		  }
		        		  
	    				  
							
	    	    	      }
	    	   }

    	    
    	      }else if(simulationType.equals("12")) {
    	    	  //dct
    	    	  
    	    	  String serviceProviderFilter_1 = null ;
    	    	  if(!dataType.equals(""))
    	      	    {
    	      	    	  String[] result0 = dataType.split(",");
    	          	 	 
    	       	 	    for (int i = 0; i < result0.length; i++) {
    	       	 	      if (i == 0) {
    	       	 	    	getProviderFilter = "" + result0[i] + "";
    	       	 	      } else {
    	       	 	    	getProviderFilter = getProviderFilter + ',' + "" + result0[i] + "";
    	       	 	      }

    	       	 	    }
    	       	 	    
    	      	      if(!whereCondition.equals(""))
    	      	      {
    	         	 	    
    	         	 	 @SuppressWarnings("unused")
						String serviceProviderFilter = "AND SERVICE_PROVIDER_ID IN ("+getProviderFilter+")";  
    	      	      }
    	      	      else
    	      	      {
    	      	    	 serviceProviderFilter_1 = " {" +
    	                " \"type\": \"contains\"," +
    	                " \"field\": \"service_provider_id\"," +
    	                " \"values\":  [ " +getProviderFilter + " ]" +
    	                " },";
    	      	      }
    	      	    }
    	    	  
    	    	  
    	    		 String   dates = "{" 
          	      	         + " \"type\": \"range\"," 
          	      			 + " \"field\": \"usage_timeframe\"," 
          	      			 + " \"lower\": " + toDatemillis + "," 
          	      			 + " \"upper\": " + fromDatemillis + "," 
          	      			 + " \"include_lower\": true," 
          	      			 + " \"include_upper\": true," 
          	      			 + " \"doc_values\": true" 
          	      			 + "  }"; 

    	    		 System.out.println("dct  whereCondition  "+whereCondition);
    	    		 System.out.println("dct  countryCode  "+countryCode);
    	    		 System.out.println("dct  callingNo1  "+callingNo1);
    	    		 System.out.println("dct  serviceProviderFilter_1  "+serviceProviderFilter_1);
    	    	  
    	    		String queryCassandra = "SELECT location_main_data_id ,country_code , data_category, device_carrier_name, device_hit_count, device_id,"
							  + " device_manufacturer_brand, device_model, location_accuracy, location_altitude, location_cou_code_num," 
	    	    	          + " location_density, location_latitude, location_longitude, location_name, service_provider_id, usage_date, " 
	    	    	          + " usage_timeframe, usage_timeline,0+0 as called_no,0+0 as  call_bdate, 0+0 as call_edate, 0+0 as calling_no "
	    	    	          + " FROM "+schema+"."+tableCriteria 
	    	    	          + " WHERE expr("+indexCriteria+"," 
	    	    	          + " '{" 
	    	    	          + " \"filter\":[" 
              	    	      + serviceProviderFilter_1  
	    	    	          + dates 
	    	    	          + "  ]" 
	    	    	          + "}') " + whereCondition 
	    	    	          + "  and  country_code = "+"'"+countryCode+"'"
	    	    	          + callingNo1+ "  allow filtering " ; 
    	    		
    	    		System.out.println("  queryCassandra  for dct "+ queryCassandra);
    	    		
    	    		
    	            
	    	        byte[] loc = template.select(queryCassandra, Location.class).toString().getBytes();
      	  	        buffer_2.write(new String(loc, StandardCharsets.UTF_8));
	    	        buffer_2.flush();
	    	        
	    	   //     queryCassandra = queryCassandra.replace("'", "~");

	    	        queryLog = "insert into "+schema+".query_log\r\n" + 
	    	        		   "(username, operation_date, log_ip, query, status)\r\n" + 
	    	        		   "values\r\n" + 
	    	        		   "('"+userId+"','"+formatter.format(date)+"','','"+queryCassandra+"',1)";
	    	        System.out.println(" logs query >>> "+queryLog);
	    	        template.select(queryLog, Location.class);
	    	        
	    	        
    	    	  
    	      }
      }
      
//        		  }
      
  }
}


      }
      
//    }
//    	  
//}
     

    if (provider.contains("2") || provider.equals("") ) // 2 cdr method
      {
    	  @SuppressWarnings("unused")
		String[] deviceArray = devices.split(",");
	  	  String   callingNo0  = "";
	  	  @SuppressWarnings("unused")
		String   callingNo1  = "";
		  
				if (!callingNo.equals("")) {
					String[] result = callingNo.split(",");

					for (int i = 0; i < result.length; i++) {
						if (i == 0) {
							callingNo0 = "" + result[i] + "";
						} else {
							callingNo0 = callingNo0 + ',' + "" + result[i] + "";
						}
						callingNo1 = "AND calling_no in (" + callingNo0 + ")";
					}
				}
		   
		   if(simulationType.equals("11") )
 	      {
				System.out.println("in TCD ");

					String finalImsiId = "";
					String ImsiId = "";
					 String   dates = "{" 
					         + " \"type\": \"range\"," 
							 + " \"field\": \"usage_timeframe\"," 
							 + " \"lower\": " + fromDatemillis + "," 
							 + " \"upper\": " + toDatemillis + "," 
							 + " \"include_lower\": true," 
							 + " \"include_upper\": true," 
							 + " \"doc_values\": true" 
							 + " },"; 

					if (!dataType.equals("")) {
						String[] result0 = dataType.split(",");

						for (int i = 0; i < result0.length; i++) {
							if (i == 0) {
								getProviderFilter = "" + result0[i] + "";
							} else {
								getProviderFilter = getProviderFilter + ',' + "" + result0[i] + "";
							}
						}
						conditionProviderFilter = "{\"type\": \"contains\",\"field\": \"service_provider_id\",\"values\":  ["
								+ getProviderFilter + " ]}\r\n";
					}

					if (!imsiId.equals("")) {
						String[] result = imsiId.split(",");

						for (int i = 0; i < result.length; i++) {
							if (i == 0) {
								finalImsiId = '"' + result[i] + '"';
							} else {
								finalImsiId = finalImsiId + ',' + '"' + result[i] + '"';
							}
						}
						ImsiId = "	{\"type\":\"contains\",\"field\": \"imsi_id\",\"values\":[ " + finalImsiId + "]} ,\r\n";
					}

					String TCDQuery = "	SELECT  imsi_id , imei_id, location_latitude, location_longitude,service_provider_id,"
							 + "usage_timeframe ,  cgi_id ,  location_azimuth , type_id , phone_number "
							 + " FROM  datacrowd.loc_location_cdr_main_new WHERE expr(idx01_cdr_main_new,"
							 + "'{" 
							 + " \"filter\":[" 
							 + ImsiId
							 + dates 
							 + conditionProviderFilter
							 + "   ]" 
							 + " }') ";

					byte[] TCDQueryResult = template.select(TCDQuery, CDRLocation.class).toString().getBytes();
					TCDQuery = TCDQuery.replace("'", "~");
					System.out.println(" TCD query >> " + TCDQuery);

					queryLog = "insert into " + schema + ".query_log\r\n"
							+ "(username, operation_date, log_ip, query, status)\r\n" + "values\r\n" + "('" + userId
							+ "','" +  formatter.format(date)  + "','','" + TCDQuery + "',1)";
					template.select(queryLog, CDRLocation.class);
					
					if (file_0.length() == 0) {
						buffer_2.write(new String(TCDQueryResult, StandardCharsets.UTF_8));
					} else {
						buffer_2.write("," + new String(TCDQueryResult, StandardCharsets.UTF_8));
					}

					buffer_2.flush();
					
				} else {
//    	    if(simulationType.equals("3") )
//    	      {
//  	      	  System.out.println(" DTP CDR ");
//
//    	    	  //,"+shapeId +"+ 0 as SHAPE_ID "+
//    	    	  
//    	    	  if (ArrayLength > 0) {
//    	  	        for (int i = 0; i < ArrayLength; i++) {
//    	  	        	
//    	  	        	System.out.println(" shape Id  ss >>>"+ObjArray.getJSONObject(i).getInt("shapeId") );
//    	  	        for (int j = 0 ; j < deviceArray.length ; j++)
//    	  	        {
//    	  	      	  if(!devices.equals("") )
//    	  	    	  {
////    	  	    		finalDevices = "AND calling_imei_id IN ("+devices+")";
//    	  	    		finalDevices = "AND calling_imei_id  = "+deviceArray[j]+" ";
//    	  	    	  }
//    	  	        	System.out.println("query 3 ");
//    	  	          String FilteringQuery = "SELECT 0+0 as location_main_data_id  , 0+0 as data_category, 0+0 as device_carrier_name, 0+0 as device_hit_count, calling_imei_id  as device_id,\r\n" + 
//    	  	          	" device_manufacturer_brand, device_model, 0+0 as location_accuracy, 0+0 as location_altitude, 0+0 as location_cou_code_num,\r\n" + 
//    	  	          	" 0+0 as location_density, start_location_latitude as location_latitude,start_location_longitude  as location_longitude, calling_no  as location_name, service_provider_id, 0+0 as  usage_date, \r\n" + 
//    	  	            " creation_date as usage_timeframe, 0+0 as usage_timeline,called_no,call_bdate,call_edate,calling_no,"+ObjArray.getJSONObject(i).getInt("shapeId") +"+ 0+0 as  as SHAPE_ID,0+0 as calling_imsi_id,0+0 as called_imsi_id,0+0 as start_location_bts_distance "+
//    	  	            " FROM  "+schema+".LOC_LOCATION_CDR_DATA " +
//    	  	            " WHERE expr(lucene_index2," +
//    	  	            " '{" +
//    	  	            "   \"filter\":[" +
//    	  	            		ObjArray.getJSONObject(i).getString("query") + query +
//    	  	            "   ]" +
//    	  	            "}')   and creation_date >  "+ fromDatemillis+" and creation_date < "+ toDatemillis+ " "+finalDevices+" "+callingNo1+"  allow filtering";
//    	  	          byte[] FilteringResult = template.select(FilteringQuery, Location.class).toString().getBytes();
//    	  	          
//    	  	        FilteringQuery = FilteringQuery.replace("'", "~");
//    	  	          System.out.println(" circle query >> " + FilteringQuery);
//
//    	  	          queryLog = "insert into "+schema+".query_log\r\n" + 
//    	  	       		   "(username, operation_date, log_ip, query, status)\r\n" + 
//    	  	       		   "values\r\n" + 
//    	  	       		   "('"+userId+"','"+formatter.format(date)+"','','"+FilteringQuery+"',1)";
//    	  	          
//    	  	          
//    	  	       System.out.println(" logs query >>> "+queryLog);
//    	  	       template.select(queryLog, Location.class);
//    	  	          
//									if (i == 0) {
//
//										if (file_0.length() == 0) {
//											buffer_2.write(new String(FilteringResult, StandardCharsets.UTF_8));
//										} else {
//											buffer_2.write("," + new String(FilteringResult, StandardCharsets.UTF_8));
//										}
//
//									} else {
//										buffer_2.write("," + new String(FilteringResult, StandardCharsets.UTF_8));
//									}
//
//    	  	          buffer_2.flush();
//    	  	          }
//    	  	        }
//    	  	      }
//    	      }
//    	      else if(simulationType.equals("1") )
//    	      {
//    	    	   	
//    	      	String[] deviceArray1 = devices.split(",");
//
//    	      	  System.out.println(" Activity Scan CDR ");
//	    		     System.out.println("  query recods  >>> "+query);
//	    		     
//	    	    	  if (!query_1.equals("")) {
//	    	    		  
//	    	    		  String serviceProviderFilter = "";
//	          	    	  String[] result0 = dataType.split(",");
//	            	 	   String getProviderFilter = "";
//	             	 	 
//							for (int i = 0; i < result0.length; i++) {
//								if (i == 0) {
//									getProviderFilter = "" + result0[i] + "";
//								} else {
//									getProviderFilter = getProviderFilter + ',' + "" + result0[i] + "";
//								}
//
//							}
//	    	    		  
//							if (!serviceProvider.equals("")) {
//								serviceProviderFilter = "  ,{" + "  \"type\": \"contains\","
//										+ "    \"field\": \"service_provider_id\"," + "  \"values\":  [ "
//										+ getProviderFilter + " ]" + "      }";
//							}
//	    	    		  
//	      	  	        for (int j = 0 ; j < deviceArray1.length ; j++)
//	      	  	        {
//	      	  	      	  if(!devices.equals("") )
//	      	  	    	  {
////		      	  	    		finalDevices = "AND calling_imei_id IN ("+devices+")";
//									finalDevices = "AND calling_imei_id  = "+deviceArray1[j]+" ";
//	      	  	    	  }
//	    	  	        	System.out.println("query 1 ");
//
//	    	    		     String queryCassandra_1 = "SELECT 0+0 as location_main_data_id  ,0+0 as data_category, 0+0 as device_carrier_name,0+0 as device_hit_count, calling_imei_id as device_id,\r\n" + 
//	    	     	  	          	" device_manufacturer_brand, device_model, 0+0 as location_accuracy, 0+0 as location_altitude, 0+0 as location_cou_code_num,\r\n" + 
//	    	     	  	          	" 0+0 as location_density, start_location_latitude as location_latitude,start_location_longitude  as location_longitude, calling_no  as location_name, service_provider_id, 0+0 as  usage_date,\r\n" + 
//	    	     	  	            " creation_date as usage_timeframe, 0+0 as usage_timeline,called_no,call_bdate,call_edate,calling_no,0+0 as calling_imsi_id,0+0 as called_imsi_id,0+0 as start_location_bts_distance "+
//	    	     	  	            " FROM  "+schema+".LOC_LOCATION_CDR_DATA " +
//	    	    	          "WHERE expr(lucene_index2," + 
//	    	    	          " '{" +
//	    	    	          "   \"filter\":[" +
//	    	    	          query_1 +
//	    	    	          serviceProviderFilter+
//	    	    	          "   ]" +
//	    	    	          "}')   and creation_date >  "+ fromDatemillis+" and creation_date < "+ toDatemillis+ " "+finalDevices+" " + callingNo1 + "   allow filtering";
//
//	    	    		     System.out.println(" cdr query >>> "+queryCassandra_1);
//	    	    		     
//	    	    	        byte[] loc = template.select(queryCassandra_1, Location.class).toString().getBytes();
//
//		  	  	              if (file_0.length() == 0) {
//		    	  	              buffer_2.write(new String(loc, StandardCharsets.UTF_8));
//			  	              }
//			  	              else
//			  	              {
//		    	  	              buffer_2.write("," + new String(loc, StandardCharsets.UTF_8));
//			  	              }
//	    	    	        buffer_2.flush();
//	    	    	        queryCassandra_1 = queryCassandra_1.replace("'", "~");
//	    	    	        queryLog = "insert into "+schema+".query_log\r\n" + 
//	    	    	        		   "(username, operation_date, log_ip, query, status)\r\n" + 
//	    	    	        		   "values\r\n" + 
//	    	    	        		   "('"+userId+"','"+formatter.format(date)+"','','"+queryCassandra_1+"',1)";
//	    	    	        System.out.println(" logs query >>> "+queryLog);
//	    	    	        template.select(queryLog, Location.class);
//	    	    	        
//	    	    	      }
//	    	    	    }
//
//    	    	  if (!query.equals("")) {
//    	    		  
//    	    		  String serviceProviderFilter = "";
//          	    	  String[] result0 = dataType.split(",");
//            	 	   String getProviderFilter = "";
//             	 	 
//          	 	    for (int i = 0; i < result0.length; i++) {
//          	 	      if (i == 0) {
//          	 	    	getProviderFilter = "" + result0[i] + "";
//          	 	      } else {
//          	 	    	getProviderFilter = getProviderFilter + ',' + "" + result0[i] + "";
//          	 	      }
//
//          	 	    }
//    	    		  
//    	  	    	  if(!serviceProvider.equals("") )
//    	  	    	  {
//    	  	            serviceProviderFilter = "  ,{" +
//    		    	              "         \"type\": \"contains\"," +
//    		    	              "         \"field\": \"service_provider_id\"," +
//    		    	              "         \"values\":  [ " + getProviderFilter + " ]" +
//    		    	              "      }";
//    		    	 }
//    	    		  
//      	  	        for (int j = 0 ; j < deviceArray1.length ; j++)
//      	  	        {
//      	  	      	  if(!devices.equals("") )
//      	  	    	  {
////	      	  	    		finalDevices = "AND calling_imei_id IN ("+devices+")";
//								finalDevices = "AND calling_imei_id  = "+deviceArray1[j]+" ";
//      	  	    	  }
//  	  	        	System.out.println("query 111");
//
//    	    		     String queryCassandra = "SELECT 0+0 as location_main_data_id  ,0+0 as data_category, 0+0 as device_carrier_name,0+0 as device_hit_count, calling_imei_id as device_id,\r\n" + 
//    	     	  	          	" device_manufacturer_brand, device_model, 0+0 as location_accuracy, 0+0 as location_altitude, 0+0 as location_cou_code_num,\r\n" + 
//    	     	  	          	" 0+0 as location_density, start_location_latitude as location_latitude,start_location_longitude  as location_longitude, calling_no  as location_name, service_provider_id, 0+0 as  usage_date,\r\n" + 
//    	     	  	            " creation_date as usage_timeframe, 0+0 as usage_timeline, called_no, call_bdate, call_edate, calling_no,0+0 as calling_imsi_id,0+0 as called_imsi_id,0+0 as start_location_bts_distance "+
//    	     	  	            " FROM  "+schema+".LOC_LOCATION_CDR_DATA " +
//    	    	          "WHERE expr(lucene_index2," + 
//    	    	          " '{" +
//    	    	          "   \"filter\":[" +
//    	    	          query +
//    	    	          serviceProviderFilter+
//    	    	          "   ]" +
//    	    	          "}')   and creation_date >  "+ fromDatemillis+" and creation_date < "+ toDatemillis+ " "+finalDevices+" " + callingNo1 + "   allow filtering";
//
//    	    		     System.out.println(" cdr query >>> 1 "+queryCassandra);
//    	    		     
//    	    	        byte[] loc = template.select(queryCassandra, Location.class).toString().getBytes();
//
//	  	  	              if (file_0.length() == 0) {
//	    	  	              buffer_2.write(new String(loc, StandardCharsets.UTF_8));
//		  	              }
//		  	              else
//		  	              {
//	    	  	              buffer_2.write("," + new String(loc, StandardCharsets.UTF_8));
//		  	              }
//	  	  	              
//    	    	        buffer_2.flush();
//    	    	        
//    	    	        queryCassandra = queryCassandra.replace("'", "~");
//
//    	    	        queryLog = "insert into "+schema+".query_log\r\n" + 
//    	    	        		   "(username, operation_date, log_ip, query, status)\r\n" + 
//    	    	        		   "values\r\n" + 
//    	    	        		   "('"+userId+"','"+formatter.format(date)+"','','"+queryCassandra+"',1)";
//    	    	        System.out.println(" logs query >>> "+queryLog);
//    	    	        template.select(queryLog, Location.class);
//    	    	        
//    	    	      }
//    	    	    }
//    	    	  System.out.println(" ArrayLength >>< "+ ArrayLength);
//    	    	  System.out.println(" > ObjArray > "+ ObjArray);
//    	    	      if (ArrayLength > 0) {
//    	    	        for (int i = 0; i < ArrayLength; i++) {
//    	    	          String serviceProviderFilter = "";
//
//    	    	          if (!ObjArray.getJSONObject(i).getString("serviceProvider").equals("")) {
//    	    	            serviceProviderFilter = " {" +
//    	    	              "         \"type\": \"contains\"," +
//    	    	              "         \"field\": \"service_provider_id\"," +
//    	    	              "         \"values\":  [ " + ObjArray.getJSONObject(i).getString("serviceProvider") + " ]" +
//    	    	              "      },";
//    	    	          }
//    	    	          
//  	      	  	        for (int j = 0 ; j < deviceArray1.length ; j++)
//  	      	  	        {
//  	      	  	      	  if(!devices.equals("") )
//  	      	  	    	  {
////  	      	  	    		finalDevices = "AND calling_imei_id IN ("+devices+")";
//  	      	  	    		finalDevices = "AND calling_imei_id  = "+deviceArray1[j]+" ";
//  	      	  	    	  }
//      	  	        	System.out.println("query 111 circle ");
//
//        	  	          String circleFilteringQuery = "SELECT 0+0 as location_main_data_id  , 0+0 as data_category, 0+0 as device_carrier_name, 0+0 as device_hit_count,calling_imei_id as device_id,\r\n" + 
//        	    	  	          	" device_manufacturer_brand, device_model, 0+0 as location_accuracy, 0+0 as location_altitude, 0+0 as location_cou_code_num,\r\n" + 
//        	    	  	          	" 0+0 as location_density, start_location_latitude as location_latitude,start_location_longitude  as location_longitude, calling_no  as location_name, service_provider_id, 0+0 as  usage_date, \r\n" + 
//        	    	  	            " creation_date as usage_timeframe, 0+0 as usage_timeline,called_no,call_bdate,call_edate,calling_no, "+ObjArray.getJSONObject(i).getInt("shapeId") +"+ 0+0 as SHAPE_ID ,0+0 as calling_imsi_id,0+0 as called_imsi_id,0+0 as start_location_bts_distance "+
//        	    	  	            " FROM  "+schema+".LOC_LOCATION_CDR_DATA " +
//    	    	            " WHERE expr(lucene_index2," +
//    	    	            " '{" +
//    	    	            "   \"filter\":[" +
////    	    	            "{" +
////    	    	            "         \"type\": \"range\"," +
////    	    	            "         \"field\": \"creation_date\"," +
////    	    	            "         \"lower\": " + ObjArray.getJSONObject(i).getLong("fromDate") + "," +
////    	    	            "         \"upper\": " + ObjArray.getJSONObject(i).getLong("toDate") + "," +
////    	    	            "         \"include_lower\": true," +
////    	    	            "         \"include_upper\": true," +
////    	    	            "         \"doc_values\": true" +
////    	    	            "      }," + 
//    	    	            serviceProviderFilter +
//    	    	            "      { type: \"geo_distance\"," +
//    	    	            " field: \"place\"," +
//    	    	            " latitude: " + ObjArray.getJSONObject(i).getString("latCircleCenter") + "," +
//    	    	            " longitude: " + ObjArray.getJSONObject(i).getString("lngCircleCenter") + "," +
//    	    	            " max_distance: \"" + ObjArray.getJSONObject(i).getString("radius") + " m\"}" +
//    	    	            "   ]" +
//    	    	            "}') and creation_date >  "+ fromDatemillis+" and creation_date < "+ toDatemillis+ " "+finalDevices+" "+callingNo1+"  allow filtering";
//    	    	          byte[] circleFilteringResult = template.select(circleFilteringQuery, Location.class).toString().getBytes();
//    	    	          
//    	    	          
//    	    	          circleFilteringQuery = circleFilteringQuery.replace("'", "~");
//    	    	          System.out.println(" circle query >> " + circleFilteringQuery);
//
//    	    	          queryLog = "insert into "+schema+".query_log\r\n" + 
//    	    	       		   "(username, operation_date, log_ip, query, status)\r\n" + 
//    	    	       		   "values\r\n" + 
//    	    	       		   "('"+userId+"','"+formatter.format(date)+"','','"+circleFilteringQuery+"',1)";
//    	    	          
//    	    	          
//    	    	       System.out.println(" logs query >>> "+queryLog);
//    	    	       template.select(queryLog, Location.class);
//    	    	          
//    	    	          if (query.equals("") && query_1.equals("")) {
//    	    	            if (i == 0) {
//
//		  	  	              if (file_0.length() == 0) {
//		    	  	              buffer_2.write(new String(circleFilteringResult, StandardCharsets.UTF_8));
//			  	              }
//			  	              else
//			  	              {
//		    	  	              buffer_2.write("," + new String(circleFilteringResult, StandardCharsets.UTF_8));
//			  	              }
//    	    	            	
//    	    	            } else {
//    	    	              buffer_2.write("," + new String(circleFilteringResult, StandardCharsets.UTF_8));
//    	    	            }
//    	    	          } else {
//    	    	            buffer_2.write("," + new String(circleFilteringResult, StandardCharsets.UTF_8));
//
//    	    	          }
//
//    	    	          buffer_2.flush();
//  	      	  	         }
//    	    	        }
//    	    	      }
//    	      }
//    	      else if(simulationType.equals("2") )
//    	      {
//    	      	  System.out.println(" Device History CDR ");
//    	      	  
//    	          String[] deviceArray2 = devices.split(",");
//
//    	    	  String serviceProviderCondition ="";
////    	    	  if(!serviceProvider.equals("") )
////    	    	  {
////    	    		  System.out.println(" in >>");
//////    	    		   serviceProviderCondition = "AND SERVICE_PROVIDER_ID IN ("+serviceProvider+")";  
////    	    		   serviceProviderCondition =
////    	    				  " expr(lucene_index2," +
////    	       	    	      " '{" +
////    	       	    	      "   \"filter\":[" +
////    	    				  "  {" +
//// 		    	              "         \"type\": \"contains\"," +
//// 		    	              "         \"field\": \"service_provider_id\"," +
//// 		    	              "         \"values\":  [ " + serviceProvider + " ]" +
//// 		    	              "      }"+
////    	    		   		  "]}') AND ";
////    	    		   
////    	    	  }
//    	    	  
//    	    	   if(!dataType.equals(""))
//    	    	   {
//    	    		   String[] result0 = dataType.split(",");
//    	    		   
//
//    	    		    for (int i = 0; i < result0.length; i++) {
//    	    		      if (i == 0) {
//    	    		    	  serviceProviderCondition = "" + result0[i] + "";
//    	    		      } else {
//    	    		    	  serviceProviderCondition = serviceProviderCondition + ',' + "" + result0[i] + "";
//    	    		      }
//
//    	    		    }
//    	    		    
//     	    		   serviceProviderCondition =
//     	    				  " expr(lucene_index2," +
//     	       	    	      " '{" +
//     	       	    	      "   \"filter\":[" +
//     	    				  "  {" +
//  		    	              "         \"type\": \"contains\"," +
//  		    	              "         \"field\": \"service_provider_id\"," +
//  		    	              "         \"values\":  [ " + serviceProviderCondition + " ]" +
//  		    	              "      }"+
//     	    		   		  "]}') AND ";
//    	    	   }
//
//    	    	  if (!devices.equals("") || !callingNo0.equals("")) {
//    	    		  
//							for (int j = 0; j < deviceArray2.length; j++) {
//								if (!devices.equals("")) {
////	      	  	        finalDevices = "AND calling_imei_id IN ("+devices+")";
//									finalDevices = "AND calling_imei_id  = " + deviceArray2[j] + " ";
//								}
//								System.out.println("query 1111 ");
//
//								String queryCassandra = "SELECT 0+0 as location_main_data_id  ,0+0 as data_category, 0+0 as device_carrier_name,0+0 as device_hit_count, calling_imei_id as device_id,\r\n"
//										+ " device_manufacturer_brand, device_model, 0+0 as location_accuracy, 0+0 as location_altitude, 0+0 as location_cou_code_num,\r\n"
//										+ " 0+0 as location_density, start_location_latitude as location_latitude,start_location_longitude  as location_longitude, calling_no  as location_name, service_provider_id, 0+0 as  usage_date,\r\n"
//										+ " creation_date as usage_timeframe, 0+0 as usage_timeline,called_no,call_bdate,call_edate,calling_no,0+0 as calling_imsi_id,0+0 as called_imsi_id,0+0 as start_location_bts_distance "
//										+ " FROM  " + schema + ".LOC_LOCATION_CDR_DATA " 
//										+ " WHERE "+ serviceProviderCondition + " creation_date >  " + fromDatemillis
//										+ " and creation_date < " + toDatemillis + " " + finalDevices + " " + callingNo1
//										+ "  allow filtering";
//
//								System.out.println(" DH query cdr >> " + queryCassandra);
//
//								byte[] loc = template.select(queryCassandra, Location.class).toString().getBytes();
//
//								if (file_0.length() == 0) {
//									buffer_2.write(new String(loc, StandardCharsets.UTF_8));
//								} else {
//									buffer_2.write("," + new String(loc, StandardCharsets.UTF_8));
//								}
//
//								buffer_2.flush();
//
//								queryCassandra = queryCassandra.replace("'", "~");
//
//								queryLog = "insert into " + schema + ".query_log\r\n"
//										+ "(username, operation_date, log_ip, query, status)\r\n" + "values\r\n" + "('"
//										+ userId + "','" + formatter.format(date) + "','','" + queryCassandra + "',1)";
//								System.out.println(" logs query >>> " + queryLog);
//								template.select(queryLog, Location.class);
//							}
// 	    	      }
//    	      }
      }

      }
      
      File f = new File(fullPath, fileData);
      @SuppressWarnings("resource")
	BufferedReader reader = new BufferedReader(new FileReader(f));
      byte[] contentFile = reader.readLine().toString().getBytes();
      finalresult=contentFile;
      buffer_1.write("[" + new String(contentFile, StandardCharsets.UTF_8) + "]");

      System.out.println(fileName + " successfully created ");

      buffer_1.flush();

      resp.setStatus("Success");



    } catch (Exception e) {
      e.printStackTrace();
    }

    return finalresult;
  }

  
  public static Set<String> getUniqueStringsBetweenCommas(String input) {
      Set<String> uniqueStrings = new LinkedHashSet<>();
      
      // Split the input string by commas
      String[] parts = input.split(",");
      
      // Loop over the parts and add non-empty strings to the set
      for (String part : parts) {
          // Trim the string to remove leading and trailing spaces
          String trimmedPart = part.trim();
          
          // Add non-empty strings to the set
          if (!trimmedPart.isEmpty()) {
              uniqueStrings.add(trimmedPart);
          }
      }

      return uniqueStrings;
  }
  

  public static String convertSetToString(Set<String> set) {
      // Use String.join to concatenate the elements with commas
      return String.join(",", set);
  }
  public static int calculateCommaSeparatedStringLength(String input) {
      // Remove any spaces in the input string
      String stringWithoutSpaces = input.replaceAll("\\s", "");

      // Calculate the length of the modified string
      // Split the input string by commas
      String[] substrings = stringWithoutSpaces.split(",");

      // Count the number of substrings
      int count = substrings.length;

      return count;
  }
  
@Override
public CustomResponse insertShape(String coordinates) {
	return null;
}

@Override
public 	String getCallingNo(String device){
	String callingNoQuery = "SELECT calling_no "+
	            "FROM  "+schema+".LOC_LOCATION_CDR_DATA " +
	            "WHERE calling_imei_id  = '"+device+"'  allow filtering";
	    
	String callingNo = template.select(callingNoQuery, String.class).toString();
return callingNo;
}

@Override
public String getKnowledgeGraphOneRecord(String devices ,String simulationId,long fromDatemillis,long toDatemillis,String callingNo,String calledNo,String linkType ,String directIndirect ,Long layer) {
	
	String status = "";
		try {
			System.out.println(" in getKnowledgeGraphOneRecord  ");
			System.out.println(" directIndirect>>>" + getCallingNo(devices));
			@SuppressWarnings("unused")
			String allDevices = "";

			if (!devices.equals("")) {
				String[] result = devices.split(",");
				String lastDevices = "";

				for (int i = 0; i < result.length; i++) {

					JSONArray array = new JSONArray(getCallingNo(result[i]));
					System.out.println(" array >>> " + array);

					for (int j = 0; j < array.length(); j++) {

						if (i == 0) {
							lastDevices = array.get(j).toString();
							System.out.println(" lastDevices 00 >>> " + lastDevices);
						} else {
							lastDevices = lastDevices + "," + array.get(j);
							System.out.println(" lastDevices 11 >>> " + lastDevices);
						}
					}
				}
				if (!callingNo.equals("")) {
					callingNo = callingNo + "," + lastDevices;
				} else {
					callingNo = lastDevices;
				}
				System.out.println(" callingNo >>> " + callingNo);
			}

			String fileName = "/knowledge_graph_" + simulationId + ".txt";
			String fullPath = urlPath;
			String fileData = "/knowledge_graph_data_" + simulationId + ".txt";

			File directory = new File(fullPath);
			directory.mkdirs();
			FileWriter file_1;
			FileWriter file_2;

			file_1 = new FileWriter(fullPath + fileName);
			file_2 = new FileWriter(fullPath + fileData);

			@SuppressWarnings("resource")
			BufferedWriter buffer_1 = new BufferedWriter(file_1);
			@SuppressWarnings("resource")
			BufferedWriter buffer_2 = new BufferedWriter(file_2);

			String callingDevice = "";
			if (!callingNo.equals("")) {
				String[] result = callingNo.split(",");

				for (int i = 0; i < result.length; i++) {

					if (i == 0) {
						callingDevice = "'" + result[i] + "'";
					} else {
						callingDevice = callingDevice + ',' + "'" + result[i] + "'";
					}

				}
			}

			String calledDevice = "";
			if (!calledNo.equals("")) {
				String[] result = calledNo.split(",");

				for (int i = 0; i < result.length; i++) {
					if (i == 0) {
						calledDevice = "'" + result[i] + "'";
					} else {
						calledDevice = calledDevice + ',' + "'" + result[i] + "'";
					}
				}
			}

			System.out.println(" directIndirect > >>>> " + directIndirect);

			String DevicesQuery = "";
			if (linkType.equals("1")) {
				// calling devices

				DevicesQuery = " SELECT 0+0 as location_main_data_id  ,country_code,0+0 as data_category, 0+0 as device_carrier_name,0+0 as device_hit_count, calling_imei_id as device_id,\r\n"
						+ " device_manufacturer_brand, device_model, 0+0 as location_accuracy, 0+0 as location_altitude, 0+0 as location_cou_code_num,\r\n"
						+ " 0+0 as location_density, start_location_latitude as location_latitude,start_location_longitude  as location_longitude, calling_no  as location_name, service_provider_id, 0+0 as  usage_date,\r\n"
						+ " creation_date as usage_timeframe, 0+0 as usage_timeline,called_no,call_bdate,call_edate,calling_no,0+0 as calling_imsi_id,0+0 as called_imsi_id,0+0 as start_location_bts_distance "
						+ " FROM  " + schema + ".LOC_LOCATION_CDR_DATA " + " WHERE call_bdate <=  " + toDatemillis
						+ " and call_edate >= " + fromDatemillis + "  and calling_no in (" + callingDevice
						+ ")  allow filtering";
			}

			byte[] callingDevicesResult = template.select(DevicesQuery, Location.class).toString().getBytes();

			File f = new File(fullPath, fileData);
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(f));

			if (!new String(callingDevicesResult).equals("[]")) {
				buffer_2.write(new String(callingDevicesResult, StandardCharsets.UTF_8));
				buffer_2.flush();
				System.out.println(fileData + " successfully created ");
				status = "Success";
				System.out.println("buffer_2   1 " + buffer_2);

			} else {
				System.out.println("noData");
				status = "noData";
				return status;
			}

//    // called devices 
// DevicesQuery = "SELECT 0+0 as location_main_data_id  ,0+0 as data_category, 0+0 as device_carrier_name,0+0 as device_hit_count, calling_imei_id as device_id,\r\n" + 
//	          	" device_manufacturer_brand, device_model, 0+0 as location_accuracy, 0+0 as location_altitude, 0+0 as location_cou_code_num,\r\n" + 
//	          	" 0+0 as location_density, start_location_latitude as location_latitude,start_location_longitude  as location_longitude, calling_no  as location_name, service_provider_id, 0+0 as  usage_date,\r\n" + 
//	            " creation_date as usage_timeframe, 0+0 as usage_timeline,called_no,call_bdate,call_edate,calling_no "+
//	            "FROM  "+schema+".LOC_LOCATION_CDR_DATA " +
//	            "WHERE call_bdate <=  "+  toDatemillis + " and call_edate >= " +  fromDatemillis + " and called_no in ("+calledDevice+")  allow filtering";
//  
//    System.out.println("DevicesQuery called = "+DevicesQuery);	  
//
// 
//byte[] CalledDevicesResult = template.select(DevicesQuery, Location.class).toString().getBytes();
//
//if(!CalledDevicesResult.equals(null)) {
//    buffer_2.write("," + new String(CalledDevicesResult, StandardCharsets.UTF_8));
//}

			if (!new String(callingDevicesResult).equals("[]")) {
				byte[] contentFile = reader.readLine().toString().getBytes();
				buffer_1.write("[" + new String(contentFile, StandardCharsets.UTF_8) + "]");
				buffer_1.flush();
				System.out.println(fileName + " successfully created ");
			}

		} catch (IOException e) {
			status = "Fail";
			e.printStackTrace();
		}

	return status;
}

@Override
public String getKnowledgeGraphRecords(String devices ,String simulationId,long fromDatemillis,long toDatemillis,String callingNo,String calledNo ,String linkType ,long layer,String status1) {

	
	
	String status = "";
	try {
		@SuppressWarnings("unused")
		String allDevices = "";	
		if(!devices.equals(""))
		{
			   String[] result = devices.split(",");
			   String lastDevices = "";
			   
			     for (int i = 0; i < result.length; i++) {
			    	 
				    	JSONArray array = new JSONArray(getCallingNo(result[i]));
				    	
				    	System.out.println(" array >>> "+ array);
				    	
					     for (int j = 0; j < array.length();j++) {
					    	 
					    	 if(i == 0)
					    	 {
					    		 lastDevices =  array.get(j).toString() ;
						    	 System.out.println(" lastDevices 00 >>> "+lastDevices);
					    	 }
					    	 else
					    	 {
					    		 lastDevices = lastDevices +","+ array.get(j) ;
						    	 System.out.println(" lastDevices 11 >>> "+lastDevices);
					    	 }

					     }

			       }
			     
			     if(!callingNo.equals(""))
			     {
					 	callingNo = callingNo + "," +lastDevices;
			     }
			     else
			     {
					 	callingNo = lastDevices;

			     }
			 	
			 	System.out.println(" callingNo >>> "+callingNo);
			
		}
		
		 
    String fileName = "/knowledge_graph_" + simulationId + ".txt";
    String fullPath = urlPath;
    String fileData = "/knowledge_graph_data_" + simulationId + ".txt";
    String fileData_1 = "/knowledge_graph_data_1_" + simulationId + ".txt";

    File directory = new File(fullPath);
    directory.mkdirs();
    FileWriter file_1;
    FileWriter file_3;

    file_1 = new FileWriter(fullPath + fileName);
    PrintWriter file_2 = new PrintWriter(new FileWriter(fullPath + fileData, true));

    file_3 = new FileWriter(fullPath + fileData_1);

    @SuppressWarnings("resource")
	BufferedWriter buffer_1 = new BufferedWriter(file_1);
    @SuppressWarnings("resource")
	BufferedWriter buffer_2 = new BufferedWriter(file_2);
    @SuppressWarnings("resource")
	BufferedWriter buffer_3 = new BufferedWriter(file_3);

    String callingDevice = "";
    if(!callingNo.equals(""))
    {
     String[] result = callingNo.split(",");
    

     for (int i = 0; i < result.length; i++) {
       if (i == 0) {
    	   callingDevice = "'" + result[i] + "'";
       } else {
    	   callingDevice = callingDevice + ',' + "'" + result[i] + "'";
       }

     }
    }
    
    String calledDevice = "";
    if(!calledNo.equals(""))
    {
     String[] result = calledNo.split(",");

     for (int i = 0; i < result.length; i++) {
       if (i == 0) {
    	   calledDevice = "'" + result[i] + "'";
       } else {
    	   calledDevice = calledDevice + ',' + "'" + result[i] + "'";
       }

     }
    }
    
    String DevicesQuery =  "";
    
    // calling devices
	 DevicesQuery = "SELECT 0+0 as location_main_data_id  ,0+0 as data_category, 0+0 as device_carrier_name,0+0 as device_hit_count, calling_imei_id as device_id,\r\n" 
			 		+ " device_manufacturer_brand, device_model, 0+0 as location_accuracy, 0+0 as location_altitude, 0+0 as location_cou_code_num,\r\n" 
		          	+ " 0+0 as location_density, start_location_latitude as location_latitude,start_location_longitude  as location_longitude, calling_no  as location_name, service_provider_id, 0+0 as  usage_date,\r\n"
		          	+ " creation_date as usage_timeframe, 0+0 as usage_timeline,called_no,call_bdate,call_edate,calling_no ,0+0 as calling_imsi_id,0+0 as called_imsi_id,0+0 as start_location_bts_distance "
		          	+ "FROM  "+schema+".LOC_LOCATION_CDR_DATA " +
	      "WHERE call_bdate <=  "+ toDatemillis + " and call_edate >= " + fromDatemillis+ "  and calling_no in ("+callingDevice+")  allow filtering";
	    
	      byte[] callingDevicesResult = template.select(DevicesQuery, Location.class).toString().getBytes();

	    System.out.println("DevicesQuery calling = "+DevicesQuery);
	    
	    
//		 if(new String(callingDevicesResult).equals("[]"))
//		    {
//				status = "noData";
//				return status;
//		    }
	    
	    

//	    // called devices 
//	 DevicesQuery = "SELECT 0+0 as location_main_data_id  ,0+0 as data_category, 0+0 as device_carrier_name,0+0 as device_hit_count, calling_imei_id as device_id,\r\n" + 
//		          	" device_manufacturer_brand, device_model, 0+0 as location_accuracy, 0+0 as location_altitude, 0+0 as location_cou_code_num,\r\n" + 
//		          	" 0+0 as location_density, start_location_latitude as location_latitude,start_location_longitude  as location_longitude, calling_no  as location_name, service_provider_id, 0+0 as  usage_date,\r\n" + 
//		            " creation_date as usage_timeframe, 0+0 as usage_timeline,called_no,call_bdate,call_edate,calling_no "+
//		            "FROM  "+schema+".LOC_LOCATION_CDR_DATA " +
//		            "WHERE call_bdate <=  "+  toDatemillis + " and call_edate >= " +  fromDatemillis + " and called_no in ("+calledDevice+")  allow filtering";
//	  
//	    System.out.println("DevicesQuery called = "+DevicesQuery);	  
//
//	 
//    byte[] CalledDevicesResult = template.select(DevicesQuery, Location.class).toString().getBytes();
//    
//    if(!CalledDevicesResult.equals(null)) {
//	    buffer_2.write("," + new String(CalledDevicesResult, StandardCharsets.UTF_8));
//    }
    
    File f = new File(fullPath, fileData);
    @SuppressWarnings("resource")
	BufferedReader reader = new BufferedReader(new FileReader(f));

//    if(!new String(callingDevicesResult).equals("[]"))
    
    System.out.println("layer >>>>"+layer);
    System.out.println("status1 >>>>"+status1);
    System.out.println(" fromDatemillis >> "+fromDatemillis);
    
    
    	if(!status1.equals("done"))
    {
    		
    	    System.out.println("layer 00>>>>"+layer);
    	    System.out.println("status1 00>>>>"+status1);
	    if(reader.ready())
	    {
		    buffer_2.write(","+new String(callingDevicesResult, StandardCharsets.UTF_8));
	    }else
	    {
	        buffer_2.write(new String(callingDevicesResult, StandardCharsets.UTF_8));
	    }
	    buffer_2.flush();
	    			
	    buffer_3.write(new String(callingDevicesResult, StandardCharsets.UTF_8));
	    buffer_3.flush();
	    status = "Success";
        System.out.println(fileData + " successfully created ");

    }
    else
    {
        System.out.println("layer 11>>>>"+layer);
        System.out.println("status1 11>>>>"+status1);
    	  if(reader.ready())
  	    {
		    byte[] contentFile = reader.readLine().toString().getBytes();
		        buffer_1.write("[" + new String(contentFile, StandardCharsets.UTF_8) + "]");
		        buffer_1.flush();
		        System.out.println(fileName + " successfully created ");
		        status = "Done";
  	    }
    	  else
    	  {
    		  status = "noData";
    	  }
	   }
    
	} catch (IOException e) {
		status = "Fail";
		e.printStackTrace();
	}

	return status;
}


private static String devicesElements(String devices) {
	devices = devices.substring(1, devices.length() - 1);
	devices = devices.replace("','", "\",\"");
	devices = "\"" + devices + "\"";

    return devices;
}

}
