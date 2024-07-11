package com.valoores.v21.usm.app.logs.invalidlogin.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.logs.invalidlogin.service.IInvalidLoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "USM Users", description = "USM Users' exposed APIs")
@RestController
@RequestMapping("/api")
public class InvalidLoginController {
	@Autowired
	public IInvalidLoginService invalidLoginService;
	
	@Operation(summary = "Get all Invalid Logs")
	@PostMapping(path = "/getAllInvalidLogs/{loginDate}", produces = "application/json")
	public List<ObjectNode> getAllInvalidLogs( @PathVariable String loginDate)
	{
		return invalidLoginService.getAllInvalidLogs(loginDate);
	}
}
