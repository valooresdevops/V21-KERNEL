package com.valoores.v21.usm.app.parameters.insystemparameters.passwordsettings.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valoores.v21.usm.app.parameters.insystemparameters.passwordsettings.service.IPwdSettingsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Password Settings Configuration", description = "Password Settings Configuration")
@RestController
@RequestMapping("/api")
public class PsdSettingsRestController {
  
	@Autowired
	private IPwdSettingsService pwdSettingsService;

	@Operation(summary = "Password Settings Configuration")
	@PostMapping("/PasswdSettings/{param_code}/{param_value}")
	public void updatePasswordSettings(@PathVariable Integer param_code, @PathVariable String param_value) {
		 pwdSettingsService.updateValues(param_code, param_value);
	}
}