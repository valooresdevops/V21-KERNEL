package com.valoores.v21.usm.app.menu.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.menu.dto.USMAccessTypeDto;
import com.valoores.v21.usm.app.menu.service.IMenuService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Menus", description = "Menu's exposed APIs")
@RestController
@RequestMapping("/api")
public class MenuRestController {

	@Autowired
	private IMenuService menuService;

	@Operation(summary = "Get menu provided a menu parent code.")
	@GetMapping("/menu/{menuParentCode}/{userId}")
	public List<ObjectNode> getAllUSMUsers(@PathVariable("menuParentCode") String menuParentCode,@PathVariable("userId") String userId) {
			System.out.println("MENU DATA>>>>>>>>>>");
		return menuService.getMenusByParentCode(menuParentCode,userId);
	}
	@Operation(summary = "get isManaged Menu.")
	@GetMapping("/menuIsManaged/{menuName}")
	public String isManaged(@PathVariable("menuName") String menuName) {
		return menuService.isManaged(menuName);
	}
	
	@Operation(summary = "Get  access right by user/role.")
	@GetMapping("/accessRight/{menuName}/{userId}")
	public USMAccessTypeDto getAccessRight(@PathVariable("menuName") String menuName,@PathVariable("userId") String userId) {
		return menuService.getAccessRight(menuName,Integer.parseInt(userId));
	}

}