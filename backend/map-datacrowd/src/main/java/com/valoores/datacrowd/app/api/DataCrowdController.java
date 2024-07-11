package com.valoores.datacrowd.app.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.unboundid.util.json.JSONException;
import com.valoores.datacrowd.app.dto.SimulationDto;
import com.valoores.datacrowd.app.service.DataCrowdService;
import com.valoores.datacrowd.backend.CustomResponse;
import com.valoores.datacrowd.common.service.ServiceProviderService;
import com.valoores.datacrowd.utils.objecttojson.ObjectToJsonRepository;

//import io.swagger.v3.oas.annotations.tags.Tag;

//@Tag(name = "Data Crowd", description = "Data Crowd APIs")
@RestController
@RequestMapping("/api")
public class DataCrowdController {

	@Autowired
	private EntityManager entityManagerR;

	@Autowired
	private DataCrowdService datacrowdservice;

	@Autowired
	private ServiceProviderService ServiceProviderService;

	@Value("${cassandra.ip}")
	private String cassandraIp;

	@GetMapping("/getdatabaseType")
	public String getdatabaseType() {
		try {
			System.out.printf("Call Cassandra");
			return datacrowdservice.getdatabaseType();

		} catch (Exception ex) {
			ex.printStackTrace();
			return "Fail";

		}
	}

	@PostMapping("/inserJsonParam")
	public CustomResponse inserJsonParam(@RequestBody String EXECUTION_PARAM) {

		CustomResponse res = CustomResponse.builder().build();
		try {
			datacrowdservice.inserJsonParam(EXECUTION_PARAM);
			res.setStatus("Success");

		} catch (Exception ex) {
			ex.printStackTrace();
			res.setStatus("Fail");

		}

		return res;
	}

	@PostMapping("/inserJsonParam2")
	public CustomResponse inserJsonParam2(@RequestBody String EXECUTION_PARAM) {

		CustomResponse res = CustomResponse.builder().build();

		try {
			datacrowdservice.inserJsonParam(EXECUTION_PARAM);
			res.setStatus("Success");

		} catch (Exception ex) {
			ex.printStackTrace();
			res.setStatus("Fail");

		}

		return res;

	}

	@GetMapping("/getTypeName")
	public String getTypeName(@RequestParam("reportType") int reportType) {

		try {

			return datacrowdservice.getTypeName(reportType);

		} catch (Exception ex) {
			ex.printStackTrace();
			return "Fail";
		}
	}

	@GetMapping("/getgraphtools")
	public List getbuttonname() {
		try {
			System.out.printf("Graphtool button name:", datacrowdservice.getbuttonname());
			return datacrowdservice.getbuttonname();

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;

		}
	}

	@GetMapping("/getmoretools")
	public List getmoretools() {
		try {
			System.out.print("LOADING THE MORE TOOLS BUTTON>>>>>>>> ");

			return datacrowdservice.getmoretools();

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;

		}

	}

	@GetMapping("/getmaptypes")
	public List getmaptypes() {
		try {
			System.out.print("LOADING THE MAP LAYER TYPE >>>>>>>> ");
			return datacrowdservice.getmaptypes();

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;

		}

	}

	@GetMapping("/getmaptypesOffline")
	public List getmaptypesOffline() {
		try {
			System.out.print("LOADING THE MAP LAYER TYPE >>>>>>>> ");
			return datacrowdservice.getmaptypesOffline();

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;

		}

	}

	@PostMapping("/getShapeType")
	public String getShapeType(@RequestParam("reportJsonParamId") int reportJsonParamId) {
		try {
			System.out.print("THE USER GET SHAPE WITH TYPE");

			return datacrowdservice.getShapeType(reportJsonParamId);

		} catch (Exception ex) {
			ex.printStackTrace();
			return "Fail";
		}
	}

	@GetMapping("/getSimulationParam/{id}")
	public Object getSimulationParam(@PathVariable("id") int id) {
		System.out.println("id >>> " + id);
		List<ObjectNode> records = ObjectToJsonRepository.getJson(entityManagerR,
				"select g.simulationId as REPORTID ," + "g.simulationName as REPORTNAME ,"
						+ "  g.simulationType as REPORTTYPE" + "   from loc_report_config g "
						+ "	  WHERE g.simulationId = " + id);
		try {

			return records.get(0);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Fail";
		}

	}

	@PostMapping("/getAllData")
	public CustomResponse getAllData(@RequestBody String object) throws JSONException {
		CustomResponse resp = CustomResponse.builder().build();

		try {

			System.out.println("Simulation  Object  >> " + object);
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			System.out.println(" CALL Cassandra API>>>>>>>>>>>>>>>>>>>>>>>>  >> ");

			String uri = cassandraIp + "/api/geAllData";
			HttpHeaders header = new HttpHeaders();
			ResponseEntity<Object> result = restTemplate.postForEntity(uri, object, Object.class);
			System.out.println(" Cassandra API (getAllData) result >> " + result);

			resp.setStatus("Success");

		} catch (Exception ex) {

			ex.printStackTrace();
			resp.setStatus("Fail");

		}

		return resp;

	}

