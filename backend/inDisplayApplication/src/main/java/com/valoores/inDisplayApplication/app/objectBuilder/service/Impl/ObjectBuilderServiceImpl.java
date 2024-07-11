package com.valoores.inDisplayApplication.app.objectBuilder.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.transform.ResultTransformer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.valoores.inDisplayApplication.app.common.ObjectToJsonRepository;
import com.valoores.inDisplayApplication.app.objectBuilder.dto.AppChartDto;
import com.valoores.inDisplayApplication.app.objectBuilder.dto.AppGridDto;
import com.valoores.inDisplayApplication.app.objectBuilder.dto.CfgObjectKpiDto;
import com.valoores.inDisplayApplication.app.objectBuilder.dto.DashboardChartDto;
import com.valoores.inDisplayApplication.app.objectBuilder.dto.DashboardGridDto;
import com.valoores.inDisplayApplication.app.objectBuilder.dto.DashboardKpiDto;
import com.valoores.inDisplayApplication.app.objectBuilder.dto.DashboardTemplateDto;
import com.valoores.inDisplayApplication.app.objectBuilder.model.AppChart;
import com.valoores.inDisplayApplication.app.objectBuilder.model.AppChartField;
import com.valoores.inDisplayApplication.app.objectBuilder.model.AppGrid;
import com.valoores.inDisplayApplication.app.objectBuilder.model.CfgDashboardChartModel;
import com.valoores.inDisplayApplication.app.objectBuilder.model.CfgDashboardGridModel;
import com.valoores.inDisplayApplication.app.objectBuilder.model.CfgDashboardObjectKpi;
import com.valoores.inDisplayApplication.app.objectBuilder.model.CfgObjectKpiModel;
import com.valoores.inDisplayApplication.app.objectBuilder.model.DashboardTemplates;
import com.valoores.inDisplayApplication.app.objectBuilder.repository.AppChartFieldRepository;
import com.valoores.inDisplayApplication.app.objectBuilder.repository.AppChartRepository;
import com.valoores.inDisplayApplication.app.objectBuilder.repository.AppGridRepository;
import com.valoores.inDisplayApplication.app.objectBuilder.repository.CfgObjectKpiRepository;
import com.valoores.inDisplayApplication.app.objectBuilder.repository.DashboardChartRepository;
import com.valoores.inDisplayApplication.app.objectBuilder.repository.DashboardGridRepository;
import com.valoores.inDisplayApplication.app.objectBuilder.repository.DashboardKpiRepository;
import com.valoores.inDisplayApplication.app.objectBuilder.repository.DashboardTemplateRepository;
import com.valoores.inDisplayApplication.app.objectBuilder.repository.SQBQueryDetailsRepo;
import com.valoores.inDisplayApplication.app.objectBuilder.service.ObjectBuilderService;
import com.valoores.inDisplayApplication.backend.CustomResponse;

@Service
public class ObjectBuilderServiceImpl implements ObjectBuilderService {

	@Resource
	private SQBQueryDetailsRepo sqbQueryDetailsRepo;

	@Autowired
	private EntityManager entityManagerR;

	@Autowired
	private AppChartRepository chartRepository;

	@Autowired
	private AppChartFieldRepository chartFieldRepository;

	@Autowired
	private AppGridRepository gridRepository;

	@Autowired
	private CfgObjectKpiRepository objectKpiRepository;

	@Autowired
	private DashboardChartRepository dashboardChartRepository;

	@Autowired
	private DashboardGridRepository dashboardGridRepository;

	@Autowired
	private DashboardKpiRepository dashboardKpiRepository;

	@Autowired
	private DashboardTemplateRepository dashboardTemplateRepository;

	@Override
	public List<ObjectNode> getQueryNameApi() {

		return ObjectToJsonRepository.getJson(entityManagerR,
				"Select c.QBE_ID as id,c.QUERY_NAME As name" + " From ObjectBuilderModel c WHERE c.CREATED_BY != -1");
	}

	@Override
	public List<ObjectNode> getAllCharts() {
		return ObjectToJsonRepository.getJson(entityManagerR,
				"SELECT a.chartId as chartId, a.chartName as title,a.CREATED_BY as createdBy,"
						+ "a.CREATION_DATE as creationDate FROM AppChart a ");
	}

	@Override
	public List<ObjectNode> getAllChartforDropdown() {
		return ObjectToJsonRepository.getJson(entityManagerR,
				"SELECT a.chartId as id, a.chartName as name FROM AppChart a ");
	}

	@Override
	public List<ObjectNode> getAllGridforDropdown() {
		return ObjectToJsonRepository.getJson(entityManagerR,
				"SELECT a.gridId as id, a.gridName as name FROM AppGrid a ");
	}

	/*
	 * @Override public List<ObjectNode> getKpiData() {
	 * 
	 * return getJson(
	 * entityManagerR,"Select c.KPI_NAME AS title , c.CREATED_BY AS created_by , c.CREATION_DATE AS creation_date "
	 * + "From CfgObjectKpiModel c");
	 */
	@Override
	public List<ObjectNode> getAllGrids() {
		return ObjectToJsonRepository.getJson(entityManagerR,
				"SELECT a.gridId as gridId, a.gridName as title,a.createdBy as createdBy,"
						+ "a.creationDate as creationDate" + " FROM AppGrid a");
	}

