package com.valoores.v21.usm.app.logs.Filter.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.logs.Filter.service.IFilterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "USM Users", description = "USM Users' exposed APIs")
@RestController
@RequestMapping("/api")

public class FilterController {
	@Autowired
	public IFilterService filterService;
	
	@Operation(summary = "Get all Logs By Application")
	@PostMapping(path = "/getApplicationEvent/{application}/{loginDate}/{logoutDate}", produces = "application/json")
	public List<ObjectNode> getApplicationEvent( @PathVariable String application,
			                                  @PathVariable String loginDate,
			                                  @PathVariable String logoutDate)
	{
		return filterService.getApplicationEvent(application,loginDate,logoutDate);
	}

}
