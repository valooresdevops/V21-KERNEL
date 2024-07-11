package com.valoores.cassandra.app.service;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONArray;
import org.springframework.stereotype.Service;
import com.valoores.cassandra.app.service.CassandraSparkService;

@Service
public interface CassandraSparkService {

public byte[] getObject(String[] myObject);
public String copyDataToOracle();
public byte[] getAllData1(long simulationId) ;
public int P_GET_LOC_LOCATION();
public String getEntityName();
//public Dataset<Row> getAllDataSparkSedona(String cql, long simulationId, String condition);
//public String getAllDataSparkSedona1( String cql, long simulationId, String deviceCondition, String dataType,
//			long fromDatemillis, long toDatemillis,String distinctHoursString, String distinctMonthString, String distinctYearString,
//			String lng, String lat ,String radius);
public byte[] getAllDataSparkSedona1( String conditionSP, long simulationId,String indexesCriteria,String tablesCriteria);
}
