package com.valoores.cassandra.app.api;

import java.awt.Point;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.valoores.cassandra.app.dto.DateDto;
import com.valoores.cassandra.app.dto.SimulationDto;
import com.valoores.cassandra.app.service.CassandraSparkService;

@RestController
@RequestMapping("/api")
public class CassandraSparkController {

	
	
	  @Autowired
	  private CassandraTemplate template;
	  
	  
	@Autowired
	private CassandraSparkService cassandrasparkservice;

	@PostMapping("/getAllData1")
	public byte[] getAllData1(@RequestParam long simulationId) {
		return cassandrasparkservice.getAllData1( simulationId);
	}

	@PostMapping("/getObject")
	public byte[] getObject(@RequestBody String[] myObject) {
		try {
			System.out.println("statrt c ");
			JSONObject obj = new JSONObject(myObject);
			String devices = obj.getString("devices");

			String FromDate   = obj.getString("fromDatemillis");
			String callingNo  = obj.getString("callingNo");
			String toDate     = obj.getString("toDatemillis");
			String linkType   = obj.getString("linkType");
			String connection = obj.getString("directIndirect");
			String layers     = obj.getString("layer");

			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return cassandrasparkservice.getObject(myObject);
	}

	@PostMapping("/convertStringToArray")
	public String convertStringToArray(@RequestBody String object1) {

		String[] Array = object1.split(",");
		String joined  = String.join("','", Array);
		String result  = "'" + joined + "'";
		return result;
	}

	@PostMapping("/copyDataToOracle")
	public String copyDataToOracle() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return cassandrasparkservice.copyDataToOracle();
	}


	@GetMapping("/convertDateRangeToJson")
	public String convertDateRangeToJson(@RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate) {
		Gson gson = new Gson();
		
		String[] startDateParts = startDate.split("/");
		int      startMonth     = Integer.parseInt(startDateParts[0]);
		int      startDay       = Integer.parseInt(startDateParts[1]);
		String   startYear      = startDateParts[2];

		String[] startTimeParts  = startDateParts[2].split(" ");
		String[] startTimeParts1 = startTimeParts[1].split(":");

		String   startHour = startTimeParts1[0];
		         startYear = startTimeParts[0];

		String[] endDateParts = endDate.split("/");
		int    endMonth = Integer.parseInt(endDateParts[0]);
		int    endDay   = Integer.parseInt(endDateParts[1]);
		String endYear  = endDateParts[2];

		String[] endTimeParts  = endDateParts[2].split(" ");
		String[] endTimeParts1 = endTimeParts[1].split(":");

		String endHour = endTimeParts1[0];
		       endYear = endTimeParts[0];

		List<Map<String, Object>> dateList = new ArrayList<>();

		for (int year = Integer.parseInt(startYear); year <= Integer.parseInt(endYear); year++) {

			Map<String, Object> yearMap = new HashMap<>();
			yearMap.put("year", year);
			int startDayOfMonth  = 0;
			int endDayOfMonth    = 0;
			int startHourOfDay   = 0;
			int endHourOfDay     = 0;
			int month            = 0;

			List<Integer> monthList = new ArrayList<>();
			Set<Integer> daySet = new HashSet<>(); // Use a Set to eliminate duplicate days
			Set<Integer> hourSet = new HashSet<>(); // Use a Set to eliminate duplicate hours
			int startMonthOfYear = (year == Integer.parseInt(startYear)) ? startMonth : 1;
			int endMonthOfYear = (year == Integer.parseInt(endYear)) ? endMonth : 12;

			for (month = startMonthOfYear; month <= endMonthOfYear; month++) {
				monthList.add(month);

				startDayOfMonth = (year == Integer.parseInt(startYear) && month == startMonth) ? startDay : 1;
				endDayOfMonth = (year == Integer.parseInt(endYear) && month == endMonth) ? endDay
						: getDaysInMonth(month, year);
			}
			month--;

			for (int day = startDayOfMonth; day <= endDayOfMonth; day++) {
				daySet.add(day);
				startHourOfDay = (year == Integer.parseInt(startYear) && month == startMonth && day == startDay)
						? Integer.parseInt(startHour)
						: 0;
				endHourOfDay = (year == Integer.parseInt(endYear) && month == endMonth && day == endDay)
						? Integer.parseInt(endHour)
						: 23;
			}

			for (int hour = startHourOfDay; hour <= endHourOfDay; hour++) {
				hourSet.add(hour);

			}

			yearMap.put("month", monthList);
			yearMap.put("day", new ArrayList<>(daySet));  
			yearMap.put("hour", new ArrayList<>(hourSet));  
			dateList.add(yearMap);

		}
		return gson.toJson(dateList);
	}

	public int getDaysInMonth(int month, int year) {
		int daysInMonth;

		if (month == 2) {
			if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
				daysInMonth = 29;
			} else {
				daysInMonth = 28;
			}
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			daysInMonth = 30;
		} else {
			daysInMonth = 31;
		}

