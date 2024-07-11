package com.valoores.cassandra.app.service.impl;

import java.net.InetSocketAddress;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.sedona.sql.utils.SedonaSQLRegistrator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
//import com.datastax.oss.driver.api.core.type.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.valoores.cassandra.app.service.CassandraSparkService;
import com.valoores.cassandra.config.SparkConfig;
import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;

import com.datastax.oss.driver.api.core.metadata.schema.ColumnMetadata;
import com.datastax.oss.driver.api.core.metadata.schema.TableMetadata;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.*;


@Service
public class CassandraSparkServiceImpl implements CassandraSparkService {


@Value("${sparkId}")
private String sparkId;
@Override
public byte[] getAllDataSparkSedona1(String conditionSP , long simulationId,String indexesCriteria,String tablesCriteria) {

SparkSession spark = SparkConfig.getSparkSession();
        List<Row>    rows = new ArrayList<>();

try {
String select;
SedonaSQLRegistrator.registerAll(spark);
String devices="";


// tablesCriteria  = "geo_data_2023_6_142_145,geo_data_2023_7_142_145,geo_data_2023_8_142_145,geo_data_2023_9_142_145,geo_data_2023_10_142_145,geo_data_2023_11_142_145,geo_data_2023_12_142_145,geo_data_2024_1_142_145,geo_data_2024_2_142_145,geo_data_2024_3_142_145,geo_data_2024_4_142_145,geo_data_2024_5_142_145,geo_data_2024_6_142_145" ;
// indexesCriteria=  "geo_index_2023_6_142_145,geo_index_2023_7_142_145,geo_index_2023_8_142_145,geo_index_2023_9_142_145,geo_index_2023_10_142_145,geo_index_2023_11_142_145,geo_index_2023_12_142_145,geo_index_2024_1_142_145,geo_index_2024_2_142_145,geo_index_2024_3_142_145,geo_index_2024_4_142_145,geo_index_2024_5_142_145,geo_index_2024_6_142_145";

if (tablesCriteria.equals("loc_location_cdr_main_new") && indexesCriteria.equals("idx01_cdr_main_new")) {
 select ="imsi_id,imei_id, location_latitude,location_longitude,service_provider_id,usage_timeframe,cgi_id,location_azimuth,type_id,phone_number";
}else {
select= "device_id,usage_timeframe,location_name,location_longitude,location_latitude,service_provider_id";
}


if (conditionSP.contains("<DHP>")) { //  type = 6  (DEVICE HISTORY PATTERN)
 //DOING THE as HERE

  String[] conditionSPArray     = conditionSP.split("<DHP>");
  for(int i=0;i<conditionSPArray.length;i++) {
// System.out.println("<<<conditionSP<>>> "+conditionSPArray[i]);

  }
  String[] tablesCriteriaArray  = tablesCriteria.split(",");
String[] indexesCriteriaArray = indexesCriteria.split(",");
// System.out.println("<<<<>>> "+conditionSPArray[1]);

try (CqlSession session = CqlSession.builder()
      .addContactPoint(new InetSocketAddress(sparkId, 9042))
      .withLocalDatacenter("datacenter1")
      .withKeyspace(CqlIdentifier.fromCql("datacrowd"))
      .withConfigLoader(
          DriverConfigLoader.programmaticBuilder()
              .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(15)) // Adjust timeout
              .build())
      .build()) {

      for (int i = 0; i < tablesCriteriaArray.length; i++) {
          String index = indexesCriteriaArray[i];
          String table = tablesCriteriaArray[i];
          String formattedLuceneQuery = conditionSPArray[1].replace("'", "''");
          String query = "SELECT " + select + " FROM datacrowd." + table + " WHERE expr(" + index + ", '" + formattedLuceneQuery + "') ";
//           System.out.println("Query  - dhp  " +i+" "+ query);
          ResultSet resultSet = session.execute(query);
          TableMetadata tableMetadata = session.getMetadata().getKeyspace("datacrowd")
                  .flatMap(ks -> ks.getTable(table)).orElse(null);
          StructType schema = createSchema(tableMetadata);
          if (resultSet.one() != null) {
              List<Row> row1 = convertResultSetToList(resultSet, schema);
              synchronized (rows) {
                  rows.addAll(row1);
              }

          }

      }
  }

System.out.println(" rows.size isss " + rows.size());

Set<Object> uniqueElements = new HashSet<>();
  for (Row row : rows) {
      Object firstElement = row.get(0);
      uniqueElements.add(firstElement);
  }
//   for (Object element : uniqueElements) {
//           devices+="\"" + element + "\",";
//       }



for (Object element : uniqueElements) {
devices+= element + ",";
}
   
  if(devices.endsWith(",")) {
   devices = devices.substring(0, devices.length() - 1);
  }
 
 
  System.out.println("the disticnt devices are "+devices);
String deviceCondition = "{\"type\":\"contains\"," + "\"field\":\"device_id\"," + "\"values\":["+ devices + "]}";

String     jsonString          = conditionSPArray[1];
JSONObject jsonObject          = new JSONObject(jsonString);
JSONArray  filters             = jsonObject.getJSONArray("filter");
JSONObject countryCodeFilter   = null;
JSONObject timeStampCodeFilter = null;

for (int i = 0; i < filters.length() ; i++) {
JSONObject filter = filters.getJSONObject(i);
if (filter.getString("field").equals("country_code")) {
countryCodeFilter = filter;
}
if (filter.getString("field").equals("usage_timeframe")) {
timeStampCodeFilter = filter;
}
}
System.out.println("countryCodeFilter      " + countryCodeFilter  );
System.out.println("timeStampCodeFilter    " + timeStampCodeFilter);

System.out.println("deviceCondition    " + deviceCondition);

// conditionSP = "{\"filter\":[" + deviceCondition + "," + timeStampCodeFilter + "," + countryCodeFilter + "]}";
conditionSP = "{\"filter\":[" + deviceCondition + "," + timeStampCodeFilter  + "]}";

System.out.println("conditionSP   in DHP  " + conditionSP);
  //THEN IT DOES THE DEVICE HISTORY
 }


String[] tablesCriteriaArray = tablesCriteria.split(",");
String[] indexesCriteriaArray = indexesCriteria.split(",");

try (CqlSession session = CqlSession.builder().addContactPoint(new InetSocketAddress(sparkId, 9042))
.withLocalDatacenter("datacenter1").withKeyspace(CqlIdentifier.fromCql("datacrowd"))
  .withConfigLoader(
          DriverConfigLoader.programmaticBuilder()
              .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(15)) // Adjust timeout
              .build())
  .build()) {



     
for (int i = 0; i < tablesCriteriaArray.length; i++) {

String index = indexesCriteriaArray[i];
String table = tablesCriteriaArray[i];
String formattedLuceneQuery = conditionSP.replace("'", "''");
String query = "SELECT " + select + " FROM datacrowd." + table + "  WHERE expr(" + index + ", '"
+ formattedLuceneQuery + "') ";

// System.out.println(" query  " + query);
ResultSet resultSet = session.execute(query);
TableMetadata tableMetadata = session.getMetadata().getKeyspace("datacrowd").flatMap(ks -> ks.getTable(table)).orElse(null);

StructType schema = createSchema(tableMetadata);

if (resultSet.one() != null) {
List<Row> row1 = convertResultSetToList(resultSet, schema);
rows.addAll(row1);
}

}

}
//        System.out.println(" end time ------------- "+java.time.LocalTime.now());
System.out.println(" rows.size --"+rows.size());
// System.out.println(" rows --"+rows);

       
       
       
//        df.show();
//        System.out.println(" count finalll  "+df.count());
// df.createOrReplaceTempView("viewTable");
//// df = spark.sql("select     device_id, location_accuracy, location_cou_code_num,location_latitude, location_longitude, location_main_data_id,"
//// + " location_name, service_provider_id, usage_date,country_code,device_carrier_name, usage_timeframe FROM  viewTable   ");
//
// df = spark.sql("select     device_id, location_accuracy, location_cou_code_num,location_latitude, location_longitude, location_main_data_id,"
// + " location_name, service_provider_id, usage_date, usage_timeframe FROM  viewTable   ");

// System.out.println("------------------ 4 ------------------");

//               df = df.withColumn( "scenario_id" , functions.lit(simulationId) )
//                     .withColumn("seq_id", functions.monotonically_increasing_id()  );

             
//               df.show();
//               System.out.println("couu 1  "+df.count());

