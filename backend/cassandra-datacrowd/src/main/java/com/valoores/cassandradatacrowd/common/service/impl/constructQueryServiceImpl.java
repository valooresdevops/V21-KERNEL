package com.valoores.cassandradatacrowd.common.service.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.valoores.cassandradatacrowd.common.service.constructQueryService;

@Service
public class constructQueryServiceImpl implements constructQueryService {

  @Override
  public String constructQuery(JSONObject arrayObj, String type) {

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
    String shapeType = "";
    String filterCoordinates = "";
    String serviceprovder = "";
    String query = "";
    Integer countOfPolygon = 0;
    Integer countOfPolyline = 0;
    String lineCoords = "";
    String polylineCoordinates = "";
    String finalLineCoords = "";
    String filterCircle = "";

    System.out.println(" arrayObj0 > " + arrayObj);
    
    if (type.equals("shape")) {
//      JSONArray ObjArray = new JSONArray();
//    JSONArray ObjArray = new JSONArray(arrayObj);
      System.out.println(" array =" + arrayObj);
//      System.out.println(" ObjArray =>" + ObjArray);
//      System.out.println(" ObjArray =>" + ObjArray.length());

      if (!arrayObj.equals(null)) {

      //  for (int i = 0; i < ObjArray.length(); i++) {
//            System.out.println(" ObjArray  i=>" + arrayObj);
//
          shapeType = arrayObj.getString("Type");
            System.out.println(" shapeType" + shapeType);

          if (shapeType.equals("Polyline")) {

            countOfPolyline++;
            JSONArray lineArray = new JSONArray();
            lineArray = arrayObj.getJSONArray("Bounds");

            for (int j = 0; j < lineArray.length(); j++) {

              if (j == 0) {
                lineCoords = lineArray.getJSONObject(j).get("lng").toString() + " " + lineArray.getJSONObject(j).get("lat").toString();
              }
              if (j > 0) {
                lineCoords = lineCoords + "," + lineArray.getJSONObject(j).get("lng").toString() + " " + lineArray.getJSONObject(j).get("lat").toString();
              }

            }

            if (countOfPolyline.equals(1)) {
              finalLineCoords = lineCoords;
            } else if (countOfPolyline.equals(2)) {
              finalLineCoords = "(" + finalLineCoords + "),(" + lineCoords + ")";
            } else {
              finalLineCoords = "(" + lineCoords + ")," + finalLineCoords;
            }

          }

          if (shapeType.equals("Rectangle")) {

            countOfPolygon++;
            //            		  
            maxLongBR = arrayObj.getJSONObject("Bounds").getJSONObject("bottomRight").get("lng").toString();
            minLatBR = arrayObj.getJSONObject("Bounds").getJSONObject("bottomRight").get("lat").toString();
            minLongTL = arrayObj.getJSONObject("Bounds").getJSONObject("topLeft").get("lng").toString();
            maxLatTL = arrayObj.getJSONObject("Bounds").getJSONObject("topLeft").get("lat").toString();
            maxLongBL = arrayObj.getJSONObject("Bounds").getJSONObject("bottomLeft").get("lng").toString();
            minLatBL = arrayObj.getJSONObject("Bounds").getJSONObject("bottomLeft").get("lat").toString();
            minLongTR = arrayObj.getJSONObject("Bounds").getJSONObject("topRight").get("lng").toString();
            maxLatTR = arrayObj.getJSONObject("Bounds").getJSONObject("topRight").get("lat").toString();

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

          if (shapeType.equals("Polygon")) {
            countOfPolygon++;
            String firstPol = "";
            String polCoords = "";
            @SuppressWarnings("unused")
            String finalPolCoords = "";
            JSONArray jsonObjPol = new JSONArray();
            jsonObjPol =  arrayObj.getJSONArray("Bounds");
            for (int j = 0; j < jsonObjPol.length(); j++) {

              if (j == 0) {
                firstPol = jsonObjPol.getJSONObject(j).get("lng").toString() + " " + jsonObjPol.getJSONObject(j).get("lat").toString();
                polCoords = jsonObjPol.getJSONObject(j).get("lng").toString() + " " + jsonObjPol.getJSONObject(j).get("lat").toString();
              }
              if (j > 0) {
                polCoords = polCoords + "," + jsonObjPol.getJSONObject(j).get("lng").toString() + " " + jsonObjPol.getJSONObject(j).get("lat").toString();;
              }
            }

            if (polygonCoordinates.equals("")) {
              polygonCoordinates = "(" + polCoords + "," + firstPol + ")";
            } else {
              polygonCoordinates = "(" + polCoords + "," + firstPol + ")" + "," + polygonCoordinates;
            }

          }

          if (shapeType.equals("Circle")) {

            String latCircleCenter =  arrayObj.getJSONObject("center").get("lat").toString();
            String lngCircleCenter =  arrayObj.getJSONObject("center").get("lng").toString();
            String radius =  arrayObj.get("radius").toString();

            if (!radius.equals("")) {
              filterCircle = "{" +
                "      type: \"geo_distance\"," +
                "      field: \"place\"," +
                "      latitude: " + latCircleCenter + "," +
                "      longitude: " + lngCircleCenter + "," +
                "      max_distance: \"" + radius + " m\"" +
                "   }";

            }

          }

        //}
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

        if (!finalLineCoords.equals("")) {
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
              "                    value: \"MULTILINESTRING(" + finalLineCoords + ")\"\r\n" +
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
              "                    value: \"LINESTRING(" + finalLineCoords + ")\"\r\n" +
              "                 }\r\n" +
              "              }\r\n" +
              "           }";
          }

        }

        JSONArray queryArray = new JSONArray();
        queryArray.put(filterCoordinates);
        queryArray.put(filterCircle);
        queryArray.put(serviceprovder);
        queryArray.put(polylineCoordinates);

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
      }
    }
    return query;

  }

}