package com.valoores.v21.usm.app.common.status.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valoores.v21.usm.app.common.status.model.Status;
import com.valoores.v21.usm.app.common.status.service.IStatusService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Status", description = "Status exposed APIs")
@RestController
@RequestMapping("/api")
public class StatusRestController {
	@Autowired
	private IStatusService statusService;

	@GetMapping("/getStatus")
	public List<Status> getStatus() {
		return statusService.getStatus();
	}
}
