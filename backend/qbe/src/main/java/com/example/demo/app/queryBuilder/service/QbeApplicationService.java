package com.example.demo.app.queryBuilder.service;

import java.util.List;



import com.example.demo.app.queryBuilder.dto.ParamDto;
import com.example.demo.app.queryBuilder.dto.QbeAuthorizedUserDto;
import com.example.demo.app.queryBuilder.dto.QueryDto;
import com.example.demo.customResponse.CustomResponse;
import com.fasterxml.jackson.databind.node.ObjectNode;


public interface QbeApplicationService {

	public List<ObjectNode> getQueries();
	
	public CustomResponse addQueryData(QueryDto queryDto);

	public CustomResponse deleteQueryData(long id);

	public CustomResponse deleteQueryDataDetails(long id);

	public List<ObjectNode> validateQuery(QueryDto queryDto);
	public List<ObjectNode> validateSubQuery(QueryDto queryDto);

	public List<ObjectNode> fetchDynamicHeaderData(String serialAttribute);

	public List<ObjectNode> fetchDynamicData(byte[] query);

	public int addParamSession(ParamDto paramDto);

	public List<ObjectNode> getParamSession(String serialAttribute);

	public List<ObjectNode> getParamCombo(String serialAttribute);

	public List<ObjectNode> getParamTypes();

	public List<ObjectNode> getHeaderData(String serialAttribute);

	public int updateParamSession(ParamDto paramDto);

	public int deleteParameter(String node, String sessionAttribute);

	public List<ObjectNode> decodeQuery(String sessionSerial, long queryId,int userId);
	public List<ObjectNode> decodeSubQuery(String sessionSerial, long queryId,int userId);
	public List<ObjectNode> getSubQueries(String sessionSerial, long queryId,int userId);

	public int updateQuery(QueryDto queryDto);

	public int deleteSessions(String sessionAttribute);

	public List<List<ObjectNode>> execQueryInDisplay(QueryDto queryDto);

	public String getParamsName(long queryId);
	
	public int checkQueryUsage(long id);

	public List<ObjectNode> exportQuery(long queryId);

	public int importQuery(QueryDto queryDto);
	public List<ObjectNode> getlookupQueries();
	
	public long importSubQuery(QueryDto queryDto);
	
	public List<ObjectNode> getQueryParams(long queryId);
	public List<ObjectNode> getQueryParamsLookup(long queryId);
	public List<ObjectNode> getQueryList();
	
	public List<ObjectNode> fetchDynamicDataQueryForm(QueryDto queryDto);

//	public List<List<ObjectNode>> getQueryParamAllData(long queryId);
	public int insertQueryType(Long queryId, String queryTypeString);
	public List<ObjectNode> checkQueryTypeExists(Long queryId);
	public int updateQueryType(Long queryId, String queryTypeString);

	public List<ObjectNode> getSecurityUserQuery(Long queryId);
	public List<ObjectNode> getQueryListUsers(long qbeId);

	public int addQbeAuthorizedUsers(QbeAuthorizedUserDto qbeAuthorizedUserDto);

	public void deleteQbeAuthorizedUsers(long qbeId, String usrCode);

	public void deleteQbeSecurityChanges(long qbeId);

	public List<ObjectNode> getQbeQueryCreatedBy(long qbeId);

	public void getAllQueryDataEngine();

	
	public void getBackQueriesEngine();


}
