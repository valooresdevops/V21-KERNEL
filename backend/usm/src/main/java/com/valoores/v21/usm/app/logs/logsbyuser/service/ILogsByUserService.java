package com.valoores.v21.usm.app.logs.logsbyuser.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface ILogsByUserService {

	public List<ObjectNode> getAllLogsByUser(String userLogs, String application, String loginDate);
}
