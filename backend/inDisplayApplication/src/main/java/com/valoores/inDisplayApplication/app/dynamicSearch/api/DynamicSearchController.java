package com.valoores.inDisplayApplication.app.dynamicSearch.api;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.inDisplayApplication.app.dynamicSearch.service.IDynamicSearchService;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class DynamicSearchController {

	@Autowired
	private IDynamicSearchService iDynamicSearchService;

	@GetMapping("/getDynamicSearch/{objectId}/{sourceQuery}")
	public List<ObjectNode> getDynamicSearch(@PathVariable long objectId ,@PathVariable String sourceQuery) {
		return iDynamicSearchService.getDynamicSearch(objectId, sourceQuery);
	}

	@GetMapping("/getSearchType")
	public List<ObjectNode> getSearchType() {
		return iDynamicSearchService.getSearchType();

	}

	@PostMapping("/getWhereCondition/{elementSize}")
	public String getWhereCondition(@RequestBody   String  listObject, @PathVariable Integer elementSize) {
		System.out.println(" innnnnnnnnnnnnnnnnnnnnnnnnnnnn");
		System.out.println(" listObject  list       "+ listObject);
		return iDynamicSearchService.getWhereCondition(listObject,elementSize);
	}
	
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/getThirdDropDown/{searchId}/{userId}")
	public List getThirdDropDown(@PathVariable   String  searchId,@PathVariable   String  userId) {
		return iDynamicSearchService.getThirdDropDown(searchId,userId);
	}
	
	
}