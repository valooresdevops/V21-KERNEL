package com.valoores.v21.usm.app.logs.fieldhistorylog.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.logs.fieldhistorylog.service.IFieldHistoryLogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag; 

@Tag(name = "USM Users", description = "USM Users' exposed APIs")
@RestController
@RequestMapping("/api")
public class FieldHistoryLogController {
	@Autowired
	public IFieldHistoryLogService fieldHistoryLogService;
	
	@GetMapping("/getFieldCombo")
	public List<ObjectNode> getFieldCombo() {
		System.out.println("testing1");
		return fieldHistoryLogService.getFieldCombo();
	}
	
	//path in GetMapping was "/getAllLogsByObject/{application}/{menuName}/{loginDate}/{logoutDate}"
	@Operation(summary = "Get field History Logs")
	@GetMapping(path = "/getAllFieldHistoryLog/{application}/{menu}/{user}/{field}/{fromDate}/{toDate}", produces = "application/json")
	@Transactional(timeout = 240)
	public List<ObjectNode> getAllFieldHistoryLog( @PathVariable String application,
			 									   @PathVariable String menu,
			 									   @PathVariable String user,
			 									   @PathVariable String field,
			                                       @PathVariable String fromDate,
			                                       @PathVariable String toDate)
	{
		
		return fieldHistoryLogService.getAllFieldHistoryLog(application,menu,user,field,fromDate,toDate);
	}

}
