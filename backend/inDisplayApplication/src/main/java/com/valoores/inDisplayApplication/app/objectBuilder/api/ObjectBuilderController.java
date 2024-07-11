package com.valoores.inDisplayApplication.app.objectBuilder.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.inDisplayApplication.app.objectBuilder.dto.AppChartDto;
import com.valoores.inDisplayApplication.app.objectBuilder.dto.AppGridDto;
import com.valoores.inDisplayApplication.app.objectBuilder.dto.CfgObjectKpiDto;
import com.valoores.inDisplayApplication.app.objectBuilder.dto.DashboardChartDto;
import com.valoores.inDisplayApplication.app.objectBuilder.dto.DashboardGridDto;
import com.valoores.inDisplayApplication.app.objectBuilder.dto.DashboardKpiDto;
import com.valoores.inDisplayApplication.app.objectBuilder.dto.DashboardTemplateDto;
import com.valoores.inDisplayApplication.app.objectBuilder.service.ObjectBuilderService;
import com.valoores.inDisplayApplication.app.objectBuilder.service.Impl.ObjectBuilderServiceImpl.QueryData;
import com.valoores.inDisplayApplication.backend.CustomResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Object Builder Apis", description = "Object Builder exposed APIs")
@RestController
@RequestMapping("/api")
public class ObjectBuilderController {

	@Autowired
	private ObjectBuilderService ObjectBuilderService;

	@Operation(summary = "Get ALL Screens Data")
    @PostMapping(path = "/getQueryName", produces = "application/json")
	public List<ObjectNode> getQueryNameApi(){
 
		return ObjectBuilderService.getQueryNameApi();

    }
	
	
	

	@Operation(summary = "Get ALL Charts Data")
    @PostMapping(path = "/getChartData", produces = "application/json")
	public List<ObjectNode> getAllCharts(){
 
		return ObjectBuilderService.getAllCharts();

    }
	
	@Operation(summary = "Get ALL Grids Data")
    @PostMapping(path = "/getGridData", produces = "application/json")
	public List<ObjectNode> getAllGrids(){
 
		return ObjectBuilderService.getAllGrids();

    }
	
	@Operation(summary = "Get ALL kpi Data")
    @PostMapping(path = "/getKpiData", produces = "application/json")
	public List<ObjectNode> getAllKpi(){
 
		return ObjectBuilderService.getAllKpi();

    }
	
	@Operation(summary = "Get ALL Charts Data Screen Builder")
    @PostMapping(path = "/getAllChartScreenBuilder", produces = "application/json")
	public List<ObjectNode> getAllChartsScreenBuilder(){
 
		return ObjectBuilderService.getAllChartScreenBuilder();

    }
	
	@Operation(summary = "Get ALL Grids Data Screen Builder")
    @PostMapping(path = "/getAllGridScreenBuilder", produces = "application/json")
	public List<ObjectNode> getAllGridsScreenBuilder(){
 
		return ObjectBuilderService.getAllGridScreenBuilder();

    }
	
	@Operation(summary = "Get ALL kpi Data Screen Builder")
    @PostMapping(path = "/getAllKpiScreenBuilder", produces = "application/json")
	public List<ObjectNode> getAllKpiScreenBuilder(){
 
		return ObjectBuilderService.getAllKpiScreenBuilder();

    }
	@Operation(summary = "get All Chart for Dropdown")
    @GetMapping(path = "/getAllChartforDropdown", produces = "application/json")
	public List<ObjectNode> getAllChartforDropdown(){
 
		return ObjectBuilderService.getAllChartforDropdown();

    }
	
	
	@Operation(summary = "get All Grid for Dropdown")
	@GetMapping(path = "/getAllGridforDropdown", produces = "application/json")
	public List<ObjectNode> getAllGridforDropdown(){
 
		return ObjectBuilderService.getAllGridforDropdown();

    }
	

	@Operation(summary = "Get ALL Charts Dashboard Data")
    @PostMapping(path = "/getDashboardChartData", produces = "application/json")
	public List<ObjectNode> getDashboardChartData(){
 
		return ObjectBuilderService.getDashboardChartData();

    }
	
	@Operation(summary = "Get ALL Grids Dashboard Data")
    @PostMapping(path = "/getDashboardGridData", produces = "application/json")
	public List<ObjectNode> getDashboardGridData(){
 
		return ObjectBuilderService.getDashboardGridData();

    }
	
	@Operation(summary = "Get ALL kpi Dashboard Data")
    @PostMapping(path = "/getDashboardKpiData", produces = "application/json")
	public List<ObjectNode> getDashboardKpiData(){
 
		return ObjectBuilderService.getDashboardKpiData();

    }
	
	@Operation(summary = "Get ALL Dashboard Template")
    @PostMapping(path = "/getDashboardTemplateData/{userId}", produces = "application/json")
	public List<ObjectNode> getDashboardTemplateData(@PathVariable("userId") long userId){
 
		return ObjectBuilderService.getDashboardTemplateData(userId);

    }
	
