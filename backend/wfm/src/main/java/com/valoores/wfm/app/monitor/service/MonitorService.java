package com.valoores.wfm.app.monitor.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface MonitorService {

	public List<ObjectNode> unusedTrigger();
	
	public List<ObjectNode> missingDoc();
	
	public List<ObjectNode> runningProcess();


	public List<ObjectNode> invalidProcess();
}