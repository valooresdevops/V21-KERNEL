package com.valoores.v21.usm.app.logs.logsbyheatmapproc.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.logs.logsbyheatmapproc.service.ILogsByHeatmapprocService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "USM Users", description = "USM Users' exposed APIs")
@RestController
@RequestMapping("/api")
public class LogsByHeatmapprocController {
	
	@Autowired
	public ILogsByHeatmapprocService logsByHeatmapprocService;
	
	@Operation(summary = "Get all Logs By Heatmap")
	@GetMapping(path = "/generateHeatMapTable/{startDate}/{endDate}", produces = "application/json")
	public List<ObjectNode> generateHeatMapTable(@PathVariable String startDate,
			                                     @PathVariable String endDate)
	
	{ 
		//System.out.println("startdate controller>>>>>>>>>"+startDate);
		//System.out.println("endDate controller>>>>>>>>>"+endDate);
		return logsByHeatmapprocService.generateHeatMapTable(startDate,endDate);
	}
}
