/*
 * package com.valoores.cassandradatacrowd.common.service.impl;
 * 
 * import java.io.BufferedReader; import java.io.BufferedWriter; import
 * java.io.File; import java.io.FileReader; import java.io.FileWriter; import
 * java.nio.charset.StandardCharsets; import java.text.SimpleDateFormat; import
 * java.util.Date;
 * 
 * import javax.servlet.http.HttpServletRequest;
 * 
 * import org.json.JSONArray; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.beans.factory.annotation.Value; import
 * org.springframework.data.cassandra.core.CassandraOperations; import
 * org.springframework.data.cassandra.core.CassandraTemplate; import
 * org.springframework.stereotype.Service;
 * 
 * import com.valoores.cassandradatacrowd.app.model.Location; import
 * com.valoores.cassandradatacrowd.common.dto.log; import
 * com.valoores.cassandradatacrowd.common.repository.logRepository; import
 * com.valoores.cassandradatacrowd.common.service.logService;
 * 
 * @Service public class logServiceImpl implements logService{
 * 
 * @Autowired private logRepository logRepository;
 * 
 * @Autowired private CassandraTemplate template;
 * 
 * @Autowired private CassandraOperations cassandraTemplate;
 * 
 * @Value("${application.repository.urlPath}") private String urlPath;
 * 
 * @Override public log insertLog(String query, String simulatuserionId, String
 * whereCondition, JSONArray ObjArray ,String simulationType,String
 * provider,long fromDatemillis,long toDatemillis,String allDevices, String
 * dataType,HttpServletRequest request) {
 * 
 * log resp = log.builder().build();
 * 
 * 
 * String schema = "datacrowd"; SimpleDateFormat formatter = new
 * SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); Date date = new Date(); String
 * userId = ""; String devices = ""; String finalDevices =""; String
 * serviceProvider = "";
 * 
 * E if(!dataType.equals("")) { String[] result0 = dataType.split(",");
 * 
 * 
 * for (int i = 0; i < result0.length; i++) { if (i == 0) { serviceProvider =
 * "'" + result0[i] + "'"; } else { serviceProvider = serviceProvider + ',' +
 * "'" + result0[i] + "'"; }
 * 
 * } }
 * 
 * if(!allDevices.equals("")) { String[] result = allDevices.split(",");
 * 
 * 
 * for (int i = 0; i < result.length; i++) { if (i == 0) { devices = "'" +
 * result[i] + "'"; } else { devices = devices + ',' + "'" + result[i] + "'"; }
 * 
 * } }
 * 
 * 
 * 
 * try {
 * 
 * if(request.getSession().getAttribute("userName") != null) { userId =
 * request.getSession().getAttribute("userName").toString(); } else { userId =
 * "no user"; }
 * 
 * String table = ""; String columns = ""; String fileData = "/data_crowd_data_"
 * + simulationId + ".txt"; String fileName = "/data_crowd_" + simulationId +
 * ".txt"; String fullPath = urlPath;
 * 
 * File file_0 = new File(fullPath+fileData);
 * 
 * Integer ArrayLength = ObjArray.length();
 * 
 * File directory = new File(fullPath); directory.mkdirs(); FileWriter file_1;
 * FileWriter file_2;
 * 
 * file_1 = new FileWriter(fullPath + fileName); file_2 = new
 * FileWriter(fullPath + fileData);
 * 
 * BufferedWriter buffer_1 = new BufferedWriter(file_1); BufferedWriter buffer_2
 * = new BufferedWriter(file_2);
 * 
 * System.out.println(" provider = ' "+provider+"'");
 * 
 * if(provider.contains("3") || provider.equals("") ) // geo {
 * 
 * if(simulationType.equals("3") ) { System.out.println(" DTP GEO ");
 * 
 * if (ArrayLength > 0) { for (int i = 0; i < ArrayLength; i++) { String
 * FilteringQuery=""; E String FilteringQuery =
 * "SELECT location_main_data_id  , data_category, device_carrier_name, device_hit_count, device_id,\r\n"
 * +
 * " device_manufacturer_brand, device_model, location_accuracy, location_altitude, location_cou_code_num,\r\n"
 * +
 * " location_density, location_latitude, location_longitude, location_name, service_provider_id, usage_date,\r\n"
 * +
 * " usage_timeframe, usage_timeline,0+0 as called_no,0+0 as call_bdate,0+0 as call_edate,0+0 as calling_no,"
 * +ObjArray.getJSONObject(i).getInt("shapeId") +"+ 0 as SHAPE_ID "+
 * "FROM  "+schema+".loc_location_main_data " + "WHERE expr(lucene_index," +
 * " '{" + "   \"filter\":[" + ObjArray.getJSONObject(i).getString("query") +
 * query + "   ]" + "}')  and usage_timeframe >  "+
 * fromDatemillis+" and usage_timeframe < "+ toDatemillis+ " " + whereCondition
 * + " allow filtering";
 * 
 * byte[] FilteringResult = template.select(FilteringQuery,
 * Location.class).toString().getBytes();
 * 
 * 
 * 
 * String queryLog = "insert into "+schema+".query_log\r\n" +
 * "(username, operation_date, log_ip, query, status)\r\n" + "values\r\n" +
 * "('"+userId+"','"+formatter.format(date)+"','','"+FilteringQuery+
 * "','success')";
 * 
 * 
 * System.out.println(" logs query >>> "+queryLog); E template.select(queryLog,
 * location.class);
 * 
 * E if (i == 0) { buffer_2.write(new String(FilteringResult,
 * StandardCharsets.UTF_8)); } else { buffer_2.write("," + new
 * String(FilteringResult, StandardCharsets.UTF_8)); }
 * 
 * buffer_2.flush();
 * 
 * } } } else if(simulationType.equals("1")) {
 * 
 * System.out.println(" Activity Scan GEO");
 * 
 * if (!query.equals("") ) { E String queryCassandra =
 * "SELECT location_main_data_id  , data_category, device_carrier_name, device_hit_count, device_id,"
 * +
 * " device_manufacturer_brand, device_model, location_accuracy, location_altitude, location_cou_code_num,"
 * +
 * " location_density, location_latitude, location_longitude, location_name, service_provider_id, usage_date, "
 * +
 * "usage_timeframe, usage_timeline,0+0 as called_no,0+0 as call_bdate,0+0 as call_edate,0+0 as calling_no "
 * + "FROM "+schema+".loc_location_main_data " + "WHERE expr(lucene_index," +
 * " '{" + "   \"filter\":[" + query + "   ]" + "}') and usage_timeframe >  "+
 * fromDatemillis+" and usage_timeframe < "+ toDatemillis+ " " + whereCondition
 * + " allow filtering";
 * 
 * System.out.println(" query all shapes  >>" +queryCassandra); String
 * queryCassandra=""; byte[] loc = template.select(queryCassandra,
 * Location.class).toString().getBytes(); E buffer_2.write(new String(loc,
 * StandardCharsets.UTF_8)); buffer_2.flush();
 * 
 * 
 * 
 * 
 * String queryLog = "insert into "+schema+".query_log\r\n" +
 * "(username, operation_date, log_ip, query, status)\r\n" + "values\r\n" +
 * "('"+userId+"','"+formatter.format(date)+"','','"+queryCassandra+
 * "','success')"; System.out.println(" logs query >>> "+queryLog); E
 * template.select(queryLog, location.class);
 * 
 * }
 * 
 * if (ArrayLength > 0) { for (int i = 0; i < ArrayLength; i++) { String
 * serviceProviderFilter = "";
 * 
 * E if (!ObjArray.getJSONObject(i).getString("serviceProvider").equals("")) {
 * serviceProviderFilter = " {" + "         \"type\": \"contains\"," +
 * "         \"field\": \"service_provider_id\"," + "         \"values\":  [ " +
 * ObjArray.getJSONObject(i).getString("serviceProvider") + " ]" + "      },"; }
 * 
 * 
 * String circleFilteringQuery = ""; E String circleFilteringQuery =
 * "SELECT location_main_data_id  , data_category, device_carrier_name, device_hit_count, device_id,"
 * +
 * " device_manufacturer_brand, device_model, location_accuracy, location_altitude, location_cou_code_num,"
 * +
 * " location_density, location_latitude, location_longitude, location_name, service_provider_id, usage_date, "
 * +
 * "usage_timeframe, usage_timeline,0+0 as called_no,0+0 as call_bdate,0+0 as call_edate,0+0 as calling_no,"
 * +ObjArray.getJSONObject(i).getInt("shapeId") +"+ 0 as SHAPE_ID "+
 * "FROM "+schema+".loc_location_main_data " + "WHERE expr(lucene_index," +
 * " '{" + "   \"filter\":[" + // "{" + // "         \"type\": \"range\"," + //
 * "         \"field\": \"usage_timeframe\"," + // "         \"lower\": " +
 * ObjArray.getJSONObject(i).getLong("fromDate") + "," + //
 * "         \"upper\": " + ObjArray.getJSONObject(i).getLong("toDate") + "," +
 * // "         \"include_lower\": true," + //
 * "         \"include_upper\": true," + // "         \"doc_values\": false" +
 * // "      }," + E serviceProviderFilter + "      { type: \"geo_distance\"," +
 * " field: \"place\"," + " latitude: " +
 * ObjArray.getJSONObject(i).getString("latCircleCenter") + "," + " longitude: "
 * + ObjArray.getJSONObject(i).getString("lngCircleCenter") + "," +
 * " max_distance: \"" + ObjArray.getJSONObject(i).getString("radius") + " m\"}"
 * + "   ]" + "}') and usage_timeframe >  "+
 * fromDatemillis+" and usage_timeframe < "+ toDatemillis+ " " + whereCondition
 * + " allow filtering";
 * 
 * byte[] circleFilteringResult = template.select(circleFilteringQuery,
 * Location.class).toString().getBytes();
 * 
 * 
 * E circleFilteringQuery = circleFilteringQuery.replace("'", "~");
 * System.out.println(" circle query >> " + circleFilteringQuery);
 * 
 * 
 * String queryLog = "insert into "+schema+".query_log\r\n" +
 * "(username, operation_date, log_ip, query, status)\r\n" + "values\r\n" +
 * "('"+userId+"','"+formatter.format(date)+"','','"+circleFilteringQuery+
 * "','success')";
 * 
 * 
 * System.out.println(" logs query >>> "+queryLog); template.select(queryLog,
 * log.class);
 * 
 * E if (query.equals("")) { if (i == 0) { buffer_2.write(new
 * String(circleFilteringResult, StandardCharsets.UTF_8)); } else {
 * buffer_2.write("," + new String(circleFilteringResult,
 * StandardCharsets.UTF_8)); } } else { buffer_2.write("," + new
 * String(circleFilteringResult, StandardCharsets.UTF_8));
 * 
 * }
 * 
 * buffer_2.flush();
 * 
 * } } }else if(simulationType.equals("2")) { System.out.println(" DTP GEO ");
 * 
 * if (!whereCondition.equals("") ) { E String queryCassandra =
 * "SELECT location_main_data_id  , data_category, device_carrier_name, device_hit_count, device_id,"
 * +
 * " device_manufacturer_brand, device_model, location_accuracy, location_altitude, location_cou_code_num,"
 * +
 * " location_density, location_latitude, location_longitude, location_name, service_provider_id, usage_date, "
 * +
 * "usage_timeframe, usage_timeline,0+0 as called_no,0+0 as call_bdate,0+0 as call_edate,0+0 as calling_no "
 * + "FROM "+schema+".loc_location_main_data " + "WHERE usage_timeframe >  "+
 * fromDatemillis+" and usage_timeframe < "+ toDatemillis+ " " + whereCondition
 * + " allow filtering";
 * 
 * 
 * System.out.println(" DH query geo >> "+queryCassandra);
 * 
 * 
 * String queryCassandra = ""; byte[] loc = template.select(queryCassandra,
 * Location.class).toString().getBytes(); buffer_2.write(new String(loc,
 * StandardCharsets.UTF_8)); buffer_2.flush();
 * 
 * queryCassandra = queryCassandra.replace("'", "~");
 * 
 * String queryLog = "insert into "+schema+".query_log\r\n" +
 * "(username, operation_date, log_ip, query, status)\r\n" + "values\r\n" +
 * "('"+userId+"','"+formatter.format(date)+"','','"+queryCassandra+
 * "','success')"; System.out.println(" logs query >>> "+queryLog);
 * template.select(queryLog, log.class);
 * 
 * }
 * 
 * } }
 * 
 * 
 * if (provider.contains("2") || provider.equals("") ) // 2 cdr method {
 * 
 * if(!devices.equals("") ) { finalDevices = "AND CALLING_NO IN ("+devices+")";
 * }
 * 
 * if(simulationType.equals("3") ) { System.out.println(" DTP CDR ");
 * 
 * //,"+shapeId +"+ 0 as SHAPE_ID "+
 * 
 * if (ArrayLength > 0) { for (int i = 0; i < ArrayLength; i++) {
 * 
 * System.out.println(" shape Id  ss >>>"+ObjArray.getJSONObject(i).getInt(
 * "shapeId") ); String FilteringQuery=""; E String FilteringQuery =
 * "SELECT 0+0 as location_main_data_id  , 0+0 as data_category, 0+0 as device_carrier_name, 0+0 as device_hit_count, calling_imei_id  as device_id,\r\n"
 * +
 * " device_manufacturer_brand, device_model, 0+0 as location_accuracy, 0+0 as location_altitude, 0+0 as location_cou_code_num,\r\n"
 * +
 * " 0+0 as location_density, start_location_latitude as location_latitude,start_location_longitude  as location_longitude, calling_no  as location_name, service_provider_id, 0+0 as  usage_date, \r\n"
 * +
 * " creation_date as usage_timeframe, 0+0 as usage_timeline,called_no,call_bdate,call_edate,calling_no,"
 * +ObjArray.getJSONObject(i).getInt("shapeId") +"+ 0 as SHAPE_ID "+
 * "FROM  "+schema+".LOC_LOCATION_CDR_DATA " + "WHERE expr(lucene_index2," +
 * " '{" + "   \"filter\":[" + ObjArray.getJSONObject(i).getString("query") +
 * query + "   ]" + "}')   and creation_date >  "+
 * fromDatemillis+" and creation_date < "+ toDatemillis+
 * " "+finalDevices+" allow filtering";
 * 
 * byte[] FilteringResult = template.select(FilteringQuery,
 * Location.class).toString().getBytes();
 * 
 * 
 * FilteringQuery = FilteringQuery.replace("'", "~");
 * System.out.println(" circle query >> " + FilteringQuery);
 * 
 * String queryLog = "insert into "+schema+".query_log\r\n" +
 * "(username, operation_date, log_ip, query, status)\r\n" + "values\r\n" +
 * "('"+userId+"','"+formatter.format(date)+"','','"+FilteringQuery+
 * "','success')";
 * 
 * 
 * System.out.println(" logs query >>> "+queryLog); template.select(queryLog,
 * log.class);
 * 
 * if (i == 0) {
 * 
 * if (file_0.length() == 0) { buffer_2.write(new String(FilteringResult,
 * StandardCharsets.UTF_8)); } else { buffer_2.write("," + new
 * String(FilteringResult, StandardCharsets.UTF_8)); }
 * 
 * } else { buffer_2.write("," + new String(FilteringResult,
 * StandardCharsets.UTF_8)); }
 * 
 * buffer_2.flush(); } } } else if(simulationType.equals("1") ) {
 * System.out.println(" Activity Scan CDR ");
 * 
 * if (!query.equals("")) { String queryCassandra=""; E String queryCassandra =
 * "SELECT 0+0 as location_main_data_id  ,0+0 as data_category, 0+0 as device_carrier_name,0+0 as device_hit_count, calling_imei_id as device_id,\r\n"
 * +
 * " device_manufacturer_brand, device_model, 0+0 as location_accuracy, 0+0 as location_altitude, 0+0 as location_cou_code_num,\r\n"
 * +
 * " 0+0 as location_density, start_location_latitude as location_latitude,start_location_longitude  as location_longitude, calling_no  as location_name, service_provider_id, 0+0 as  usage_date,\r\n"
 * +
 * " creation_date as usage_timeframe, 0+0 as usage_timeline,called_no,call_bdate,call_edate,calling_no "
 * + "FROM  "+schema+".LOC_LOCATION_CDR_DATA " + "WHERE expr(lucene_index2," +
 * " '{" + "   \"filter\":[" + query + "   ]" + "}')   and creation_date >  "+
 * fromDatemillis+" and creation_date < "+ toDatemillis+
 * " "+finalDevices+"  allow filtering";
 * 
 * byte[] loc = template.select(queryCassandra,
 * Location.class).toString().getBytes();
 * 
 * if (file_0.length() == 0) { buffer_2.write(new String(loc,
 * StandardCharsets.UTF_8)); } else { buffer_2.write("," + new String(loc,
 * StandardCharsets.UTF_8)); }
 * 
 * buffer_2.flush();
 * 
 * E queryCassandra = queryCassandra.replace("'", "~");
 * 
 * String queryLog = "insert into "+schema+".query_log\r\n" +
 * "(username, operation_date, log_ip, query, status)\r\n" + "values\r\n" +
 * "('"+userId+"','"+formatter.format(date)+"','','"+queryCassandra+
 * "','success')"; System.out.println(" logs query >>> "+queryLog);
 * template.select(queryLog, log.class);
 * 
 * }
 * 
 * if (ArrayLength > 0) { for (int i = 0; i < ArrayLength; i++) { String
 * serviceProviderFilter = "";
 * 
 * if (!ObjArray.getJSONObject(i).getString("serviceProvider").equals("")) {
 * serviceProviderFilter = " {" + "         \"type\": \"contains\"," +
 * "         \"field\": \"service_provider_id\"," + "         \"values\":  [ " +
 * ObjArray.getJSONObject(i).getString("serviceProvider") + " ]" + "      },"; }
 * 
 * String circleFilteringQuery=""; E String circleFilteringQuery =
 * "SELECT 0+0 as location_main_data_id  , 0+0 as data_category, 0+0 as device_carrier_name, 0+0 as device_hit_count,calling_imei_id as device_id,\r\n"
 * +
 * " device_manufacturer_brand, device_model, 0+0 as location_accuracy, 0+0 as location_altitude, 0+0 as location_cou_code_num,\r\n"
 * +
 * " 0+0 as location_density, start_location_latitude as location_latitude,start_location_longitude  as location_longitude, calling_no  as location_name, service_provider_id, 0+0 as  usage_date, \r\n"
 * +
 * " creation_date as usage_timeframe, 0+0 as usage_timeline,called_no,call_bdate,call_edate,calling_no, "
 * +ObjArray.getJSONObject(i).getInt("shapeId") +"+ 0 as SHAPE_ID "+
 * "FROM  "+schema+".LOC_LOCATION_CDR_DATA " + "WHERE expr(lucene_index2," +
 * " '{" + "   \"filter\":[" +
 * 
 * 
 * // "{" + // "         \"type\": \"range\"," + //
 * "         \"field\": \"creation_date\"," + // "         \"lower\": " +
 * ObjArray.getJSONObject(i).getLong("fromDate") + "," + //
 * "         \"upper\": " + ObjArray.getJSONObject(i).getLong("toDate") + "," +
 * // "         \"include_lower\": true," + //
 * "         \"include_upper\": true," + // "         \"doc_values\": false" +
 * // "      }," + E serviceProviderFilter + "      { type: \"geo_distance\"," +
 * " field: \"place\"," + " latitude: " +
 * ObjArray.getJSONObject(i).getString("latCircleCenter") + "," + " longitude: "
 * + ObjArray.getJSONObject(i).getString("lngCircleCenter") + "," +
 * " max_distance: \"" + ObjArray.getJSONObject(i).getString("radius") + " m\"}"
 * + "   ]" + "}') and creation_date >  "+
 * fromDatemillis+" and creation_date < "+ toDatemillis+
 * " "+finalDevices+"  allow filtering";
 * 
 * byte[] circleFilteringResult = template.select(circleFilteringQuery,
 * Location.class).toString().getBytes();
 * 
 * 
 * circleFilteringQuery = circleFilteringQuery.replace("'", "~");
 * System.out.println(" circle query >> " + circleFilteringQuery);
 * 
 * String queryLog = "insert into "+schema+".query_log\r\n" +
 * "(username, operation_date, log_ip, query, status)\r\n" + "values\r\n" +
 * "('"+userId+"','"+formatter.format(date)+"','','"+circleFilteringQuery+
 * "','success')";
 * 
 * 
 * System.out.println(" logs query >>> "+queryLog); template.select(queryLog,
 * log.class);
 * 
 * 
 * if (query.equals("")) { if (i == 0) {
 * 
 * if (file_0.length() == 0) { buffer_2.write(new String(circleFilteringResult,
 * StandardCharsets.UTF_8)); } else { buffer_2.write("," + new
 * String(circleFilteringResult, StandardCharsets.UTF_8)); }
 * 
 * } else { buffer_2.write("," + new String(circleFilteringResult,
 * StandardCharsets.UTF_8)); } } else { buffer_2.write("," + new
 * String(circleFilteringResult, StandardCharsets.UTF_8));
 * 
 * }
 * 
 * buffer_2.flush();
 * 
 * } } } else if(simulationType.equals("2") ) {
 * System.out.println(" Device History CDR ");
 * 
 * String serviceProviderCondition =""; if(!serviceProvider.equals("") ) {
 * System.out.println(" in >>"); serviceProviderCondition =
 * "AND SERVICE_PROVIDER_ID IN ("+serviceProvider+")"; }
 * 
 * if (!devices.equals("")) { String queryCassandra=""; E String queryCassandra
 * =
 * "SELECT 0+0 as location_main_data_id  ,0+0 as data_category, 0+0 as device_carrier_name,0+0 as device_hit_count, calling_imei_id as device_id,\r\n"
 * +
 * " device_manufacturer_brand, device_model, 0+0 as location_accuracy, 0+0 as location_altitude, 0+0 as location_cou_code_num,\r\n"
 * +
 * " 0+0 as location_density, start_location_latitude as location_latitude,start_location_longitude  as location_longitude, calling_no  as location_name, service_provider_id, 0+0 as  usage_date,\r\n"
 * +
 * " creation_date as usage_timeframe, 0+0 as usage_timeline,called_no,call_bdate,call_edate,calling_no "
 * + "FROM  "+schema+".LOC_LOCATION_CDR_DATA " + "WHERE   creation_date >  "+
 * fromDatemillis+" and creation_date < "+ toDatemillis+" "+
 * finalDevices+" "+serviceProviderCondition+" allow filtering";
 * 
 * System.out.println(" DH query cdr >> "+queryCassandra);
 * 
 * byte[] loc = template.select(queryCassandra,
 * Location.class).toString().getBytes();
 * 
 * if (file_0.length() == 0) { buffer_2.write(new String(loc,
 * StandardCharsets.UTF_8)); } else { buffer_2.write("," + new String(loc,
 * StandardCharsets.UTF_8)); }
 * 
 * buffer_2.flush();
 * 
 * queryCassandra = queryCassandra.replace("'", "~");
 * 
 * String queryLog = "insert into "+schema+".query_log\r\n" +
 * "(username, operation_date, log_ip, query, status)\r\n" + "values\r\n" +
 * "('"+userId+"','"+formatter.format(date)+"','','"+queryCassandra+
 * "','success')"; System.out.println(" logs query >>> "+queryLog);
 * template.select(queryLog, log.class);
 * 
 * } }
 * 
 * }
 * 
 * 
 * 
 * File f = new File(fullPath, fileData); BufferedReader reader = new
 * BufferedReader(new FileReader(f)); byte[] contentFile =
 * reader.readLine().toString().getBytes(); buffer_1.write("[" + new
 * String(contentFile, StandardCharsets.UTF_8) + "]");
 * System.out.println(fileName + " successfully created ");
 * 
 * buffer_1.flush();
 * 
 * resp.setStatus("Success");
 * 
 * } catch (Exception e) { e.printStackTrace(); }
 * 
 * return resp; }
 * 
 * 
 * }
 */