package com.valoores.v21.usm.app.common.syslines.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valoores.v21.usm.app.common.syslines.model.Syslines;
import com.valoores.v21.usm.app.common.syslines.service.ISyslinesService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Syslines", description = " Syslines exposed APIs.")
@RestController
@RequestMapping("/api")
public class SyslinesRestController {

	@Autowired
	private ISyslinesService sysLinesService;

	@GetMapping("/getSyslines/{heaCode}")
	public List<Syslines> getSyslinesComboTest(@PathVariable("heaCode") Integer heaCode) {
		return sysLinesService.getSyslinesCombo(heaCode);
	}
	
	
	@GetMapping("/getSyslinesPeriod")
	public List<Syslines> getSyslinesPeriodCombo() {
		return sysLinesService.getSyslinesPeriodCombo();
	}
   
}