        // Write DataFrame to Cassandra
// df.write()
//  .format("org.apache.spark.sql.cassandra")
//  .option("keyspace","datacrowd")
//  .option("table","geo_scenarios_data")
//  .option("spark.cassandra.connection.host",sparkId)
//  .option("spark.cassandra.auth.username","cassandra")
//  .option("spark.cassandra.auth.password","cassandra")
//  .option("spark.cassandra.output.batch.size.rows", "10")    
//  .option("spark.cassandra.output.concurrent.writes", "5")    
//                  .mode("append")
//  .save();

// System.out.println("------------------ copied to cassandra ------------------" );
//            spark.stop();

//        System.out.println(" start json convert  time ------------- "+java.time.LocalTime.now());

// byte[] json =  df.toJSON().collectAsList().toString().getBytes();

//        System.out.println(" end json convert  time ------------- "+java.time.LocalTime.now());

// System.out.println("jsonResult >>>>>>>>>>>>>>>> String ============  "+jsonResult);
// spark.stop();

// if (df != null) {
// System.out.println("result1 : data is not null");
// System.out.println("jsonResult = "+jsonResult);
// return json;
// }

      System.out.println(" ssssssssssssss time ------------- "+java.time.LocalTime.now());
if (rows != null) {
System.out.println("result1 : data is not null");
// System.out.println("jsonResult = "+jsonResult);
      System.out.println(" eeeeeeeeeeeee time ------------- "+java.time.LocalTime.now());

return rows.toString().getBytes();
}

} catch (Exception e) {
e.printStackTrace();
}
// spark.stop();
return null;
}