	@Override
	public List<ObjectNode> selectChart(long id) {
		try {
			List<ObjectNode> result = ObjectToJsonRepository.getJson(entityManagerR,
					"SELECT C.chartName as chartName," + "C.objectKpiId as objectKpiId," + "C.query as query,"
							+ "C.chartSize as chartSize," + "C.chartHTitle as chartHTitle,"
							+ "C.chartVTitle as chartVTitle ," + "C.showLegend as isShowLegend," + "C.is3d as is3d,"
							+ "C.isHorizontal as isHorizental ," + "A.fieldName as fieldName,"
							+ "A.queryFieldName as queryFieldName," + "A.fieldColor as fieldColor,"
							+ " A.isSerie as isSerie," + "A.serieType as serieType," + "A.drilldown as drilldown,"
							+ "A.drilldownType as drilldownType " + "FROM AppChart C ,AppChartField A "
							+ "WHERE C.chartId =" + id + " AND A.chartId = " + id);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	public static List<ObjectNode> getJson(EntityManager entityManagerR, String query) {

		List<ObjectNode> json = new ArrayList<>();

		ObjectMapper mapper = new ObjectMapper();

		entityManagerR.createQuery(query).unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new ResultTransformer() {
					@Override
					public Object transformTuple(Object[] tuple, String[] aliases) {

						ObjectNode node = mapper.createObjectNode();
						for (int i = 0; i < tuple.length; i++) {
							if (tuple[i] != null) {

								node.put(aliases[i], tuple[i].toString());
							}
						}
						json.add(node);
						return json;
					}

					@SuppressWarnings("rawtypes")
					@Override
					public List transformList(List tuples) {
						return tuples;
					}
				}).getResultList();
		return json;

	}

	@SuppressWarnings("deprecation")
	public static List<ObjectNode> getJsonNativeQuery(EntityManager entityManagerR, String query) {

		List<ObjectNode> json = new ArrayList<>();

		ObjectMapper mapper = new ObjectMapper();

		entityManagerR.createNativeQuery(query).unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new ResultTransformer() {
					@Override
					public Object transformTuple(Object[] tuple, String[] aliases) {

						ObjectNode node = mapper.createObjectNode();
						for (int i = 0; i < tuple.length; i++) {
							if (tuple[i] != null) {
								node.put(aliases[i], tuple[i].toString());
							}
						}
						json.add(node);
						return json;
					}

					@SuppressWarnings("rawtypes")
					@Override
					public List transformList(List tuples) {
						return tuples;
					}
				}).getResultList();
		return json;
	}

