package com.valoores.v21.usm.app.logs.logsbyuser.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.logs.logsbyuser.service.ILogsByUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "USM Users", description = "USM Users' exposed APIs")
@RestController
@RequestMapping("/api")
public class LogsByUserController {
	@Autowired
	public ILogsByUserService logsByUserService;
	
//	@GetMapping("/getUserCombo")
//	public List<UserLogs> getUserCombo() {
//		return logsByUserService.getUserCombo();
//	}
	
//	@GetMapping("/getApplicationCombo")
//	public List<USMApplication> getApplicationCombo() {
//		return logsByUserService.getApplicationCombo();
//	}
	
	@Operation(summary = "Get all Logs By User")
	@PostMapping(path = "/getAllLogsByUser/{userLogs}/{application}/{loginDate}", produces = "application/json")
	public List<ObjectNode> getAllLogsByUser( @PathVariable String userLogs,
			                                  @PathVariable String application,
			                                  @PathVariable String loginDate)
	{
		return logsByUserService.getAllLogsByUser(userLogs,application,loginDate);
	}
}
