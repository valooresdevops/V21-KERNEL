package com.valoores.v21.usm.app.logs.invalidlogin.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface IInvalidLoginService {

	public List<ObjectNode> getAllInvalidLogs(String loginDate);
}