	@Override
	public List<ObjectNode> selectGrid(long id) {
		try {

			List<ObjectNode> result = ObjectToJsonRepository.getJson(entityManagerR, "Select gridName as gridName,"
					+ "query as query," + "objectKpiId as objectKpiId " + " From AppGrid" + " where gridId = " + id);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<ObjectNode> selectKpi(long id) {
		try {
			List<ObjectNode> result = ObjectToJsonRepository.getJson(entityManagerR,
					"Select kpiName as kpiName, " + "qbeId as qbeId," + "isRatio as isRatio,"
							+ "mainValue as mainValue," + "mainLabel as mainLabel," + "extraLabel as extraLabel,"
							+ "extraValue as extraValue ," + "isPercentage as isPercentage,"
							+ "backgroundColor as backgroundColor," + "textColor as textColor," + "chart as chart,"
							+ "grid as grid ," + "report as report " + "From CfgObjectKpiModel " + "where kpiId=" + id);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public List<ObjectNode> getAllKpi() {
		return ObjectToJsonRepository.getJson(entityManagerR,
				"SELECT a.kpiId as kpiId, a.kpiName as title,a.createdBy as createdBy,"
						+ "a.creationDate as creationDate" + " From CfgObjectKpiModel a where a.kpiId <>0 ");

	}

	
	@Override
	public List<ObjectNode> getAllChartScreenBuilder() {
		return ObjectToJsonRepository.getJson(entityManagerR,
				"SELECT a.chartId as CHART_ID, a.chartName as CHART_NAME FROM AppChart a ");
	}
	
	@Override
	public List<ObjectNode> getAllGridScreenBuilder() {
		return ObjectToJsonRepository.getJson(entityManagerR,
				"SELECT a.gridId as GRID_ID, a.gridName as GRID_NAME FROM AppGrid a");
	}
	@Override
	public List<ObjectNode> getAllKpiScreenBuilder() {
		return ObjectToJsonRepository.getJson(entityManagerR,
				"SELECT a.kpiId as KPI_ID, a.kpiName as KPI_NAME From CfgObjectKpiModel a where a.kpiId <>0 ");
		

	}
	
	
	@Override
	public void deleteChart(long id) {
		chartFieldRepository.deleteById(id);
		chartRepository.deleteById(id);

	}

	@Override
	public void deleteGrid(long id) {
		gridRepository.deleteById(id);
	}

	@Override
	public void deleteKpi(long id) {
		objectKpiRepository.deleteById(id);
	}

	@Transactional
	@Override
	public CustomResponse updateChart(AppChartDto chartDto) {

		CustomResponse resp = CustomResponse.builder().build();
		long chartId = chartDto.getChartId();
		String chartName = chartDto.getChartName();
		Integer objectKpiId = chartDto.getObjectKpiId();
		int query = chartDto.getQuery();
		Integer chartSize = chartDto.getChartSize();
		String chartHTitle = chartDto.getChartHTitle();
		String chartVTitle = chartDto.getChartVTitle();
		String showLegend = chartDto.getShowLegend();
		String is3d = chartDto.getIs3d();
		String isHorizontal = chartDto.getIsHorizontal();
		Integer chartType = chartDto.getChartType();
		chartRepository.updateChart(chartId, chartName, objectKpiId, query, chartSize, chartHTitle, chartVTitle,
				showLegend, is3d, isHorizontal, chartType);
		chartFieldRepository.deleteById(chartId);

		Date now1 = new Date();
		String selectedRow = chartDto.getChartVTitle();
		String selectedColumn = chartDto.getChartHTitle();
		String[] selectedRow1 = selectedRow.split(",");
		String[] selectedColumn1 = selectedColumn.split(",");

		List<AppChartField> columnModelList = new ArrayList<>();

		for (int i = 0; i < selectedRow1.length; i++) {
			AppChartField columnModel2 = new AppChartField();
			columnModel2.setFieldName(selectedRow1[i]);
			columnModel2.setChartId(chartId);
			columnModel2.setQueryFieldName(chartDto.getQueryFieldName());
			columnModel2.setFieldColor(chartDto.getFieldColor());
			columnModel2.setIsSerie("0");
			columnModel2.setSerieType(chartDto.getSerieType());
			columnModel2.setDrilldown(chartDto.getDrilldown());
			columnModel2.setDrilldownType(chartDto.getDrilldownType());
			columnModel2.setCREATED_BY(chartDto.getUserId());
			columnModel2.setCreationDate(now1);
			columnModelList.add(columnModel2);
		}
		for (int i = 0; i < selectedColumn1.length; i++) {
			AppChartField columnModel2 = new AppChartField();
			columnModel2.setFieldName(selectedColumn1[i]);
			columnModel2.setChartId(chartId);
			columnModel2.setQueryFieldName(chartDto.getQueryFieldName());
			columnModel2.setFieldColor(chartDto.getFieldColor());
			columnModel2.setIsSerie("1");
			columnModel2.setSerieType(chartDto.getSerieType());
			columnModel2.setDrilldown(chartDto.getDrilldown());
			columnModel2.setDrilldownType(chartDto.getDrilldownType());
			columnModel2.setCREATED_BY(chartDto.getUserId());
			columnModel2.setCreationDate(now1);
			columnModelList.add(columnModel2);
		}

		chartFieldRepository.saveAll(columnModelList);

		resp.setCode("0");
		resp.setStatus("success");
		resp.setDescription("updated successfully!");
		return resp;
	}

	@Transactional
	@Override
	public CustomResponse updateGrid(AppGridDto gridDto) {
		CustomResponse resp = CustomResponse.builder().build();
		long gridId = gridDto.getGridId();
		long objectKpiId = gridDto.getObjectKpiId();
		Integer query = gridDto.getQuery();
		String gridName = gridDto.getGridName();

		gridRepository.updateGrid(gridId, query, objectKpiId, gridName);
		resp.setCode("0");
		resp.setStatus("success");
		resp.setDescription("updated successfully!");
		return resp;
	}

	@Transactional
	@Override
	public CustomResponse updateKpi(CfgObjectKpiDto kpiDto)
	{
		CustomResponse resp = CustomResponse.builder().build();
		long kpiId = kpiDto.getKpiId();
		String kpiName = kpiDto.getKpiName();
		long qbeId = kpiDto.getQbeId();
		String isRatio = kpiDto.getIsRatio();
		String mainValue = kpiDto.getMainValue();
		String mainLabel = kpiDto.getMainLabel();
		String extraLabel = kpiDto.getExtraLabel();
		String extraValue = kpiDto.getExtraValue();
		String isPercentage = kpiDto.getIsPercentage();
		String backgroundColor = kpiDto.getBackgroundColor();
		String textColor = kpiDto.getTextColor();
		String chart = kpiDto.getChart();
		String grid = kpiDto.getGrid();
		String report = kpiDto.getReport();

		objectKpiRepository.updateKpi(kpiId, kpiName, qbeId, isRatio, mainValue, mainLabel, extraLabel, extraValue,
				isPercentage, backgroundColor, textColor, chart, grid, report);
		
		String dropdownChart = kpiDto.getDropdownChart();
		String dropdownGrid = kpiDto.getDropdownGrid();
		if(dropdownChart == ""){
			dropdownChart=null;
		}
		if(dropdownGrid == ""){
			dropdownGrid=null;
		}

		if(dropdownChart != null && dropdownChart !="")
		{
			chartRepository.removeDropdownChart(kpiId);

			String[] dropdownChart1 = dropdownChart.split(",");
			
			for (int i = 0; i < dropdownChart1.length; i++)
			{
				Long dropdownChart11 = Long.parseLong(dropdownChart1[i]);
				chartRepository.updateDropdownChart(dropdownChart11, kpiId);
			}
		}
		else
		{
			chartRepository.removeDropdownChart(kpiId);
		}

		if(dropdownGrid != null && dropdownGrid != "")
		{
			gridRepository.removeDropdownGrid(kpiId);
			String[] dropdownGrid1 = dropdownGrid.split(",");
		
			for (int i = 0; i < dropdownGrid1.length; i++)
			{
				Long dropdownGrid11 = Long.parseLong(dropdownGrid1[i]);
				gridRepository.updateDropdownGrid(dropdownGrid11, kpiId);
			}
		}
		else
		{
			gridRepository.removeDropdownGrid(kpiId);
		}

		resp.setCode("0");
		resp.setStatus("success");
		resp.setDescription("updated successfully!");
		return resp;
	}

	@Override
	public CustomResponse addChart(AppChartDto chartDto) {
		@SuppressWarnings("unused")
		Integer fieldId = chartDto.getFieldId();
		try {
			Date now = new Date();
			AppChart columnModel = new AppChart();
			columnModel.setChartName(chartDto.getChartName());
			columnModel.setObjectKpiId(0);
			columnModel.setQuery(chartDto.getQuery());
			columnModel.setChartSize(chartDto.getChartSize());
			columnModel.setChartHTitle(chartDto.getChartHTitle());
			columnModel.setChartVTitle(chartDto.getChartVTitle());
			columnModel.setShowLegend(chartDto.getShowLegend());
			columnModel.setIs3d(chartDto.getIs3d());
			columnModel.setIsHorizontal(chartDto.getIsHorizontal());
			columnModel.setChartType(chartDto.getChartType());
			columnModel.setCREATED_BY(chartDto.getUserId());
			columnModel.setCREATION_DATE(now);
			chartRepository.save(columnModel);

			Date now1 = new Date();
			String selectedRow = chartDto.getChartVTitle();
			String selectedColumn = chartDto.getChartHTitle();
			String[] selectedRow1 = selectedRow.split(",");
			String[] selectedColumn1 = selectedColumn.split(",");

			List<AppChartField> columnModelList = new ArrayList<>();

			for (int i = 0; i < selectedRow1.length; i++) {

				AppChartField columnModel2 = new AppChartField();
				columnModel2.setFieldName(selectedRow1[i]);
				columnModel2.setChartId(columnModel.getChartId());
				columnModel2.setQueryFieldName(chartDto.getQueryFieldName());
				columnModel2.setFieldColor(chartDto.getFieldColor());
				columnModel2.setIsSerie("0");
				columnModel2.setSerieType(chartDto.getSerieType());
				columnModel2.setDrilldown(chartDto.getDrilldown());
				columnModel2.setDrilldownType(chartDto.getDrilldownType());
				columnModel2.setCREATED_BY(chartDto.getUserId());
				columnModel2.setCreationDate(now1);
				columnModelList.add(columnModel2);
			}
			for (int i = 0; i < selectedColumn1.length; i++) {
				AppChartField columnModel2 = new AppChartField(); // Create a new instance for each item
				columnModel2.setFieldName(selectedColumn1[i]);
				columnModel2.setChartId(columnModel.getChartId());
				columnModel2.setQueryFieldName(chartDto.getQueryFieldName());
				columnModel2.setFieldColor(chartDto.getFieldColor());
				columnModel2.setIsSerie("1");
				columnModel2.setSerieType(chartDto.getSerieType());
				columnModel2.setDrilldown(chartDto.getDrilldown());
				columnModel2.setDrilldownType(chartDto.getDrilldownType());
				columnModel2.setCREATED_BY(chartDto.getUserId());
				columnModel2.setCreationDate(now1);
				columnModelList.add(columnModel2);
			}

			chartFieldRepository.saveAll(columnModelList); // Save the list

		} catch (Exception e) {
			e.printStackTrace();
		}
		return CustomResponse.builder().code("0").status("success").description("SAVED SUCCESSFULLY").build();
	}

	@Override
	public CustomResponse addGrid(AppGridDto gridDto)
	{
		try
		{
			Date now = new Date();
			AppGrid columnModel = new AppGrid();
			columnModel.setObjectKpiId(gridDto.getObjectKpiId());
			columnModel.setQuery(gridDto.getQuery());
			columnModel.setGridName(gridDto.getGridName());
			columnModel.setCreatedBy(gridDto.getUserId());
			columnModel.setCreationDate(now);
			gridRepository.save(columnModel);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return CustomResponse.builder().code("0").status("success").description("SAVED SUCCESSFULLY").build();
	}

	@Override
	public CustomResponse addKpi(CfgObjectKpiDto kpiDto) {
		try {
			Date now = new Date();
			CfgObjectKpiModel columnModel = new CfgObjectKpiModel();
			columnModel.setKpiName(kpiDto.getKpiName());
			columnModel.setQbeId(kpiDto.getQbeId());
			columnModel.setIsRatio(kpiDto.getIsRatio());
			columnModel.setMainLabel(kpiDto.getMainLabel());
			columnModel.setMainValue(kpiDto.getMainValue());
			columnModel.setExtraLabel(kpiDto.getExtraLabel());
			columnModel.setExtraValue(kpiDto.getExtraValue());
			columnModel.setIsPercentage(kpiDto.getIsPercentage());
			columnModel.setBackgroundColor(kpiDto.getBackgroundColor());
			columnModel.setTextColor(kpiDto.getTextColor());
			columnModel.setChart(kpiDto.getChart());
			columnModel.setGrid(kpiDto.getGrid());
			columnModel.setReport(kpiDto.getReport());
			columnModel.setCreatedBy(kpiDto.getUserId());
			columnModel.setCreationDate(now);
			objectKpiRepository.save(columnModel);

			String dropdownChart = kpiDto.getDropdownChart();
			String dropdownGrid = kpiDto.getDropdownGrid();
			if (dropdownChart!= null) {
				
				String[] dropdownChart1 = dropdownChart.split(",");

				for (int i = 0; i < dropdownChart1.length; i++) {
					Long dropdownChart11 = Long.parseLong(dropdownChart1[i]);
					chartRepository.updateDropdownChart(dropdownChart11, columnModel.getKpiId());
				}
		}else {}
			if (dropdownGrid !=null) {
				
				String[] dropdownGrid1 = dropdownGrid.split(",");
				for (int i = 0; i < dropdownGrid1.length; i++) {
					Long dropdownGrid11 = Long.parseLong(dropdownGrid1[i]);
					gridRepository.updateDropdownGrid(dropdownGrid11, columnModel.getKpiId());
				}
			}else {
				
			}} catch (Exception e) {
			e.printStackTrace();
		}
		return CustomResponse.builder().code("0").status("success").description("SAVED SUCCESSFULLY").build();
	}

	@Override
	public List<ObjectNode> decodeQuery(long queryId) {
		byte[] encodedQuery = sqbQueryDetailsRepo.getQueryBlob(queryId);
		String decodedQuery = new String(Base64.decodeBase64(encodedQuery));

		String dataStoreId = decodedQuery.substring(decodedQuery.indexOf("storeId=\"" + 1),
				decodedQuery.indexOf("\">"));
		dataStoreId = dataStoreId.split("\"")[1];

		@SuppressWarnings("unused")
		String queryName = decodedQuery.substring(decodedQuery.indexOf("<name>") + 15,
				decodedQuery.indexOf("</name>") - 3);

		@SuppressWarnings("unused")
		String query = decodedQuery.substring(decodedQuery.indexOf("<sql>") + 14, decodedQuery.indexOf("</sql>") - 3);

		String headsString = decodedQuery.split("<LstHeads>")[1];
		headsString = headsString.split("</LstHeads>")[0];

		String[] headers = headsString.split("<EltHeads ");

		String queryHeadsJsonString = "[";

		for (int i = 1; i < headers.length; i++) {
			String header = headers[i].split("dbName=\"")[1];

			header = header.split("\"")[0];

			queryHeadsJsonString += "{\"id\":\"" + header + "\",\"name\":\"" + header + "\"}";

			if (i != headers.length - 1) {
				queryHeadsJsonString += ",";

			}
		}
		queryHeadsJsonString += "]";

		ObjectMapper objMapper = new ObjectMapper();
		List<ObjectNode> list = new ArrayList<>();
		JsonNode jsonArray;
		try {
			jsonArray = objMapper.readTree(queryHeadsJsonString);

			for (JsonNode element : jsonArray) {
				ObjectNode object = objMapper.treeToValue(element, ObjectNode.class);
				list.add(object);
			}
		} catch (JsonProcessingException e) {

			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<ObjectNode> decodeGridQuery(long gridId) {
		long queryId = gridRepository.selectQuery(gridId);
		byte[] encodedQuery = sqbQueryDetailsRepo.getQueryBlob(queryId);
		String decodedQuery = new String(Base64.decodeBase64(encodedQuery));

		String dataStoreId = decodedQuery.substring(decodedQuery.indexOf("storeId=\"" + 1),
				decodedQuery.indexOf("\">"));
		dataStoreId = dataStoreId.split("\"")[1];

		@SuppressWarnings("unused")
		String queryName = decodedQuery.substring(decodedQuery.indexOf("<name>") + 15,
				decodedQuery.indexOf("</name>") - 3);

		@SuppressWarnings("unused")
		String query = decodedQuery.substring(decodedQuery.indexOf("<sql>") + 14, decodedQuery.indexOf("</sql>") - 3);

		String headsString = decodedQuery.split("<LstHeads>")[1];
		headsString = headsString.split("</LstHeads>")[0];

		String[] headers = headsString.split("<EltHeads ");

		String queryHeadsJsonString = "[";

		for (int i = 1; i < headers.length; i++) {
			String header = headers[i].split("dbName=\"")[1];
			header = header.split("\"")[0];
			String field = headers[i].split("fieldType=\"")[1];
			field = field.split("\"")[0];

			queryHeadsJsonString += "{\"headerName\":\"" + header + "\",\"field\":\"" + header + "\"}";

			if (i != headers.length - 1) {
				queryHeadsJsonString += ",";

			}
		}
		queryHeadsJsonString += "]";

		ObjectMapper objMapper = new ObjectMapper();
		List<ObjectNode> list = new ArrayList<>();
		JsonNode jsonArray;
		try {
			jsonArray = objMapper.readTree(queryHeadsJsonString);

			for (JsonNode element : jsonArray) {
				ObjectNode object = objMapper.treeToValue(element, ObjectNode.class);
				list.add(object);
			}
		} catch (JsonProcessingException e) {

			e.printStackTrace();
		}

		return list;
	}

	public class QueryData {
		private List<ObjectNode> records;
		private Integer chartType;
		@SuppressWarnings("unused")
		private long chartId;
		private Integer is3d;


		public QueryData(List<ObjectNode> records, Integer chartType, Integer is3d) {
			this.records = records;
			this.chartType = chartType;
			this.is3d = is3d;

		}
		
		public QueryData(List<ObjectNode> records, Integer chartType,long chartId, Integer is3d) {
			this.records = records;
			this.chartType = chartType;
			this.chartId = chartId;
			this.is3d = is3d;


		}

		public QueryData(List<ObjectNode> objectNodeList, int endIndex) {
		}

		public List<ObjectNode> getRecords() {
			return records;
		}

		public Integer getChartType() {
			return chartType;
		}
		
		public Integer getIs3d() {
			return is3d;
		}
	}

	@Override
	public List<ObjectNode> getChartQueryData(long chartId) {
		long queryId = chartRepository.selectQuery(chartId);
		byte[] encodedQuery = sqbQueryDetailsRepo.getQueryBlob(queryId);

		String decodedQuery = new String(Base64.decodeBase64(encodedQuery));

		String dataStoreId = decodedQuery.substring(decodedQuery.indexOf("storeId=\"" + 1),
				decodedQuery.indexOf("\">"));
		dataStoreId = dataStoreId.split("\"")[1];

		@SuppressWarnings("unused")
		String queryName = decodedQuery.substring(decodedQuery.indexOf("<name>") + 15,
				decodedQuery.indexOf("</name>") - 3);

		String query = decodedQuery.substring(decodedQuery.indexOf("<sql>") + 14, decodedQuery.indexOf("</sql>") - 3);

		List<ObjectNode> records = getJsonNativeQuery(entityManagerR, query);

		return records;
	}

	@Override
	public QueryData getQueryData(long chartId) {
		long queryId = chartRepository.selectQuery(chartId);
		Integer chartType = chartRepository.selectChartType(chartId);
		byte[] encodedQuery = sqbQueryDetailsRepo.getQueryBlob(queryId);
		Integer Is3d = chartRepository.checkIfIs3d(chartId);

		String decodedQuery = new String(Base64.decodeBase64(encodedQuery));

		String dataStoreId = decodedQuery.substring(decodedQuery.indexOf("storeId=\"" + 1),
				decodedQuery.indexOf("\">"));
		dataStoreId = dataStoreId.split("\"")[1];

		@SuppressWarnings("unused")
		String queryName = decodedQuery.substring(decodedQuery.indexOf("<name>") + 15,
				decodedQuery.indexOf("</name>") - 3);

		String query = decodedQuery.substring(decodedQuery.indexOf("<sql>") + 14, decodedQuery.indexOf("</sql>") - 3);

		List<ObjectNode> records = getJsonNativeQuery(entityManagerR, query);
		return new QueryData(records, chartType,chartId,Is3d);
	}


	@Override
	public List<ObjectNode> getKpiChartData(long id)
	{
		try
		{
			List<ObjectNode> result = ObjectToJsonRepository.getJson(entityManagerR,					
					"Select chartId as id From AppChart Where objectKpiId = " + id);

			return result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<ObjectNode> getKpiGridData(long id)
	{
		try
		{
			List<ObjectNode> result = ObjectToJsonRepository.getJson(entityManagerR,
					"Select gridId as id  From AppGrid Where objectKpiId = " + id);
			return result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	
	@Override
	public List<ObjectNode> getGridQueryData(long gridId) {
		long queryId = gridRepository.selectQuery(gridId);
		byte[] encodedQuery = sqbQueryDetailsRepo.getQueryBlob(queryId);

		String decodedQuery = new String(Base64.decodeBase64(encodedQuery));

		String dataStoreId = decodedQuery.substring(decodedQuery.indexOf("storeId=\"" + 1),
				decodedQuery.indexOf("\">"));
		dataStoreId = dataStoreId.split("\"")[1];

		@SuppressWarnings("unused")
		String queryName = decodedQuery.substring(decodedQuery.indexOf("<name>") + 15,
				decodedQuery.indexOf("</name>") - 3);

		String query = decodedQuery.substring(decodedQuery.indexOf("<sql>") + 14, decodedQuery.indexOf("</sql>") - 3);

		List<ObjectNode> records = getJsonNativeQuery(entityManagerR, query);
		return records;
	}

	@Override
	public List<ObjectNode> getKpiQueryData(long kpiId) {
		long queryId = objectKpiRepository.selectQuery(kpiId);
		byte[] encodedQuery = sqbQueryDetailsRepo.getQueryBlob(queryId);

		String decodedQuery = new String(Base64.decodeBase64(encodedQuery));

		String dataStoreId = decodedQuery.substring(decodedQuery.indexOf("storeId=\"" + 1),
				decodedQuery.indexOf("\">"));
		dataStoreId = dataStoreId.split("\"")[1];

		@SuppressWarnings("unused")
		String queryName = decodedQuery.substring(decodedQuery.indexOf("<name>") + 15,
				decodedQuery.indexOf("</name>") - 3);

		String query = decodedQuery.substring(decodedQuery.indexOf("<sql>") + 14, decodedQuery.indexOf("</sql>") - 3);

		List<ObjectNode> records = getJsonNativeQuery(entityManagerR, query);
		return records;
	}

	@Override
	public List<ObjectNode> decodeKpiQuery(long kpiId) {
		long queryId = objectKpiRepository.selectQuery(kpiId);
		byte[] encodedQuery = sqbQueryDetailsRepo.getQueryBlob(queryId);
		String decodedQuery = new String(Base64.decodeBase64(encodedQuery));

		String dataStoreId = decodedQuery.substring(decodedQuery.indexOf("storeId=\"" + 1),
				decodedQuery.indexOf("\">"));
		dataStoreId = dataStoreId.split("\"")[1];

		@SuppressWarnings("unused")
		String queryName = decodedQuery.substring(decodedQuery.indexOf("<name>") + 15,
				decodedQuery.indexOf("</name>") - 3);

		@SuppressWarnings("unused")
		String query = decodedQuery.substring(decodedQuery.indexOf("<sql>") + 14, decodedQuery.indexOf("</sql>") - 3);

		String headsString = decodedQuery.split("<LstHeads>")[1];
		headsString = headsString.split("</LstHeads>")[0];
		String[] headers = headsString.split("<EltHeads ");

		String queryHeadsJsonString = "[";

		for (int i = 1; i < headers.length; i++) {
			String header = headers[i].split("dbName=\"")[1];

			header = header.split("\"")[0];
			String field = headers[i].split("fieldType=\"")[1];
			field = field.split("\"")[0];
			queryHeadsJsonString += "{\"ID\":\"" + i + "\",\"header\":\"" + header + "\"}";

			if (i != headers.length - 1) {
				queryHeadsJsonString += ",";

			}
		}
		queryHeadsJsonString += "]";
		ObjectMapper objMapper = new ObjectMapper();
		List<ObjectNode> list = new ArrayList<>();
		JsonNode jsonArray;
		try {
			jsonArray = objMapper.readTree(queryHeadsJsonString);

			for (JsonNode element : jsonArray) {
				ObjectNode object = objMapper.treeToValue(element, ObjectNode.class);
				list.add(object);
			}
		} catch (JsonProcessingException e) {

			e.printStackTrace();
		}

		return list;
	}

	@Override
	public void deleteDashboardChart(long id) {
		dashboardChartRepository.deleteById(id);
	}

	@Override
	public List<ObjectNode> getDashboardTemplateData(long userId) {
		return ObjectToJsonRepository.getJson(entityManagerR,
				"select a.templateID as id, a.templateName as dashboardName,b.USER_LOGIN as userName "
						+ "from DashboardTemplates a , UserModel b " + "where a.createdBy=b.USER_ID and a.createdBy ="
						+ userId);
	}

	@Override
	public List<ObjectNode> getDashboardTemplateTab(long userId) {
	return ObjectToJsonRepository.getJson(entityManagerR, "select templateID as tabId, templateName as title from DashboardTemplates  where createdBy = " + userId);
	}

	@Override
	public List<ObjectNode> getDashboardTemplateName(long id) {
		return ObjectToJsonRepository.getJson(entityManagerR,
				"SELECT  a.templateName as templateName From DashboardTemplates a where a.templateID =" + id);
	}

	@Transactional
	@Override
	public CustomResponse updateDashboardTempalte(DashboardTemplateDto dashboardTemplateDto) {

		CustomResponse resp = CustomResponse.builder().build();
		long templateID = dashboardTemplateDto.getTemplateID();
		String templateName = dashboardTemplateDto.getTemplateName();
		dashboardTemplateRepository.updateDashboardTemplate(templateID, templateName);
		resp.setCode("0");
		resp.setStatus("success");
		resp.setDescription("updated successfully!");
		return resp;
	}

	@Override
	public CustomResponse addDashboardTempalte(DashboardTemplateDto dashboardDto) {
		try {
			Date now = new Date();
			DashboardTemplates columnModel = new DashboardTemplates();
			columnModel.setTemplateName(dashboardDto.getTemplateName());
			columnModel.setUserIds(dashboardDto.getUserIds());
			columnModel.setRoleNames(dashboardDto.getRoleNames());
			columnModel.setPREF_DATA("{\"display\":[]}");
			columnModel.setTemplatePId(null);
			columnModel.setCreationDate(now);
			columnModel.setCreatedBy(dashboardDto.getUserId());
			dashboardTemplateRepository.save(columnModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CustomResponse.builder().code("0").status("success").description("SAVED SUCCESSFULLY").build();
	}

	@Override
	public void deleteDashboardTempalte(long id) {
		dashboardTemplateRepository.deleteById(id);
	}

	@Override
	public List<ObjectNode> getDashboardChartData() {
		return ObjectToJsonRepository.getJson(entityManagerR,
				"SELECT a.chartId as id, a.chartName as name,a.chartSize as size FROM AppChart a ");
	}

	@Override
	public List<ObjectNode> getDashboardKpiData() {
		return ObjectToJsonRepository.getJson(entityManagerR,
				"SELECT a.kpiId as id, a.kpiName as name FROM CfgObjectKpiModel a where a.kpiId <>0 ");
	}

	@Override
	public void deleteDashboardKpi(long id) {
		dashboardKpiRepository.deleteById(id);
	}

	@Override
	public CustomResponse addDashboardKpi(DashboardKpiDto dashboardKpiDto) {
		try {
			Date now = new Date();
			String selectedRow = String.valueOf(dashboardKpiDto.getKpiId());
			String[] selectedRow1 = selectedRow.split(",");
			Long templateId = null;
			List<CfgDashboardObjectKpi> columnModelList = new ArrayList<>();
			List<String> prefData = new ArrayList<>();
			for (int i = 0; i < selectedRow1.length; i++) {
				Long selectedRow11 = Long.parseLong(selectedRow1[i]);
				CfgDashboardObjectKpi columnModel = new CfgDashboardObjectKpi();
				columnModel.setDashboardKpiId(dashboardKpiDto.getDashboardKpiId());
				columnModel.setKpiId(selectedRow11);
				columnModel.setOrder(dashboardKpiDto.getOrder());
				columnModel.setSize(dashboardKpiDto.getSize());
				columnModel.setTemplateId(dashboardKpiDto.getTemplateId());
				columnModel.setCreatedBy(dashboardKpiDto.getUserId());
				columnModel.setCreationDate(now);
				dashboardKpiRepository.save(columnModel);

				columnModelList.add(columnModel);

				prefData.add("\"Kpi_" + selectedRow11 + "\"");
				templateId = columnModel.getTemplateId();
			}

			dashboardKpiRepository.saveAll(columnModelList);

			String content = null;
			String jsonString = dashboardTemplateRepository.getPrefData(templateId);
			int startIndex = jsonString.indexOf('[');
			int endIndex = jsonString.indexOf(']');
			if (startIndex != -1 && endIndex != -1) {
				content = jsonString.substring(startIndex + 1, endIndex);
			} else {
			}
			
			String PrefDataString = "{\"display\":[" + prefData.toString().replace("[", "").replace("]", "") + ","
					+ content + "]}";
			dashboardTemplateRepository.updateDashboardTemplatePrefData(templateId, PrefDataString);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return CustomResponse.builder().code("0").status("success").description("SAVED SUCCESSFULLY").build();

	}

	@Override
	public CustomResponse addDashboardChart(DashboardChartDto dashboardChartDto) {
		try {
			Date now = new Date();
			Long templateId = null;
			String selectedRow = String.valueOf(dashboardChartDto.getChartId());

			String[] selectedRow1 = selectedRow.split(",");

			List<CfgDashboardChartModel> columnModelList = new ArrayList<>();
			List<String> prefData = new ArrayList<>();

			for (int i = 0; i < selectedRow1.length; i++) {
				Long selectedRow11 = Long.parseLong(selectedRow1[i]);
				CfgDashboardChartModel columnModel2 = new CfgDashboardChartModel();
				columnModel2.setDashboardChartId(dashboardChartDto.getDashboardChartId());
				columnModel2.setChartId(selectedRow11);
				columnModel2.setOrder(dashboardChartDto.getOrder());
				columnModel2.setSize(dashboardChartDto.getSize());
				columnModel2.setTemplateId(dashboardChartDto.getTemplateId());
				columnModel2.setCreatedBy(dashboardChartDto.getUserId());
				columnModel2.setCreationDate(now);
				columnModelList.add(columnModel2);
				prefData.add("\"Chart_" + selectedRow11 + "\"");
				templateId = columnModel2.getTemplateId();
			}
			dashboardChartRepository.saveAll(columnModelList);
			String content = null;
			
			String jsonString = dashboardTemplateRepository.getPrefData(templateId);
			int startIndex = jsonString.indexOf('[');
			int endIndex = jsonString.indexOf(']');
			if (startIndex != -1 && endIndex != -1) {
				content = jsonString.substring(startIndex + 1, endIndex);
			} else {
			}

			String PrefDataString = "{\"display\":[" + prefData.toString().replace("[", "").replace("]", "") + ","
					+ content + "]}";
			dashboardTemplateRepository.updateDashboardTemplatePrefData(templateId, PrefDataString);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return CustomResponse.builder().code("0").status("success").description("SAVED SUCCESSFULLY").build();

	}

	@Override
	public List<ObjectNode> getDashboardGridData() {
		return ObjectToJsonRepository.getJson(entityManagerR,
				"SELECT a.gridId as id, a.gridName as name FROM AppGrid a");
	}

	@Override
	public void deleteDashboardGrid(long id) {
		dashboardGridRepository.deleteById(id);
	}

	@Override
	public CustomResponse addDashboardGrid(DashboardGridDto dashboardGridDto) {
		try {
			Date now = new Date();
			Long templateId = null;
		    String selectedRow = String.valueOf(dashboardGridDto.getGridId());
			System.out.println("selectedRow === " + dashboardGridDto.getGridId());
			String[] selectedRow1 = selectedRow.split(",");

			List<CfgDashboardGridModel> columnModelList = new ArrayList<>();
			List<String> prefData = new ArrayList<>();


			for (int i = 0; i < selectedRow1.length; i++) {
				Long selectedRow11 = Long.parseLong(selectedRow1[i]);
				CfgDashboardGridModel columnModel = new CfgDashboardGridModel();
				columnModel.setDashboardGridId(dashboardGridDto.getDashboardGridId());
				columnModel.setGridId(selectedRow11);
				columnModel.setOrder(dashboardGridDto.getOrder());
				columnModel.setSize(dashboardGridDto.getSize());
				columnModel.setTemplateId(dashboardGridDto.getTemplateId());
				columnModel.setCreatedBy(dashboardGridDto.getUserId());
				columnModel.setCreationDate(now);
				columnModelList.add(columnModel);
				prefData.add("\"Grid_" + selectedRow11 + "\"");
				templateId = columnModel.getTemplateId();
			}
			String content = null;
	
			String jsonString = dashboardTemplateRepository.getPrefData(templateId);
			int startIndex = jsonString.indexOf('[');
			int endIndex = jsonString.indexOf(']');
			if (startIndex != -1 && endIndex != -1) {
				content = jsonString.substring(startIndex + 1, endIndex);
			} else {
			}

			String PrefDataString = "{\"display\":[" + prefData.toString().replace("[", "").replace("]", "") + ","
					+ content + "]}";
			dashboardTemplateRepository.updateDashboardTemplatePrefData(templateId, PrefDataString);
			} catch (Exception e) {
			e.printStackTrace();
		}
		return CustomResponse.builder().code("0").status("success").description("SAVED SUCCESSFULLY").build();

	}

	public String displayDashboard(long templateId) {
		String jsonString = dashboardTemplateRepository.getPrefData(templateId);
		String content = null;
		int startIndex = jsonString.indexOf('[');
		int endIndex = jsonString.indexOf(']');
		if (startIndex != -1 && endIndex != -1) {
			content = jsonString.substring(startIndex + 1, endIndex);
		} else {
		}
		String[] content1 = content.split(",");
		String dashboardArrayString = null;
		String ChartContent = "";

		for (int i = 0; i < content1.length; i++) {

			content1[i] = content1[i].replace("\"", "").replaceAll("\\s", "");
			String[] parts = content1[i].split("_");

			String Type = parts[0];
			String ID = parts[1];
			switch (Type) {
			case "Kpi":
				List<ObjectNode> header = decodeKpiQuery(Long.parseLong(ID));
				List<ObjectNode> record = getKpiQueryData(Long.parseLong(ID));
				if (dashboardArrayString == null) {
					dashboardArrayString = "[{\"ID\":" + ID + ",\"type\":\"Kpi\",\"Header\":" + header
							+ " ,\"Records\":" + record + "}";
				} else {
					dashboardArrayString += ",{\"ID\":" + ID + ",\"type\":\"Kpi\",\"Header\":" + header
							+ " ,\"Records\":" + record + "}";
				}
				break;
			case "Grid":
				record = getGridQueryData(Long.parseLong(ID));
				header = decodeGridQuery(Long.parseLong(ID));
				if (dashboardArrayString == null) {
					dashboardArrayString = "[{\"ID\":" + ID + ",\"type\":\"Grid\",\"Header\":" + header
							+ " ,\"Records\":" + record + "}";
				} else {
					dashboardArrayString += ",{\"ID\":" + ID + ",\"type\":\"Grid\",\"Header\":" + header
							+ " ,\"Records\":" + record + "}";
				}
				break;
			case "Chart":
				record = getChartQueryData(Long.parseLong(ID));
				
				Integer chartType = chartRepository.selectChartType(Long.parseLong(ID));
				Integer is3d = chartRepository.checkIfIs3d(Long.parseLong(ID));
				if (dashboardArrayString == null) {
					ChartContent = "{ \"records\":" + record + ",\"chartType\":" + chartType + "}";
					dashboardArrayString = "[{\"ID\":" + ID + ",\"type\":\"Chart\",\"data\":" + ChartContent  +",\"is3d\":" +is3d+"}";
				} else {
					ChartContent = "{ \"records\":" + record + ",\"chartType\":" + chartType+"}";
					dashboardArrayString += ",{\"ID\":" + ID + ",\"type\":\"Chart\",\"data\":" + ChartContent +",\"is3d\":" +is3d+"}";
				}

				break;

			}
			

		}
		dashboardArrayString += "]";

		return dashboardArrayString;
	}
	@Override
	public void deleteSelectedObject(long templateId, String selectedobject) {
		String jsonString = dashboardTemplateRepository.getPrefData(templateId);
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray displayArray = jsonObject.getJSONArray("display");

			removeElementFromArray(displayArray, selectedobject);
			String[] parts = selectedobject.split("_");
			String Type = parts[0];
			String ID = parts[1];
			switch (Type) {
			case "Kpi":
				dashboardKpiRepository.deleteByKpiId(Long.parseLong(ID));
				break;
			case "Grid":
				dashboardGridRepository.deleteByGridId(Long.parseLong(ID));
				break;
			case "Chart":
				dashboardChartRepository.deleteByChartId(Long.parseLong(ID));
				break;
			}

			String updatedJsonString = jsonObject.toString();
			dashboardTemplateRepository.updateDashboardTemplatePrefData(templateId, updatedJsonString);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void removeElementFromArray(JSONArray array, String elementToRemove) {
		for (int i = 0; i < array.length(); i++) {
			if (array.get(i).equals(elementToRemove)) {
				array.remove(i);
				i--;
				break;
			}

		}
	}
	


	public class selectedRelatedToKpi {
		private List<ObjectNode> records;
		private long id;

		public selectedRelatedToKpi(List<ObjectNode> list, long gridId) {
			this.records = list;
			this.id = gridId;
		}

		public List<ObjectNode> getRecords() {
			return records;
		}

		public long getId() {
			return id;
		}
	}

	public class selectedchartRelatedToKpi {
		private QueryData records;
		private Long id;

		@SuppressWarnings("unused")
		private ObjectNode selectedchartRelatedToKpi(QueryData list, Long id){
			this.records = (QueryData) list;
			this.id = id;
			return selectedchartRelatedToKpi(list,id);
		}

		public QueryData getRecords() {
			return records;
		}

		public Long getId() {
			return id;
		}
		
	}

	public List<Map<String, Object>> selectGridRelatedToKpi(long kpiId) {
		List<ObjectNode> gridIds = getJson(entityManagerR,
				"SELECT a.gridId as id FROM AppGrid a WHERE objectKpiId =" + kpiId);
		List<Map<String, Object>> result = new ArrayList<>();

		for (int i = 0; i < gridIds.size(); i++) {
			Long gridId = gridIds.get(i).get("id").asLong();
			Map<String, Object> gridInfo = new HashMap<>();
			gridInfo.put("id", gridId);
			gridInfo.put("records", decodeGridQuery(gridId));
			result.add(gridInfo);
		}

		return result;
	}

	public class ChartData {
		private Long chartId;
		private List<QueryData> queryDataList;

		public ChartData(Long chartId, List<QueryData> queryDataList) {
			this.chartId = chartId;
			this.queryDataList = queryDataList;
		}

		public Long getChartId() {
			return chartId;
		}

		public void setChartId(Long chartId) {
			this.chartId = chartId;
		}

		public List<QueryData> getQueryDataList() {
			return queryDataList;
		}

		public void setQueryDataList(List<QueryData> queryDataList) {
			this.queryDataList = queryDataList;
		}
	}

	@Override
	public List<QueryData> selectChartRelatedToKpi(long kpiId) {
		List<QueryData> list = new ArrayList<>();
		List<ObjectNode> chartIds = getJson(entityManagerR,
				"SELECT a.chartId as id FROM AppChart a WHERE objectKpiId =" + kpiId);
		for (int i = 0; i < chartIds.size(); i++) {
			Long chartId = chartIds.get(i).get("id").asLong();
			list.add(getQueryData(chartId));
		}
		return list;
	}

	
}
