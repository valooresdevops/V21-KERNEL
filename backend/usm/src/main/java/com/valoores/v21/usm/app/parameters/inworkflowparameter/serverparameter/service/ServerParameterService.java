package com.valoores.v21.usm.app.parameters.inworkflowparameter.serverparameter.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface ServerParameterService {

	List<ObjectNode> getAllWorkFlowParameter(String currServerUrl, String currServerName);
}
