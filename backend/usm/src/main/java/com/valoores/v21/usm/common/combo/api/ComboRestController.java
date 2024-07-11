package com.valoores.v21.usm.common.combo.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valoores.v21.usm.common.combo.service.IComboService;

import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Combo")
@RestController
@RequestMapping("/api")
public class ComboRestController {

	@Autowired
	private IComboService comboService ;
	
	@SuppressWarnings("rawtypes")
	@GetMapping("/getCombo")
	public List getCombo(String entityName) {
		return comboService.getCombo(entityName);
	}

}