// @Override
// public Dataset<Row> getAllDataSparkSedona(String cql, long simulationId, String condition) {
//
// SparkSession spark = SparkConfig.getSparkSession();
// spark.sparkContext();
// Dataset<Row> df = null;
// try {
// long startTime = System.currentTimeMillis();
//
// System.out.println(" condition >>> " + condition);
//
// SedonaSQLRegistrator.registerAll(spark);
// String keyspace = "datacrowd";
// String table = "geo_data_2023_10_142";
// long usage_timeframe_threshold = 1641546000000L;
//
// int service_provider_id = 9;
//
// long timebSHow = System.currentTimeMillis();
// long TTimebSHow = timebSHow - startTime;
//
// df = spark.read().format("org.apache.spark.sql.cassandra").option("table", table)
// .option("keyspace", keyspace).load()
//// .filter(col("year_no").isin(year_list))
//// .filter(col("month_no").isin(month_list)).filter(col("day_no").isin(day_list))
//// .filter(col("hour_no").isin(hour_list))
////                .filter(col("usage_timeframe").$greater(lit(usage_timeframe_threshold)))
// .filter(col("usage_timeframe").gt(usage_timeframe_threshold))
// .filter(col("service_provider_id").equalTo(service_provider_id));
////                .filter(expr(String.format("ST_Point(location_longitude, location_latitude) = ST_Point(%f, %f)", location_longitude, location_latitude)))
//// .limit(10);
//
// System.out.println(" df start >> ");
// System.out.println(" df count >> " + df.count());
//
// df.show();
// df.createOrReplaceTempView("view1");
//
// Dataset<Row> result = spark.sql(
// "select * from view1  where   ST_Point(location_longitude, location_latitude) = ST_Point(44.403755 ,  33.407547)");
// result.show();
// spark.stop();
//// long timeaSHow  = System.currentTimeMillis();
//// long TTimeaSHow = timeaSHow - TTimebSHow;
//// Properties connectionProperties = new Properties();
//// connectionProperties.put("user", "ssdx_eng");
//// connectionProperties.put("password", "ssdx_eng");
////
//// df.write().mode("overwrite").jdbc("jdbc:oracle:thin:@10.1.10.164:1521/vdcdb",
//// "SSDX_ENG.TMP_REPORT_COORDINATES_6_" + simulationId, connectionProperties);
//
//// System.out.println("------------------ successfully copied------------------");
// long endTime = System.currentTimeMillis();
// long totalTime = endTime - startTime;
// System.out.println("totalTime  = " + totalTime);
//
//// spark.stop();
// } catch (Exception e) {
// e.printStackTrace();
// }
//
// if (df != null) {
// System.out.println("result1 : data not null");
// return df;
// } else {
// System.out.println("result1 : data  is  null");
// return df;
// }
//
// }

