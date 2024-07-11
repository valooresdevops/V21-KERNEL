package com.valoores.v21.usm.app.common.logs.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.common.logs.service.IUserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "USM Users", description = "USM Users' exposed APIs")
@RestController
@RequestMapping("/api")
public class UserController {
	@Autowired
	public IUserService userService;
	
	@PostMapping("/getUserCombo")
	public List<ObjectNode> getUserCombo() {
		return userService.getUserCombo();
	}
//	 

}
