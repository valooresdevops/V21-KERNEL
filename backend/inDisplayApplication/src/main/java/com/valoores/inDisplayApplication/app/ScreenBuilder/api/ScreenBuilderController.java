package com.valoores.inDisplayApplication.app.ScreenBuilder.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.inDisplayApplication.app.ScreenBuilder.dto.ScreenDto;
import com.valoores.inDisplayApplication.app.ScreenBuilder.service.ScreenBuilderService;
import com.valoores.inDisplayApplication.backend.CustomResponse;

import io.swagger.v3.oas.annotations.Operation;


@SpringBootApplication
@RestController
@RequestMapping("/api")
public class ScreenBuilderController {

	@Autowired
	private ScreenBuilderService screenBuilderService;
	
	
	@Operation(summary = "Get ALL Screens Data")
    @PostMapping(path = "/getAllScreens", produces = "application/json")
	public List<ObjectNode> getAllScreens(){
 
		return screenBuilderService.getAllScreens();

    }
	
	
	
	@Operation(summary = "Get all Table Data")
    @PostMapping(path = "/fetchGridsTableData", produces = "application/json")
	public List<ObjectNode> fetchGridsTableData(){
 
		return screenBuilderService.fetchGridsTableData();

    }
	  
	
	@Operation(summary = "Get Application Data")
    @PostMapping(path = "/fetchApplicationList", produces = "application/json")
	public List<ObjectNode> fetchApplicationList(){
 
		return screenBuilderService.fetchApplicationList();

    }
	  
    @PostMapping("/fetchParentMenuList/{menuCode}")
	public List<ObjectNode> fetchParentMenuList(@PathVariable String menuCode){
 
		return screenBuilderService.fetchParentMenuList(menuCode);

    }


	@PostMapping("/addScreen")
	public int addScreen(@RequestBody ScreenDto screenDto ) {
		
		System.out.println("getScreenName>>>>>>>>>>>>> "+screenDto.getScreenName());
		System.out.println("getApplication>>>>>>>>>>>>> "+screenDto.getApplication());
		System.out.println("getApplicationId>>>>>>>>>>>>> "+screenDto.getApplicationId());
		System.out.println("getParentMenuId>>>>>>>>>>>>> "+screenDto.getParentMenuId());	
		System.out.println("getParentMenu>>>>>>>>>>>>> "+screenDto.getParentMenu());
		System.out.println("getData>>>>>>>>>>>>> "+screenDto.getData());
		System.out.println("isTemplate>>>>>>>>>>>>> "+screenDto.isTemplate());
		System.out.println("isSuspended>>>>>>>>>>>>> "+screenDto.getIsSuspended());
		
		return screenBuilderService.addScreen(screenDto);
	}
	
	 @Operation(summary = "Delete an existing Screen")
	    @DeleteMapping("/deleteScreen/{id}")
	    public CustomResponse deleteScreen(@PathVariable("id") long id) {

	        CustomResponse resp = screenBuilderService.deleteScreen(id);
	        return resp;
	    }
	 
	 
	 @GetMapping("/getScreenData/{screenId}")
		public List<ObjectNode> getScreenData(@PathVariable("screenId") long screenId) {

			return screenBuilderService.getScreenData(screenId);
		}
	 
	 @PostMapping("/updateScreen")
		public int updateScreen(@RequestBody ScreenDto screenDto ) {

		
			return screenBuilderService.updateScreen(screenDto);
		}
	 
	 
	 @GetMapping("/getScreenPreviewData/{menuVariable}")
		public List<ObjectNode> getScreenPreviewData(@PathVariable("menuVariable") String menuVariable) {

			return screenBuilderService.getScreenPreviewData(menuVariable);
		}
	 
}
