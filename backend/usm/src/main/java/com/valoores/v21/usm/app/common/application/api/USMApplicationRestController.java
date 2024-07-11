package com.valoores.v21.usm.app.common.application.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.common.application.model.USMApplication;
import com.valoores.v21.usm.app.common.application.service.IUSMApplicationService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "USM  Application", description = "USM  Application exposed APIs.")
@RestController
@RequestMapping("/api")
public class USMApplicationRestController {
	
	@Autowired
	private IUSMApplicationService usmapplication ;
	
	@GetMapping("/getUSMApplication")
	public List<USMApplication> getApplicationCombo() {
		return usmapplication.getApplicationCombo();
	}
   
	
	@GetMapping("/getUSMApplicationMenu/{id}")
	public List<ObjectNode> getApplicationMenuCombo(@PathVariable("id") String id) {
		return usmapplication.getApplicationMenuCombo(id);
	}

	@PostMapping("/getUSMApplicationRelatedUserId/{id}")
	public List<ObjectNode> getUSMApplicationRelatedUserId(@PathVariable("id") long id) {
		return usmapplication.getUSMApplicationRelatedUserId(id);
	}
	
	//added by Mira for the role access rights
	@PostMapping("/getUSMApplicationRelatedRoleId/{id}")
	public List<ObjectNode> getUSMApplicationRelatedRoleId(@PathVariable("id") long id) {
		return usmapplication.getUSMApplicationRelatedRoleId(id);
	}
}