	@PostMapping("/getSimulationDataSpark")
	public String getSimulationDataSpark(@RequestBody String object) throws JSONException {
		CustomResponse resp = CustomResponse.builder().build();
		JSONObject simulationObject = new JSONObject(object);
		SimulationDto simulationDto = new SimulationDto();
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		byte[] records;
		String results = "";

		String reportTypeSelected = "";
		String reportType = "";
		String providerTypeP = "";

		String dataType = "";
		try {
			reportTypeSelected = simulationObject.getString("reportTypeId");

			dataType = simulationObject.getString("dataType");

			System.out.println("Simulation  Object >>>> " + simulationObject);

			System.out.println("simulationId" + simulationObject.getString("simulationId"));

			// insert simulation object
//			CustomResponse resp1 = datacrowdservice.inserJsonParam(object);
//			long simulationId   =resp1.getId();

			CustomResponse resp1 = datacrowdservice.inserJsonParam2(object);

			long simulationId = Integer.valueOf(simulationObject.getString("simulationId"));
			System.out.println(" simulationId" + simulationId);

			// get providers
			if (dataType.equals("")) {
				System.out.println("dataType1" + dataType);
				System.out.println(" dataType all 111111>>>>>>>>>>>>>>>>>>>>>>>> >> " + dataType);

				int[] listOfProvider = ServiceProviderService.getallproviderType();

				System.out.println(" dataType all 22222222222222>>>>>>>>>>>>>>>>>>>>>>>> >> " + dataType);

				dataType = seperatedByComma(listOfProvider);

				System.out.println(" dataType all >>>>>>>>>>>>>>>>>>>>>>>> >> " + dataType);

				int[] providerType = ServiceProviderService.getproviderType(dataType);
				providerTypeP = seperatedByComma(providerType);
				System.out.println("providerTypeP1" + providerTypeP);

			} else {
				System.out.println("dataType2" + dataType);

				System.out.println("zzzzzzz simulationId" + simulationId);

				int[] providerType = ServiceProviderService.getproviderType(dataType);
				providerTypeP = seperatedByComma(providerType);
				System.out.println("providerTypeP2" + providerTypeP);

				simulationObject.remove("dataType");
				simulationObject.put("dataType", dataType);

			}

//            if (reportTypeSelected.equals("6")) {
//                //dht		
//            	System.out.println("innn >> "+simulationObject);
//
//            	//simulationObject.remove("reportType");
//    			simulationObject.put("reportType","1");
//            	//simulationObject.remove("reportTypeId");
//    			simulationObject.put("reportTypeId","1");
//            	System.out.println("out >> "+simulationObject);
//
//              }
//
//              if (reportTypeSelected.equals("7")) {
//                //POI
//              //	simulationObject.remove("reportType");
//      			simulationObject.put("reportType","2");
//              	//simulationObject.remove("reportTypeId");
//      			simulationObject.put("reportTypeId","2");
//              }

			reportType = simulationObject.getString("reportTypeId");

			simulationObject.put("provider", providerTypeP);
			simulationObject.put("simulationId", String.valueOf(simulationId));

			// insert data simulation
			simulationDto.setSimulationId(simulationId);
			simulationDto.setSimulationName(simulationObject.getString("reportName"));
			simulationDto.setSimulationType(simulationObject.getString("reportTypeId"));
			simulationDto.setFilterbdate(simulationObject.getString("DateTimeTo"));
			simulationDto.setFilteredate(simulationObject.getString("DateTimeFrom"));
			simulationDto.setFilterstd("1");
			simulationDto.setExecutionDate(formatter.format(date));
			simulationDto.setExecutedBy(simulationObject.getString("userCode"));
			simulationDto.setFilterdevicesId(simulationObject.getString("Devices"));
			simulationDto.setCreationDate(formatter.format(date));
			simulationDto.setCreatedBy(Integer.parseInt(simulationObject.getString("userCode").trim()));
			simulationDto.setFilteredate(simulationObject.getString("DateTimeFrom"));

//			String dateconverted = convertDateRangeToJson(simulationObject.getString("DateTimeFrom"),simulationObject.getString("DateTimeTo"));
//			JSONArray jsonArray = new JSONArray(dateconverted);
//
//			System.out.println(" jsonArray  " + jsonArray);

			datacrowdservice.insertSimulation(simulationDto);
			System.out.println("simulation final object >> " + simulationObject);

			// String result = callCassandraApi(simulationObject.toString());
			results = callSparkconstructquery(simulationObject.toString());

//			if(result.equals("Success")){
//			    loadFile((int) simulationId, "simulation", reportType, simulationObject.getJSONArray("Coordinates").length());
//			    
//				 if (reportTypeSelected.equals("6")) {
//		                //dht
//		            	//simulationObject.remove("reportType");
//		    			simulationObject.put("reportType","2");
//		            	//simulationObject.remove("reportTypeId");
//		    			simulationObject.put("reportTypeId","2");
//		    			String devices=getdevices((int)simulationId);
//
//		    			simulationObject.put("Devices",devices);
//		    			System.out.println("devices >> "+devices);
//
//		    			System.out.println("simulation object >> "+simulationObject);
//
//		    			 result=callCassandraApi(simulationObject.toString());
//						 loadFile((int) simulationId, "simulation", reportType, simulationObject.getJSONArray("Coordinates").length());
//			    			System.out.println("outtt dht  >> ");
//
//				  }
//				 if(reportTypeSelected.equals("7")) {
//						loadFile((int) simulationId, "simulation", reportType, simulationObject.getJSONArray("Coordinates").length());
//						deleteCassandraFiles(String.valueOf(simulationId));
//						ServiceProviderService.callprocedure((int) simulationId, simulationObject.getString("userCode"));//DataSimulation
//						ServiceProviderService.DataSimulationpoi((int) simulationId, simulationObject.getString("userCode"),simulationObject.getString("Devices"),simulationObject.getInt("meter"),simulationObject.getInt("RecipientUser"),simulationObject.getInt("EDGEHEIGHT"));
//						records = datacrowdservice.getSimulationpoi((int) simulationId, simulationObject.getString("userCode"),simulationObject.getString("Devices"),simulationObject.getInt("meter"),simulationObject.getInt("RecipientUser"),simulationObject.getInt("EDGEHEIGHT"));
//						}else {
//			    deleteCassandraFiles(String.valueOf(simulationId));
//				ServiceProviderService.callprocedure((int) simulationId, simulationObject.getString("userCode"));//DataSimulation
//				ServiceProviderService.DataSimulation((int) simulationId, simulationObject.getString("userCode"));
//				records = datacrowdservice.getSimulation((int) simulationId, simulationObject.getString("userCode"));
//						}
//			}
//			else
//			{
//				records = result.getBytes();
//				
//			}

		} catch (Exception ex) {

			ex.printStackTrace();
			records = "Fail".getBytes();

		}

		return results;

	}

