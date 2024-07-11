package com.valoores.cassandradatacrowd.app.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valoores.cassandradatacrowd.app.dto.CustomResponse;
import com.valoores.cassandradatacrowd.app.service.ILocationService;

/**
 * @author marcelino.a
 */

@RestController
@RequestMapping("/api")
public class LocationRestController {

  @Autowired
  private ILocationService locationService;

  @Value("${application.repository.urlPath}")
  private String urlPath;
  
  @Value("${application.shaperep.urlPath}")
  private String shapeRepositoryPath;

  @GetMapping("/deleteCassandraFile/{id}")
  public CustomResponse deleteCassandraFile(@PathVariable("id") String id) {

    CustomResponse resp = CustomResponse.builder().build();
    try {

      String fullPath = urlPath;
      String fileName = "/data_crowd_" + id + ".txt";
      String fileData = "/data_crowd_data_" + id + ".txt";

      File file_1 = new File(fullPath + fileName);
      File file_2 = new File(fullPath + fileData);

      if (file_1.delete()) {
        resp.setStatus("success");
        System.out.println(fileName + " deleted successfully");
      } else {
        resp.setStatus("Failed");
        System.out.println("Failed to delete the file");
      }

      if (file_2.delete()) {
        resp.setStatus("success");
        System.out.println(fileData + " deleted successfully");
      } else {
        resp.setStatus("Failed");
        System.out.println("Failed to delete the file");
      }

    } catch (Exception e) {

      e.printStackTrace();

    }
    return resp;
  }

