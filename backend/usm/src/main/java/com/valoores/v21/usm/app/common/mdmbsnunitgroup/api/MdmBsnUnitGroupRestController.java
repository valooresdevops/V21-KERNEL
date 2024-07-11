package com.valoores.v21.usm.app.common.mdmbsnunitgroup.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valoores.v21.usm.app.common.mdmbsnunitgroup.model.MdmBsnUnitGroup;
import com.valoores.v21.usm.app.common.mdmbsnunitgroup.service.IMdmBsnUnitGroupService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "MdmBsnUnitGroup", description = "MdmBsnUnitGroup exposed APIs")
@RestController
@RequestMapping("/api")
public class MdmBsnUnitGroupRestController {
	@Autowired
	private IMdmBsnUnitGroupService mdmBsnUnitGroupService;

	@GetMapping("/getMdmBsnUnitGroup")
	public List<MdmBsnUnitGroup> getMdmBsnUnitGroup() {
		return mdmBsnUnitGroupService.getMdmBsnUnitGroup();
	}

}
