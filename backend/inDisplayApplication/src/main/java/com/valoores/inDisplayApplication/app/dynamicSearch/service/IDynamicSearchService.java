package com.valoores.inDisplayApplication.app.dynamicSearch.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface IDynamicSearchService {
	public List<ObjectNode> getSearchType();

	List<ObjectNode> getDynamicSearch(long objectId,String sourceQuery);

//	public List<ObjectNode> getWhereCondition(String objectId);
	public String getWhereCondition(String objectId,Integer elementSize);

	@SuppressWarnings("rawtypes")
	public List  getThirdDropDown(String searchId,String userId);

	public List<ObjectNode> getQueryParams(long queryId);


//	public List<ObjectNode> getLibrary();

}
