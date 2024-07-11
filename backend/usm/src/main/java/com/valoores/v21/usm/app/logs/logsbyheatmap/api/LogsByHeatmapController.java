package com.valoores.v21.usm.app.logs.logsbyheatmap.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.logs.logsbyheatmap.service.ILogsByHeatmapService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "USM Users", description = "USM Users' exposed APIs")
@RestController
@RequestMapping("/api")
public class LogsByHeatmapController {
	@Autowired
	public ILogsByHeatmapService logsByHeatmapService;
	
//	@GetMapping("/getUserCombo")
//	public List<UserLogs> getUserCombo() {
//		return logsByUserService.getUserCombo();
//	}
	
//	@GetMapping("/getApplicationCombo")
//	public List<USMApplication> getApplicationCombo() {
//		return logsByUserService.getApplicationCombo();
//	}
	
	@Operation(summary = "Get all Logs By Heatmap")
	@GetMapping(path = "/getAllLogsByHeatmap/{loginDate}/{logoutDate}", produces = "application/json")
	public List<ObjectNode> getAllLogsByHeatmap( @PathVariable String loginDate,
			                                  @PathVariable String logoutDate)
	{
		return logsByHeatmapService.getAllLogsByHeatmap(loginDate,logoutDate);
	}
}
