package com.valoores.v21.usm.app.logs.fieldhistorylog.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface IFieldHistoryLogService {

	public List<ObjectNode> getFieldCombo() ;
	//public List<ObjectNode> getAllFieldHistoryLog( String application, String menu, String user, String field, Date fromDate, Date toDate);
//	public List<ObjectNode> getAllFieldHistoryLog(String application, String menu, String user, String field,
//			java.util.Date fromDate, java.util.Date toDate);
	public List<ObjectNode> getAllFieldHistoryLog(String application, String menu, String user, String field,
			String fromDate, String toDate);
}
