package com.valoores.cassandradatacrowd.app.service;


import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.stereotype.Service;

import com.valoores.cassandradatacrowd.app.dto.CustomResponse;

@Service
public interface ILocationService {

	byte[] FilteringData(String query ,String query_1, String simulationId,String whereCondition,
								JSONArray  ObjShapesArrayByDate,JSONArray ObjArray, String simulationType,
								String provider ,long fromDatemillis,long toDatemillis,String allDevices,
								String dataType,String callingNo,String imsiId,JSONArray countryCode,
								JSONArray arrayOfDate,String SubRegion,String Region,HttpServletRequest request);

	CustomResponse insertShape(String coordinates);

	String  getKnowledgeGraphRecords(String devices,String simulationId,long fromDatemillis, long toDatemillis,
			String callingNo, String calledNo ,String linkType , long layer,String status);

	String getKnowledgeGraphOneRecord(String devices,String simulationId, long fromDatemillis, long toDatemillis, String callingNo,
			String calledNo, String linkType ,String directIndirect ,Long layer);

	public String getCallingNo(String device) ;

//	CustomResponse FilteringData(String query, String query_1, String simulationId, String whereCondition,
//			JSONArray objShapesArrayByDate, JSONArray objArray, String simulationType, String provider,
//			long fromDatemillis, long toDatemillis, String allDevices, String dataType, String callingNo, String imsiId,
//			String countryCode, JSONArray arrayOfDate, ArrayList<String[]> subRegion, String region,
//			HttpServletRequest request);
	
}
