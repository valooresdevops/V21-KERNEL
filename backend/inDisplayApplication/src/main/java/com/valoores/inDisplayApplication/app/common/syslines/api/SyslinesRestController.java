package com.valoores.inDisplayApplication.app.common.syslines.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valoores.inDisplayApplication.app.common.syslines.model.Syslines;
import com.valoores.inDisplayApplication.app.common.syslines.service.ISyslinesService;

import io.swagger.v3.oas.annotations.tags.Tag;
@Tag(name = "Syslines", description = " Syslines exposed APIs.")
@RestController
@RequestMapping("/api")
public class SyslinesRestController {

	@Autowired
	private ISyslinesService sysLinesService;
	
	@GetMapping("/getSysLinesData/{heaCode}")
	public List<Syslines> getSysLinesData(@PathVariable Integer heaCode) {
		return sysLinesService.getSysLinesData(heaCode);
	}
	
	@GetMapping("/getSysLinesDataWithIds/{heaCode}/{ids}")
	public List<Syslines> getSysLinesDataWithIds(@PathVariable Integer heaCode, @PathVariable String ids) {
		return sysLinesService.getSysLinesDataWithIds(heaCode, ids);
	}
	
}