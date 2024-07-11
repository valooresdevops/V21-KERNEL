package com.valoores.v21.usm.app.logs.logsbyobject.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.logs.logsbyobject.service.ILogsByObjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag; 

@Tag(name = "USM Users", description = "USM Users' exposed APIs")
@RestController
@RequestMapping("/api")
public class LogsByObjectController {
	@Autowired
	public ILogsByObjectService logsByObjectService;
	
//	@GetMapping("/getMenuName/{id}")
//	public List<ObjectNode> getMenuCombo(@PathVariable("id") String id) {
//System.out.println("test >>>>>>>>>>>>>>>>>> " + id);
//		return logsByObjectService.getMenuCombo(id);
//	}
	
//	@GetMapping("/getApplicationObjectCombo")
//	public List<USMApplication> getApplicationCombo() {
//		return logsByObjectService.getApplicationCombo();
//	}
	
	@Operation(summary = "Get all Logs By Object")
	@GetMapping(path = "/getAllLogsByObject/{application}/{menuName}/{loginDate}/{logoutDate}", produces = "application/json")
	@Transactional(timeout = 240)
	public List<ObjectNode> getAllLogsByObject( @PathVariable String application,
			 									@PathVariable String menuName,
			                                    @PathVariable String loginDate,
			                                    @PathVariable String logoutDate)
	{
		return logsByObjectService.getAllLogsByObject(application,menuName,loginDate,logoutDate);
	}

}
