package com.valoores.v21.usm.app.logs.logsbyheatmap.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface ILogsByHeatmapService {

	List<ObjectNode> getAllLogsByHeatmap(String loginDate, String logoutDate);

}
