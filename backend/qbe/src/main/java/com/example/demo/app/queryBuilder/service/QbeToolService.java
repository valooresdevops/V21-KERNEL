package com.example.demo.app.queryBuilder.service;

import java.util.List;


import com.fasterxml.jackson.databind.node.ObjectNode;


public interface QbeToolService {

	public List<ObjectNode> getAllTables();

	public List<ObjectNode> getTableColumns(String tableName);
	
}