	public String convertDateRangeToJson(String startDate, String endDate) {
		Gson gson = new Gson();

		System.out.println(" inn convertDateRangeToJson");

		String[] startDateParts = startDate.split("/");
		int startMonth = Integer.parseInt(startDateParts[0]);
		int startDay = Integer.parseInt(startDateParts[1]);
		String startYear = startDateParts[2];

		String[] startTimeParts = startDateParts[2].split(" ");
		String[] startTimeParts1 = startTimeParts[1].split(":");

		String startHour = startTimeParts1[0];
		startYear = startTimeParts[0];

		String[] endDateParts = endDate.split("/");
		int endMonth = Integer.parseInt(endDateParts[0]);
		int endDay = Integer.parseInt(endDateParts[1]);
		String endYear = endDateParts[2];

		String[] endTimeParts = endDateParts[2].split(" ");
		String[] endTimeParts1 = endTimeParts[1].split(":");

		String endHour = endTimeParts1[0];
		endYear = endTimeParts[0];

		List<Map<String, Object>> dateList = new ArrayList<>();

		for (int year = Integer.parseInt(startYear); year <= Integer.parseInt(endYear); year++) {

			Map<String, Object> yearMap = new HashMap<>();
			yearMap.put("year", year);
			int startDayOfMonth = 0;
			int endDayOfMonth = 0;
			int startHourOfDay = 0;
			int endHourOfDay = 0;
			int month = 0;

			List<Integer> monthList = new ArrayList<>();
			Set<Integer> daySet = new HashSet<>(); // Use a Set to eliminate duplicate days
			Set<Integer> hourSet = new HashSet<>(); // Use a Set to eliminate duplicate hours
			int startMonthOfYear = (year == Integer.parseInt(startYear)) ? startMonth : 1;
			int endMonthOfYear = (year == Integer.parseInt(endYear)) ? endMonth : 12;

			for (month = startMonthOfYear; month <= endMonthOfYear; month++) {
				monthList.add(month);

				startDayOfMonth = (year == Integer.parseInt(startYear) && month == startMonth) ? startDay : 1;
				endDayOfMonth = (year == Integer.parseInt(endYear) && month == endMonth) ? endDay
						: getDaysInMonth(month, year);
			}
			month--;

			for (int day = startDayOfMonth; day <= endDayOfMonth; day++) {
				daySet.add(day);
				startHourOfDay = (year == Integer.parseInt(startYear) && month == startMonth && day == startDay)
						? Integer.parseInt(startHour)
						: 0;
				endHourOfDay = (year == Integer.parseInt(endYear) && month == endMonth && day == endDay)
						? Integer.parseInt(endHour)
						: 23;
			}

			for (int hour = startHourOfDay; hour <= endHourOfDay; hour++) {
				hourSet.add(hour);

			}

			yearMap.put("month", monthList);
			yearMap.put("day", new ArrayList<>(daySet)); // Convert the Set to a List
			yearMap.put("hour", new ArrayList<>(hourSet)); // Convert the Set to a List
			dateList.add(yearMap);

		}

		System.out.println("date converted   " + gson.toJson(dateList));
		System.out.println(" out convertDateRangeToJson");

		return gson.toJson(dateList);
	}

	public int getDaysInMonth(int month, int year) {
		int daysInMonth;

		if (month == 2) {
			if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
				daysInMonth = 29;
			} else {
				daysInMonth = 28;
			}
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			daysInMonth = 30;
		} else {
			daysInMonth = 31;
		}

