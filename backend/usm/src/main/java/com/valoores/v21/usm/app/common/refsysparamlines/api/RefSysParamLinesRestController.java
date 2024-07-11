package com.valoores.v21.usm.app.common.refsysparamlines.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valoores.v21.usm.app.common.refsysparamlines.model.RefSysParamLines;
import com.valoores.v21.usm.app.common.refsysparamlines.service.IRefSysParamLinesService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "RefSysParamLines", description = "RefSysParamLines exposed APIs")
@RestController
@RequestMapping("/api")
public class RefSysParamLinesRestController {
	@Autowired
	private IRefSysParamLinesService refSysParamLinesService;

	@GetMapping("/getRefSysParamLines")
	public List<RefSysParamLines> getRefSysParamLines() {
		return refSysParamLinesService.getRefSysParamLines();
	}
}
