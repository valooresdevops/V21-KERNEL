package com.valoores.v21.usm.app.logs.logsbyobject.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface ILogsByObjectService {

	//public List<USMApplication> getApplicationCombo();

	//public List<ObjectNode> getMenuCombo(String id);
	
	public List<ObjectNode> getAllLogsByObject(String application,String menuName,String loginDate, String logoutDate);
}