		return daysInMonth;
	}
	@PostMapping("/constructquery")
	public byte[] constructquery(@RequestBody String object1) {
		byte[] result = null;

		try {
			long startDate =	System.currentTimeMillis();
			
			Date date = new Date();
			SimpleDateFormat formatter          = new SimpleDateFormat("MM/dd/yyyy HH:MM");
			SimulationDto    simulationDto      = new SimulationDto();
			JSONObject       simulationObject   = new JSONObject(object1);
			String 			 deviceCondition    = "";
			String           dateconverted      = null;
			String           firstPol           = "";

			int shapeId      = 0;
			int circleCnt    = 0;
			int rectangleCnt = 0;
			int polygonCnt   = 0;
			int polylineCnt  = 0;

			String devices1 = convertStringToArray(simulationObject.getString("Devices"));
			String dataType = simulationObject.getString("dataType");
			String simulationToDate   = simulationObject.getString("DateTimeTo");
			String simulationFromDate = simulationObject.getString("DateTimeFrom");
			long   simulationId       = simulationObject.getLong("simulationId");

			String    shapeType        = "";
			JSONArray arrayObjPolygone = new JSONArray();
			JSONArray ObjArray         = new JSONArray();
			String    conditionSP      = "";
			String    allRegions       = "";
			String    allCountries     = "";
			String    tablesCriteria   = "";
			String    indexesCriteria  = "";


			for (int i = 0; i < simulationObject.getJSONArray("Coordinates").length(); i++) {

				
			shapeType = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getString("Type");
			
			if(shapeType.equals("Circle")) {circleCnt++;}
			if(shapeType.equals("Rectangle")) {rectangleCnt++;}
			if(shapeType.equals("Polygon")) {polygonCnt++;}
			if(shapeType.equals("Polyline")) {polylineCnt++;}
		
			}
			simulationDto.setSimulationId(simulationId);
			simulationDto.setSimulationName(simulationObject.getString("reportName"));
			simulationDto.setSimulationType(simulationObject.getString("reportTypeId"));
			simulationDto.setFilterbdate(simulationObject.getString("DateTimeTo"));
			simulationDto.setFilteredate(simulationObject.getString("DateTimeFrom"));
			simulationDto.setFilterstd("1");
			simulationDto.setExecutionDate(formatter.format(date));
			simulationDto.setExecutedBy(simulationObject.getString("userCode"));
			simulationDto.setFilterdevicesId(simulationObject.getString("Devices"));
			simulationDto.setCreationDate(formatter.format(date));
			simulationDto.setCreatedBy(Integer.parseInt(simulationObject.getString("userCode").trim()));
			simulationDto.setFilteredate(simulationObject.getString("DateTimeFrom"));

			arrayObjPolygone = simulationObject.getJSONArray("Coordinates");

			long   reporttype       = Integer.valueOf(simulationDto.getSimulationType());
			String FromDate         = simulationFromDate;
			Date   fromDateparse    = new Date(FromDate);
			long   fromDatemillis   = fromDateparse.getTime();
			String ToDate           = simulationToDate;
			Date   toDateparse      = new Date(ToDate);
			long   toDatemillis     = toDateparse.getTime();
			JSONObject circleObject = new JSONObject();
			
			Integer countOfPolygon = 0;
			String maxLongBR = "";
			String minLatBR  = "";
			String minLongTL = "";
			String maxLatTL  = "";
			String maxLongBL = "";
			String minLatBL  = "";
			String minLongTR = "";
			String maxLatTR  = "";
			String lng    = null;
			String lat    = null;
			String radius = null;
			JSONArray countryCodeArray = null ;

			Integer countOfPolyline  = 0;
			String  finalLineCoords  = "";
			String  conditionExpr    = null;
			String  ImsiCondition = "";

	dateconverted = convertDateRangeToJson(simulationObject.getString("DateTimeFrom"),simulationObject.getString("DateTimeTo"));
			JSONArray jsonArray = new JSONArray(dateconverted);
			
			
			List<DateDto> dateRanges = new ArrayList<>();

			for (int l = 0; l < jsonArray.length(); l++) {

				JSONObject jsonObject = jsonArray.getJSONObject(l);
				DateDto    dateRange  = new DateDto();
				JSONArray  monthArray = jsonObject.getJSONArray("month");
				List<Integer> months  = new ArrayList<>();

				for (int j = 0; j < monthArray.length(); j++) {
					months.add(monthArray.getInt(j));
				}
				dateRange.setMonth(months);

				dateRange.setYear(jsonObject.getInt("year"));
				JSONArray dayArray = jsonObject.getJSONArray("day");
				List<Integer> days = new ArrayList<>();

				for (int j = 0; j < dayArray.length(); j++) {
					days.add(dayArray.getInt(j));
				}
				dateRange.setDay(days);

				JSONArray HourArray = jsonObject.getJSONArray("hour");
				List<Integer> Hours = new ArrayList<>();

				for (int j = 0; j < HourArray.length(); j++) {
					Hours.add(HourArray.getInt(j));
				}

				dateRange.setHours(Hours);
				dateRanges.add(dateRange);
			}

			Set<Integer> distinctYears  = new HashSet<>();
			Set<Integer> distinctMonths = new HashSet<>();
			Set<Integer> distinctDays   = new HashSet<>();
			Set<Integer> distinctHours  = new HashSet<>();

			for (DateDto dateRange : dateRanges) {
				distinctYears.add(dateRange.getYear());
				List<Integer> months = dateRange.getMonth();
				distinctMonths.addAll(months);

				List<Integer> days = dateRange.getDay();
				distinctDays.addAll(days);

				List<Integer> Hours = dateRange.getHours();
				distinctHours.addAll(Hours);
			}

			  String   dates = " {" 
		   			  + " \"type\": \"range\","  
		   			  + " \"field\": \"usage_timeframe\"," 
		   			  + " \"lower\": " + fromDatemillis + "," 
		   			  + " \"upper\": " + toDatemillis + "," 
		   			  + " \"include_lower\": true," 
		   			  + " \"include_upper\": true," 
		   			  + " \"doc_values\": true" 
		   			  + " }";
			  
			  if (reporttype == 11) { // TCD

					// building condition for imsiId
					String imsi = "";
					if (!simulationObject.getString("imsiId").equals("")) {
						String imsiId = simulationObject.getString("imsiId");
						String[] arrayOfImsiId = imsiId.split(",");
						if (arrayOfImsiId.length == 1) {
							imsi = "\"" + arrayOfImsiId[0] + "\"";
						} else {
							String formattedImsiId = Arrays.stream(arrayOfImsiId).map(device -> "\"" + device + "\"")
									.collect(Collectors.joining(","));
							System.out.println(formattedImsiId);
							imsi = formattedImsiId;
						}
						System.out.println("imsiId>>>>>><<<<<<<<<<< " + imsi);
						ImsiCondition = "{\"type\":\"contains\"," + "\"field\":\"imsi_id\"," + "\"values\":[" + imsi + "]}";
					} else {
						ImsiCondition = "";
					}

//					// building the full condition
					conditionExpr = "{\"filter\":[" + dates +","+ ImsiCondition + "]}";
					System.out.println("<TCD> " + conditionExpr);
//
					
					
//					String table = "loc_location_cdr_main_new";
//					String index = "idx01_cdr_main_new";
//					tablesCriteria  = "-1";
//					indexesCriteria = "-1";
					tablesCriteria  = "loc_location_cdr_main_new";
					indexesCriteria = "idx01_cdr_main_new";

				} // end TCD
				else {


//			  countryCodeArray = simulationObject.getJSONArray("Coordinates").getJSONObject(0).getJSONArray("countrycodes");
			if (reporttype == 1 || reporttype == 6) {
				countryCodeArray = simulationObject.getJSONArray("Coordinates").getJSONObject(0).getJSONArray("countrycodes");
			} else if (reporttype == 2) {
				countryCodeArray = simulationObject.getJSONArray("countryCode");
			}

			for (int i = 0; i < countryCodeArray.length(); i++) {
				JSONArray countryArray = countryCodeArray.getJSONArray(i);
				JSONArray codesArray = countryArray.getJSONArray(2);
				allRegions += countryArray.get(1) + ",";

				for (int j = 0; j < codesArray.length(); j++) {
				String country = codesArray.getString(j);
				allCountries += country + ",";
				}
				}
			if (allCountries.endsWith(",")) {
				allCountries = allCountries.substring(0, allCountries.length() - 1);
				}
				if (allRegions.endsWith(",")) {
				allRegions = allRegions.substring(0, allRegions.length() - 1);
				}

				String    tableName = "geo_data";
				String 	  indexName = "geo_index";
				String    country_codeArray = null;
				JSONArray arrayOfDate    = null;
				String    DateTimeFrom   =  simulationObject.getString("DateTimeFrom");
				String    DateTimeTo     =  simulationObject.getString("DateTimeTo");
				String    dateconverted2 = convertDateRangeToJson(DateTimeFrom, DateTimeTo);
				arrayOfDate = new JSONArray(dateconverted2);
 
		if (arrayOfDate != null) {

				country_codeArray = allCountries;
				for (int o = 0; o < arrayOfDate.length(); o++) {

					String year = String.valueOf(arrayOfDate.getJSONObject(o).getInt("year"));
					JSONArray month = arrayOfDate.getJSONObject(o).getJSONArray("month");

					if (!year.equals("")) {
						for (int p = 0; p < month.length(); p++) {

							String monthValue = month.get(p).toString();
							String tableCriteria = tableName + "_" + year + "_" + monthValue + "_";
							String indexCriteria = indexName + "_" + year + "_" + monthValue + "_";

							String regionArray[] = null;
							String updatedtableCriteria = null;
							String updatedindexCriteria = null;

							String Region = allRegions;
							if (Region.contains(",")) {
								regionArray = Region.split(",");
								for (int i = 0; i < regionArray.length; i++) {
									updatedtableCriteria = tableCriteria + regionArray[i];
									updatedindexCriteria = indexCriteria + regionArray[i];
									if (tablesCriteria.equals("")) {
										tablesCriteria = updatedtableCriteria;
									} else {

										tablesCriteria = tablesCriteria + ',' + updatedtableCriteria;
									}
									if (indexesCriteria.equals("")) {
										indexesCriteria = updatedindexCriteria;

									} else {
										indexesCriteria = indexesCriteria + ',' + updatedindexCriteria;
									}

									tablesCriteria = convertSetToString(getUniqueStringsBetweenCommas(tablesCriteria));
									indexesCriteria = convertSetToString(getUniqueStringsBetweenCommas(indexesCriteria));

								}
							} else {
								updatedtableCriteria = tableCriteria + Region;
								updatedindexCriteria = indexCriteria + Region;
								if (tablesCriteria.equals("")) {
									tablesCriteria = updatedtableCriteria;
								} else {

									tablesCriteria = tablesCriteria + ',' + updatedtableCriteria;
								}
								if (indexesCriteria.equals("")) {
									indexesCriteria = updatedindexCriteria;

								} else {
									indexesCriteria = indexesCriteria + ',' + updatedindexCriteria;
								}

								tablesCriteria  = convertSetToString(getUniqueStringsBetweenCommas(tablesCriteria));
								indexesCriteria = convertSetToString(getUniqueStringsBetweenCommas(indexesCriteria));

							}

						}

					}

				}
			}
		
		
		
		
		  String[] tables= tablesCriteria.split(",");
		  String[] indexes= indexesCriteria.split(",");

		  String allExistingTables  ="";
		  String allExistingIndexes ="";
		  for (int l=0;l < tables.length; l++) {
		  System.out.println("tables[l]>>>"+tables[l]);
		  String cqlsh = "SELECT count(1) FROM system_schema.tables where table_name = '"+tables[l]+ "' allow filtering";

		  Integer tableExists =   template.selectOne(cqlsh, Integer.class);
		  if(tableExists>0)
		  {
		  allExistingTables  += tables[l]+",";
		  allExistingIndexes += indexes[l]+",";
		  }
		  
		  }
		  
		  if(allExistingTables.endsWith(",")) {
		  allExistingTables = allExistingTables.substring(0, allExistingTables.length() - 1);
		  }
		  
		  
		  if(allExistingIndexes.endsWith(",")) {
			  allExistingIndexes = allExistingIndexes.substring(0, allExistingIndexes.length() - 1);
			  }

		    indexesCriteria = allExistingIndexes;
		    tablesCriteria  = allExistingTables;
		    

 				
		
			
			if (!simulationObject.getJSONArray("Coordinates").equals("[]")) {

				for (int i = 0; i < simulationObject.getJSONArray("Coordinates").length(); i++) {
					shapeType = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getString("Type");
					shapeId   = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getInt("ID");
					
					if (shapeType.equals("Circle")) {

						circleObject.put("latCircleCenter", simulationObject.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("center").get("lat").toString());
						circleObject.put("lngCircleCenter", simulationObject.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("center").get("lng").toString());
						circleObject.put("radius", simulationObject.getJSONArray("Coordinates").getJSONObject(i).get("radius").toString());
						circleObject.put("serviceProvider", dataType);
						circleObject.put("fromDate", fromDatemillis);
						circleObject.put("toDate"  , toDatemillis);
						circleObject.put("shapeId" , shapeId);
						ObjArray.put(circleObject);

						 lng    = circleObject.getString("lngCircleCenter");
						 lat    = circleObject.getString("latCircleCenter");
						 radius = circleObject.getString("radius");
						 
						if ( circleCnt ==1 ) {
							conditionSP = 
						                "{\"type\":\"geo_distance\"," +
						                "\"field\":\"place\"," +
						                "\"latitude\":"+lat+"," +
						                "\"longitude\":"+lng +"," +
						                "\"max_distance\":\"" +radius+ " m\"}" 
						              ;
							 
						} else {
							if (i == 0) {
								conditionSP += 
						                "{\"type\":\"geo_distance\"," +
						                "\"field\":\"place\"," +
						                "\"latitude\":"+lat+"," +
						                "\"longitude\":"+lng +"," +
						                "\"max_distance\":\"" +radius+ " m\"}" 
						              ;
  
							} else {
  								conditionSP += 
						                ",{\"type\":\"geo_distance\"," +
						                "\"field\":\"place\"," +
						                "\"latitude\":"+lat+"," +
						                "\"longitude\":"+lng +"," +
						                "\"max_distance\":\"" +radius+ " m\"}" 
						              ;
							}
						}
						
					}  
					else if (shapeType.equals("Rectangle")) {
						maxLongBR = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("bottomRight").get("lng").toString();
						minLatBR  = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("bottomRight").get("lat").toString();
						minLongTL = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("topLeft").get("lng").toString();
						maxLatTL  = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("topLeft").get("lat").toString();
						maxLongBL = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("bottomLeft").get("lng").toString();
						minLatBL  = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("bottomLeft").get("lat").toString();
						minLongTR = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("topRight").get("lng").toString();
						maxLatTR  = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("topRight").get("lat").toString();

						if (rectangleCnt <= 1) {

						    String wktPolygon = "POLYGON((" +
						            minLongTL + " " + maxLatTR + "," +
						            maxLongBR + " " + maxLatTR + "," +
						            maxLongBR + " " + minLatBL + "," +
						            minLongTL + " " + minLatBL + "," +
						            minLongTL + " " + maxLatTR + "))";

						    String geoShapeFilter = String.format(
						        "{\"type\": \"geo_shape\"," +
						        "\"field\": \"place\"," +
						        "\"shape\": {" +
						        "\"type\": \"wkt\"," +
						        "\"value\": \"%s\"" +
						        "}}",
						        wktPolygon
						    );

						    if (!conditionSP.contentEquals("")) {
						        conditionSP += "," + geoShapeFilter;
						    } else {
						        conditionSP += geoShapeFilter;
						    }

						} else {
						    for (int i1 = 0; i1 < rectangleCnt; i1++) {
						        String wktPolygon = "POLYGON((" +
						                minLongTL + " " + maxLatTR + "," +
						                maxLongBR + " " + maxLatTR + "," +
						                maxLongBR + " " + minLatBL + "," +
						                minLongTL + " " + minLatBL + "," +
						                minLongTL + " " + maxLatTR + "))";

						        String geoShapeFilter = String.format(
						            "{\"type\": \"geo_shape\"," +
						            "\"field\": \"place\"," +
						            "\"shape\": {" +
						            "\"type\": \"wkt\"," +
						            "\"value\": \"%s\"" +
						            "}}",
						            wktPolygon
						        );

						        if (i1 == 0) {
						            if (!conditionSP.contentEquals("")) {
						                conditionSP += "," + geoShapeFilter;
						            } else {
						                conditionSP += geoShapeFilter;
						            }
						        } else {
						            conditionSP += "," + geoShapeFilter;
						        }
						    }
						}
					}  
					else if (shapeType.equals("Polygon")) {
						countOfPolygon++;
						String polCoords = "";
						JSONArray jsonObjPol = new JSONArray();
						jsonObjPol = simulationObject.getJSONArray("Coordinates").getJSONObject(i)
								.getJSONArray("Bounds");
						for (int j = 0; j < jsonObjPol.length(); j++) {

							if (j == 0) {
								firstPol = jsonObjPol.getJSONObject(j).get("lng").toString() + " "
										+ jsonObjPol.getJSONObject(j).get("lat").toString();

								polCoords = jsonObjPol.getJSONObject(j).get("lng").toString() + " "
										+ jsonObjPol.getJSONObject(j).get("lat").toString();
							}
							if (j > 0) {
								polCoords = polCoords + "," + jsonObjPol.getJSONObject(j).get("lng").toString() + " "
										+ jsonObjPol.getJSONObject(j).get("lat").toString();
								;
							}
						}
						
					    String firstLng = jsonObjPol.getJSONObject(0).get("lng").toString();
					    String firstLat = jsonObjPol.getJSONObject(0).get("lat").toString();
					    polCoords += "," + firstLng + " " + firstLat;

					    String wktPolygon = "POLYGON((" + polCoords + "))";

					    String geoShapeFilter = String.format(
					        "{\"type\": \"geo_shape\"," +
					        "\"field\": \"place\"," +
					        "\"shape\": {" +
					        "\"type\": \"wkt\"," +
					        "\"value\": \"%s\"" +
					        "}}",
					        wktPolygon
					    );

					    if (!conditionSP.isEmpty()) {
					        conditionSP += "," + geoShapeFilter;
					    } else {
					        conditionSP = geoShapeFilter;
					    }
					 
						 
//						conditionSP =" and ST_Within(ST_Point(location_longitude, location_latitude), ST_GeomFromText('"
//								+ polygonCoordinates + "'))" ;
//						System.out.println(" conditionSP polygon " + conditionSP);

					} // end shape Polygon
					else if (shapeType.equals("Polyline")) {
						countOfPolyline++;
						
						JSONArray lineArray = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getJSONArray("Bounds");
 
						for (int j = 0; j < lineArray.length(); j++) {
						    double lngP = lineArray.getJSONObject(j).getDouble("lng");
						    double latP = lineArray.getJSONObject(j).getDouble("lat");

						    if (!finalLineCoords.isEmpty()) {
						        finalLineCoords += ",";
						    }
						    finalLineCoords += "[" + lngP + "," + latP + "]";
						}

						String geoShapeFilter = String.format(
						    "{\"type\": \"geo_shape\"," +
						    "\"field\": \"place\"," +
						    "\"shape\": {" +
						    "\"type\": \"linestring\"," +
						    "\"coordinates\": [%s]" +
						    "}}",
						    finalLineCoords
						);

						if (!conditionSP.isEmpty()) {
						    conditionSP += "," + geoShapeFilter;
						} else {
						    conditionSP = geoShapeFilter;
						}
						
//						if ( polylineCnt ==1) {
//							conditionSP += " and ST_Within(ST_Point(location_longitude, location_latitude), ST_GeomFromText('POLYGON(("
//									+ finalLineCoords + "))'))";
//						} else {
//							System.out.println(" cnt polyline > 1");
//							if (i == 0) {
//								conditionSPFirst = "  ST_Within(ST_Point(location_longitude, location_latitude), ST_GeomFromText('POLYGON(("
//										+ finalLineCoords + "))'))";
//							} else {
//
//								int commaIndex  = finalLineCoords.indexOf("),(");
//								finalLineCoords = finalLineCoords.substring(commaIndex + 2);
//								finalLineCoords = finalLineCoords.substring(1, finalLineCoords.length() - 1);
//
//								conditionSP += " and (";
//								conditionSP += conditionSPFirst;
//								conditionSP += " OR  ST_Within(ST_Point(location_longitude, location_latitude), ST_GeomFromText('POLYGON(("
//										+ finalLineCoords + "))'))";
//								conditionSP += ")";
//							}
//						}

					}   
				}  
			} 
			String finalCountries;
			String[] countries = allCountries.split(",");
			if (countries.length == 1) {
				finalCountries = "\"" + countries[0] + "\"";
			} else {
				String formattedCountries = Arrays.stream(countries).map(device -> "\"" + device + "\"")
						.collect(Collectors.joining(","));
				System.out.println(formattedCountries);
				finalCountries = formattedCountries;
			}
			
			if (reporttype == 1) {
				
		 
				
				
				if (!simulationObject.getString("Devices").equals("")) {
					devices1 = convertStringToArray(simulationObject.getString("Devices"));
					deviceCondition = " ,{\"type\":\"contains\","
									  +"\"field\":\"device_id\"," 
									  +"values:\""+devices1+"\"}";
									  		
				} else {
					deviceCondition = " ";  
				}

			
				if ( circleCnt <= 1 ) { 
			 conditionExpr  = "{\"filter\":[" 
				+ conditionSP
			    +deviceCondition 
			    +","+dates
			    +",{\"type\":\"contains\"," 
			    +"\"field\":\"country_code\"," 
			    +"\"values\":"+finalCountries+"}"
			    +"]}"
			    ;
				}else {
					
			conditionExpr  = 
				"{\"filter\":[" 
				+"{\"type\": \"boolean\",\r\n" + 
			    " \"should\": ["
				+ conditionSP 
				+" ] }"
				+deviceCondition
			    +","+dates
				+",{\"type\":\"contains\"," 
				+"\"field\":\"country_code\","
				+"\"values\":"+finalCountries+"}"
				+ "]}";
					  
				}
				
				if ( rectangleCnt == 1 && rectangleCnt !=0) { 
					 conditionExpr  = "{\"filter\":[" 
						+ conditionSP
					    +deviceCondition 
					    +","+dates
					    +",{\"type\":\"contains\"," 
					    +"\"field\":\"country_code\"," 
					    +"\"values\":[422]}]}";
						}else {
							if(rectangleCnt !=0) {
							conditionExpr  = "{\"filter\":[" 
									+"{\"type\": \"boolean\",\r\n" 
								    +" \"should\": ["
									+ conditionSP 
									+" ]"
									+ " }  "
									+deviceCondition
								    +","+dates
									+",{\"type\":\"contains\"," 
									+"\"field\":\"country_code\","
									+"\"values\":"+finalCountries+"}"
									+ "] }";
							
							}

						}
				if ( polygonCnt == 1 && polygonCnt !=0 ) { 
					 conditionExpr  = "{\"filter\":[" 
						+ conditionSP
					    +deviceCondition 
					    +","+dates
					    +",{\"type\":\"contains\"," 
					    +"\"field\":\"country_code\"," 
					    +"\"values\":"+finalCountries+"}]}";
						}else {
							if(polygonCnt !=0) {
							conditionExpr  = "{\"filter\":[" 
									+"{\"type\": \"boolean\",\r\n" 
								    +" \"should\": ["
									+ conditionSP 
									+" ]"
									+ " }  "
									+deviceCondition
								    +","+dates
									+",{\"type\":\"contains\"," 
									+"\"field\":\"country_code\","
									+"\"values\":"+finalCountries+"}"
									+ "] }";
							}
						}
				
			}   
			else if (reporttype == 2) {  
				
				
				
				// building condition for devices
				if (!simulationObject.getString("Devices").equals("")) {
					devices1 = convertStringToArray(simulationObject.getString("Devices"));
					System.out.println("devices are >>>>>>>>> " + devices1);
					devices1 = devices1.replace("'", "");

					String[] arrayOfDevice = devices1.split(",");
					if (arrayOfDevice.length == 1) {
						devices1 = "\"" + arrayOfDevice[0] + "\"";
					} else {
						String formattedDevices = Arrays.stream(arrayOfDevice).map(device -> "\"" + device + "\"")
								.collect(Collectors.joining(","));
						System.out.println(formattedDevices);
						devices1 = formattedDevices;
					}
					System.out.println("the devices1 are " + devices1);
					deviceCondition = "{\"type\":\"contains\"," + "\"field\":\"device_id\"," + "\"values\":["
							+ devices1 + "]}";
				} else {
					deviceCondition = "";
				}

				// building condition for countries
				System.out.println("object1 DH " + object1);
//				String finalCountries;
//				String[] countries = allCountries.split(",");
//				if (countries.length == 1) {
//					finalCountries = "\"" + countries[0] + "\"";
//				} else {
//					String formattedCountries = Arrays.stream(countries).map(device -> "\"" + device + "\"")
//							.collect(Collectors.joining(","));
//					System.out.println(formattedCountries);
//					finalCountries = formattedCountries;
//				}

				String countryCondition = "{\"type\":\"contains\"," + "\"field\":\"country_code\"," + "\"values\":["
						+ finalCountries + "]}";

				// building the full condition
				conditionExpr = "{\"filter\":[" + deviceCondition + "," + dates + "," + countryCondition + "]}";
				
//				conditionExpr = "{\"filter\":[" + dates +  deviceCondition  + "]}"; query without country 


				System.out.println(" conditionExpr DH " + conditionExpr);

			
				
				
				

//				if (!simulationObject.getString("Devices").equals("")) {
//					devices1 = convertStringToArray(simulationObject.getString("Devices"));
//					deviceCondition = " and device_id in (" + devices1 + ")";
//				} else {
//					deviceCondition = "";
//				}
 
			}  
			else if (reporttype == 3) { // DTP

			}  
			
				}
			
		   //result = cassandrasparkservice.getAllData1(long simulationId) ;
			result = cassandrasparkservice.getAllDataSparkSedona1( conditionExpr,simulationId,indexesCriteria,tablesCriteria);

			long endDate =	System.currentTimeMillis();
			long totallDate =	endDate -startDate;

			System.out.println(" totallDate is  "+totallDate);
			
			return result;
//			return "Success";

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
//		return result;
	}

	@GetMapping("/P_GET_LOC_LOCATION")
		public int P_GET_LOC_LOCATION() {
		System.out.println("innnnnnnnnnnn");
		int count = cassandrasparkservice.P_GET_LOC_LOCATION();
			return count;
			 
		}

	public static Point getCircleCoord(double theta, double xCenter, double yCenter, double radius) {
		double x = radius * Math.cos(theta) + xCenter;
		double y = radius * Math.sin(theta) + yCenter;
		return new Point((int) x, (int) y);
	}
	
	
	
	@PostMapping("/isPointInCircle")
	public static boolean isPointInCircle(@RequestParam("lon") double lon, @RequestParam("lat") double lat,@RequestParam("centerlon")  double centerLon,
            @RequestParam("centerLat")  double centerLat, @RequestParam("radius") double radiusKm) {
		System.out.println(" innnnnnnnnnnn");
	    double radiusDegrees = radiusKm  ;  
		System.out.println(" radiusDegrees  ==  "+radiusDegrees);

	    double distance = Math.sqrt(Math.pow(lon - centerLon, 2) + Math.pow(lat - centerLat, 2));
		System.out.println(" distance  ==  "+distance);
		double res = distance - radiusDegrees ;
		System.out.println(" res  ==  "+res);

	    return distance <= radiusDegrees;
	}
	
	public static Set<String> getUniqueStringsBetweenCommas(String input) {
		Set<String> uniqueStrings = new LinkedHashSet<>();

		String[] parts = input.split(",");
		for (String part : parts) {
		String trimmedPart = part.trim();
		if (!trimmedPart.isEmpty()) {
		uniqueStrings.add(trimmedPart);
		}
		}

		return uniqueStrings;
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

		public static String convertSetToString(Set<String> set) {
		return String.join(",", set);
		}
}


//package com.valoores.cassandra.app.api;
//
//import java.awt.Point;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import com.google.gson.Gson;
//import com.valoores.cassandra.app.dto.DateDto;
//import com.valoores.cassandra.app.dto.SimulationDto;
//import com.valoores.cassandra.app.service.CassandraSparkService;
//
//@RestController
//@RequestMapping("/api")
//public class CassandraSparkController {
//
//@Autowired
//private CassandraSparkService cassandrasparkservice;
//
//@PostMapping("/getAllData1")
//public byte[] getAllData1(@RequestParam long simulationId) {
//return cassandrasparkservice.getAllData1( simulationId);
//}
//
//@PostMapping("/getObject")
//public byte[] getObject(@RequestBody String[] myObject) {
//try {
//System.out.println("statrt c ");
//JSONObject obj = new JSONObject(myObject);
//String devices = obj.getString("devices");
//
//String FromDate   = obj.getString("fromDatemillis");
//String callingNo  = obj.getString("callingNo");
//String toDate     = obj.getString("toDatemillis");
//String linkType   = obj.getString("linkType");
//String connection = obj.getString("directIndirect");
//String layers     = obj.getString("layer");
//
//Thread.sleep(1000);
//} catch (InterruptedException e) {
//e.printStackTrace();
//}
//
//return cassandrasparkservice.getObject(myObject);
//}
//
//@PostMapping("/convertStringToArray")
//public String convertStringToArray(@RequestBody String object1) {
//
//String[] Array = object1.split(",");
//String joined  = String.join("','", Array);
//String result  = "'" + joined + "'";
//return result;
//}
//
//@PostMapping("/copyDataToOracle")
//public String copyDataToOracle() {
//try {
//Thread.sleep(1000);
//} catch (InterruptedException e) {
//e.printStackTrace();
//}
//return cassandrasparkservice.copyDataToOracle();
//}
//
//
//@GetMapping("/convertDateRangeToJson")
//public String convertDateRangeToJson(@RequestParam("startDate") String startDate,
//@RequestParam("endDate") String endDate) {
//Gson gson = new Gson();
//
//String[] startDateParts = startDate.split("/");
//int      startMonth     = Integer.parseInt(startDateParts[0]);
//int      startDay       = Integer.parseInt(startDateParts[1]);
//String   startYear      = startDateParts[2];
//
//String[] startTimeParts  = startDateParts[2].split(" ");
//String[] startTimeParts1 = startTimeParts[1].split(":");
//
//String   startHour = startTimeParts1[0];
//        startYear = startTimeParts[0];
//
//String[] endDateParts = endDate.split("/");
//int    endMonth = Integer.parseInt(endDateParts[0]);
//int    endDay   = Integer.parseInt(endDateParts[1]);
//String endYear  = endDateParts[2];
//
//String[] endTimeParts  = endDateParts[2].split(" ");
//String[] endTimeParts1 = endTimeParts[1].split(":");
//
//String endHour = endTimeParts1[0];
//      endYear = endTimeParts[0];
//
//List<Map<String, Object>> dateList = new ArrayList<>();
//
//for (int year = Integer.parseInt(startYear); year <= Integer.parseInt(endYear); year++) {
//
//Map<String, Object> yearMap = new HashMap<>();
//yearMap.put("year", year);
//int startDayOfMonth  = 0;
//int endDayOfMonth    = 0;
//int startHourOfDay   = 0;
//int endHourOfDay     = 0;
//int month            = 0;
//
//List<Integer> monthList = new ArrayList<>();
//Set<Integer> daySet = new HashSet<>(); // Use a Set to eliminate duplicate days
//Set<Integer> hourSet = new HashSet<>(); // Use a Set to eliminate duplicate hours
//int startMonthOfYear = (year == Integer.parseInt(startYear)) ? startMonth : 1;
//int endMonthOfYear = (year == Integer.parseInt(endYear)) ? endMonth : 12;
//
//for (month = startMonthOfYear; month <= endMonthOfYear; month++) {
//monthList.add(month);
//
//startDayOfMonth = (year == Integer.parseInt(startYear) && month == startMonth) ? startDay : 1;
//endDayOfMonth = (year == Integer.parseInt(endYear) && month == endMonth) ? endDay
//: getDaysInMonth(month, year);
//}
//month--;
//
//for (int day = startDayOfMonth; day <= endDayOfMonth; day++) {
//daySet.add(day);
//startHourOfDay = (year == Integer.parseInt(startYear) && month == startMonth && day == startDay)
//? Integer.parseInt(startHour)
//: 0;
//endHourOfDay = (year == Integer.parseInt(endYear) && month == endMonth && day == endDay)
//? Integer.parseInt(endHour)
//: 23;
//}
//
//for (int hour = startHourOfDay; hour <= endHourOfDay; hour++) {
//hourSet.add(hour);
//
//}
//
//yearMap.put("month", monthList);
//yearMap.put("day", new ArrayList<>(daySet));  
//yearMap.put("hour", new ArrayList<>(hourSet));  
//dateList.add(yearMap);
//
//}
//return gson.toJson(dateList);
//}
//
//public int getDaysInMonth(int month, int year) {
//int daysInMonth;
//
//if (month == 2) {
//if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
//daysInMonth = 29;
//} else {
//daysInMonth = 28;
//}
//} else if (month == 4 || month == 6 || month == 9 || month == 11) {
//daysInMonth = 30;
//} else {
//daysInMonth = 31;
//}
//
//return daysInMonth;
//}
//
//@PostMapping("/constructquery")
//public byte[] constructquery(@RequestBody String object1) {
//byte[] result = null;
//
//try {
//long startDate = System.currentTimeMillis();
//
//Date date = new Date();
//SimpleDateFormat formatter          = new SimpleDateFormat("MM/dd/yyyy HH:MM");
//SimulationDto    simulationDto      = new SimulationDto();
//JSONObject       simulationObject   = new JSONObject(object1);
//String deviceCondition    = "";
//String           dateconverted      = null;
//String           firstPol           = "";
//
//int shapeId      = 0;
//int circleCnt    = 0;
//int rectangleCnt = 0;
//int polygonCnt   = 0;
//int polylineCnt  = 0;
//
//String devices1 = convertStringToArray(simulationObject.getString("Devices"));
//String dataType = simulationObject.getString("dataType");
//String simulationToDate   = simulationObject.getString("DateTimeTo");
//String simulationFromDate = simulationObject.getString("DateTimeFrom");
//long   simulationId       = simulationObject.getLong("simulationId");
//
//String    shapeType        = "";
//JSONArray arrayObjPolygone = new JSONArray();
//JSONArray ObjArray         = new JSONArray();
//String    conditionSP      = "";
//String    allRegions       = "";
//String    allCountries     = "";
//String    tablesCriteria   = "";
//String    indexesCriteria  = "";
//
//
//for (int i = 0; i < simulationObject.getJSONArray("Coordinates").length(); i++) {
//
//
//shapeType = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getString("Type");
//
//if(shapeType.equals("Circle")) {circleCnt++;}
//if(shapeType.equals("Rectangle")) {rectangleCnt++;}
//if(shapeType.equals("Polygon")) {polygonCnt++;}
//if(shapeType.equals("Polyline")) {polylineCnt++;}
//
//}
//simulationDto.setSimulationId(simulationId);
//simulationDto.setSimulationName(simulationObject.getString("reportName"));
//simulationDto.setSimulationType(simulationObject.getString("reportTypeId"));
//simulationDto.setFilterbdate(simulationObject.getString("DateTimeTo"));
//simulationDto.setFilteredate(simulationObject.getString("DateTimeFrom"));
//simulationDto.setFilterstd("1");
//simulationDto.setExecutionDate(formatter.format(date));
//simulationDto.setExecutedBy(simulationObject.getString("userCode"));
//simulationDto.setFilterdevicesId(simulationObject.getString("Devices"));
//simulationDto.setCreationDate(formatter.format(date));
//simulationDto.setCreatedBy(Integer.parseInt(simulationObject.getString("userCode").trim()));
//simulationDto.setFilteredate(simulationObject.getString("DateTimeFrom"));
//
//arrayObjPolygone = simulationObject.getJSONArray("Coordinates");
//
//long   reporttype       = Integer.valueOf(simulationDto.getSimulationType());
//String FromDate         = simulationFromDate;
//Date   fromDateparse    = new Date(FromDate);
//long   fromDatemillis   = fromDateparse.getTime();
//String ToDate           = simulationToDate;
//Date   toDateparse      = new Date(ToDate);
//long   toDatemillis     = toDateparse.getTime();
//JSONObject circleObject = new JSONObject();
//
//Integer countOfPolygon = 0;
//String maxLongBR = "";
//String minLatBR  = "";
//String minLongTL = "";
//String maxLatTL  = "";
//String maxLongBL = "";
//String minLatBL  = "";
//String minLongTR = "";
//String maxLatTR  = "";
//String lng    = null;
//String lat    = null;
//String radius = null;
//JSONArray countryCodeArray = null ;
//
//Integer countOfPolyline  = 0;
//String  finalLineCoords  = "";
//String  conditionExpr    = null;
//
//
//dateconverted = convertDateRangeToJson(simulationObject.getString("DateTimeFrom"),simulationObject.getString("DateTimeTo"));
//JSONArray jsonArray = new JSONArray(dateconverted);
//
// countryCodeArray = simulationObject.getJSONArray("Coordinates").getJSONObject(0).getJSONArray("countrycodes");
//
//for (int i = 0; i < countryCodeArray.length(); i++) {
//JSONArray countryArray = countryCodeArray.getJSONArray(i);
//JSONArray codesArray = countryArray.getJSONArray(2);
//allRegions += countryArray.get(1) + ",";
//
//for (int j = 0; j < codesArray.length(); j++) {
//String country = codesArray.getString(j);
//allCountries += country + ",";
//}
//}
//if (allCountries.endsWith(",")) {
//allCountries = allCountries.substring(0, allCountries.length() - 1);
//}
//if (allRegions.endsWith(",")) {
//allRegions = allRegions.substring(0, allRegions.length() - 1);
//}
//
//String    tableName = "geo_data";
//String  indexName = "geo_index";
//String    country_codeArray = null;
//JSONArray arrayOfDate    = null;
//String    DateTimeFrom   =  simulationObject.getString("DateTimeFrom");
//String    DateTimeTo     =  simulationObject.getString("DateTimeTo");
//String    dateconverted2 = convertDateRangeToJson(DateTimeFrom, DateTimeTo);
//arrayOfDate = new JSONArray(dateconverted2);
//
//if (arrayOfDate != null) {
//
//country_codeArray = allCountries;
//for (int o = 0; o < arrayOfDate.length(); o++) {
//
//String year = String.valueOf(arrayOfDate.getJSONObject(o).getInt("year"));
//JSONArray month = arrayOfDate.getJSONObject(o).getJSONArray("month");
//
//if (!year.equals("")) {
//for (int p = 0; p < month.length(); p++) {
//
//String monthValue = month.get(p).toString();
//String tableCriteria = tableName + "_" + year + "_" + monthValue + "_";
//String indexCriteria = indexName + "_" + year + "_" + monthValue + "_";
//
//String regionArray[] = null;
//String updatedtableCriteria = null;
//String updatedindexCriteria = null;
//
//String Region = allRegions;
//if (Region.contains(",")) {
//regionArray = Region.split(",");
//for (int i = 0; i < regionArray.length; i++) {
//updatedtableCriteria = tableCriteria + regionArray[i];
//updatedindexCriteria = indexCriteria + regionArray[i];
//if (tablesCriteria.equals("")) {
//tablesCriteria = updatedtableCriteria;
//} else {
//
//tablesCriteria = tablesCriteria + ',' + updatedtableCriteria;
//}
//if (indexesCriteria.equals("")) {
//indexesCriteria = updatedindexCriteria;
//
//} else {
//indexesCriteria = indexesCriteria + ',' + updatedindexCriteria;
//}
//
//tablesCriteria = convertSetToString(getUniqueStringsBetweenCommas(tablesCriteria));
//indexesCriteria = convertSetToString(
//getUniqueStringsBetweenCommas(indexesCriteria));
//
//}
//} else {
//updatedtableCriteria = tableCriteria + Region;
//updatedindexCriteria = indexCriteria + Region;
//if (tablesCriteria.equals("")) {
//tablesCriteria = updatedtableCriteria;
//} else {
//
//tablesCriteria = tablesCriteria + ',' + updatedtableCriteria;
//}
//if (indexesCriteria.equals("")) {
//indexesCriteria = updatedindexCriteria;
//
//} else {
//indexesCriteria = indexesCriteria + ',' + updatedindexCriteria;
//}
//
//tablesCriteria = convertSetToString(getUniqueStringsBetweenCommas(tablesCriteria));
//indexesCriteria = convertSetToString(getUniqueStringsBetweenCommas(indexesCriteria));
//
//}
//
//}
//
//}
//
//}
//
//}
//
//List<DateDto> dateRanges = new ArrayList<>();
//
//for (int l = 0; l < jsonArray.length(); l++) {
//
//JSONObject jsonObject = jsonArray.getJSONObject(l);
//DateDto    dateRange  = new DateDto();
//JSONArray  monthArray = jsonObject.getJSONArray("month");
//List<Integer> months  = new ArrayList<>();
//
//for (int j = 0; j < monthArray.length(); j++) {
//months.add(monthArray.getInt(j));
//}
//dateRange.setMonth(months);
//
//dateRange.setYear(jsonObject.getInt("year"));
//JSONArray dayArray = jsonObject.getJSONArray("day");
//List<Integer> days = new ArrayList<>();
//
//for (int j = 0; j < dayArray.length(); j++) {
//days.add(dayArray.getInt(j));
//}
//dateRange.setDay(days);
//
//JSONArray HourArray = jsonObject.getJSONArray("hour");
//List<Integer> Hours = new ArrayList<>();
//
//for (int j = 0; j < HourArray.length(); j++) {
//Hours.add(HourArray.getInt(j));
//}
//
//dateRange.setHours(Hours);
//dateRanges.add(dateRange);
//}
//
//Set<Integer> distinctYears  = new HashSet<>();
//Set<Integer> distinctMonths = new HashSet<>();
//Set<Integer> distinctDays   = new HashSet<>();
//Set<Integer> distinctHours  = new HashSet<>();
//
//for (DateDto dateRange : dateRanges) {
//distinctYears.add(dateRange.getYear());
//List<Integer> months = dateRange.getMonth();
//distinctMonths.addAll(months);
//
//List<Integer> days = dateRange.getDay();
//distinctDays.addAll(days);
//
//List<Integer> Hours = dateRange.getHours();
//distinctHours.addAll(Hours);
//}
//
// String   dates = " {"
//   + " \"type\": \"range\","  
//   + " \"field\": \"usage_timeframe\","
//   + " \"lower\": " + fromDatemillis + ","
//   + " \"upper\": " + toDatemillis + ","
//   + " \"include_lower\": true,"
//   + " \"include_upper\": true,"
//   + " \"doc_values\": true"
//   + " }";
//
//if (!simulationObject.getJSONArray("Coordinates").equals("[]")) {
//
//for (int i = 0; i < simulationObject.getJSONArray("Coordinates").length(); i++) {
//shapeType = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getString("Type");
//shapeId   = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getInt("ID");
//
//if (shapeType.equals("Circle")) {
//
//circleObject.put("latCircleCenter", simulationObject.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("center").get("lat").toString());
//circleObject.put("lngCircleCenter", simulationObject.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("center").get("lng").toString());
//circleObject.put("radius", simulationObject.getJSONArray("Coordinates").getJSONObject(i).get("radius").toString());
//circleObject.put("serviceProvider", dataType);
//circleObject.put("fromDate", fromDatemillis);
//circleObject.put("toDate"  , toDatemillis);
//circleObject.put("shapeId" , shapeId);
//ObjArray.put(circleObject);
//
//lng    = circleObject.getString("lngCircleCenter");
//lat    = circleObject.getString("latCircleCenter");
//radius = circleObject.getString("radius");
//
//if ( circleCnt ==1 ) {
//conditionSP =
//               "{\"type\":\"geo_distance\"," +
//               "\"field\":\"place\"," +
//               "\"latitude\":"+lat+"," +
//               "\"longitude\":"+lng +"," +
//               "\"max_distance\":\"" +radius+ " m\"}"
//             ;
//
//} else {
//if (i == 0) {
//conditionSP +=
//               "{\"type\":\"geo_distance\"," +
//               "\"field\":\"place\"," +
//               "\"latitude\":"+lat+"," +
//               "\"longitude\":"+lng +"," +
//               "\"max_distance\":\"" +radius+ " m\"}"
//             ;
// 
//} else {
//  conditionSP +=
//               ",{\"type\":\"geo_distance\"," +
//               "\"field\":\"place\"," +
//               "\"latitude\":"+lat+"," +
//               "\"longitude\":"+lng +"," +
//               "\"max_distance\":\"" +radius+ " m\"}"
//             ;
//}
//}
//
//}  
//else if (shapeType.equals("Rectangle")) {
//maxLongBR = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("bottomRight").get("lng").toString();
//minLatBR  = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("bottomRight").get("lat").toString();
//minLongTL = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("topLeft").get("lng").toString();
//maxLatTL  = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("topLeft").get("lat").toString();
//maxLongBL = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("bottomLeft").get("lng").toString();
//minLatBL  = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("bottomLeft").get("lat").toString();
//minLongTR = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("topRight").get("lng").toString();
//maxLatTR  = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("topRight").get("lat").toString();
//
//if (rectangleCnt <= 1) {
//
//   String wktPolygon = "POLYGON((" +
//           minLongTL + " " + maxLatTR + "," +
//           maxLongBR + " " + maxLatTR + "," +
//           maxLongBR + " " + minLatBL + "," +
//           minLongTL + " " + minLatBL + "," +
//           minLongTL + " " + maxLatTR + "))";
//
//   String geoShapeFilter = String.format(
//       "{\"type\": \"geo_shape\"," +
//       "\"field\": \"place\"," +
//       "\"shape\": {" +
//       "\"type\": \"wkt\"," +
//       "\"value\": \"%s\"" +
//       "}}",
//       wktPolygon
//   );
//
//   if (!conditionSP.contentEquals("")) {
//       conditionSP += "," + geoShapeFilter;
//   } else {
//       conditionSP += geoShapeFilter;
//   }
//
//} else {
//   for (int i1 = 0; i1 < rectangleCnt; i1++) {
//       String wktPolygon = "POLYGON((" +
//               minLongTL + " " + maxLatTR + "," +
//               maxLongBR + " " + maxLatTR + "," +
//               maxLongBR + " " + minLatBL + "," +
//               minLongTL + " " + minLatBL + "," +
//               minLongTL + " " + maxLatTR + "))";
//
//       String geoShapeFilter = String.format(
//           "{\"type\": \"geo_shape\"," +
//           "\"field\": \"place\"," +
//           "\"shape\": {" +
//           "\"type\": \"wkt\"," +
//           "\"value\": \"%s\"" +
//           "}}",
//           wktPolygon
//       );
//
//       if (i1 == 0) {
//           if (!conditionSP.contentEquals("")) {
//               conditionSP += "," + geoShapeFilter;
//           } else {
//               conditionSP += geoShapeFilter;
//           }
//       } else {
//           conditionSP += "," + geoShapeFilter;
//       }
//   }
//}
//}  
//else if (shapeType.equals("Polygon")) {
//countOfPolygon++;
//String polCoords = "";
//JSONArray jsonObjPol = new JSONArray();
//jsonObjPol = simulationObject.getJSONArray("Coordinates").getJSONObject(i)
//.getJSONArray("Bounds");
//for (int j = 0; j < jsonObjPol.length(); j++) {
//
//if (j == 0) {
//firstPol = jsonObjPol.getJSONObject(j).get("lng").toString() + " "
//+ jsonObjPol.getJSONObject(j).get("lat").toString();
//
//polCoords = jsonObjPol.getJSONObject(j).get("lng").toString() + " "
//+ jsonObjPol.getJSONObject(j).get("lat").toString();
//}
//if (j > 0) {
//polCoords = polCoords + "," + jsonObjPol.getJSONObject(j).get("lng").toString() + " "
//+ jsonObjPol.getJSONObject(j).get("lat").toString();
//;
//}
//}
//
//   String firstLng = jsonObjPol.getJSONObject(0).get("lng").toString();
//   String firstLat = jsonObjPol.getJSONObject(0).get("lat").toString();
//   polCoords += "," + firstLng + " " + firstLat;
//
//   String wktPolygon = "POLYGON((" + polCoords + "))";
//
//   String geoShapeFilter = String.format(
//       "{\"type\": \"geo_shape\"," +
//       "\"field\": \"place\"," +
//       "\"shape\": {" +
//       "\"type\": \"wkt\"," +
//       "\"value\": \"%s\"" +
//       "}}",
//       wktPolygon
//   );
//
//   if (!conditionSP.isEmpty()) {
//       conditionSP += "," + geoShapeFilter;
//   } else {
//       conditionSP = geoShapeFilter;
//   }
//
//
//// conditionSP =" and ST_Within(ST_Point(location_longitude, location_latitude), ST_GeomFromText('"
//// + polygonCoordinates + "'))" ;
//// System.out.println(" conditionSP polygon " + conditionSP);
//
//} // end shape Polygon
//else if (shapeType.equals("Polyline")) {
//countOfPolyline++;
//
//JSONArray lineArray = simulationObject.getJSONArray("Coordinates").getJSONObject(i).getJSONArray("Bounds");
// 
//for (int j = 0; j < lineArray.length(); j++) {
//   double lngP = lineArray.getJSONObject(j).getDouble("lng");
//   double latP = lineArray.getJSONObject(j).getDouble("lat");
//
//   if (!finalLineCoords.isEmpty()) {
//       finalLineCoords += ",";
//   }
//   finalLineCoords += "[" + lngP + "," + latP + "]";
//}
//
//String geoShapeFilter = String.format(
//   "{\"type\": \"geo_shape\"," +
//   "\"field\": \"place\"," +
//   "\"shape\": {" +
//   "\"type\": \"linestring\"," +
//   "\"coordinates\": [%s]" +
//   "}}",
//   finalLineCoords
//);
//
//if (!conditionSP.isEmpty()) {
//   conditionSP += "," + geoShapeFilter;
//} else {
//   conditionSP = geoShapeFilter;
//}
//
//// if ( polylineCnt ==1) {
//// conditionSP += " and ST_Within(ST_Point(location_longitude, location_latitude), ST_GeomFromText('POLYGON(("
//// + finalLineCoords + "))'))";
//// } else {
//// System.out.println(" cnt polyline > 1");
//// if (i == 0) {
//// conditionSPFirst = "  ST_Within(ST_Point(location_longitude, location_latitude), ST_GeomFromText('POLYGON(("
//// + finalLineCoords + "))'))";
//// } else {
////
//// int commaIndex  = finalLineCoords.indexOf("),(");
//// finalLineCoords = finalLineCoords.substring(commaIndex + 2);
//// finalLineCoords = finalLineCoords.substring(1, finalLineCoords.length() - 1);
////
//// conditionSP += " and (";
//// conditionSP += conditionSPFirst;
//// conditionSP += " OR  ST_Within(ST_Point(location_longitude, location_latitude), ST_GeomFromText('POLYGON(("
//// + finalLineCoords + "))'))";
//// conditionSP += ")";
//// }
//// }
//
//}  
//}  
//}
//
//
//if (reporttype == 1) {
//
//
//
//
//if (!simulationObject.getString("Devices").equals("")) {
//devices1 = convertStringToArray(simulationObject.getString("Devices"));
//deviceCondition = " ,{\"type\":\"contains\","
// +"\"field\":\"device_id\","
// +"values:\""+devices1+"\"}";
// 
//} else {
//deviceCondition = " ";  
//}
//
//
//if ( circleCnt <= 1 ) {
//conditionExpr  = "{\"filter\":["
//+ conditionSP
//   +deviceCondition
//   +","+dates
////    +",{\"type\":\"contains\","
////    +"\"field\":\"country_code\","
////    +"\"values\":[422]}"
//   +"]}"
//   ;
//}else {
//
//conditionExpr  =
//"{\"filter\":["
//+"{\"type\": \"boolean\",\r\n" +
//   " \"should\": ["
//+ conditionSP
//+" ] }"
//+deviceCondition
//   +","+dates
//// +",{\"type\":\"contains\","
//// +"\"field\":\"country_code\","
//// +"\"values\":[422]}"
//+ "]}";
// 
//}
//
//if ( rectangleCnt == 1 && rectangleCnt !=0) {
//conditionExpr  = "{\"filter\":["
//+ conditionSP
//   +deviceCondition
//   +","+dates
//   +",{\"type\":\"contains\","
//   +"\"field\":\"country_code\","
//   +"\"values\":[422]}]}";
//}else {
//if(rectangleCnt !=0) {
//conditionExpr  = "{\"filter\":["
//+"{\"type\": \"boolean\",\r\n"
//   +" \"should\": ["
//+ conditionSP
//+" ]"
//+ " }  "
//+deviceCondition
//   +","+dates
//+",{\"type\":\"contains\","
//+"\"field\":\"country_code\","
//+"\"values\":[422]}"
//+ "] }";
//
//}
//
//}
//if ( polygonCnt == 1 && polygonCnt !=0 ) {
//conditionExpr  = "{\"filter\":["
//+ conditionSP
//   +deviceCondition
//   +","+dates
//   +",{\"type\":\"contains\","
//   +"\"field\":\"country_code\","
//   +"\"values\":[422]}]}";
//}else {
//if(polygonCnt !=0) {
//conditionExpr  = "{\"filter\":["
//+"{\"type\": \"boolean\",\r\n"
//   +" \"should\": ["
//+ conditionSP
//+" ]"
//+ " }  "
//+deviceCondition
//   +","+dates
//+",{\"type\":\"contains\","
//+"\"field\":\"country_code\","
//+"\"values\":[422]}"
//+ "] }";
//}
//}
//
//}  
//else if (reporttype == 2) {  
//
//// if (!simulationObject.getString("Devices").equals("")) {
//// devices1 = convertStringToArray(simulationObject.getString("Devices"));
//// deviceCondition = " and device_id in (" + devices1 + ")";
//// } else {
//// deviceCondition = "";
//// }
// 
//}  
//else if (reporttype == 3) { // DTP
//
//}  
//
//  //result = cassandrasparkservice.getAllData1(long simulationId) ;
//result = cassandrasparkservice.getAllDataSparkSedona1( conditionExpr,simulationId,indexesCriteria,tablesCriteria);
//
//long endDate = System.currentTimeMillis();
//long totallDate = endDate -startDate;
//
//System.out.println(" totallDate is  "+totallDate);
//
//return result;
//// return "Success";
//
//} catch (Exception e) {
//e.printStackTrace();
//return null;
//}
//// return result;
//}
//
//@GetMapping("/P_GET_LOC_LOCATION")
//public int P_GET_LOC_LOCATION() {
//System.out.println("innnnnnnnnnnn");
//int count = cassandrasparkservice.P_GET_LOC_LOCATION();
//return count;
//
//}
//
//public static Point getCircleCoord(double theta, double xCenter, double yCenter, double radius) {
//double x = radius * Math.cos(theta) + xCenter;
//double y = radius * Math.sin(theta) + yCenter;
//return new Point((int) x, (int) y);
//}
//
//
//
//@PostMapping("/isPointInCircle")
//public static boolean isPointInCircle(@RequestParam("lon") double lon, @RequestParam("lat") double lat,@RequestParam("centerlon")  double centerLon,
//            @RequestParam("centerLat")  double centerLat, @RequestParam("radius") double radiusKm) {
//System.out.println(" innnnnnnnnnnn");
//   double radiusDegrees = radiusKm  ;  
//System.out.println(" radiusDegrees  ==  "+radiusDegrees);
//
//   double distance = Math.sqrt(Math.pow(lon - centerLon, 2) + Math.pow(lat - centerLat, 2));
//System.out.println(" distance  ==  "+distance);
//double res = distance - radiusDegrees ;
//System.out.println(" res  ==  "+res);
//
//   return distance <= radiusDegrees;
//}
//
//public static Set<String> getUniqueStringsBetweenCommas(String input) {
//Set<String> uniqueStrings = new LinkedHashSet<>();
//
//String[] parts = input.split(",");
//for (String part : parts) {
//String trimmedPart = part.trim();
//if (!trimmedPart.isEmpty()) {
//uniqueStrings.add(trimmedPart);
//}
//}
//
//return uniqueStrings;
//}
//
//public static String convertSetToString(Set<String> set) {
//return String.join(",", set);
//}
//}