		return daysInMonth;
	}

	public List<String> jsonArrayToList(String jsonArray) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		List<String> list = mapper.readValue(jsonArray, new TypeReference<List<String>>() {
		});
		return list;
	}

	@PostMapping("/getSimulationData")
	public byte[] getSimulationData(@RequestBody String object) throws JSONException {

		long start = System.nanoTime();

		CustomResponse resp = CustomResponse.builder().build();
		JSONObject simulationObject = new JSONObject(object);

		SimulationDto simulationDto = new SimulationDto();
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		byte[] records = null;

		String reportTypeSelected = "";
		String reportType = "";
		String providerTypeP = "";

		String dataType = "";
		try {
			reportTypeSelected = simulationObject.getString("reportTypeId");

			dataType = simulationObject.getString("dataType");

			System.out.println("Simulation  Object >>>> " + simulationObject);

			System.out.println("simulationId" + simulationObject.getString("simulationId"));
			System.out.println("coordinate>>>" + simulationObject.getJSONArray("Coordinates"));
//			System.out.println("countryCode>>>"+simulationObject.getJSONArray("countryCode"));
//			System.out.println("countryCode>>>"+simulationObject.getJSONArray("countryCode"));

//			jsonArrayToList(simulationObject.getJSONArray("countryCode").toString());
//			System.out.println("jsonArrayToList>>>"+jsonArrayToList(simulationObject.getJSONArray("countryCode").toString()));	
//			getcountry(jsonArrayToList(simulationObject.getJSONArray("countryCode").toString()));
//			System.out.println("getcountry>>>"+getcountry(jsonArrayToList(simulationObject.getJSONArray("countryCode").toString())).toString());	

			CustomResponse resp1 = datacrowdservice.inserJsonParam2(object);

			long simulationId = Integer.valueOf(simulationObject.getString("simulationId"));
			System.out.println(" simulationId" + simulationId);

			// get providers
			if (dataType.equals("")) {

//				if(reportTypeSelected.equals("11")) {
//					
//					System.out.println(" dataType is null ");
//					simulationObject.put("dataType",2);
//					System.out.println("simulationObject = "+ simulationObject);
//
//				}else {
				System.out.println("dataType1" + dataType);

				int[] listOfProvider = ServiceProviderService.getallproviderType();
				dataType = seperatedByComma(listOfProvider);

				System.out.println(" data >>>>>>>>>>>>>>>>>>>>" + dataType);
				int[] providerType = ServiceProviderService.getproviderType(dataType);
				providerTypeP = seperatedByComma(providerType);
				System.out.println("providerTypeP1" + providerTypeP);

				simulationObject.remove("dataType");
				simulationObject.put("dataType", dataType);
//				}

			} else {
				System.out.println("dataType2" + dataType);

				System.out.println("zzzzzzz simulationId" + simulationId);

				int[] providerType = ServiceProviderService.getproviderType(dataType);
				providerTypeP = seperatedByComma(providerType);
				System.out.println("providerTypeP2" + providerTypeP);

				simulationObject.remove("dataType");
				simulationObject.put("dataType", dataType);

			}

			if (reportTypeSelected.equals("6")) {
				// dht
				System.out.println("innn >> " + simulationObject);

				// simulationObject.remove("reportType");
				simulationObject.put("reportType", "1");
				// simulationObject.remove("reportTypeId");
				simulationObject.put("reportTypeId", "1");
				System.out.println("out >> " + simulationObject);

			}

			if (reportTypeSelected.equals("12")) {
				// dct
				System.out.println("innn >> 12  " + simulationObject);

//            	//simulationObject.remove("reportType");
//    			simulationObject.put("reportType","1");
//            	//simulationObject.remove("reportTypeId");
//    			simulationObject.put("reportTypeId","1");
				System.out.println("out >> 12 " + simulationObject);

			}

			if (reportTypeSelected.equals("7")) {
				// POI
				// simulationObject.remove("reportType");
				simulationObject.put("reportType", "2");
				// simulationObject.remove("reportTypeId");
				simulationObject.put("reportTypeId", "2");
			}

			if (reportTypeSelected.equals("10")) {
				// dht
				System.out.println("innn >> " + simulationObject);

				// simulationObject.remove("reportType");
				simulationObject.put("reportType", "1");
				// simulationObject.remove("reportTypeId");
				simulationObject.put("reportTypeId", "1");
				System.out.println("out >> " + simulationObject);

			}

			reportType = simulationObject.getString("reportTypeId");

			if (reportTypeSelected.equals("11")) {
				simulationObject.put("provider", "2");
			} else {
				simulationObject.put("provider", providerTypeP);
			}

			simulationObject.put("simulationId", String.valueOf(simulationId));

			// insert data simulation
			simulationDto.setSimulationId(simulationId);
			simulationDto.setSimulationName(simulationObject.getString("reportName"));
			simulationDto.setSimulationType(simulationObject.getString("reportTypeId"));
			simulationDto.setFilterbdate(simulationObject.getString("DateTimeTo"));
			simulationDto.setFilteredate(simulationObject.getString("DateTimeFrom"));
			simulationDto.setFilterstd("1");
			simulationDto.setExecutionDate(formatter.format(date));
			simulationDto.setExecutedBy(simulationObject.getString("userCode"));
			simulationDto.setFilterdevicesId(simulationObject.getString("Devices"));
			simulationDto.setCreationDate(formatter.format(date));
			simulationDto.setCreatedBy(Integer.parseInt(simulationObject.getString("userCode").trim()));
			simulationDto.setFilteredate(simulationObject.getString("DateTimeFrom"));
			datacrowdservice.insertSimulation(simulationDto);
			System.out.println("in>>>>>>");
			datacrowdservice.deletelast_Simulation(Integer.parseInt(simulationObject.getString("userCode").trim()));

			datacrowdservice.insertlast_Simulation(simulationDto);

			String dateconverted = convertDateRangeToJson(simulationObject.getString("DateTimeFrom"),
					simulationObject.getString("DateTimeTo"));
			JSONArray arrayOfDate = new JSONArray(dateconverted);
//			simulationDto.setArrayOfDate(arrayOfDate);
//			System.out.println(" jsonArray  " + arrayOfDate);
//			
			System.out.println("out>>>>>");
			simulationObject.put("arrayOfDate", arrayOfDate);
			System.out.println("simulation final object >> " + simulationObject);

			byte[] result = callCassandraApi(simulationObject.toString());
			records = result;
//			if (result.equals("Success")) {
//				loadFile((int) simulationId, "simulation", reportType,
//						simulationObject.getJSONArray("Coordinates").length());
//
//				if (reportTypeSelected.equals("6")) {
//					// dhp
//					// simulationObject.remove("reportType");
//					simulationObject.put("reportType", "2");
//					// simulationObject.remove("reportTypeId");
//					simulationObject.put("reportTypeId", "2");
//					String devices = getdevices((int) simulationId);
//
//					simulationObject.put("Devices", devices);
//					System.out.println("devices >> " + devices);
//
//					System.out.println("simulation object >> " + simulationObject);
////		    			//zaher
////		    			simulationObject.put("CountryObject",CountryObject);
//
//					result = callCassandraApi(simulationObject.toString());
//					loadFile((int) simulationId, "simulation", reportType,
//							simulationObject.getJSONArray("Coordinates").length());
//					System.out.println("outtt dht  >> ");
//
//				}
//				if (reportTypeSelected.equals("7")) {
//					loadFile((int) simulationId, "simulation", reportType,
//							simulationObject.getJSONArray("Coordinates").length());
//						deleteCassandraFiles(String.valueOf(simulationId));
//					ServiceProviderService.callprocedure((int) simulationId, simulationObject.getString("userCode"));// DataSimulation
//					ServiceProviderService.DataSimulationpoi((int) simulationId, simulationObject.getString("userCode"),
//							simulationObject.getString("Devices"), simulationObject.getInt("meter"),
//							simulationObject.getInt("RecipientUser"), simulationObject.getInt("EDGEHEIGHT"));
//					records = datacrowdservice.getSimulationpoi((int) simulationId,
//							simulationObject.getString("userCode"), simulationObject.getString("Devices"),
//							simulationObject.getInt("meter"), simulationObject.getInt("RecipientUser"),
//							simulationObject.getInt("EDGEHEIGHT"));
//				} else if (reportTypeSelected.equals("8")) {
//					loadFile((int) simulationId, "simulation", reportType,
//							simulationObject.getJSONArray("Coordinates").length());
//							deleteCassandraFiles(String.valueOf(simulationId));
//					ServiceProviderService.callprocedure((int) simulationId, simulationObject.getString("userCode"));// DataSimulation
//					ServiceProviderService.DataSimulationpoi((int) simulationId, simulationObject.getString("userCode"),
//							simulationObject.getString("Devices"), simulationObject.getInt("meter"),
//							simulationObject.getInt("RecipientUser"), simulationObject.getInt("EDGEHEIGHT"));
//					records = datacrowdservice.getSimulationpoi((int) simulationId,
//							simulationObject.getString("userCode"), simulationObject.getString("Devices"),
//							simulationObject.getInt("meter"), simulationObject.getInt("RecipientUser"),
//							simulationObject.getInt("EDGEHEIGHT"));
//				} else {
//			    deleteCassandraFiles(String.valueOf(simulationId));
//					ServiceProviderService.callprocedure((int) simulationId, simulationObject.getString("userCode"));// DataSimulation
//					ServiceProviderService.DataSimulation((int) simulationId, simulationObject.getString("userCode"));
//					records = datacrowdservice.getSimulation((int) simulationId,
//							simulationObject.getString("userCode"));
//				}
//			} else {
//				records = result;
//			}
//
//			System.out.println(" result cassandra response >>>>>> " + result);
			System.out.println("records " + records);

		} catch (Exception ex) {

			ex.printStackTrace();
			records = "Fail".getBytes();

		}
		long elapsedTime = System.nanoTime() - start;
		System.out.println(" elapsedTime=" + elapsedTime);
		return records;

	}

	@Transactional
	@PostMapping("/insertSimulation")
	public CustomResponse insertSimulation(@RequestBody SimulationDto simulationDto) throws JSONException {
		CustomResponse resp = CustomResponse.builder().build();

		try {

			datacrowdservice.insertSimulation(simulationDto);

		} catch (Exception ex) {

			ex.printStackTrace();
			resp.setStatus("Fail");

		}

		return resp;

	}

	@Transactional
	@PostMapping("/insertlast_Simulation")
	public CustomResponse insertlast_Simulation(@RequestBody SimulationDto simulationDto) throws JSONException {
		CustomResponse resp = CustomResponse.builder().build();

		try {

			datacrowdservice.insertlast_Simulation(simulationDto);

		} catch (Exception ex) {

			ex.printStackTrace();
			resp.setStatus("Fail");

		}

		return resp;

	}

	@GetMapping("/getSimulationpoi/{simulationId}/{Usercode}")
	public byte[] getSimulationpoi(@PathVariable int simulationId, @PathVariable String Usercode,
			@PathVariable String devices, @PathVariable int meter, @PathVariable int recipientuser,
			@PathVariable int edgeheight) throws JSONException {

		return datacrowdservice.getSimulationpoi((int) simulationId, Usercode, devices, meter, recipientuser,
				edgeheight);

	}

	@PostMapping("/getcountry/")
	public List<Object> getcountry(@RequestBody List<String> jsonArray) throws JSONException {
		System.out.println("getcountry jsonArray" + jsonArray);
		return datacrowdservice.getcountry(jsonArray);

	}

	@PostMapping("/getcountry2/")
	public List<Object> getcountry2(@RequestBody List<String> countryIds) throws JSONException {
		System.out.println("countryIds" + countryIds);
		return datacrowdservice.getcountry2(countryIds);

	}

	@GetMapping("/getALLcountryIDS/")
	public List<Object> getALLcountryIDS() throws JSONException {
		return datacrowdservice.getALLcountryIDS();

	}

	@GetMapping("/getSimulation/{simulationId}/{Usercode}")
	public byte[] getSimulation(@PathVariable int simulationId, @PathVariable String Usercode) throws JSONException {
		byte[] records;

		try {

			records = datacrowdservice.getSimulation((int) simulationId, Usercode);
			ServiceProviderService.callprocedure((int) simulationId, Usercode); // DataSimulation

		} catch (Exception ex) {

			ex.printStackTrace();
			records = "Fail".getBytes();

		}
		return records;

	}

	@PostMapping("/getSimulation2/")
	public byte[] getSimulation2(@RequestBody String object) throws JSONException {
		byte[] records;
		JSONObject simulationObject = new JSONObject(object);
		SimulationDto simulationDto = new SimulationDto();
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

		try {
			int simulationId = Integer.valueOf(simulationObject.getString("simulationId"));

			simulationDto.setSimulationId(simulationId);
			simulationDto.setSimulationName(simulationObject.getString("reportName"));
			simulationDto.setSimulationType(simulationObject.getString("reportTypeId"));
			simulationDto.setFilterbdate(simulationObject.getString("DateTimeTo"));
			simulationDto.setFilteredate(simulationObject.getString("DateTimeFrom"));
			simulationDto.setFilterstd("1");
			simulationDto.setExecutionDate(formatter.format(date));
			simulationDto.setExecutedBy(simulationObject.getString("userCode"));
			simulationDto.setFilterdevicesId(simulationObject.getString("Devices"));
			simulationDto.setCreationDate(formatter.format(date));
			simulationDto.setCreatedBy(Integer.parseInt(simulationObject.getString("userCode").trim()));
			simulationDto.setFilteredate(simulationObject.getString("DateTimeFrom"));
			String usercode = simulationObject.getString("userCode");
			datacrowdservice.insertSimulation(simulationDto);

			records = datacrowdservice.getSimulation((int) simulationId, usercode);

		} catch (Exception ex) {

			ex.printStackTrace();
			records = "Fail".getBytes();

		}
		return records;

	}

	@GetMapping("/getExecutionParam/{simulationId}")
	public byte[] getExecutionParam(@PathVariable int simulationId) throws JSONException {
		byte[] records;

		try {

			records = datacrowdservice.getExecutionParam((int) simulationId);

		} catch (Exception ex) {

			ex.printStackTrace();
			records = "Fail".getBytes();

		}
		return records;

	}

	@PostMapping("/getSelectedShape/{simulationId}")
	public byte[] getSelectedShape(@PathVariable int simulationId) throws JSONException {
		byte[] records;

		try {
			System.out.printf(" Get selected shape where simulationId " + simulationId);
			records = datacrowdservice.getSelectedShape(simulationId);

		} catch (Exception ex) {

			ex.printStackTrace();
			records = "Fail".getBytes();

		}
		return records;
	}

	@PostMapping("/getfixedelementsObject")
	public List<Object> getfixedelementsObject(@RequestBody List<Object> simulationId) throws JSONException {
		try {
			System.out.printf("get fixed elements Objects");

			return datacrowdservice.getfixedelementsObject(simulationId);

		} catch (Exception ex) {
			ex.printStackTrace();
//			 List<Object> result = new ArrayList<>();
//		        result.add("Fail");
//		        return result;
			return null;
		}

	}

	@PostMapping("/getfixedelementsObject2")
	public List<Object> getfixedelementsObject2(@RequestBody List<String> simulationId) throws JSONException {
		try {
			System.out.printf("get fixed elements Objects display all");

			datacrowdservice.insertIDS(simulationId);

			return datacrowdservice.getfixedelementsObject2();

		} catch (Exception ex) {
			ex.printStackTrace();
//			 List<Object> result = new ArrayList<>();
//		        result.add("Fail");
//		        return result;
			return null;
		}

	}

	@PostMapping("/getfixedelementsObject2Bts/{userId}")
	public String getfixedelementsObject2BTS(@RequestBody String BTSObject, @PathVariable("userId") Integer userId)
			throws JSONException {
		try {
			JSONObject btsObject = new JSONObject(BTSObject);

			// Extract the "ids" array
			JSONArray idsArray = btsObject.getJSONArray("ids");

			// Convert the JSONArray to a List<String>
			List<String> idsList = new ArrayList<>();
			for (int i = 0; i < idsArray.length(); i++) {
				idsList.add(idsArray.getString(i));
			}

			String sqlCond = btsObject.getString("sqlcond");

			// Now, idsList contains the "ids" values
			System.out.println("idsList: " + idsList);
			System.out.println("sqlCond: " + sqlCond);

			return datacrowdservice.getfixedelementsObject2Bts(idsList, userId, sqlCond);

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;

		}

	}

	@GetMapping("/displaySimulation/{queryId}/{userId}")
	public void DisplaySimulation(@PathVariable("queryId") Integer queryId, @PathVariable("userId") String simulation) {
		try {
			datacrowdservice.DisplaySimulation(queryId, simulation);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

//	@CrossOrigin(origins = "https://10.1.8.112:4200")
	@GetMapping("/getShapelimit")
	public List<Object> getShapelimit() {
		try {
			System.out.printf("Get shape limit for each layer");
			return datacrowdservice.getShapelimit();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;

		}

	}

	public String seperatedByComma(int[] Integers) {
		String data = "";
		try {
			for (int i = 0; i < Integers.length; i++) {
				if (i == 0) {
					data = "" + Integers[i];
				} else {
					data = data + "," + Integers[i];
				}

			}
			return data;
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Fail";
		}

	}

	public String seperatedByCommaString(String[] String) {
		String data = "";
		for (int i = 0; i < String.length; i++) {
			if (i == 0) {
				data = "" + String[i];
			} else {
				data = data + "," + String[i];
			}

		}
		return data;

	}

	public byte[] callCassandraApi(String object) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			System.out.println(" CALL Cassandra API>>>>>>>>>>>>>>>>>>>>>>>>  >> ");

//			String uri = cassandraIp + "/api/geAllData";
//		    String uri = "http://10.1.8.136:8888/api/geAllData";
//			String uri = "http://10.1.8.29:8888/api/geAllData";
			String uri = "http://10.1.8.40:8092/api/constructquery";

			System.out.println(" uri  " + uri);
			System.out.println(" object   " + object);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<>(object, headers);

			String result = restTemplate.postForObject(uri, entity, String.class);
			System.out.println(" Cassandra API (getAllData) result >> " + result);
			return result.getBytes();

//
//			JSONObject statusObj = new JSONObject(result);
//			return statusObj.getString("status");

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;

		}

	}

	public String callSparkconstructquery(String object) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			System.out.println(" CALL Cassandra API>>>>>>>>>>>>>>>>>>>>>>>>  >> ");

			String uri = "http://10.1.8.112:8089" + "/api/constructquery";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<>(object, headers);

			String result = restTemplate.postForObject(uri, entity, String.class);
			System.out.println(" Cassandra API (getAllData) result >> " + result);

