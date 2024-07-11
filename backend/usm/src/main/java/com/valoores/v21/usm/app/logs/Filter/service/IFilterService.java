package com.valoores.v21.usm.app.logs.Filter.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface IFilterService {
	
	public List<ObjectNode> getApplicationEvent(String application,String fromDate, String toDate);

}