@Override
public byte[] getAllData1(long simulationId) {
byte[] resultList = null;
SparkSession spark = SparkConfig.getSparkSession();
try {

SedonaSQLRegistrator.registerAll(spark);

Dataset<Row> df = spark.read().format("org.apache.spark.sql.cassandra").option("keyspace", "datacrowd")
.option("table", "geo_data_2023_10_142").load();
df.createOrReplaceTempView("viewTable");
System.out.print("df >>>>>>>>>>>>>>>>");
df = spark.sql("select  DEVICE_ID ,SERVICE_PROVIDER_ID,USAGE_TIMEFRAME,COUNTRY_ALPHA2,COUNTRY_ALPHA3,COUNTRY_CODE,DATA_CATEGORY,DAY_NO,DEVICE_CARRIER_NAME,DEVICE_HIT_COUNT,DEVICE_MANUFACTURER_BRAND,DEVICE_MODEL\r\n" +
",HOUR_NO,INSERTION_DATE,IP_D,LOCATION_ACCURACY,LOCATION_ALTITUDE,LOCATION_COU_CODE_NUM,LOCATION_DENSITY,LOCATION_LATITUDE,LOCATION_LONGITUDE,LOCATION_MAIN_DATA_ID,LOCATION_NAME\r\n" +
",MONTH_NO,REGION_CODE,SOURCE_FILE_ID,SUBREGION_CODE,USAGE_DATE,USAGE_TIMELINE,YEAR_NO  FROM  viewTable  ");
df.show(10);
Properties connectionProperties = new Properties();
connectionProperties.put("user", "ssdx_eng");
connectionProperties.put("password", "ssdx_eng");

df.write().mode("append").jdbc("jdbc:oracle:thin:@10.1.10.164:1521/vdcdb",
"tmp_report_coordinates_6_"+simulationId, connectionProperties);

} catch (Exception e) {
e.printStackTrace();
}
spark.stop();
return resultList;
}

@Override
public String copyDataToOracle() {
SparkSession spark = SparkConfig.getSparkSession();
try {
Dataset<Row> df = spark.read().format("org.apache.spark.sql.cassandra").option("keyspace", "test")
.option("table", "loc_location_main_data_test2").load();

df.createOrReplaceTempView("cassandraData");
Dataset<Row> cassandraRecords = spark.sql("SELECT * FROM cassandraData ");
Thread.sleep(5000);

Properties connectionProperties = new Properties();
connectionProperties.put("user", "ssdx_eng");
connectionProperties.put("password", "ssdx_eng");
cassandraRecords.write().mode("append").jdbc("jdbc:oracle:thin:@10.1.10.164:1521/vdcdb",
"techdba.loc_location_main_data_test2", connectionProperties);

} catch (Exception e) {
e.printStackTrace();
}
return "Success";
}

@Override
public byte[] getObject(String[] myObject) {

try {

} catch (Exception e) {
e.printStackTrace();
}
return null;
}

@Override
public int P_GET_LOC_LOCATION() {
return 0;
}

@Override
public String getEntityName() {
return null;
}

    private static StructType createSchema(TableMetadata tableMetadata) {
        List<StructField> fields = new ArrayList<>();
        if (tableMetadata != null) {
            for (ColumnMetadata column : tableMetadata.getColumns().values()) {
                String columnName = column.getName().asCql(true);
               
                DataType dataType = mapDataType(column.getType());

                fields.add(DataTypes.createStructField(columnName, dataType, true));
            }
        }
        return DataTypes.createStructType(fields);
    }



 
   
   

