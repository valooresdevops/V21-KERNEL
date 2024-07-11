package com.valoores.v21.usm.app.parameters.inworkflowparameter.serverparameter.api;

import java.net.InetAddress;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.parameters.inworkflowparameter.serverparameter.service.ServerParameterService;

import io.swagger.v3.oas.annotations.tags.Tag;
@Tag(name = "USM Users", description = "USM Users' exposed APIs")
@RestController
@RequestMapping("/api")
public class ServerParameterController {
	
	@Autowired 
	public ServerParameterService serverParameterService;
	
	@GetMapping("/getAllWorkFlowParameter")
	public List<ObjectNode> getAllWorkFlowParameter() {
		String currServerUrl = "";
		String currServerName = "";
		try {
			currServerUrl = InetAddress.getLocalHost().getHostAddress();
			currServerName = InetAddress.getLocalHost().getHostName();
			
		} catch (Exception e) {
		}
		
		return serverParameterService.getAllWorkFlowParameter(currServerUrl, currServerName);
	}
}
