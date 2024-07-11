package com.valoores.v21.usm.app.common.application.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.common.application.model.USMApplication;

public interface IUSMApplicationService {

	public List<USMApplication> getApplicationCombo();
	
	
	List<ObjectNode> getApplicationMenuCombo(String id);
	
	 List<ObjectNode> getUSMApplicationRelatedUserId(long id);


	 List<ObjectNode> getUSMApplicationRelatedRoleId(long id);
}
