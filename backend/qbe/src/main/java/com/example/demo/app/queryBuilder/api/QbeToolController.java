package com.example.demo.app.queryBuilder.api;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.demo.app.queryBuilder.service.QbeApplicationService;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//
//import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.app.queryBuilder.service.QbeToolService;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.swagger.v3.oas.annotations.Operation;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class QbeToolController {

	@Autowired
	private QbeToolService qbeToolService;
	
	
	
	 @Operation(summary = "Get all tables")
	    @PostMapping(path = "/getAllTables", produces = "application/json")
		public List<ObjectNode> getAllTables(){
	 
			return qbeToolService.getAllTables();

	    }
	 
	 
	 @Operation(summary = "Get table Columns")
	    @PostMapping(path = "/getTableColumns/{tableName}", produces = "application/json")
		public List<ObjectNode> getTableColumns(@PathVariable String tableName){
	 
			return qbeToolService.getTableColumns(tableName);

	    }
	
}