	@Operation(summary = "Get ALL Dashboard Template Tab")
    @PostMapping(path = "/getDashboardTemplateTab/{userId}")
	public List<ObjectNode> getDashboardTemplateTab(@PathVariable("userId") long userId){
 
		return ObjectBuilderService.getDashboardTemplateTab(userId);

    }
	
	@Operation(summary = "Get  Dashboard Template Name")
	@GetMapping(path = "/getDashboardTemplateName/{id}")
	public List<ObjectNode> getDashboardTemplateName(@PathVariable("id") long id){
 
		return ObjectBuilderService.getDashboardTemplateName(id);

    }
	
	@Operation(summary = "Add new DahboardsTemplate")
	@PostMapping("/addDashboardTempalte")
	public CustomResponse addDashboardTempalte(@RequestBody DashboardTemplateDto dashboardDto) {
		return ObjectBuilderService.addDashboardTempalte(dashboardDto);
	}
	

	@Operation(summary = "Delete From DashboardTempalte")
	@DeleteMapping("/deleteDashboardTempalte/{id}")
	public void deleteDashboardTempalte(@PathVariable("id") long id) {
		 ObjectBuilderService.deleteDashboardTempalte(id);
	}
	
	@Operation(summary = "Update  DashboardTempalte")
	@PostMapping("/updateDashboardTempalte")
	public CustomResponse updateDashboardTempalte(@RequestBody DashboardTemplateDto dashboardTemplateDto) {

		System.out.println("dashboardTemplateDto name === " + dashboardTemplateDto.getTemplateName());
		System.out.println("dashboardTemplateDto ID === " + dashboardTemplateDto.getTemplateID());
		return ObjectBuilderService.updateDashboardTempalte(dashboardTemplateDto);
	}
	
	
	@Operation(summary = "get Chart")
	@GetMapping("/selectChart/{id}")
	public List<ObjectNode> selectChart(@PathVariable("id") long id) {
		return ObjectBuilderService.selectChart(id);
	}
	@Operation(summary = "get Grid")
	@GetMapping("/selectGrid/{id}")
	public List<ObjectNode> selectGrid(@PathVariable("id") long id) {
		return ObjectBuilderService.selectGrid(id);
	}
	@Operation(summary = "get Kpi")
	@GetMapping("/selectKpi/{id}")
	public List<ObjectNode> selectKpi(@PathVariable("id") long id) {
		return ObjectBuilderService.selectKpi(id);
	}
	
	@Operation(summary = "get Kpi's chart")
	@GetMapping("/getKpiChartData/{id}")
	public List<ObjectNode> getKpiChartData(@PathVariable("id") long id) {
		return ObjectBuilderService.getKpiChartData(id);
	}

	@Operation(summary = "get Kpi's grid")
	@GetMapping("/getKpiGridData/{id}")
	public List<ObjectNode> getKpiGridData(@PathVariable("id") long id) {
		return ObjectBuilderService.getKpiGridData(id);
	}
	
	@Operation(summary = "Delete From Chart")
	@DeleteMapping("/deleteChart/{id}")
	public void deleteChart(@PathVariable("id") long id) {
		 ObjectBuilderService.deleteChart(id);
	}
	
	@Operation(summary = "Delete From Grid")
	@DeleteMapping("/deleteGrid/{id}")
	public void deleteGrid(@PathVariable("id") Integer id) {
		 ObjectBuilderService.deleteGrid(id);
	}
	
	@Operation(summary = "Delete From kpi")
	@DeleteMapping("/deleteKpi/{id}")
	public void deleteKpi(@PathVariable("id") Integer id) {
		 ObjectBuilderService.deleteKpi(id);
	}
	@Operation(summary = "Update existing Chart")
	@PostMapping("/updateChart")
	public CustomResponse updateChart(@RequestBody AppChartDto chartDto) {
		return ObjectBuilderService.updateChart(chartDto);
	}
	
	@Operation(summary = "Update existing Grid")
	@PostMapping("/updateGrid")
	public CustomResponse updateGrid(@RequestBody AppGridDto gridDto) {
		return ObjectBuilderService.updateGrid(gridDto);
	}
	
	@Operation(summary = "Update existing Kpi")
	@PostMapping("/updateKpi")
	public CustomResponse updateKpi(@RequestBody CfgObjectKpiDto kpiDto) {
		return ObjectBuilderService.updateKpi(kpiDto);
	}
	
	@Operation(summary = "Add new Chart")
	@PostMapping("/addChart")
	public CustomResponse addChart(@RequestBody AppChartDto chartDto) {
		return ObjectBuilderService.addChart(chartDto);
	}
	
	
	@Operation(summary = "Add new Grid")
	@PostMapping("/addGrid")
	public CustomResponse addGrid(@RequestBody AppGridDto gridDto) {
		return ObjectBuilderService.addGrid(gridDto);
	}
	