//    private static DataType mapDataType( com.datastax.oss.driver.api.core.type.DataType dataType) {
//        return DataTypes.StringType;  
//    
//    }

   
   
    //mod
    private static org.apache.spark.sql.types.DataType mapDataType(com.datastax.oss.driver.api.core.type.DataType dataType) {
        if (dataType.equals(com.datastax.oss.driver.api.core.type.DataTypes.TEXT)) {
            return org.apache.spark.sql.types.DataTypes.StringType;
        } else if (dataType.equals(com.datastax.oss.driver.api.core.type.DataTypes.INT)) {
            return org.apache.spark.sql.types.DataTypes.IntegerType;
        } else if (dataType.equals(com.datastax.oss.driver.api.core.type.DataTypes.FLOAT)) {
            return org.apache.spark.sql.types.DataTypes.FloatType;
        } else if (dataType.equals(com.datastax.oss.driver.api.core.type.DataTypes.DOUBLE)) {
            return org.apache.spark.sql.types.DataTypes.DoubleType;
        } else if (dataType.equals(com.datastax.oss.driver.api.core.type.DataTypes.BOOLEAN)) {
            return org.apache.spark.sql.types.DataTypes.BooleanType;
        } else if (dataType.equals(com.datastax.oss.driver.api.core.type.DataTypes.TIMESTAMP)) {
            return org.apache.spark.sql.types.DataTypes.TimestampType;
        } else if (dataType.equals(com.datastax.oss.driver.api.core.type.DataTypes.BIGINT)) {
            return org.apache.spark.sql.types.DataTypes.LongType;
        } else if (dataType.equals(com.datastax.oss.driver.api.core.type.DataTypes.VARINT)) {
            return org.apache.spark.sql.types.DataTypes.createDecimalType();
        } else if (dataType.equals(com.datastax.oss.driver.api.core.type.DataTypes.DECIMAL)) {
            return org.apache.spark.sql.types.DataTypes.createDecimalType();
        } else if (dataType.equals(com.datastax.oss.driver.api.core.type.DataTypes.UUID)) {
            return org.apache.spark.sql.types.DataTypes.StringType;
        } else if (dataType.equals(com.datastax.oss.driver.api.core.type.DataTypes.TIMEUUID)) {
            return org.apache.spark.sql.types.DataTypes.StringType;
        }
        // Add more mappings as needed

        // Default to StringType if the data type is not explicitly mapped
        return org.apache.spark.sql.types.DataTypes.StringType;
    }

   
   
    private static List<Row> convertResultSetToList(ResultSet resultSet,StructType schema) throws SQLException {
        List<Row> rows = new ArrayList<>();
        for (com.datastax.oss.driver.api.core.cql.Row row : resultSet) {
            Object[] values = new Object[row.size()];
            for (int i = 0; i < row.size(); i++) {
                values[i] = convertToDataType(row.getObject(i), schema.fields()[i].dataType());
            }
            rows.add(RowFactory.create(values));
        }
        return rows;
    }
   
   

    private static Object convertToDataType(Object value, DataType dataType) {
        if (value == null) {
            return null;
        } else if (dataType.equals(DataTypes.IntegerType)) {
       
            return Long.parseLong(value.toString());
        }
//        else if (dataType.equals(DataTypes.LongType)) {
//         System.out.println(" Longggggggggggggggg");
//            return Long.parseLong(value.toString());
//        }
        else if (dataType.equals(DataTypes.FloatType)) {

            return Float.parseFloat(value.toString());
        }
        else if (dataType.equals(DataTypes.DoubleType)) {

            return Double.parseDouble(value.toString());
        } else if (dataType.equals(DataTypes.BooleanType)) {

            return Boolean.parseBoolean(value.toString());
        } else {
            return '"'+value.toString()+'"';  
        }
    }
   
}