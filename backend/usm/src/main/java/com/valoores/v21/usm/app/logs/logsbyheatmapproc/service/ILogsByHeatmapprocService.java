package com.valoores.v21.usm.app.logs.logsbyheatmapproc.service;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface ILogsByHeatmapprocService {

	List<ObjectNode> generateHeatMapTable(String startDate, String endDate);
}
