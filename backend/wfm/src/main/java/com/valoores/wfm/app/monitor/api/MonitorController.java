package com.valoores.wfm.app.monitor.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.wfm.app.monitor.service.MonitorService;


@SpringBootApplication
@RestController
@RequestMapping("/api")
public class MonitorController {@Autowired
	private MonitorService monitorservice;


@PostMapping("/unusedTrigger")
public List<ObjectNode> unusedTrigger(){

	return monitorservice.unusedTrigger();
}

@PostMapping("/missingDoc")
public List<ObjectNode> missingDoc(){

	return monitorservice.missingDoc();
}

@PostMapping("/runningProcess")
public List<ObjectNode> runningProcess(){

	return monitorservice.runningProcess();

}

@PostMapping("/invalidProcess")
public List<ObjectNode> invalidProcess(){

	return monitorservice.invalidProcess();
}
}
