/*
 * package com.valoores.cassandradatacrowd.common.api;
 * 
 * import java.util.Date;
 * 
 * import javax.servlet.http.HttpServletRequest;
 * 
 * import org.json.JSONArray; import org.json.JSONObject; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestBody; import
 * org.springframework.web.bind.annotation.RestController;
 * 
 * import com.valoores.cassandradatacrowd.common.dto.log; import
 * com.valoores.cassandradatacrowd.common.service.logService;
 * 
 * @RestController public class logRestController {
 * 
 * @Autowired private logService logService;
 * 
 * @PostMapping("/geAllData") public log geAllData(@RequestBody String location,
 * HttpServletRequest request) {
 * 
 * try {
 * 
 * JSONObject obj = new JSONObject(location); JSONArray circleObjArray = new
 * JSONArray(); JSONArray arrayObjPolygone = new JSONArray(); JSONArray ObjArray
 * = new JSONArray();
 * 
 * Integer shapeId = 0; String dataType = obj.getString("dataType"); String
 * provider = obj.getString("provider"); String simulationType =
 * obj.getString("reportType"); String simulationToDate =
 * obj.getString("DateTimeTo"); String simulationTimeZone =
 * obj.getString("TimeZone"); String reportName = obj.getString("reportName");
 * String simulationFromDate = obj.getString("DateTimeFrom"); String
 * simulationDevices = obj.getString("Devices"); String Coordinates =
 * obj.getJSONArray("Coordinates").toString(); String simulationId =
 * obj.getString("simulationId"); String telephoneNumber =
 * obj.getString("telephoneNumber"); String maxLongBR = ""; String minLatBR =
 * ""; String minLongTL = ""; String maxLatTL = ""; String maxLongBL = "";
 * String minLatBL = ""; String minLongTR = ""; String maxLatTR = ""; String
 * polygonCoordinates = ""; String rectangleCoordinates = ""; String radius =
 * ""; String shapeType = ""; String filterCoordinates = ""; String filterCircle
 * = ""; String latCircleCenter = ""; String lngCircleCenter = ""; String dates
 * = ""; String serviceprovder = ""; String devices = ""; String query = "";
 * Integer countOfPolygon = 0; Integer countOfPolyline = 0; String
 * whereCondition = ""; String allDevices = ""; String lineCoords = ""; String
 * polylineCoordinates = ""; String finalLineCoords = "";
 * 
 * allDevices = telephoneNumber+simulationDevices; String FromDate =
 * simulationFromDate; Date fromDateparse = new Date(FromDate); long
 * fromDatemillis = fromDateparse.getTime();
 * 
 * String ToDate = simulationToDate; Date toDateparse = new Date(ToDate); long
 * toDatemillis = toDateparse.getTime();
 * 
 * if (simulationType.equals("1")) {
 * 
 * if (!Coordinates.equals("[]")) {
 * 
 * arrayObjPolygone = obj.getJSONArray("Coordinates");
 * 
 * for (int i = 0; i < arrayObjPolygone.length(); i++) {
 * 
 * shapeType =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getString("Type");
 * 
 * if (shapeType.equals("Polyline")) {
 * 
 * countOfPolyline++; JSONArray lineArray = new JSONArray(); lineArray =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONArray("Bounds");
 * 
 * for (int j = 0; j < lineArray.length(); j++) {
 * 
 * if (j == 0) { lineCoords = lineArray.getJSONObject(j).get("lng").toString() +
 * " " + lineArray.getJSONObject(j).get("lat").toString(); } if (j > 0) {
 * lineCoords = lineCoords + "," +
 * lineArray.getJSONObject(j).get("lng").toString() + " " +
 * lineArray.getJSONObject(j).get("lat").toString(); }
 * 
 * }
 * 
 * if (countOfPolyline.equals(1)) { finalLineCoords = lineCoords; } else if
 * (countOfPolyline.equals(2)) { finalLineCoords = "(" + finalLineCoords + "),("
 * + lineCoords + ")"; } else { finalLineCoords = "(" + lineCoords + ")," +
 * finalLineCoords; }
 * 
 * }
 * 
 * if (shapeType.equals("Rectangle")) {
 * 
 * countOfPolygon++; // maxLongBR =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").
 * getJSONObject("bottomRight").get("lng").toString(); minLatBR =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").
 * getJSONObject("bottomRight").get("lat").toString(); minLongTL =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").
 * getJSONObject("topLeft").get("lng").toString(); maxLatTL =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").
 * getJSONObject("topLeft").get("lat").toString(); maxLongBL =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").
 * getJSONObject("bottomLeft").get("lng").toString(); minLatBL =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").
 * getJSONObject("bottomLeft").get("lat").toString(); minLongTR =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").
 * getJSONObject("topRight").get("lng").toString(); maxLatTR =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").
 * getJSONObject("topRight").get("lat").toString();
 * 
 * if (rectangleCoordinates.equals("")) { rectangleCoordinates = "(" + maxLongBR
 * + " " + minLatBR + "," + maxLongBL + " " + minLatBL + "," + minLongTL + " " +
 * maxLatTL + "," + minLongTR + " " + maxLatTR + "," + maxLongBR + " " +
 * minLatBR + ")"; } else { rectangleCoordinates = "(" + maxLongBR + " " +
 * minLatBR + "," + maxLongBL + " " + minLatBL + "," + minLongTL + " " +
 * maxLatTL + "," + minLongTR + " " + maxLatTR + "," + maxLongBR + " " +
 * minLatBR + ")" + "," + rectangleCoordinates; }
 * 
 * }
 * 
 * if (shapeType.equals("Polygon")) { countOfPolygon++; String firstPol = "";
 * String polCoords = ""; String finalPolCoords = ""; JSONArray jsonObjPol = new
 * JSONArray(); jsonObjPol =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONArray("Bounds"); for
 * (int j = 0; j < jsonObjPol.length(); j++) {
 * 
 * if (j == 0) { firstPol = jsonObjPol.getJSONObject(j).get("lng").toString() +
 * " " + jsonObjPol.getJSONObject(j).get("lat").toString(); polCoords =
 * jsonObjPol.getJSONObject(j).get("lng").toString() + " " +
 * jsonObjPol.getJSONObject(j).get("lat").toString(); } if (j > 0) { polCoords =
 * polCoords + "," + jsonObjPol.getJSONObject(j).get("lng").toString() + " " +
 * jsonObjPol.getJSONObject(j).get("lat").toString();; } }
 * 
 * if (polygonCoordinates.equals("")) { polygonCoordinates = "(" + polCoords +
 * "," + firstPol + ")"; } else { polygonCoordinates = "(" + polCoords + "," +
 * firstPol + ")" + "," + polygonCoordinates; }
 * 
 * }
 * 
 * if (shapeType.equals("Circle")) {
 * 
 * JSONObject circleObject = new JSONObject();
 * circleObject.put("latCircleCenter",
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("center").get(
 * "lat").toString()); circleObject.put("lngCircleCenter",
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("center").get(
 * "lng").toString()); circleObject.put("radius",
 * obj.getJSONArray("Coordinates").getJSONObject(i).get("radius").toString());
 * circleObject.put("serviceProvider", dataType); circleObject.put("fromDate",
 * fromDatemillis); circleObject.put("toDate", toDatemillis);
 * circleObject.put("shapeId",shapeId); ObjArray.put(circleObject);
 * 
 * }
 * 
 * } String multtpolygonCoordinates = "";
 * 
 * if (!polygonCoordinates.equals("") && !rectangleCoordinates.equals("")) {
 * multtpolygonCoordinates = polygonCoordinates + "," + rectangleCoordinates; }
 * 
 * if (!polygonCoordinates.equals("") && rectangleCoordinates.equals("")) {
 * multtpolygonCoordinates = polygonCoordinates; }
 * 
 * if (polygonCoordinates.equals("") && !rectangleCoordinates.equals("")) {
 * multtpolygonCoordinates = rectangleCoordinates; }
 * 
 * if (!multtpolygonCoordinates.equals("")) { if (countOfPolygon > 1) {
 * multtpolygonCoordinates = multtpolygonCoordinates.replace("(", "((");
 * multtpolygonCoordinates = multtpolygonCoordinates.replace(")", "))");
 * 
 * filterCoordinates = "{" + "      type: \"geo_shape\"," +
 * "      field: \"place\"," + "      shape: {" + "         type: \"wkt\"," +
 * "         value: \"MULTIPOLYGON(" + multtpolygonCoordinates + ")\"" +
 * "      }" + "    }"; } else { filterCoordinates = "{" +
 * "      type: \"geo_shape\"," + "      field: \"place\"," + "      shape: {" +
 * "         type: \"wkt\"," + "         value: \"POLYGON(" +
 * multtpolygonCoordinates + ")\"" + "      }" + "    }"; }
 * 
 * }
 * 
 * if (!finalLineCoords.equals("")) { if (countOfPolyline > 1) {
 * polylineCoordinates = " {\r\n" + "              type: \"geo_shape\",\r\n" +
 * "              field: \"place\",\r\n" +
 * "              operation: \"intersects\",\r\n" + "              shape: {\r\n"
 * + "                 type: \"buffer\",\r\n" +
 * "                 max_distance: \"30m\",\r\n" +
 * "                 shape: {\r\n" + "                    type: \"wkt\",\r\n" +
 * "                    value: \"MULTILINESTRING(" + finalLineCoords + ")\"\r\n"
 * + "                 }\r\n" + "              }\r\n" + "           }"; } else {
 * polylineCoordinates = " {\r\n" + "              type: \"geo_shape\",\r\n" +
 * "              field: \"place\",\r\n" +
 * "              operation: \"intersects\",\r\n" + "              shape: {\r\n"
 * + "                 type: \"buffer\",\r\n" +
 * "                 max_distance: \"30m\",\r\n" +
 * "                 shape: {\r\n" + "                    type: \"wkt\",\r\n" +
 * "                    value: \"LINESTRING(" + finalLineCoords + ")\"\r\n" +
 * "                 }\r\n" + "              }\r\n" + "           }"; } }
 * 
 * if (!simulationFromDate.equals("") && !simulationToDate.equals("")) {
 * 
 * dates = "{" + "         \"type\": \"range\"," +
 * "         \"field\": \"usage_timeframe\"," + "         \"lower\": " +
 * fromDatemillis + "," + "         \"upper\": " + toDatemillis + "," +
 * "         \"include_lower\": true," + "         \"include_upper\": true," +
 * "         \"doc_values\": false" + "      }"; }
 * 
 * if (!telephoneNumber.equals("")) {
 * 
 * String[] result = telephoneNumber.split(","); String dd = "";
 * 
 * for (int i = 0; i < result.length; i++) { if (i == 0) { dd = "'" + result[i]
 * + "'"; } else { dd = dd + ',' + "'" + result[i] + "'"; }
 * 
 * }
 * 
 * devices = "AND device_id in (" + dd + ")"; }
 * 
 * if (!simulationDevices.equals("")) {
 * 
 * String[] result = simulationDevices.split(","); String dd = "";
 * 
 * for (int i = 0; i < result.length; i++) { if (i == 0) { dd = "'" + result[i]
 * + "'"; } else { dd = dd + ',' + "'" + result[i] + "'"; }
 * 
 * }
 * 
 * devices = "AND device_id in (" + dd + ")"; }
 * 
 * if (!dataType.equals("")) { serviceprovder = "      {" +
 * "         \"type\": \"contains\"," +
 * "         \"field\": \"service_provider_id\"," + "         \"values\":  [ " +
 * dataType + " ]" + "      }"; }
 * 
 * if (!filterCoordinates.equals("")) { JSONArray queryArray = new JSONArray();
 * queryArray.put(filterCoordinates); // queryArray.put(dates);
 * queryArray.put(serviceprovder); queryArray.put(polylineCoordinates);
 * 
 * for (int k = 0; k < queryArray.length(); k++) { if
 * (!queryArray.get(k).equals("")) {
 * 
 * if (k == 0) { query = queryArray.get(k).toString(); } else { if
 * (!query.equals("")) { query = query + "," + queryArray.get(k).toString(); }
 * else { query = queryArray.get(k).toString(); } } } } }
 * 
 * }
 * 
 * whereCondition = devices;
 * 
 * } else if (simulationType.equals("2")) {
 * 
 * System.out.println("telephone number - simulation devices - "+telephoneNumber
 * +simulationDevices);
 * 
 * 
 * if (!simulationFromDate.equals("") && !simulationToDate.equals("")) {
 * 
 * 
 * dates = "{" + "         \"type\": \"range\"," +
 * "         \"field\": \"usage_timeframe\"," + "         \"lower\": " +
 * fromDatemillis + "," + "         \"upper\": " + toDatemillis + "," +
 * "         \"include_lower\": true," + "         \"include_upper\": true," +
 * "         \"doc_values\": false" + "      }"; }
 * 
 * if (!telephoneNumber.equals("")) {
 * 
 * String[] result = telephoneNumber.split(","); String dd = "";
 * 
 * for (int i = 0; i < result.length; i++) { if (i == 0) { dd = "'" + result[i]
 * + "'"; } else { dd = dd + ',' + "'" + result[i] + "'"; }
 * 
 * }
 * 
 * devices = "AND device_id in (" + dd + ")"; }
 * 
 * if (!simulationDevices.equals("")) {
 * 
 * String[] result = simulationDevices.split(","); String dd = "";
 * 
 * for (int i = 0; i < result.length; i++) { if (i == 0) { dd = "'" + result[i]
 * + "'"; } else { dd = dd + ',' + "'" + result[i] + "'"; }
 * 
 * }
 * 
 * devices = " AND device_id in (" + dd + ") "; }
 * 
 * if (!dataType.equals("")) { // serviceprovder = "      {" + //
 * "         \"type\": \"contains\"," + //
 * "         \"field\": \"service_provider_id\"," + //
 * "         \"values\":  [ " + dataType + " ]" + // "      }";
 * 
 * String ddd = ""; String[] result = dataType.split(",");
 * 
 * for (int i = 0; i < result.length; i++) { if (i == 0) { ddd = "'" + result[i]
 * + "'"; } else { ddd = ddd + ',' + "'" + result[i] + "'"; } }
 * 
 * serviceprovder = " AND SERVICE_PROVIDER_ID IN ("+ddd+") ";
 * 
 * }
 * 
 * // JSONArray queryArray = new JSONArray(); // queryArray.put(dates); //
 * queryArray.put(serviceprovder);
 * 
 * // for (int k = 0; k < queryArray.length(); k++) { // if
 * (!queryArray.get(k).equals("")) { // // if (k == 0) { // query =
 * queryArray.get(k).toString(); // } else { // if (!query.equals("")) { //
 * query = query + "," + queryArray.get(k).toString(); // } else { // query =
 * queryArray.get(k).toString(); // } // } // } // } whereCondition = devices +
 * serviceprovder;
 * 
 * } else if (simulationType.equals("3")) {
 * 
 * if (!Coordinates.equals("[]")) {
 * 
 * arrayObjPolygone = obj.getJSONArray("Coordinates");
 * 
 * if (!simulationFromDate.equals("") && !simulationToDate.equals("")) {
 * 
 * dates = "{" + "         \"type\": \"range\"," +
 * "         \"field\": \"usage_timeframe\"," + "         \"lower\": " +
 * fromDatemillis + "," + "         \"upper\": " + toDatemillis + "," +
 * "         \"include_lower\": true," + "         \"include_upper\": true," +
 * "         \"doc_values\": false" + "      }"; }
 * 
 * if (!telephoneNumber.equals("")) {
 * 
 * String[] result = telephoneNumber.split(","); String dd = "";
 * 
 * for (int i = 0; i < result.length; i++) { if (i == 0) { dd = "'" + result[i]
 * + "'"; } else { dd = dd + ',' + "'" + result[i] + "'"; }
 * 
 * }
 * 
 * devices = "AND device_id in (" + dd + ")"; }
 * 
 * if (!simulationDevices.equals("")) {
 * 
 * String[] result = simulationDevices.split(","); String dd = "";
 * 
 * for (int i = 0; i < result.length; i++) { if (i == 0) { dd = "'" + result[i]
 * + "'"; } else { dd = dd + ',' + "'" + result[i] + "'"; }
 * 
 * }
 * 
 * devices = "AND device_id in (" + dd + ")"; }
 * 
 * 
 * if (!dataType.equals("")) { serviceprovder = "      {" +
 * "         \"type\": \"contains\"," +
 * "         \"field\": \"service_provider_id\"," + "         \"values\":  [ " +
 * dataType + " ]" + "      }"; }
 * 
 * for (int i = 0; i < arrayObjPolygone.length(); i++) {
 * 
 * shapeType =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getString("Type"); shapeId =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getInt("ID"); if
 * (shapeType.equals("Polyline")) { countOfPolyline++;
 * 
 * JSONArray lineArray = new JSONArray(); lineArray =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONArray("Bounds");
 * 
 * for (int j = 0; j < lineArray.length(); j++) {
 * 
 * if (j == 0) { lineCoords = lineArray.getJSONObject(j).get("lng").toString() +
 * " " + lineArray.getJSONObject(j).get("lat").toString(); } if (j > 0) {
 * lineCoords = lineCoords + "," +
 * lineArray.getJSONObject(j).get("lng").toString() + " " +
 * lineArray.getJSONObject(j).get("lat").toString(); }
 * 
 * }
 * 
 * 
 * 
 * // if (countOfPolyline.equals(1)) { polylineCoordinates = " {\r\n" +
 * "              type: \"geo_shape\",\r\n" +
 * "              field: \"place\",\r\n" +
 * "              operation: \"intersects\",\r\n" + "              shape: {\r\n"
 * + "                 type: \"buffer\",\r\n" +
 * "                 max_distance: \"30m\",\r\n" +
 * "                 shape: {\r\n" + "                    type: \"wkt\",\r\n" +
 * "                    value: \"LINESTRING(" + lineCoords + ")\"\r\n" +
 * "                 }\r\n" + "              }\r\n" + "           }"; // } //
 * else { // polylineCoordinates = " {\r\n" + //
 * "              type: \"geo_shape\",\r\n" + //
 * "              field: \"place\",\r\n" + //
 * "              operation: \"intersects\",\r\n" + //
 * "              shape: {\r\n" + // "                 type: \"buffer\",\r\n" +
 * // "                 max_distance: \"30m\",\r\n" + //
 * "                 shape: {\r\n" + // "                    type: \"wkt\",\r\n"
 * + // "                    value: \"LINESTRING(" + lineCoords + ")\"\r\n" + //
 * "                 }\r\n" + // "              }\r\n" + // "           }," +
 * polylineCoordinates; // }
 * 
 * JSONObject DTPObj = new JSONObject();
 * DTPObj.put("query",polylineCoordinates); DTPObj.put("shapeId",shapeId);
 * ObjArray.put(DTPObj);
 * 
 * }
 * 
 * if (shapeType.equals("Rectangle")) {
 * 
 * countOfPolygon++; // maxLongBR =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").
 * getJSONObject("bottomRight").get("lng").toString(); minLatBR =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").
 * getJSONObject("bottomRight").get("lat").toString(); minLongTL =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").
 * getJSONObject("topLeft").get("lng").toString(); maxLatTL =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").
 * getJSONObject("topLeft").get("lat").toString(); maxLongBL =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").
 * getJSONObject("bottomLeft").get("lng").toString(); minLatBL =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").
 * getJSONObject("bottomLeft").get("lat").toString(); minLongTR =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").
 * getJSONObject("topRight").get("lng").toString(); maxLatTR =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("Bounds").
 * getJSONObject("topRight").get("lat").toString();
 * 
 * // if (rectangleCoordinates.equals("")) { rectangleCoordinates = "(" +
 * maxLongBR + " " + minLatBR + "," + maxLongBL + " " + minLatBL + "," +
 * minLongTL + " " + maxLatTL + "," + minLongTR + " " + maxLatTR + "," +
 * maxLongBR + " " + minLatBR + ")";
 * 
 * rectangleCoordinates = "{" + "      type: \"geo_shape\"," +
 * "      field: \"place\"," + "      shape: {" + "         type: \"wkt\"," +
 * "         value: \"POLYGON((" + maxLongBR + " " + minLatBR + "," + maxLongBL
 * + " " + minLatBL + "," + minLongTL + " " + maxLatTL + "," + minLongTR + " " +
 * maxLatTR + "," + maxLongBR + " " + minLatBR + "))\"" + "      }" + "    }";
 * 
 * // } else { // rectangleCoordinates = "{" + // "      type: \"geo_shape\"," +
 * // "      field: \"place\"," + // "      shape: {" + //
 * "         type: \"wkt\"," + // "         value: \"POLYGON((" + maxLongBR + //
 * " " + // minLatBR + // "," + // maxLongBL + // " " + // minLatBL + // "," +
 * // minLongTL + // " " + // maxLatTL + // "," + // minLongTR + // " " + //
 * maxLatTR + // "," + // maxLongBR + // " " + // minLatBR + "))\"" + //
 * "      }" + // "    }," + rectangleCoordinates; // }
 * 
 * JSONObject DTPObj = new JSONObject();
 * DTPObj.put("query",rectangleCoordinates); DTPObj.put("shapeId",shapeId);
 * ObjArray.put(DTPObj);
 * 
 * }
 * 
 * if (shapeType.equals("Polygon")) { countOfPolygon++; String firstPol = "";
 * String polCoords = ""; JSONArray jsonObjPol = new JSONArray(); jsonObjPol =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONArray("Bounds"); for
 * (int j = 0; j < jsonObjPol.length(); j++) {
 * 
 * if (j == 0) { firstPol = jsonObjPol.getJSONObject(j).get("lng").toString() +
 * " " + jsonObjPol.getJSONObject(j).get("lat").toString(); polCoords =
 * jsonObjPol.getJSONObject(j).get("lng").toString() + " " +
 * jsonObjPol.getJSONObject(j).get("lat").toString(); } if (j > 0) { polCoords =
 * polCoords + "," + jsonObjPol.getJSONObject(j).get("lng").toString() + " " +
 * jsonObjPol.getJSONObject(j).get("lat").toString();; } }
 * 
 * // if (polygonCoordinates.equals("")) {
 * 
 * polygonCoordinates = "{" + "      type: \"geo_shape\"," +
 * "      field: \"place\"," + "      shape: {" + "         type: \"wkt\"," +
 * "         value: \"POLYGON(" + "(" + polCoords + "," + firstPol + ")" + ")\""
 * + "      }" + "    }";
 * 
 * // } else { // // polygonCoordinates = "{" + // "      type: \"geo_shape\","
 * + // "      field: \"place\"," + // "      shape: {" + //
 * "         type: \"wkt\"," + // "         value: \"POLYGON(" + "(" + polCoords
 * + "," + firstPol + ")" + ")\"" + // "      }" + // "    } ," +
 * polygonCoordinates; // }
 * 
 * JSONObject DTPObj = new JSONObject(); DTPObj.put("query",polygonCoordinates);
 * DTPObj.put("shapeId",shapeId); ObjArray.put(DTPObj); }
 * 
 * // if (!polygonCoordinates.equals("") && !rectangleCoordinates.equals("")) {
 * // filterCoordinates = polygonCoordinates + "," + rectangleCoordinates; // }
 * // // if (!polygonCoordinates.equals("") && rectangleCoordinates.equals(""))
 * { // filterCoordinates = polygonCoordinates; // } // // if
 * (polygonCoordinates.equals("") && !rectangleCoordinates.equals("")) { //
 * filterCoordinates = rectangleCoordinates; // }
 * 
 * if (shapeType.equals("Circle")) {
 * 
 * latCircleCenter =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("center").get(
 * "lat").toString(); lngCircleCenter =
 * obj.getJSONArray("Coordinates").getJSONObject(i).getJSONObject("center").get(
 * "lng").toString(); radius =
 * obj.getJSONArray("Coordinates").getJSONObject(i).get("radius").toString();
 * 
 * // if (filterCircle.equals("")) {
 * 
 * if (!radius.equals("")) { filterCircle = "{" +
 * "      type: \"geo_distance\"," + "      field: \"place\"," +
 * "      latitude: " + latCircleCenter + "," + "      longitude: " +
 * lngCircleCenter + "," + "      max_distance: \"" + radius + " m\"" + "   }";
 * 
 * }
 * 
 * // } else { // if (!radius.equals("")) { // filterCircle = "{" + //
 * "      type: \"geo_distance\"," + // "      field: \"place\"," + //
 * "      latitude: " + latCircleCenter + "," + // "      longitude: " +
 * lngCircleCenter + "," + // "      max_distance: \"" + radius + " m\"" + //
 * "   }," + filterCircle; // // } // }
 * 
 * JSONObject DTPObj = new JSONObject(); DTPObj.put("query",filterCircle);
 * DTPObj.put("shapeId",shapeId); ObjArray.put(DTPObj);
 * 
 * }
 * 
 * }
 * 
 * 
 * 
 * JSONArray queryArray = new JSONArray();
 * 
 * // queryArray.put(dates); queryArray.put(serviceprovder);
 * 
 * for (int k = 0; k < queryArray.length(); k++) { if
 * (!queryArray.get(k).equals("")) {
 * 
 * if (k == 0) {
 * 
 * if(ObjArray.isEmpty()) { query = queryArray.get(k).toString(); } else { query
 * = ","+queryArray.get(k).toString();
 * 
 * }
 * 
 * 
 * } else { if (!query.equals("")) { query = query + "," +
 * queryArray.get(k).toString(); } else { query = queryArray.get(k).toString();
 * } } } } }
 * 
 * System.out.println("query  0  >> "+query);
 * System.out.println("devices  0  >> "+devices);
 * System.out.println("ObjArray  0  >> "+query); whereCondition = devices;
 * 
 * }
 * 
 * log resp = logService.FilteringData(query, simulationId, whereCondition,
 * ObjArray,simulationType,provider, fromDatemillis,toDatemillis,
 * allDevices,dataType,request); return resp;
 * 
 * } catch (Exception e) { e.printStackTrace(); } return null;
 * 
 * }
 * 
 * }
 */