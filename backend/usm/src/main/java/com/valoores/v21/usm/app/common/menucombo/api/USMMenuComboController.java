package com.valoores.v21.usm.app.common.menucombo.api;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.v21.usm.app.common.menucombo.model.USMMenu;
import com.valoores.v21.usm.app.common.menucombo.service.IUSMMenuComboService;

import io.swagger.v3.oas.annotations.tags.Tag;



@Tag(name = "USM Users", description = "USM Users' exposed APIs")
@RestController
@RequestMapping("/api")
public class USMMenuComboController {
	
	@Autowired
	public IUSMMenuComboService usmmenuComboService;
	

	@GetMapping("/getMenuName")
	public List<USMMenu> getMenuName() {
		return usmmenuComboService.getMenuName();
	}
	
	@GetMapping("/getMenuName/{menuCode}")
	public List<ObjectNode> getMenuCombo(@PathVariable("menuCode") String menuCode) {
System.out.println("test >>>>>>>>>>>>>>>>>> " + menuCode);
		return usmmenuComboService.getMenuCombo(menuCode);
	}
	@GetMapping("/getMenuNamewithsub/{menuCode}")
	public List<ObjectNode> getMenuCombowithSubMenu(@PathVariable("menuCode") String menuCode) {
System.out.println("test >>>>>>>>>>>>>>>>>> " + menuCode);
		return usmmenuComboService.getMenuCombowithSubMenu(menuCode);
	}
}
