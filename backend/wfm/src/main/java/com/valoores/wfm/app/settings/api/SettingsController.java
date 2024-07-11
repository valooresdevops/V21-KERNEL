package com.valoores.wfm.app.settings.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.wfm.app.settings.service.SettingsService;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class SettingsController {
	
	@Autowired
	private SettingsService settingsservice;

	
    @PostMapping("/getVacations")
	public List<ObjectNode> getVacations(){
 
		return settingsservice.getVacations();
}

}