//			JSONObject statusObj = new JSONObject(result) ;
			return result;

		} catch (Exception ex) {
			ex.printStackTrace();
			return "Fail";

		}

	}

	@PostMapping("/distanceAPI")
	public String distanceAPI(@RequestBody String object) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			System.out.println(" distanceAPI>>>>>>>>>>>>>>>>>>>>>>>>  >> ");

			String uri = "http://10.1.8.53:8880/aoi";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<>(object, headers);

			String result = restTemplate.postForObject(uri, entity, String.class);
			System.out.println(" distanceAPI result >> " + result);

			JSONObject statusObj = new JSONObject(result);
			return statusObj.getString("status");

		} catch (Exception ex) {
			ex.printStackTrace();
			return "Fail";

		}

	}

	public String deleteCassandraFiles(String id) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			System.out.println(" delete file in progress >>>>>>>>>>>>>>>>>>>>>>>>  >> ");

			String uri = cassandraIp + "/api/deleteCassandraFile/" + id;

			String result = restTemplate.getForObject(uri, String.class);
			System.out.println(" files have been deleted >> " + result);

			return result;

		} catch (Exception ex) {
			ex.printStackTrace();
			return "Fail";

		}

	}

	public String loadFile(Integer simulationId, String description, String reportType, Integer cnt) {
		try {

			if (!reportType.equals("3")) {
				ServiceProviderService.DHloadfile(simulationId, description);
			} else {
				ServiceProviderService.DTPloadfile(simulationId, cnt);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return "";

	}

	@GetMapping("/getgeoresult/{queryId}")
	public String getgeoresult(@PathVariable int queryId) throws JSONException {
		try {
			return datacrowdservice.getgeoresult(queryId);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Fail";
		}

	}

	public String getdevices(int queryId) throws JSONException {
		@SuppressWarnings("unchecked")
		String listString = datacrowdservice.getdevices(queryId).stream().map(Object::toString)
				.collect(Collectors.joining(",")).toString();
		return listString;
	}

	@GetMapping("/updateQueryName/{queryId}/{QueryName}")
	public boolean updateQueryName(@PathVariable String QueryName, @PathVariable int queryId) throws JSONException {
		boolean checkreportname = datacrowdservice.checkreportname(queryId, QueryName);

		try {
			if (checkreportname == false) {
				datacrowdservice.updatequeryname(queryId, QueryName);
				System.out.println("  Has saved a new simualtion with name " + QueryName);
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return checkreportname;
	}

	@PostMapping("/SaveShape")
	public CustomResponse SaveShape(@RequestBody String Obj) throws JSONException {

		try {
			System.out.printf("Has saved a shape with object: " + Obj);
			CustomResponse resp = datacrowdservice.SaveShape(Obj);
			return resp;

		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return null;

	}

	@GetMapping("/getdirection/{v_method_log}/{devices}")
	public byte[] getdirection(@PathVariable("v_method_log") Integer v_method_log,
			@PathVariable("devices") String devices) {
		return datacrowdservice.getdirection(v_method_log, devices);

	}

	@GetMapping("/getdirectionByTime/{v_method_log}/{devices}")
	public byte[] getdirectionByTime(@PathVariable("v_method_log") Integer v_method_log,
			@PathVariable("devices") String devices) {
		return datacrowdservice.getdirectionByTime(v_method_log, devices);

	}

	@PostMapping("/executeAOIActivity/{queryId}")
	public String executeAOIActivity(@PathVariable("queryId") String queryId) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			JSONObject object = new JSONObject();
			object.put("id", queryId);
			object.put("devices", "");
			String uri = "http://10.1.8.53:8880/aoi";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			HttpEntity<String> entity = new HttpEntity<>(object.toString(), headers);

			System.out.println(" executeAOIActivity result >> " + queryId);

			String result = restTemplate.postForObject(uri, entity, String.class);

			return result;

		} catch (Exception ex) {
			ex.printStackTrace();
			return "Fail";

		}
	}

	@PostMapping("/getcountrycode")
	public String getcountrycode(@RequestBody String Obj) {
		try {

			System.out.println("Obj >> " + Obj);

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

			String uri = "http://10.1.8.112:1212/reverse_geocode/";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<>(Obj, headers);

			String result = restTemplate.postForObject(uri, entity, String.class);
			System.out.println("getcountrycode result >> " + result);

			return result;

		} catch (Exception ex) {
			ex.printStackTrace();
			return "Fail";

		}
	}

	@PostMapping("/openAOIResults/{queryId}")
	public String openAOIResults(@PathVariable("queryId") String queryId) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			System.out.println("openAOIResults>>>>>>>>>>>>>>>>>>>>>>>>>> ");
			JSONObject object = new JSONObject();
			object.put("id", queryId);
			object.put("devices", "");
			String uri = "http://10.1.8.53:8880/aoi";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			System.out.println(" object >> " + object.toString());
			HttpEntity<String> entity = new HttpEntity<>(object.toString(), headers);

			System.out.println(" openAOIResults result >> " + queryId);

			String result = restTemplate.postForObject(uri, entity, String.class);

			return result;

		} catch (Exception ex) {
			ex.printStackTrace();
			return "Fail";

		}
	}

	@GetMapping("/getLastSimualtionID/{createdBy}")
	public Object getLastSimualtionID(@PathVariable int createdBy) {

		return datacrowdservice.getLastSimualtionID(createdBy);

	}

	@GetMapping("/deleteLastSimualtionID/{createdBy}")
	public Object deleteLastSimualtionID(@PathVariable int createdBy) {

		return datacrowdservice.deletelast_Simulation(createdBy);

	}

	@PostMapping("/scanfixedelements")
	public void scanfixedelements(@RequestBody String object) throws JSONException {
		JSONObject simulationObject = new JSONObject(object);
		String reportTypeSelected = simulationObject.getString("reportTypeId");
		SimulationDto simulationDto = new SimulationDto();
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

		try {
			System.out.print("scanfixedelements");
			if (reportTypeSelected.equals("9")) {

				datacrowdservice.inserJsonParam2(object);

				simulationObject.put("reportType", "1");
				simulationObject.put("reportTypeId", "1");

				getSimulationData(simulationObject.toString());
			}
			if (reportTypeSelected.equals("8")) {
				long simulationId = Integer.valueOf(simulationObject.getString("simulationId"));

				// insert data simulation
				simulationDto.setSimulationId(simulationId);
				simulationDto.setSimulationName(simulationObject.getString("reportName"));
				simulationDto.setSimulationType(simulationObject.getString("reportTypeId"));
				simulationDto.setFilterbdate(simulationObject.getString("DateTimeTo"));
				simulationDto.setFilteredate(simulationObject.getString("DateTimeFrom"));
				simulationDto.setFilterstd("1");
				simulationDto.setExecutionDate(formatter.format(date));
				simulationDto.setExecutedBy(simulationObject.getString("userCode"));
				simulationDto.setFilterdevicesId(simulationObject.getString("Devices"));
				simulationDto.setCreationDate(formatter.format(date));
				simulationDto.setCreatedBy(Integer.parseInt(simulationObject.getString("userCode").trim()));
				simulationDto.setFilteredate(simulationObject.getString("DateTimeFrom"));

				System.out.print("before>>>>>>>");
				datacrowdservice.insertSimulation(simulationDto);
				System.out.print("after insertSimulation");
				datacrowdservice.inserJsonParam2(object);

			}

			int simulationId = Integer.valueOf(simulationObject.getString("simulationId"));
			System.out.println(" simulationId" + simulationId);
			datacrowdservice.scanfixedelements(simulationId);
			datacrowdservice.P_VCIS_RETAIL_RECORDS(simulationId);

		} catch (Exception ex) {
			ex.printStackTrace();
//			 List<Object> result = new ArrayList<>();
//		        result.add("Fail");
//		        return result;
		}
	}

	@GetMapping("/getVcisfixedelementsID/{simulationId}")
	public List<Object> getVcisfixedelementsID(@PathVariable("simulationId") Long simulationId) {

		try {
			return datacrowdservice.getVcisfixedelementsID(simulationId);
		} catch (Exception ex)

		{
			ex.printStackTrace();
			return null;
		}
	}

	@PostMapping("/addFixedelements/")
	public String addFixedelements(@RequestBody String Object) {
		System.out.println("Object" + Object);
		// You can validate and process the search request here
		// For demonstration purposes, let's just forward the parameters to the service
		return datacrowdservice.addFixedelements(Object);
	}

	@GetMapping("/typefixedelementsID/")
	public List<Object> gettypefixedelements() {

		try {
			return datacrowdservice.getFixedElementsTye();
		} catch (Exception ex)

		{
			ex.printStackTrace();
			return null;
		}
	}

	@GetMapping("/getSimulationTypes")
	public List getSimulationTypes() {

		try {

			return datacrowdservice.getSimulationTypes();

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}