	@Operation(summary = "Add new kpi")
	@PostMapping("/addKpi")
	public CustomResponse addKpi(@RequestBody CfgObjectKpiDto kpiDto) {
		return ObjectBuilderService.addKpi(kpiDto);
	}
	
	 @PostMapping("/decodeQuery/{queryId}")
		public List<ObjectNode> decodeQuery(@PathVariable("queryId") long queryId) {

			return ObjectBuilderService.decodeQuery(queryId);
		}
	 
	 @PostMapping("/decodeGridQuery/{gridId}")
		public List<ObjectNode> decodeGridQuery(@PathVariable("gridId") long gridId) {

			return ObjectBuilderService.decodeGridQuery(gridId);
		}
	 
	 
	 @PostMapping("/getQueryData/{chartId}")
		public QueryData getQueryData(@PathVariable("chartId") long chartId) {

			return ObjectBuilderService.getQueryData(chartId);
		}
	 @PostMapping("/getGridQueryData/{gridId}")
		public List<ObjectNode> getGridQueryData(@PathVariable("gridId") long gridId) {

			return ObjectBuilderService.getGridQueryData(gridId);
		}
	 
	 @PostMapping("/getChartQueryData/{chartId}")
		public List<ObjectNode> getChartQueryData(@PathVariable("chartId") long chartId) {

			return ObjectBuilderService.getChartQueryData(chartId);
		}
	 
	 @PostMapping("/getKpiQueryData/{kpiId}")
		public List<ObjectNode> getKpiQueryData(@PathVariable("kpiId") long kpiId) {

			return ObjectBuilderService.getKpiQueryData(kpiId);
		}
	 @PostMapping("/decodeKpiQuery/{kpiId}")
		public List<ObjectNode> decodeKpiQuery(@PathVariable("kpiId") long kpiId) {

			return ObjectBuilderService.decodeKpiQuery(kpiId);
		}
	 @Operation(summary = "Add new Dashboard Grid")
	 @PostMapping("/addDashboardGrid")
		public CustomResponse addDashboardGrid(@RequestBody DashboardGridDto dashboardGridDto) {
			return ObjectBuilderService.addDashboardGrid(dashboardGridDto);
		}
	 @Operation(summary = "Add new Dashboard chart")
	 @PostMapping("/addDashboardChart")
		public CustomResponse addDashboardChart(@RequestBody DashboardChartDto dashboardChartDto) {
			return ObjectBuilderService.addDashboardChart(dashboardChartDto);
		}
	 @Operation(summary = "Add new Dashboard kpi")
	 @PostMapping("/addDashboardKpi")
		public CustomResponse addDashboardKpi(@RequestBody DashboardKpiDto dashboardKpiDto) {
			return ObjectBuilderService.addDashboardKpi(dashboardKpiDto);
		}
	 
	 @Operation(summary = "Delete From Dashboard Chart")
	 @DeleteMapping("/deleteDashboardChart/{id}")
		public void deleteDashboardChart(@PathVariable("id") long id) {
			 ObjectBuilderService.deleteDashboardChart(id);
		}
	 @Operation(summary = "Delete From Dashboard Grid")
	 @DeleteMapping("/deleteDashboardGrid/{id}")
		public void deleteDashboardGrid(@PathVariable("id") long id) {
			 ObjectBuilderService.deleteDashboardGrid(id);
		}
	 @Operation(summary = "Delete From Dashboard Kpi")
	 @DeleteMapping("/deleteDashboardKpi/{id}")
		public void deleteDashboardKpi(@PathVariable("id") long id) {
			 ObjectBuilderService.deleteDashboardKpi(id);
		}
	 
	 @Operation(summary = "Display Dashboard")
	    @PostMapping(path = "/displayDashboard/{templateId}")
		public String displayDashboard(@PathVariable("templateId") long templateId){
	 
			return ObjectBuilderService.displayDashboard(templateId);

	    }
	 @Operation(summary = "Delete template OBJECT")
	 @PostMapping("/deleteSelectedObject/{templateId}/{selectedObject}")
		public void deleteSelectedObject(@PathVariable("templateId") long templateId ,@PathVariable("selectedObject") String selectedObject) {
		 ObjectBuilderService.deleteSelectedObject(templateId , selectedObject);
		}

	 @Operation(summary = "Select Chart RELATED TO KPI")
	 @PostMapping("/selectChartRelatedToKpi/{kpiId}")
		public List<QueryData> selectChartRelatedToKpi(@PathVariable("kpiId") long kpiId ) {
		 return ObjectBuilderService.selectChartRelatedToKpi(kpiId);
		}
	 
	 
	 @Operation(summary = "Select Grid RELATED TO KPI")
	 @PostMapping("/selectGridRelatedToKpi/{kpiId}")
		public List<Map<String, Object>> selectGridRelatedToKpi(@PathVariable("kpiId") long kpiId ) {
		 return ObjectBuilderService.selectGridRelatedToKpi(kpiId);
		}
	 
	 

	

	 
}
