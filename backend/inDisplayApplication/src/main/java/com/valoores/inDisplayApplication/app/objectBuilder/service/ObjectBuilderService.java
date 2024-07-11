package com.valoores.inDisplayApplication.app.objectBuilder.service;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.inDisplayApplication.app.objectBuilder.dto.AppChartDto;
import com.valoores.inDisplayApplication.app.objectBuilder.dto.AppGridDto;
import com.valoores.inDisplayApplication.app.objectBuilder.dto.CfgObjectKpiDto;
import com.valoores.inDisplayApplication.app.objectBuilder.dto.DashboardChartDto;
import com.valoores.inDisplayApplication.app.objectBuilder.dto.DashboardGridDto;
import com.valoores.inDisplayApplication.app.objectBuilder.dto.DashboardKpiDto;
import com.valoores.inDisplayApplication.app.objectBuilder.dto.DashboardTemplateDto;
import com.valoores.inDisplayApplication.app.objectBuilder.service.Impl.ObjectBuilderServiceImpl.QueryData;
import com.valoores.inDisplayApplication.backend.CustomResponse;


public interface ObjectBuilderService {

	List<ObjectNode> getQueryNameApi();

	List<ObjectNode> getAllCharts();

	List<ObjectNode> getAllGrids();
	List<ObjectNode> getAllKpi();
	void deleteChart(long id);
	void deleteGrid(long id);
	void deleteKpi(long id);
	CustomResponse updateChart(AppChartDto chartDto);
	CustomResponse updateGrid(AppGridDto gridDto);
	CustomResponse updateKpi(CfgObjectKpiDto kpiDto);
	CustomResponse addChart(AppChartDto chartDto);
	CustomResponse addGrid(AppGridDto gridDto);
	CustomResponse addKpi(CfgObjectKpiDto kpiDto);
	List<ObjectNode> selectChart(long id);
	List<ObjectNode> selectGrid(long id);
	List<ObjectNode> selectKpi(long id);

	List<ObjectNode> decodeQuery(long queryId);

	QueryData getQueryData(long chartId);

	List<ObjectNode> decodeGridQuery(long gridId);

	List<ObjectNode> getGridQueryData(long gridId);
	
	List<ObjectNode> getChartQueryData(long chartId);

	List<ObjectNode> getKpiQueryData(long kpiId);

	List<ObjectNode> decodeKpiQuery(long kpiId);

	CustomResponse addDashboardKpi(DashboardKpiDto dashboardKpiDto);

	CustomResponse addDashboardChart(DashboardChartDto dashboardChartDto);

	CustomResponse addDashboardGrid(DashboardGridDto dashboardGridDto);

	void deleteDashboardKpi(long id);

	void deleteDashboardGrid(long id);

	void deleteDashboardChart(long id);

	List<ObjectNode> getDashboardChartData();

	List<ObjectNode> getDashboardGridData();

	List<ObjectNode> getDashboardKpiData();

	List<ObjectNode> getDashboardTemplateData(long userId);

	CustomResponse addDashboardTempalte(DashboardTemplateDto dashboardDto);

	void deleteDashboardTempalte(long id);

	CustomResponse updateDashboardTempalte(DashboardTemplateDto dashboardTemplateDto);

	List<ObjectNode> getDashboardTemplateName(long id);

	List<ObjectNode> getDashboardTemplateTab(long userId);

	String displayDashboard(long templateId);


	void deleteSelectedObject(long templateId, String selectedobject);

	List<QueryData> selectChartRelatedToKpi(long kpiId);

	List<Map<String, Object>> selectGridRelatedToKpi(long kpiId);

	List<ObjectNode> getAllChartforDropdown();

	List<ObjectNode> getAllGridforDropdown();

	List<ObjectNode> getAllChartScreenBuilder();

	List<ObjectNode> getAllGridScreenBuilder();

	List<ObjectNode> getAllKpiScreenBuilder();

	List<ObjectNode> getKpiChartData(long id);

	List<ObjectNode> getKpiGridData(long id);


}