  @PostMapping("/geAllData")
  public String geAllData(@RequestBody String location, HttpServletRequest request) {

    try {

      JSONObject obj = new JSONObject(location);
     // JSONArray circleObjArray = new JSONArray();
      JSONArray arrayObjPolygone = new JSONArray();
      JSONArray ObjArray = new JSONArray();
      JSONArray ObjShapesArrayByDate = new JSONArray();

      JSONArray arrayOfDate = new JSONArray();
//      JSONArray arrayOfRegion = new JSONArray();

//      String Region= "2,142,19,150";
//      String SubRegion= "202,39,21,15,145";
      String Region=null;
      String SubRegion=null;

//      ArrayList<String[]> SubRegion = new ArrayList<>();

      arrayOfDate= obj.getJSONArray("arrayOfDate");
      
      
      System.out.println(" arrayOfDate>>>>>>>>>>>>>>>>"+arrayOfDate);
      //datacrowd.geo_data_<REG>_<SUB>_YYYY_MM

      Integer shapeId = 0;  
      String dataType = obj.getString("dataType");
      String provider = obj.getString("provider");
      String simulationType = obj.getString("reportType");
      String simulationToDate = obj.getString("DateTimeTo");
      //String simulationTimeZone = obj.getString("TimeZone");
      //String reportName = obj.getString("reportName");
      String simulationFromDate = obj.getString("DateTimeFrom");
      String simulationDevices = obj.getString("Devices");
      String imsiId = obj.getString("imsiId");
      String Coordinates = obj.getJSONArray("Coordinates").toString();
      String simulationId = obj.getString("simulationId");
      String telephoneNumber = obj.getString("telephoneNumber");
      String selectedStartDate = "";
      String selectedEndDate = "";
      String maxLongBR = "";
      String minLatBR = "";
      String minLongTL = "";
      String maxLatTL = "";
      String maxLongBL = "";
      String minLatBL = "";
      String minLongTR = "";
      String maxLatTR = "";
      String polygonCoordinates = "";
      String rectangleCoordinates = "";
      String radius = "";
      String shapeType = "";
      String filterCoordinates = "";
      String filterCircle = "";
      String latCircleCenter = "";
      String lngCircleCenter = "";
      @SuppressWarnings("unused")
      String dates = "";
      String serviceprovder = "";
      String devices = "";
      String callingNo = "";
      String query = "";
      String query_1 = "";
      Integer countOfPolygon = 0;
      Integer countOfPolyline = 0;
      @SuppressWarnings("unused")
      Integer countOfPolylineByDate = 0;
      String whereCondition = "";
      String allDevices = "";
      String lineCoords = "";
      String polylinelineCoords= "";
      String polylineCoordinates = "";
      String finalLineCoords = "";
      String polylineFinalLineCoords = "";
      @SuppressWarnings("unused")
      String routeOriginLat = "";
      @SuppressWarnings("unused")
      String routeOriginLng = "";
      @SuppressWarnings("unused")
      String routeDestLat = "";
      @SuppressWarnings("unused")
      String routeDestLng = "";
      @SuppressWarnings("unused")
      String routeLine = "";
      @SuppressWarnings("unused")
      String routeLineCoords = "";
      String finalRouteLineCoords = "";
      @SuppressWarnings("unused")
      Integer countOfRoute = 0;
      String finalPolRouteLine= "";
      JSONArray countryCode=  new JSONArray();

      
      System.out.println(" from date =="+simulationFromDate);
      
      allDevices = simulationDevices;
      String FromDate = simulationFromDate;
      @SuppressWarnings("deprecation")
      Date fromDateparse = new Date(FromDate);
      long fromDatemillis = fromDateparse.getTime();

      String ToDate = simulationToDate;
      @SuppressWarnings("deprecation")
      Date toDateparse = new Date(ToDate);
      long toDatemillis = toDateparse.getTime();

      
      System.out.println(" FromDate   "+FromDate);
      System.out.println(" ToDate   "+ToDate);
      
      
      System.out.println(" fromDatemillis   "+fromDatemillis);
      System.out.println(" ToDate   "+toDatemillis);

      
      if (simulationType.equals("1")) {

        if (!Coordinates.equals("[]")) {

          arrayObjPolygone = obj.getJSONArray("Coordinates");

          for (int i = 0; i < arrayObjPolygone.length(); i++) {
        	 
            shapeType = obj.getJSONArray("Coordinates").getJSONObject(i).getString("Type");
            shapeId =  obj.getJSONArray("Coordinates").getJSONObject(i).getInt("ID"); 

            JSONArray shapeArray = new JSONArray();
            JSONObject ASObj = new JSONObject();
            ASObj.put("shapeId",shapeId);
            shapeArray.put(ASObj);
            
            if (shapeType.equals("Polyline")) {
            	
   //zaher
                
                JSONArray countryjson = new JSONArray(obj.getJSONArray("Coordinates").getJSONObject(i).getJSONArray("countrycodes"));

                System.out.println("countryjson Polyline >>>>"+countryjson);
            	

              JSONArray lineArray = new JSONArray();
              lineArray = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONArray("Bounds");



              
              selectedStartDate  = obj.getJSONArray("Coordinates").getJSONObject(i).getString("selectedStartDate");
              selectedEndDate = obj.getJSONArray("Coordinates").getJSONObject(i).getString("selectedEndDate");

              
              if(selectedStartDate.equals("-1"))
              {       

                  for (int j = 0; j < lineArray.length(); j++) {

                      if (j == 0) {
                        lineCoords = lineArray.getJSONObject(j).get("lng").toString() + " " + lineArray.getJSONObject(j).get("lat").toString();
                      }
                      if (j > 0) {
                        lineCoords = lineCoords + "," + lineArray.getJSONObject(j).get("lng").toString() + " " + lineArray.getJSONObject(j).get("lat").toString();
                      }

                    }
                  
                  countOfPolyline++;

                  if (countOfPolyline.equals(1)) {
                    finalLineCoords = lineCoords;
                  } else if (countOfPolyline.equals(2)) {
                    finalLineCoords = "(" + finalLineCoords + "),(" + lineCoords + ")";
                  } else {
                    finalLineCoords = "(" + lineCoords + ")," + finalLineCoords;
                  }
                    
              }
              else
              {//
            	  
                  for (int j = 0; j < lineArray.length(); j++) {

                      if (j == 0) {
                    	  polylinelineCoords = lineArray.getJSONObject(j).get("lng").toString() + " " + lineArray.getJSONObject(j).get("lat").toString();
                      }
                      if (j > 0) {
                    	  polylinelineCoords = polylinelineCoords + "," + lineArray.getJSONObject(j).get("lng").toString() + " " + lineArray.getJSONObject(j).get("lat").toString();
                      }

                    }
                  
//                  countOfPolylineByDate++;

//                  if (countOfPolylineByDate.equals(1)) {
                	  polylineFinalLineCoords = polylinelineCoords;
//                    } else if (countOfPolylineByDate.equals(2)) {
////                    	polylineFinalLineCoords = "(" + polylineFinalLineCoords + "),(" + polylinelineCoords + ")";
//                    } else {
//                    	polylineFinalLineCoords = "(" + polylinelineCoords + ")," + polylineFinalLineCoords;
//                }
            	  
                  String finalPolylineCoordinates  = "";
                  
                  
                  if (!polylineFinalLineCoords.equals("") ) {
//                    if (countOfPolylineByDate > 1) {
//                       finalPolylineCoordinates = " {\r\n" +
//                        "              type: \"geo_shape\",\r\n" +
//                        "              field: \"place\",\r\n" +
//                        "              operation: \"intersects\",\r\n" +
//                        "              shape: {\r\n" +
//                        "                 type: \"buffer\",\r\n" +
//                        "                 max_distance: \"30m\",\r\n" +
//                        "                 shape: {\r\n" +
//                        "                    type: \"wkt\",\r\n" +
//                        "                    value: \"MULTILINESTRING(" + polylineFinalLineCoords + ")\"\r\n" +
//                        "                 }\r\n" +
//                        "              }\r\n" +
//                        "           }";
//                    } else {
                    	finalPolylineCoordinates = " {\r\n" +
                        "              type: \"geo_shape\",\r\n" +
                        "              field: \"place\",\r\n" +
                        "              operation: \"intersects\",\r\n" +
                        "              shape: {\r\n" +
                        "                 type: \"buffer\",\r\n" +
                        "                 max_distance: \"30m\",\r\n" +
                        "                 shape: {\r\n" +
                        "                    type: \"wkt\",\r\n" +
                        "                    value: \"LINESTRING(" + polylineFinalLineCoords + ")\"\r\n" +
                        "                 }\r\n" +
                        "              }\r\n" +
                        "           }";
//                    }
                    
                    	 //zaher
                 	   if(selectedStartDate.equals(""))
                        { 
                 		   System.out.println(" FromDate <>  "+FromDate);
                 		      System.out.println(" ToDate  <> "+ToDate);
                 		      selectedStartDate=FromDate;
                 		      selectedEndDate=ToDate;
                 		      
                        }
                 	   	
                    	
                    JSONObject polylineObjectByDate = new JSONObject();
                    polylineObjectByDate.put("coordinates", finalPolylineCoordinates);
                    polylineObjectByDate.put("selectedStartDate", selectedStartDate);
                    polylineObjectByDate.put("selectedEndDate", selectedEndDate);
                    polylineObjectByDate.put("countryjson", countryjson);

              	  
              	  ObjShapesArrayByDate.put(polylineObjectByDate);
              	  
                  }
               
              }

            }
            
//            if (shapeType.equals("Route")) {
//            	System.out.println(" route >>>  "+ obj.getJSONArray("Coordinates").getJSONObject(i).get("Origin").getClass().getSimpleName()) ;
//
//            if(obj.getJSONArray("Coordinates").getJSONObject(i).get("Origin").getClass().getSimpleName().equals("JSONObject"))
//            {
//            	System.out.println(" route >>>  "+ obj.getJSONArray("Coordinates").getJSONObject(i).get("Origin").getClass().getSimpleName()) ;
//            	countOfRoute++;
//                JSONArray lineArray = new JSONArray();
//                routeOriginLng = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Origin").get("lng").toString();
//                routeOriginLat = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Origin").get("lat").toString();
//                routeDestLng = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Destination").get("lng").toString();
//                routeDestLat =  obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Destination").get("lat").toString();
//                routeLine =   obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Destination").get("lat").toString();
//                
//                
//                JSONArray routeValueArray = new JSONArray();
//
//                routeValueArray = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONArray("Value");
//                
//                for (int j = 0; j < routeValueArray.length(); j++) {
//                	
//                	 JSONArray routeValueContainArray = new JSONArray();
//
//                	 routeValueContainArray = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONArray("Value").getJSONArray(j);
//                	 
//                     for (int k = 0; k < lineArray.length(); k++) {
//
//                         if (k == 0) {
//                         	routeLineCoords = lineArray.get(k).toString();
//                         	System.out.println("routeLineCoords >> 11  "+routeLineCoords);
//                         }
//                         if (k > 0) {
//                         	routeLineCoords = routeLineCoords + "," +  lineArray.get(k).toString();
//                         	System.out.println("routeLineCoords >> 22  "+routeLineCoords);
//
//                         }
//
//                       }
//
//                }
//
//                if (countOfRoute.equals(1)) {
//                	finalRouteLineCoords = routeOriginLat +","+ routeOriginLng +","+ routeLineCoords  +","+ routeDestLat+","+routeDestLng;
//                } else if (countOfPolyline.equals(2)) {
//                	finalRouteLineCoords = "(" + finalRouteLineCoords + "),(" + routeOriginLat +","+ routeOriginLng +","+ routeLineCoords  +","+ routeDestLat+","+routeDestLng + ")";
//                } else {
//                	finalRouteLineCoords = "(" + routeOriginLat +","+ routeOriginLng +","+ routeLineCoords  +","+ routeDestLat+","+routeDestLng + ")," + finalRouteLineCoords;
//                }
//
//              }
//            
//            System.out.println("finalRouteLineCoords >> "+finalRouteLineCoords);
//            }

            if (shapeType.equals("Rectangle")) {

            	   //zaher
                
                JSONArray countryjson = new JSONArray(obj.getJSONArray("Coordinates").getJSONObject(i).getJSONArray("countrycodes"));

                System.out.println("countryjson Rectangle >>>>"+countryjson);
                
                
            	
              countOfPolygon++;
              //            		  
              maxLongBR = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("bottomRight").get("lng").toString();
              minLatBR = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("bottomRight").get("lat").toString();
              minLongTL = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("topLeft").get("lng").toString();
              maxLatTL = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("topLeft").get("lat").toString();
              maxLongBL = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("bottomLeft").get("lng").toString();
              minLatBL = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("bottomLeft").get("lat").toString();
              minLongTR = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("topRight").get("lng").toString();
              maxLatTR = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("topRight").get("lat").toString();

              selectedStartDate  = obj.getJSONArray("Coordinates").getJSONObject(i).getString("selectedStartDate");
              selectedEndDate = obj.getJSONArray("Coordinates").getJSONObject(i).getString("selectedEndDate");

              
              if(selectedStartDate.equals("-1"))
              {        
                  if (rectangleCoordinates.equals("")) {
                      rectangleCoordinates = "(" + maxLongBR +
                        " " +
                        minLatBR +
                        "," +
                        maxLongBL +
                        " " +
                        minLatBL +
                        "," +
                        minLongTL +
                        " " +
                        maxLatTL +
                        "," +
                        minLongTR +
                        " " +
                        maxLatTR +
                        "," +
                        maxLongBR +
                        " " +
                        minLatBR +
                        ")";
                    } else {
                      rectangleCoordinates = "(" + maxLongBR +
                        " " +
                        minLatBR +
                        "," +
                        maxLongBL +
                        " " +
                        minLatBL +
                        "," +
                        minLongTL +
                        " " +
                        maxLatTL +
                        "," +
                        minLongTR +
                        " " +
                        maxLatTR +
                        "," +
                        maxLongBR +
                        " " +
                        minLatBR +
                        ")" +
                        "," +
                        rectangleCoordinates;
                    }
              }
              else
              {
            	  
            	  String rectangleCoordinatesByDate = "(" + maxLongBR +
                             " " +
                             minLatBR +
                             "," +
                             maxLongBL +
                             " " +
                             minLatBL +
                             "," +
                             minLongTL +
                             " " +
                             maxLatTL +
                             "," +
                             minLongTR +
                             " " +
                             maxLatTR +
                             "," +
                             maxLongBR +
                             " " +
                             minLatBR +
                             ")";
            	  
            	  String  filterRectangleCoordinates = "";
            	  
                  if (!rectangleCoordinatesByDate.equals("")) {
//                    	  rectangleCoordinatesByDate = rectangleCoordinatesByDate.replace("(", "((");
//                    	  rectangleCoordinatesByDate = rectangleCoordinatesByDate.replace(")", "))");
//
//                    	  filterRectangleCoordinates = "{" +
//                          "      type: \"geo_shape\"," +
//                          "      field: \"place\"," +
//                          "      shape: {" +
//                          "         type: \"wkt\"," +
//                          "         value: \"MULTIPOLYGON(" + rectangleCoordinatesByDate + ")\"" +
//                          "      }" +
//                          "    }";
//                    	  
                    	  filterRectangleCoordinates = "{" +
                                  "      type: \"geo_shape\"," +
                                  "      field: \"place\"," +
                                  "      shape: {" +
                                  "         type: \"wkt\"," +
                                  "         value: \"POLYGON(" + rectangleCoordinatesByDate + ")\"" +
                                  "      }" +
                                  "    }";
                    	  
                          
                    	  //zaher
                   	   if(selectedStartDate.equals(""))
                          { 
                   		   System.out.println(" FromDate <>  "+FromDate);
                   		      System.out.println(" ToDate  <> "+ToDate);
                   		      selectedStartDate=FromDate;
                   		      selectedEndDate=ToDate;
                   		      
                          }
                   	  
                    	  
                          JSONObject rectangleObjectByDate = new JSONObject();
                          rectangleObjectByDate.put("coordinates", filterRectangleCoordinates);
                          rectangleObjectByDate.put("selectedStartDate", selectedStartDate);
                          rectangleObjectByDate.put("selectedEndDate", selectedEndDate);
                          rectangleObjectByDate.put("countryjson", countryjson);

                    	  
                    	  ObjShapesArrayByDate.put(rectangleObjectByDate);
                    	  
                  
                  } 
            	  
              }

            }

            if (shapeType.equals("Polygon")) {
              countOfPolygon++;
              String firstPol = "";
              String polCoords = "";
              @SuppressWarnings("unused")
              String finalPolCoords = "";
              JSONArray jsonObjPol = new JSONArray();
              jsonObjPol = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONArray("Bounds");
              for (int j = 0; j < jsonObjPol.length(); j++) {

                if (j == 0) {
                  firstPol = jsonObjPol.getJSONObject(j).get("lng").toString() + " " + jsonObjPol.getJSONObject(j).get("lat").toString();
                  polCoords = jsonObjPol.getJSONObject(j).get("lng").toString() + " " + jsonObjPol.getJSONObject(j).get("lat").toString();
                }
                if (j > 0) {
                  polCoords = polCoords + "," + jsonObjPol.getJSONObject(j).get("lng").toString() + " " + jsonObjPol.getJSONObject(j).get("lat").toString();;
                }
              }

  
              
              selectedStartDate  = obj.getJSONArray("Coordinates").getJSONObject(i).getString("selectedStartDate");
              selectedEndDate = obj.getJSONArray("Coordinates").getJSONObject(i).getString("selectedEndDate");
              //zaher             
              JSONArray countryjson = new JSONArray(obj.getJSONArray("Coordinates").getJSONObject(i).getJSONArray("countrycodes"));

              System.out.println("countryjson>>>>"+countryjson);

              

              if(selectedStartDate.equals("-1"))
              {        
                  if (polygonCoordinates.equals("")) {
                      polygonCoordinates = "(" + polCoords + "," + firstPol + ")";
                    } else {
                      polygonCoordinates = "(" + polCoords + "," + firstPol + ")" + "," + polygonCoordinates;
                    }
                    
              }
              else
              {
            	  String polygonCoordinatesByDate ="(" + polCoords + "," + firstPol + ")";
            	  
            	  
       	  String  filterPolCoordinates = "";
            	  
                  if (!polygonCoordinatesByDate.equals("")) {
//                      if (countOfPolygon > 1) {
//                    	  polygonCoordinatesByDate = polygonCoordinatesByDate.replace("(", "((");
//                    	  polygonCoordinatesByDate = polygonCoordinatesByDate.replace(")", "))");

//                    	  filterPolCoordinates = "{" +
//                          "      type: \"geo_shape\"," +
//                          "      field: \"place\"," +
//                          "      shape: {" +
//                          "         type: \"wkt\"," +
//                          "         value: \"MULTIPOLYGON(" + polygonCoordinatesByDate + ")\"" +
//                          "      }" +
//                          "    }";
//                      } else {
                    	  filterPolCoordinates = "{" +
                          "      type: \"geo_shape\"," +
                          "      field: \"place\"," +
                          "      shape: {" +
                          "         type: \"wkt\"," +
                          "         value: \"POLYGON(" + polygonCoordinatesByDate + ")\"" +
                          "      }" +
                          "    }";
//                      }
                      
                      //zaher
                    	   if(selectedStartDate.equals(""))
                           { 
                    		   System.out.println(" FromDate <>  "+FromDate);
                    		      System.out.println(" ToDate  <> "+ToDate);
                    		      selectedStartDate=FromDate;
                    		      selectedEndDate=ToDate;
                    		      
                           }
                    	  
                    	  
                      JSONObject polygonObjectByDate = new JSONObject();
                      polygonObjectByDate.put("coordinates", filterPolCoordinates);
                      polygonObjectByDate.put("selectedStartDate", selectedStartDate);
                      polygonObjectByDate.put("selectedEndDate", selectedEndDate);
                      polygonObjectByDate.put("countryjson", countryjson);

                	  
                	  ObjShapesArrayByDate.put(polygonObjectByDate);

                    }
              }

            }

            if (shapeType.equals("Circle")) {
            	
   //zaher
                
                JSONArray countryjson = new JSONArray(obj.getJSONArray("Coordinates").getJSONObject(i).getJSONArray("countrycodes"));

                System.out.println("countryjson Circle >>>>"+countryjson);
            	
             selectedStartDate  = obj.getJSONArray("Coordinates").getJSONObject(i).getString("selectedStartDate");
             selectedEndDate = obj.getJSONArray("Coordinates").getJSONObject(i).getString("selectedEndDate");

             
             //zaher
      	   if(selectedStartDate.equals(""))
             { 
      		   System.out.println(" FromDate <>  "+FromDate);
      		      System.out.println(" ToDate  <> "+ToDate);
      		      selectedStartDate=FromDate;
      		      selectedEndDate=ToDate;
      		      
             }
      	  
             
              JSONObject circleObject = new JSONObject();
              circleObject.put("latCircleCenter", obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("center").get("lat").toString());
              circleObject.put("lngCircleCenter", obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("center").get("lng").toString());
              circleObject.put("radius", obj.getJSONArray("Coordinates").getJSONObject(i).get("radius").toString());
              circleObject.put("serviceProvider", dataType);
              circleObject.put("fromDate", fromDatemillis);
              circleObject.put("toDate", toDatemillis);
              circleObject.put("shapeId",shapeId);
              circleObject.put("selectedStartDate", selectedStartDate);
              circleObject.put("selectedEndDate", selectedEndDate);
              circleObject.put("countryjson", countryjson);

              ObjArray.put(circleObject);
              

            }

          }
          String multtpolygonCoordinates = "";

          if (!polygonCoordinates.equals("") && !rectangleCoordinates.equals("")) {
            multtpolygonCoordinates = polygonCoordinates + "," + rectangleCoordinates;
          }

          if (!polygonCoordinates.equals("") && rectangleCoordinates.equals("")) {
            multtpolygonCoordinates = polygonCoordinates;
          }

          if (polygonCoordinates.equals("") && !rectangleCoordinates.equals("")) {
            multtpolygonCoordinates = rectangleCoordinates;
          }

          if (!multtpolygonCoordinates.equals("")) {
            if (countOfPolygon > 1) {
              multtpolygonCoordinates = multtpolygonCoordinates.replace("(", "((");
              multtpolygonCoordinates = multtpolygonCoordinates.replace(")", "))");

              filterCoordinates = "{" +
                "      type: \"geo_shape\"," +
                "      field: \"place\"," +
                "      shape: {" +
                "         type: \"wkt\"," +
                "         value: \"MULTIPOLYGON(" + multtpolygonCoordinates + ")\"" +
                "      }" +
                "    }";
            } else {
              filterCoordinates = "{" +
                "      type: \"geo_shape\"," +
                "      field: \"place\"," +
                "      shape: {" +
                "         type: \"wkt\"," +
                "         value: \"POLYGON(" + multtpolygonCoordinates + ")\"" +
                "      }" +
                "    }";
            }

          }
          
          //finalRouteLineCoords
          
          if(!finalLineCoords.equals("")  && !finalRouteLineCoords.equals(""))
          {
        	  finalPolRouteLine = finalLineCoords + "," +finalRouteLineCoords;
          }else if (!finalLineCoords.equals(""))
          {
        	  finalPolRouteLine = finalLineCoords;
          }else
          {
        	 finalPolRouteLine = finalRouteLineCoords;
          }
        	  
          
          
          
          
          if (!finalPolRouteLine.equals("") ) {
            if (countOfPolyline > 1) {
              polylineCoordinates = " {\r\n" +
                "              type: \"geo_shape\",\r\n" +
                "              field: \"place\",\r\n" +
                "              operation: \"intersects\",\r\n" +
                "              shape: {\r\n" +
                "                 type: \"buffer\",\r\n" +
                "                 max_distance: \"30m\",\r\n" +
                "                 shape: {\r\n" +
                "                    type: \"wkt\",\r\n" +
                "                    value: \"MULTILINESTRING(" + finalPolRouteLine + ")\"\r\n" +
                "                 }\r\n" +
                "              }\r\n" +
                "           }";
            } else {
              polylineCoordinates = " {\r\n" +
                "              type: \"geo_shape\",\r\n" +
                "              field: \"place\",\r\n" +
                "              operation: \"intersects\",\r\n" +
                "              shape: {\r\n" +
                "                 type: \"buffer\",\r\n" +
                "                 max_distance: \"30m\",\r\n" +
                "                 shape: {\r\n" +
                "                    type: \"wkt\",\r\n" +
                "                    value: \"LINESTRING(" + finalPolRouteLine + ")\"\r\n" +
                "                 }\r\n" +
                "              }\r\n" +
                "           }";
            }
          }

          if (!simulationFromDate.equals("") && !simulationToDate.equals("")) {

            dates = "{" +
              "         \"type\": \"range\"," +
              "         \"field\": \"usage_timeframe\"," +
              "         \"lower\": " + fromDatemillis + "," +
              "         \"upper\": " + toDatemillis + "," +
              "         \"include_lower\": true," +
              "         \"include_upper\": true," +
              "         \"doc_values\": false" +
              "      }";
          }

          if (!telephoneNumber.equals("")) {

            String[] result = telephoneNumber.split(",");
            String dd = "";

            for (int i = 0; i < result.length; i++) {
              if (i == 0) {
                dd = "'" + result[i] + "'";
              } else {
                dd = dd + ',' + "'" + result[i] + "'";
              }

            }

            callingNo =  dd ;
          }

          if (!simulationDevices.equals("")) {

            String[] result = simulationDevices.split(",");
            String dd = "";

            for (int i = 0; i < result.length; i++) {
              if (i == 0) {
                dd = "'" + result[i] + "'";
              } else {
                dd = dd + ',' + "'" + result[i] + "'";
              }

            }

            devices = "AND device_id in (" + dd + ")";
          }

//          if (!dataType.equals("")) {
//            serviceprovder = "      {" +
//              "         \"type\": \"contains\"," +
//              "         \"field\": \"service_provider_id\"," +
//              "         \"values\":  [ " + dataType + " ]" +
//              "      }";
        	  
//        	  String ddd = "";
//              String[] result = dataType.split(",");
//
//              for (int i = 0; i < result.length; i++) {
//                if (i == 0) {
//                  ddd = "'" + result[i] + "'";
//                } else {
//                  ddd = ddd + ',' + "" + result[i] + "";
//                }
//              }
//              
//          	serviceprovder = " AND SERVICE_PROVIDER_ID IN ("+ddd+") ";

        	  
//          }

//          if (!filterCoordinates.equals("")) {
            JSONArray queryArray = new JSONArray();
            queryArray.put(filterCoordinates);
//            queryArray.put(dates);
//            queryArray.put(serviceprovder);
//            queryArray.put(polylineCoordinates);

            for (int k = 0; k < queryArray.length(); k++) {
              if (!queryArray.get(k).equals("")) {

                if (k == 0) {
                  query = queryArray.get(k).toString();
                } else {
                  if (!query.equals("")) {
                    query = query + "," + queryArray.get(k).toString();
                  } else {
                    query = queryArray.get(k).toString();
                  }
                }
              }
            }
            
            System.out.println(" Polygon Arrays >>>>>>>>>>>>>>>>>>>>>>>>>>> "+queryArray);
            
            query_1 = polylineCoordinates;
            
            
//          }

        }

				whereCondition = devices /* + serviceprovder */;

      } else if (simulationType.equals("2")) {
    	  
          System.out.println("telephone number - simulation devices - "+telephoneNumber+simulationDevices);
          System.out.println("countryCode DH"+ obj.getJSONArray("countryCode"));
          countryCode= obj.getJSONArray("countryCode");
        

        

        if (!simulationFromDate.equals("") && !simulationToDate.equals("")) {


          dates = "{" +
            "         \"type\": \"range\"," +
            "         \"field\": \"usage_timeframe\"," +
            "         \"lower\": " + fromDatemillis + "," +
            "         \"upper\": " + toDatemillis + "," +
            "         \"include_lower\": true," +
            "         \"include_upper\": true," +
            "         \"doc_values\": false" +
            "      }";
        }

        if (!telephoneNumber.equals("")) {

          String[] result = telephoneNumber.split(",");
          String dd = "";

          for (int i = 0; i < result.length; i++) {
            if (i == 0) {
              dd = "'" + result[i] + "'";
            } else {
              dd = dd + ',' + "'" + result[i] + "'";
            }

          }

          callingNo = dd ;
        }

        if (!simulationDevices.equals("")) {

          String[] result = simulationDevices.split(",");
          String dd = "";

          for (int i = 0; i < result.length; i++) {
            if (i == 0) {
              dd = "'" + result[i] + "'";
            } else {
              dd = dd + ',' + "'" + result[i] + "'";
            }

          }

          devices = " AND device_id in (" + dd + ") ";
        }

        if (!dataType.equals("")) {
//          serviceprovder = "      {" +
//            "         \"type\": \"contains\"," +
//            "         \"field\": \"service_provider_id\"," +
//            "         \"values\":  [ " + dataType + " ]" +
//            "      }";
        	
        	  String ddd = "";
              String[] result = dataType.split(",");

              for (int i = 0; i < result.length; i++) {
                if (i == 0) {
                  ddd = "" + result[i] + "";
                } else {
                  ddd = ddd + ',' + "" + result[i] + "";
                }
              }
        	
        	serviceprovder = " AND SERVICE_PROVIDER_ID IN ("+ddd+") ";
        	
        }

//        JSONArray queryArray = new JSONArray();
//        queryArray.put(dates);
//        queryArray.put(serviceprovder);

//        for (int k = 0; k < queryArray.length(); k++) {
//          if (!queryArray.get(k).equals("")) {
//
//            if (k == 0) {
//              query = queryArray.get(k).toString();
//            } else {
//              if (!query.equals("")) {
//                query = query + "," + queryArray.get(k).toString();
//              } else {
//                query = queryArray.get(k).toString();
//              }
//            }
//          }
//        }
        whereCondition = devices + serviceprovder;

      } else if (simulationType.equals("3")) {

        if (!Coordinates.equals("[]")) {

          arrayObjPolygone = obj.getJSONArray("Coordinates");
          
          if (!simulationFromDate.equals("") && !simulationToDate.equals("")) {

              dates = "{" +
                "         \"type\": \"range\"," +
                "         \"field\": \"usage_timeframe\"," +
                "         \"lower\": " + fromDatemillis + "," +
                "         \"upper\": " + toDatemillis + "," +
                "         \"include_lower\": true," +
                "         \"include_upper\": true," +
                "         \"doc_values\": false" +
                "      }";
            }

            if (!telephoneNumber.equals("")) {

              String[] result = telephoneNumber.split(",");
              String dd = "";

              for (int i = 0; i < result.length; i++) {
                if (i == 0) {
                  dd = "'" + result[i] + "'";
                } else {
                  dd = dd + ',' + "'" + result[i] + "'";
                }

              }

              callingNo = dd ;
            }

            if (!simulationDevices.equals("")) {

              String[] result = simulationDevices.split(",");
              String dd = "";

              for (int i = 0; i < result.length; i++) {
                if (i == 0) {
                  dd = "'" + result[i] + "'";
                } else {
                  dd = dd + ',' + "'" + result[i] + "'";
                }

              }

              devices = "AND device_id in (" + dd + ")";
            }
            

            if (!dataType.equals("")) {
              serviceprovder = "      {" +
                "         \"type\": \"contains\"," +
                "         \"field\": \"service_provider_id\"," +
                "         \"values\":  [ " + dataType + " ]" +
                "      }";
            }

          for (int i = 0; i < arrayObjPolygone.length(); i++) {

            shapeType = obj.getJSONArray("Coordinates").getJSONObject(i).getString("Type");
            shapeId =  obj.getJSONArray("Coordinates").getJSONObject(i).getInt("ID"); 
            System.out.println("dtp country json>>>"+obj.getJSONArray("Coordinates").getJSONObject(i).getJSONArray("countrycodes"));
            JSONArray countryjson = new JSONArray(obj.getJSONArray("Coordinates").getJSONObject(i).getJSONArray("countrycodes"));

            if (shapeType.equals("Polyline")) {
              countOfPolyline++;

              JSONArray lineArray = new JSONArray();
              lineArray = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONArray("Bounds");

              for (int j = 0; j < lineArray.length(); j++) {

                if (j == 0) {
                  lineCoords = lineArray.getJSONObject(j).get("lng").toString() + " " + lineArray.getJSONObject(j).get("lat").toString();
                }
                if (j > 0) {
                  lineCoords = lineCoords + "," + lineArray.getJSONObject(j).get("lng").toString() + " " + lineArray.getJSONObject(j).get("lat").toString();
                }

              }
              


//              if (countOfPolyline.equals(1)) {
                polylineCoordinates = " {\r\n" +
                  "              type: \"geo_shape\",\r\n" +
                  "              field: \"place\",\r\n" +
                  "              operation: \"intersects\",\r\n" +
                  "              shape: {\r\n" +
                  "                 type: \"buffer\",\r\n" +
                  "                 max_distance: \"30m\",\r\n" +
                  "                 shape: {\r\n" +
                  "                    type: \"wkt\",\r\n" +
                  "                    value: \"LINESTRING(" + lineCoords + ")\"\r\n" +
                  "                 }\r\n" +
                  "              }\r\n" +
                  "           }";
//              }
//              else {
//                polylineCoordinates = " {\r\n" +
//                  "              type: \"geo_shape\",\r\n" +
//                  "              field: \"place\",\r\n" +
//                  "              operation: \"intersects\",\r\n" +
//                  "              shape: {\r\n" +
//                  "                 type: \"buffer\",\r\n" +
//                  "                 max_distance: \"30m\",\r\n" +
//                  "                 shape: {\r\n" +
//                  "                    type: \"wkt\",\r\n" +
//                  "                    value: \"LINESTRING(" + lineCoords + ")\"\r\n" +
//                  "                 }\r\n" +
//                  "              }\r\n" +
//                  "           }," + polylineCoordinates;
//              }
              
              JSONObject DTPObj = new JSONObject();
              DTPObj.put("query",polylineCoordinates);
              DTPObj.put("shapeId",shapeId);
              DTPObj.put("countryjson",countryjson);

              
              ObjArray.put(DTPObj);

            }

            if (shapeType.equals("Rectangle")) {

              countOfPolygon++;
              //            		  
              maxLongBR  = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("bottomRight").get("lng").toString();
              minLatBR   = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("bottomRight").get("lat").toString();
              minLongTL  = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("topLeft").get("lng").toString();
              maxLatTL   = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("topLeft").get("lat").toString();
              maxLongBL  = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("bottomLeft").get("lng").toString();
              minLatBL   = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("bottomLeft").get("lat").toString();
              minLongTR  = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("topRight").get("lng").toString();
              maxLatTR   = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").getJSONObject("topRight").get("lat").toString();

//              if (rectangleCoordinates.equals("")) {
                rectangleCoordinates = "(" + maxLongBR +
                  " " +
                  minLatBR +
                  "," +
                  maxLongBL +
                  " " +
                  minLatBL +
                  "," +
                  minLongTL +
                  " " +
                  maxLatTL +
                  "," +
                  minLongTR +
                  " " +
                  maxLatTR +
                  "," +
                  maxLongBR +
                  " " +
                  minLatBR +
                  ")";

                rectangleCoordinates = "{" +
                  "      type: \"geo_shape\"," +
                  "      field: \"place\"," +
                  "      shape: {" +
                  "         type: \"wkt\"," +
                  "         value: \"POLYGON((" + maxLongBR +
                  " " +
                  minLatBR +
                  "," +
                  maxLongBL +
                  " " +
                  minLatBL +
                  "," +
                  minLongTL +
                  " " +
                  maxLatTL +
                  "," +
                  minLongTR +
                  " " +
                  maxLatTR +
                  "," +
                  maxLongBR +
                  " " +
                  minLatBR + "))\"" +
                  "      }" +
                  "    }";

//              } else {
//                rectangleCoordinates = "{" +
//                  "      type: \"geo_shape\"," +
//                  "      field: \"place\"," +
//                  "      shape: {" +
//                  "         type: \"wkt\"," +
//                  "         value: \"POLYGON((" + maxLongBR +
//                  " " +
//                  minLatBR +
//                  "," +
//                  maxLongBL +
//                  " " +
//                  minLatBL +
//                  "," +
//                  minLongTL +
//                  " " +
//                  maxLatTL +
//                  "," +
//                  minLongTR +
//                  " " +
//                  maxLatTR +
//                  "," +
//                  maxLongBR +
//                  " " +
//                  minLatBR + "))\"" +
//                  "      }" +
//                  "    }," + rectangleCoordinates;
//              }
              
              JSONObject DTPObj = new JSONObject();
              DTPObj.put("query",rectangleCoordinates);
              DTPObj.put("shapeId",shapeId);
              DTPObj.put("countryjson",countryjson);

              ObjArray.put(DTPObj);

            }

            if (shapeType.equals("Polygon")) {
              countOfPolygon++;
              String firstPol = "";
              String polCoords = "";
              JSONArray jsonObjPol = new JSONArray();
              jsonObjPol = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONArray("Bounds");
              for (int j = 0; j < jsonObjPol.length(); j++) {

                if (j == 0) {
                  firstPol = jsonObjPol.getJSONObject(j).get("lng").toString() + " " + jsonObjPol.getJSONObject(j).get("lat").toString();
                  polCoords = jsonObjPol.getJSONObject(j).get("lng").toString() + " " + jsonObjPol.getJSONObject(j).get("lat").toString();
                }
                if (j > 0) {
                  polCoords = polCoords + "," + jsonObjPol.getJSONObject(j).get("lng").toString() + " " + jsonObjPol.getJSONObject(j).get("lat").toString();;
                }
              }

//              if (polygonCoordinates.equals("")) {

                polygonCoordinates = "{" +
                  "      type: \"geo_shape\"," +
                  "      field: \"place\"," +
                  "      shape: {" +
                  "         type: \"wkt\"," +
                  "         value: \"POLYGON(" + "(" + polCoords + "," + firstPol + ")" + ")\"" +
                  "      }" +
                  "    }";

//              } else {
//
//                polygonCoordinates = "{" +
//                  "      type: \"geo_shape\"," +
//                  "      field: \"place\"," +
//                  "      shape: {" +
//                  "         type: \"wkt\"," +
//                  "         value: \"POLYGON(" + "(" + polCoords + "," + firstPol + ")" + ")\"" +
//                  "      }" +
//                  "    } ," + polygonCoordinates;
//              }

                JSONObject DTPObj = new JSONObject();
                DTPObj.put("query",polygonCoordinates);
                DTPObj.put("shapeId",shapeId);
                DTPObj.put("countryjson",countryjson);
                ObjArray.put(DTPObj);
            }

//            if (!polygonCoordinates.equals("") && !rectangleCoordinates.equals("")) {
//              filterCoordinates = polygonCoordinates + "," + rectangleCoordinates;
//            }
//
//            if (!polygonCoordinates.equals("") && rectangleCoordinates.equals("")) {
//              filterCoordinates = polygonCoordinates;
//            }
//
//            if (polygonCoordinates.equals("") && !rectangleCoordinates.equals("")) {
//              filterCoordinates = rectangleCoordinates;
//            }

            if (shapeType.equals("Circle")) {

              latCircleCenter = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("center").get("lat").toString();
              lngCircleCenter = obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("center").get("lng").toString();
              radius = obj.getJSONArray("Coordinates").getJSONObject(i).get("radius").toString();

//              if (filterCircle.equals("")) {

                if (!radius.equals("")) {
                  filterCircle = "{" +
                    "      type: \"geo_distance\"," +
                    "      field: \"place\"," +
                    "      latitude: " + latCircleCenter + "," +
                    "      longitude: " + lngCircleCenter + "," +
                    "      max_distance: \"" + radius + " m\"" +
                    "   }";

                }
                
//              } else {
//                if (!radius.equals("")) {
//                  filterCircle = "{" +
//                    "      type: \"geo_distance\"," +
//                    "      field: \"place\"," +
//                    "      latitude: " + latCircleCenter + "," +
//                    "      longitude: " + lngCircleCenter + "," +
//                    "      max_distance: \"" + radius + " m\"" +
//                    "   }," + filterCircle;
//
//                }
//              }
                
                System.out.println("countryjson<><>"+countryjson);

              JSONObject DTPObj = new JSONObject();
              DTPObj.put("query",filterCircle);
              DTPObj.put("shapeId",shapeId);
              DTPObj.put("countryjson",countryjson);

              ObjArray.put(DTPObj);
              
            }

          }

    

          JSONArray queryArray = new JSONArray();
          
//          queryArray.put(dates);
          queryArray.put(serviceprovder);

          for (int k = 0; k < queryArray.length(); k++) {
            if (!queryArray.get(k).equals("")) {

              if (k == 0) {
            	  
            	  if(ObjArray.isEmpty())
            	  {
                      query = queryArray.get(k).toString();
            	  }
            	  else
            	  {
                      query = ","+queryArray.get(k).toString();

            	  }
            	  
            	  
              } else {
                if (!query.equals("")) {
                  query = query + "," + queryArray.get(k).toString();
                } else {
                  query = queryArray.get(k).toString();
                }
              }
            }
          }
        }
        
        System.out.println("query  0  >> "+query);
        System.out.println("devices  0  >> "+devices);
        System.out.println("ObjArray  0  >> "+query);
        whereCondition = devices;

      }else if (simulationType.equals("11")) {
    	  
      
//          JSONObject TCDObject = new JSONObject();
//          JSONArray TCDArray = new JSONArray();
//
//          //imsiId
//          String TCDFilter = "";
//          
//          if (!simulationFromDate.equals("") && !simulationToDate.equals("")) {
//
//              if(!TCDFilter.equals(""))
//              {
//            	  TCDFilter = TCDFilter +  ",{" +
//                          "         \"type\": \"range\"," +
//                          "         \"field\": \"usage_timeframe\"," +
//                          "         \"lower\": " + fromDatemillis + "," +
//                          "         \"upper\": " + toDatemillis + "," +
//                          "         \"include_lower\": true," +
//                          "         \"include_upper\": true," +
//                          "         \"doc_values\": false" +
//                          "      }";
//              }
//              else
//              {
//            	  TCDFilter =    "{" +
//                          "         \"type\": \"range\"," +
//                          "         \"field\": \"usage_timeframe\"," +
//                          "         \"lower\": " + fromDatemillis + "," +
//                          "         \"upper\": " + toDatemillis + "," +
//                          "         \"include_lower\": true," +
//                          "         \"include_upper\": true," +
//                          "         \"doc_values\": false" +
//                          "      }";
//              }
//            }
//    	  
//          if (!imsiId.equals("")) {
//
//              String[] result = imsiId.split(",");
//              String dd = "";
//
//              for (int i = 0; i < result.length; i++) {
//                if (i == 0) {
//                  dd = "'" + result[i] + "'";
//                } else {
//                  dd = dd + ',' + "'" + result[i] + "'";
//                }
//
//              }
//             
//              if(!TCDFilter.equals(""))
//              {
//            	  TCDFilter =  TCDFilter +  ",{\"type\": \"contains\",\"field\": \"called_imsi_id\",\"values\":  [ "+dd+"]}";  
//              }
//              else
//              {
//            	  TCDFilter =  "{\"type\": \"contains\",\"field\": \"called_imsi_id\",\"values\":  [ "+dd+"]}";  
//              }
//              
////              devices = "AND device_id in (" + dd + ")";
//            }
//          
//   	      if(!dataType.equals(""))
//    	    {
//    	    	  String[] result0 = dataType.split(",");
//       	 	   String getProviderFilter = "";
//        	 	 
//     	 	    for (int i = 0; i < result0.length; i++) {
//     	 	      if (i == 0) {
//     	 	    	getProviderFilter = "" + result0[i] + "";
//     	 	      } else {
//     	 	    	getProviderFilter = getProviderFilter + ',' + "" + result0[i] + "";
//     	 	      }
//     	 	    }
//     	 	    
//                if(!TCDFilter.equals(""))
//                {
//              	  TCDFilter =  TCDFilter +  " ,{" +
//      	                "         \"type\": \"contains\"," +
//      	                "         \"field\": \"service_provider_id\"," +
//      	                "         \"values\":  [ " +getProviderFilter + " ]" +
//      	                "      },";;  
//                }
//                else
//                {
//              	  TCDFilter =  " {" +
//      	                "         \"type\": \"contains\"," +
//      	                "         \"field\": \"service_provider_id\"," +
//      	                "         \"values\":  [ " +getProviderFilter + " ]" +
//      	                "      },";
//                }
//
//
//    	 	 
//
//    	    }
   	      
   	      
          
          
    	  
      }
      
      System.out.println(" from date"+fromDatemillis);
      System.out.println(" to Datemillis"+toDatemillis);
      
      
      System.out.println("country code geAllData "+countryCode);
     	 System.out.println(" callingNo geAllData "+callingNo);


      byte[] resp = locationService.FilteringData(query,query_1, simulationId, whereCondition,ObjShapesArrayByDate,
    		  											  ObjArray,simulationType,provider,
    		  											  fromDatemillis,toDatemillis,
    		  											  allDevices,dataType,callingNo,imsiId,countryCode,arrayOfDate,SubRegion,Region,request);
      System.out.println("resp filtering "+resp);
      return resp.toString();

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
//    return null;

  }
  
  
  @PostMapping("/getKnowledgeGraphOneRecord")
  public String getKnowledgeGraphOneRecord(@RequestBody String Params) {

	  String status = ""; 	  
    try {
        System.out.println(" in  getKnowledgeGraphOneRecord>>>>>>>>>>");

        JSONObject obj = new JSONObject(Params);
        String simulationId   = obj.getString("simulationId");
        Long fromDatemillis   = obj.getLong("fromDatemillis");
        Long  toDatemillis    = obj.getLong("toDatemillis");
		String callingNo      = obj.getString("callingNo");
		String calledNo       =  obj.getString("calledNo");
		String linkType       =  obj.getString("linkType");
		String directIndirect = obj.getString("directIndirect");
		String devices = obj.getString("devices");

		
		long layer   =1;
		if(directIndirect.equals("indirect")){
			layer = obj.getLong("layer");
		}

        System.out.println(" directIndirect >> "+directIndirect);
       

        
        

		
		status = locationService.getKnowledgeGraphOneRecord(devices, simulationId,  fromDatemillis,  toDatemillis, callingNo,  calledNo , linkType , directIndirect ,layer );
    	
    }catch (Exception e) {
    	status = "Fail";
    	e.printStackTrace();
    
    }
	return status;
    }
  
	/*
	 * @GetMapping("/getCallingNo") public String getCallingNo(@RequestBody String
	 * device) {
	 * 
	 * String callingNo = ""; try { device = locationService.getCallingNo(device);
	 * 
	 * }catch (Exception e) { e.printStackTrace();
	 * 
	 * } return callingNo; }
	 */
  
	@GetMapping("/getCallingNo/{device}")
	  public 	String  getCallingNo(@PathVariable("device") String device) {

	    String callingNo ="";
	    try {
	    	callingNo = locationService.getCallingNo(device).toString();	     
	    } catch (Exception e) {
              e.printStackTrace();
            }
	    return callingNo;
	  }

	
  @PostMapping("/getKnowledgeGraphRecords")
  public String getKnowledgeGraphRecords(@RequestBody String Params) {

	  String status = ""; 	  
    try {
        JSONObject obj = new JSONObject(Params);
        String simulationId    = obj.getString("simulationId");
        long   fromDatemillis  = obj.getLong("fromDatemillis");
        long   toDatemillis    = obj.getLong("toDatemillis");
		String callingNo       = obj.getString("callingNo");
		String calledNo        = obj.getString("calledNo");	
		String linkType       =  obj.getString("linkType");
		String status1       =  obj.getString("status");
		String devices       =  obj.getString("devices");

		
		
		 System.out.println(" fromDatemillis >> "+fromDatemillis);
	     

		long layer = obj.getLong("layers");

        
		status = locationService.getKnowledgeGraphRecords(devices, simulationId,  fromDatemillis,  toDatemillis, callingNo,  calledNo ,linkType, layer, status1);
    	
    }catch (Exception e) {
    	status = "Fail";
    	e.printStackTrace();
    
    }
	return status;
    }
  
  @PostMapping("/checkShapeType")
  public CustomResponse checkShapeType(@RequestBody String coordinates) {

    try {
    
        String fileName = "/shape_json.txt";
        String fullPath = shapeRepositoryPath;
        
        File f = new File(fullPath, fileName);
        @SuppressWarnings({ "resource", "unused" })
        BufferedReader reader = new BufferedReader(new FileReader(f));
        
        
    	System.out.println(" afterr >>>>>>>  reader.readLine().toString()");
    	
    }catch (Exception e) {
    
    	e.printStackTrace();
    
    }
	return null;
    }
  
  @PostMapping("/insertShape")
  public CustomResponse insertShape(@RequestBody String coordinates) {

    try {
        
    	System.out.println(" insert shape method )");
        @SuppressWarnings("unused")
        CustomResponse resp = locationService.insertShape(coordinates);

    	
    	
    }catch (Exception e) {
    
    	e.printStackTrace();
    
    }
	return null;
    }

}