package com.valoores.cassandradatacrowd.app.service;

import org.json.JSONArray;

import com.valoores.cassandradatacrowd.app.dto.CustomResponse;

public interface IQueryBuilderService {

//	String getShapeApi(String id);

	CustomResponse GetQueryBuilderResult(String attribute, String whereCondition, String id, JSONArray finalAOIArray, String tableName,String tableInde,String LuceneQuery);
	
}
