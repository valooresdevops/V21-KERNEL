package com.valoores.datacrowd.app.service.impl;




import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.valoores.datacrowd.app.dto.SimulationDto;
import com.valoores.datacrowd.app.model.LOC_LOCATION_MAP_OBJECT_SHAPE;
import com.valoores.datacrowd.app.model.LOC_TELCO_BTS_CELL_DEF;
import com.valoores.datacrowd.app.model.WebApi;
import com.valoores.datacrowd.app.model.loc_report_config;
import com.valoores.datacrowd.app.model.techLast_simul;
import com.valoores.datacrowd.app.model.tmp_ins_ds;
import com.valoores.datacrowd.app.repository.AngularButtonRepository;
import com.valoores.datacrowd.app.repository.DatabaseTypeRepository;
import com.valoores.datacrowd.app.repository.FixedElementsRepository;
import com.valoores.datacrowd.app.repository.KnowledgeGraphRepository;
import com.valoores.datacrowd.app.repository.LOC_PAR_LOCATION_MAPRepository;
import com.valoores.datacrowd.app.repository.LastSimulationRepository;
import com.valoores.datacrowd.app.repository.MapTypesRepository;
import com.valoores.datacrowd.app.repository.ObjectShapeRepository;
import com.valoores.datacrowd.app.repository.RefSysLineRepository;
import com.valoores.datacrowd.app.repository.SimulationRepository;
import com.valoores.datacrowd.app.repository.TechReortParamsRepository;
import com.valoores.datacrowd.app.repository.TmpReportCoordinatesRepository;
import com.valoores.datacrowd.app.repository.WebApiMethodRepository;
import com.valoores.datacrowd.app.repository.refsysRepository;
import com.valoores.datacrowd.app.repository.techgeoresultRepository;
import com.valoores.datacrowd.app.repository.tmp_ins_dsRepository;
import com.valoores.datacrowd.app.service.DataCrowdService;
import com.valoores.datacrowd.backend.CustomResponse;
import com.valoores.datacrowd.common.service.ServiceProviderService;





@Service
public class DataCrowdImpl implements DataCrowdService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private  WebApiMethodRepository WebApiMethodRepository;
	
	@Autowired
	private  techgeoresultRepository techgeoresultRepository;

	@Autowired
	private  ObjectShapeRepository objectShapeRepository;
	
	@Autowired
	private DatabaseTypeRepository DatabaseTypeRepository;
	
	
	@Autowired
	private FixedElementsRepository FixedElementsRepository;
	
	@Autowired
	private ServiceProviderService serviceProviderService;
	
	@Autowired
	private SimulationRepository simulationRepository;

	@Autowired
	private refsysRepository refsysRepository;
	
	@Autowired
	private  AngularButtonRepository angularButtonRepository;
	
	@Autowired
	private  MapTypesRepository maptypesrepository;
	
	@Autowired
	private  KnowledgeGraphRepository knowledgeGraphRepository;
	
	@Autowired
	private  RefSysLineRepository refSysLineRepository;
	
	@SuppressWarnings("unused")
	@Autowired
	private  TechReortParamsRepository TechReortParamsRepository;

	@SuppressWarnings("unused")
	@Autowired
	private  TmpReportCoordinatesRepository TmpReportCoordinatesRepository;
	
	@SuppressWarnings("unused")
	@Autowired
	private  SimulationRepository SimulationRepository;

	@Autowired
	private  LastSimulationRepository LastSimulationRepository;
	
	
	@Autowired
	private  tmp_ins_dsRepository tmp_ins_dsRepository;
	
	
	@Autowired
	private  LOC_PAR_LOCATION_MAPRepository LOC_PAR_LOCATION_MAPRepository;
    @Autowired
	private JdbcTemplate jdbcTemplate;

    
	@Override
	public String getdatabaseType() {
		return DatabaseTypeRepository.getdatabasetype();
	}


	@Override
	public CustomResponse inserJsonParam(String executionParam) {
		
		CustomResponse resp = CustomResponse.builder().build();
		
		try {
		
		WebApi webapi = new WebApi();
		
		Date date = new Date();
		webapi.setExecutionParam(executionParam);
		webapi.setMethodId(1);
		webapi.setExcutionBdate(date);
		
		 WebApiMethodRepository.save(webapi);
		 resp.setId(webapi.getMethodlogId());
		 
		 System.out.println(" response === simulation ID == "+resp);
		resp.setCode("0");
		resp.setStatus("Success");
		 
		}catch(Exception ex)
		{
			resp.setStatus("fail");
			ex.printStackTrace();
		}
		 
		return resp;
		
	}


	@Override
	public String getTypeName(int reportType) {
		return refsysRepository.getTypeName(reportType);
	}


	@Override
	public String getShapeType(int reportJsonParamId) {
		return WebApiMethodRepository.getShapeType(reportJsonParamId);
	}


	@Transactional
	public CustomResponse insertSimulation(SimulationDto simulationDto) {
		
		CustomResponse resp = CustomResponse.builder().build();
		
		try {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

		loc_report_config simulationModel = new loc_report_config();
		
		simulationModel.setSimulationId( Long.valueOf(simulationDto.getSimulationId()));
		simulationModel.setSimulationName(simulationDto.getSimulationName());
		simulationModel.setSimulationType(simulationDto.getSimulationType());
		simulationModel.setFilterbdate(dateFormat.parse(simulationDto.getFilterbdate()));
		simulationModel.setFilteredate(dateFormat.parse(simulationDto.getFilteredate()));
		simulationModel.setFilterdevicesId(simulationDto.getFilterdevicesId());
		simulationModel.setFilterstd(simulationDto.getFilterstd());
		simulationModel.setExecutionDate(dateFormat.parse(simulationDto.getExecutionDate()));
		simulationModel.setExecutedBy(simulationDto.getExecutedBy());
		simulationModel.setCreatedBy(simulationDto.getCreatedBy());
		simulationModel.setCreationDate(dateFormat.parse(simulationDto.getCreationDate()));
		
		 simulationRepository.save(simulationModel);
		 resp.setStatus("Success");
		 resp.setCode(String.valueOf(simulationModel.getSimulationId()));
		 
		 
		}catch(Exception es) {
			
			es.printStackTrace();
			 
		}
		 return resp;
		 
	}


	@Override
	public void DisplaySimulation(Integer queryId, String userId) {

	    StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("locdba.PCK_CYB_ENGINE.P_EXECUTE_IMPORT_DATA_ENGINE")//, P_EXECUTE_MAP_EXPLORE.class
	    .registerStoredProcedureParameter("REPORT_JSON_PARAM_ID", Integer.class, ParameterMode.IN)
		.registerStoredProcedureParameter("USERID", String.class, ParameterMode.IN);
	    storedProcedure.setParameter("REPORT_JSON_PARAM_ID", queryId);
	    storedProcedure.setParameter("USERID", userId);
	    storedProcedure.execute();
		
	}

	@Override
	public byte[]  getSimulationpoi(int simulationId,String usercode,String devices,int meter,int recipientuser,int edgeheight) {

		
		System.out.println(" >>>> getsimulation 333333333331");
		serviceProviderService.DataSimulationpoi(simulationId,"34",devices,meter,recipientuser,edgeheight);

		byte[]  result = entityManager.createQuery("select  t.excutionResult from WebApi t where t.methodlogId = "+simulationId).getSingleResult().toString().getBytes();
		
		
		System.out.println(" >>>> getsimulation 444444444444444444422222222222222222222");
		

		return result;
	   
	}
	
	@Override
	public byte[]  getSimulation(int simulationId,String usercode) {

		
		
		System.out.println(" >>>> getsimulation 11111111111111111111");
		serviceProviderService.DataSimulation(simulationId,"34");

		byte[]  result = entityManager.createQuery("select  t.excutionResult from WebApi t where t.methodlogId = "+simulationId).getSingleResult().toString().getBytes();
		
		
		System.out.println(" >>>> getsimulation 222222222222222222222");
		

		return result;
	   
	}
	
//	@Override
//	public byte[]  getSimulationTest(int simulationId,String usercode) {
//
//		
//		
//		System.out.println(" >>>> getsimulation 11111111111111111111");
//		serviceProviderService.DataSimulation(simulationId,"34");
//
//		byte[]  result = entityManager.createQuery("select  t.excutionResult from WebApi t where t.methodlogId = "+simulationId).getSingleResult().toString().getBytes();
//		
//		
//		System.out.println(" >>>> getsimulation 222222222222222222222");
//		
//
//		return result;
//	   
//	}
	
	@Override
	public byte[] getExecutionParam(int simulationId) {
byte[]  result = entityManager.createQuery("select  t.executionParam from WebApi t where t.methodlogId = "+simulationId).getSingleResult().toString().getBytes(); 
		
		return result;
	   
	}

	
	@Override
	public byte[]  getSelectedShape(int  simulationId) {

		
//		byte[]  result = entityManager.createQuery("select  t.objectShapeValue from LOC_LOCATION_MAP_OBJECT_SHAPE t where t.shapeid = "+simulationId).getSingleResult().toString().getBytes(); 
		byte[] result = objectShapeRepository.getSelectedShape(simulationId);
		return result;
	   
	}
	
	
//	
//	@Override
//	public   ResponseEntity<byte[]>  getSelectedShape(int simulationId) {
//
//		
////		byte[]  result = entityManager.createQuery("select  t.objectShapeValue from LOC_LOCATION_MAP_OBJECT_SHAPE t where t.shapeid = "+simulationId).getSingleResult().toString().getBytes(); 
//		Query query = entityManager.createQuery("select  t.objectShapeValue from LOC_LOCATION_MAP_OBJECT_SHAPE t where t.shapeid = :simulationId");
//		query.setParameter("simulationId", simulationId);
//		LOC_LOCATION_MAP_OBJECT_SHAPE LOC_LOCATION_MAP_OBJECT_SHAPE = (LOC_LOCATION_MAP_OBJECT_SHAPE) query.getSingleResult();
//		byte[] executionParam = LOC_LOCATION_MAP_OBJECT_SHAPE.getObjectShapeValue();
//		
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//		headers.setContentLength(executionParam.length);
//		
//		return  new ResponseEntity<>(executionParam, headers, HttpStatus.OK);
//	   
//	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getbuttonname() {
		
		return angularButtonRepository.getButtonName();	
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getmoretools() {
		
		return angularButtonRepository.getMoreTools();	
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getmaptypes() {
		
		return maptypesrepository.getmaptypes();	
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getmaptypesOffline() {
		
		return maptypesrepository.getmaptypesOffline();	
	}
 



	@Override
	public CustomResponse inserJsonParam2(String executionParam) {
CustomResponse resp = CustomResponse.builder().build();
		

String simulationid = "";
		try {
			JSONObject simulationObject = new JSONObject(executionParam);
			
		System.out.println("simulationObject>>"+simulationObject);
		
		WebApi webapi = new WebApi();
		simulationid=simulationObject.getString("simulationId");
		System.out.println("simulationid>>>>>"+simulationid);
		Date date = new Date();
	webapi.setExecutionParam(executionParam);
	webapi.setMethodId(1);
	webapi.setExcutionBdate(date);
	webapi.setMethodId(1);
	webapi.setMethodlogId(Integer.valueOf(simulationid));
	System.out.println("webapi>>"+webapi.getExecutionParam());

	System.out.println("in>>>>>>>>>>>>>");

	    WebApiMethodRepository.save(webapi);
	    
		System.out.println("out>>>>>>>>>>>>>");

		resp.setId(Integer.valueOf(simulationid));
		 
		System.out.println(" response === simulation ID == "+resp);
		resp.setCode("0");
		resp.setStatus("Success");
		 
		}catch(Exception ex)
		{
			resp.setStatus("Success");
			ex.printStackTrace();
		}
		 
		return resp;
	}


	@Override
	public String getgeoresult(int queryId) {
		return techgeoresultRepository.getgeoresult(queryId);
	}


	@Override
	public boolean checkreportname(int queryId,String QueryName) {
		return knowledgeGraphRepository.existsBySimulationIdAndSimulationName(queryId,QueryName);
		
//		String result =  entityManager.createQuery("select count(p) = 1  from loc_report_config t where t.simulationName = "+QueryName+" and t.simulationId ="+queryId).getSingleResult().toString();
//		
//		return result;
	}


	@Override
	public CustomResponse updatequeryname(int queryId, String QueryName) {

CustomResponse resp = CustomResponse.builder().build();
		

		try {
			System.out.println(" update query , id = "+queryId);
		    StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("locdba.PCK_CYB_ENGINE.P_ACTIVITY_SCAN_REPORT_TYPE")//, P_EXECUTE_MAP_EXPLORE.class
		    	    .registerStoredProcedureParameter("REPORT_JSON_PARAM_ID", Integer.class, ParameterMode.IN)
		    		.registerStoredProcedureParameter("USERID", String.class, ParameterMode.IN);
		    	    storedProcedure.setParameter("REPORT_JSON_PARAM_ID", queryId);
		    	    storedProcedure.setParameter("USERID", -6);
		    	    storedProcedure.execute();
					System.out.println(" execute procedure ,done");

			loc_report_config	locreport=new	loc_report_config();
			
			locreport.setSimulationName(QueryName);
			locreport.setSimulationId(queryId);
			knowledgeGraphRepository.save(locreport);
			System.out.println(" update query ,done");

		 
		resp.setCode("0");
		resp.setStatus("Success");
		 
		}catch(Exception ex)
		{
			resp.setStatus("Fail");
			ex.printStackTrace();
		}
		 
		return resp;
		
	}


	@Override
	public List<Object>  getfixedelementsObject(List<Object>  simulationId) {
		System.out.print("in>>>>");
		return FixedElementsRepository.getfixedelementsObject(simulationId);
//		Object result = entityManager.createQuery("SELECT  B.shapeid, B.name, E.type, B.lng, B. lat FROM LOC_TELCO_BTS_CELL_DEF B, INV_EQUIPMENT E WHERE E.equipmentid = B.equimentid AND E.equipmentTypeId IN (3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13) AND  B.shapeid="+simulationId);
//		System.out.print("out>>>>"+result);
//		System.out.print(result);
//		return result;
	
	}

	
	@Override
	public List<Object>  getfixedelementsObject2() {
        System.out.print("in>>>>");

    return FixedElementsRepository.getfixedelementsObject2();

//        Object result = entityManager.createQuery("SELECT B.shapeid, B.name, E.type, B.lng, B. lat FROM LOC_TELCO_BTS_CELL_DEF B, INV_EQUIPMENT E WHERE E.equipmentid = B.equimentid AND E.equipmentTypeId IN (3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13) AND  B.shapeid="+simulationId);
//        System.out.print("out>>>>"+result);
//        System.out.print(result);
//        return result; 
	}
	
	
		
		
	
	@Override
	public String  getfixedelementsObject2Bts(List<String> BTSCELLID,Integer USERID, String sqlCond) {
		System.out.print("in>>>>");

	    StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("locdba.PCK_DATACROWD_ENGINE.P_DISPLAY_BTS")
	    		.registerStoredProcedureParameter("SQLCOND", String.class, ParameterMode.IN)
		.registerStoredProcedureParameter("USERID", Integer.class, ParameterMode.IN);
	    storedProcedure.setParameter("SQLCOND", sqlCond);
	    storedProcedure.setParameter("USERID", USERID);
	    storedProcedure.execute();
	    
	    String tableName = "SSDX_TMP.TMP_DISPLAY_FIX_ELEMENTS_" + USERID;
        String sql = "SELECT JSON FROM " + tableName;
        
        @SuppressWarnings("unused")
		String result = entityManager.createNativeQuery(sql).getSingleResult().toString();

        return jdbcTemplate.queryForObject(sql, new RowMapper<String>() {
	     	
        	@Override
        	public String mapRow(@SuppressWarnings("null") ResultSet rs, int rowNum) throws SQLException {
        	    Clob clob = rs.getClob("JSON");
        	    System.out.println("JSON clob >> " + clob);
        	    if (clob != null) {
        	        String json = clob.getSubString(1, (int) clob.length());
        	        if (json.length() == 0) {
        	            return null;
        	        } else {
        	            return json;
        	        }
        	    } else {
        	        return "noData";
        	    }
        	}});
        
//        return jdbcTemplate.queryForObject(sql, new RowMapper<String>() {
//	     	
//        	@Override
//        	public String mapRow(ResultSet rs, int rowNum) throws SQLException {
//        	    Clob clob = rs.getClob("Polyline");
//        	    System.out.println("JSON clob >> " + clob);
//        	    if (clob != null) {
//        	        String json = clob.getSubString(1, (int) clob.length());
//        	        if (json.length() == 0) {
//        	            return null;
//        	        } else {
//        	            return json;
//        	        }
//        	    } else {
//        	        return "noData";
//        	    }
//        	}
//        	      	
//        }).getBytes();
//        System.out.println("result >> "+result);
		
//	return result;
	
	}

	@Override
	public CustomResponse SaveShape(String obj) {	
		CustomResponse resp = CustomResponse.builder().build();
		
		
		try {
//		

			JSONObject simulationObject = new JSONObject(obj);
			System.out.println("simulationObject>>>"+simulationObject);
			String shapeName=simulationObject.getString("Name");
			System.out.println("shapeName>>>"+shapeName);
			String Type=simulationObject.getString("Type");
			System.out.println("Type>>>"+Type);
//		 int Usercode=simulationObject.getInt("Usercode");
//			System.out.println("Usercode>>>"+Usercode);


	 

	     Integer shapeType=refSysLineRepository.getlinecode(Type);
		 boolean x=objectShapeRepository.existsByshapeName(shapeName);
			System.out.println("shapeType>>>"+shapeType);

		 
		 if(x==true) {
				System.out.println("Shape Name Already Exists");
				
			

		 }else {
			 byte[] shapeobj = obj.getBytes();
			 LOC_LOCATION_MAP_OBJECT_SHAPE objectshape  = new LOC_LOCATION_MAP_OBJECT_SHAPE();
			// objectshape.setShapeid(reportJsonParamId);
			 objectshape.setShapeName(shapeName);
			 objectshape.setObject_shape_type(shapeType);
			 objectshape.setObjectShapeValue(shapeobj);
			 // objectshape.setCreatedby(Usercode);
			 objectshape.setMap_id(1);
			 objectShapeRepository.save(objectshape);
			 
			 resp.setCode("0");
				resp.setStatus("Success");
				resp.setId(objectshape.getShapeid());
		 }
		 
	}catch(Exception ex)
	{
		resp.setStatus("fail");
		ex.printStackTrace();
	}
		 
		 return resp;
	}


	@Override
	public List<Object> getShapelimit() {
		return LOC_PAR_LOCATION_MAPRepository.getShapelimit();
	}

	

	@SuppressWarnings("rawtypes")
	@Override
	public List getdevices(int queryId) {
		
		try {
			
	        String tableName = " ssdx_eng.tmp_report_coordinates_6_" + queryId;
	        String sql = "SELECT distinct DEVICE_ID FROM " + tableName;
	        List data= jdbcTemplate.queryForList(sql,String.class);
	        
	        @SuppressWarnings("unused")
			List<String> devices = new ArrayList<>();
	        @SuppressWarnings("unused")
			JSONArray jsonArray = new JSONArray(data);
//	        
//	        for (int i = 0; i < jsonArray.length(); i++) {
//	            JSONObject jsonObject = jsonArray.getJSONObject(i);
//	            String deviceId = jsonObject.getString("DEVICE_ID");
//	            if (!devices.contains(deviceId)) {
//	                devices.add(deviceId);
//
//	            }
//	        }
        	    return data;

	     
			}catch (Exception e) {
				e.printStackTrace();
			}        
	        return null   ;
	    }
	







	@SuppressWarnings("null")
	@Override
	public  byte[]  getdirection(Integer v_method_log, String devices) {
	
	    StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("locdba.pck_datacrowd_engine.P_CYB_CALCULATE_DISTANCE")
	    .registerStoredProcedureParameter("V_METHOD_LOG", Integer.class, ParameterMode.IN)
		.registerStoredProcedureParameter("DEVICES", String.class, ParameterMode.IN);
	    storedProcedure.setParameter("V_METHOD_LOG", v_method_log);
	    storedProcedure.setParameter("DEVICES", devices);
	    storedProcedure.execute();
	    
	    String tableName = "ssdx_tmp.TECH_geo_result_" + v_method_log;
        String sql = "SELECT Polyline FROM " + tableName;
        return jdbcTemplate.queryForObject(sql, new RowMapper<String>() {
	     	
        	@Override
        	public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        	    Clob clob = rs.getClob("Polyline");
        	    System.out.println("JSON clob >> " + clob);
        	    if (clob != null) {
        	        String json = clob.getSubString(1, (int) clob.length());
        	        if (json.length() == 0) {
        	            return null;
        	        } else {
        	            return json;
        	        }
        	    } else {
        	        return "noData";
        	    }
        	}
        	      	
        }).getBytes();
        
	    
		
	}


	@Transactional
	public CustomResponse insertlast_Simulation(SimulationDto simulationDto) {
		
		CustomResponse resp = CustomResponse.builder().build();
		
		try {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        techLast_simul techLast_simul = new techLast_simul();
		
        techLast_simul.setSimulid(Long.valueOf(simulationDto.getSimulationId()));
        techLast_simul.setName(simulationDto.getSimulationName());
        techLast_simul.setUpdateDate(dateFormat.parse(simulationDto.getExecutionDate()));
        //techLast_simul.setUpdatedBy(Integer.valueOf(simulationDto.getExecutedBy()));
		techLast_simul.setCreatedBy(simulationDto.getCreatedBy());
		techLast_simul.setCreationDate(dateFormat.parse(simulationDto.getCreationDate()));
		
		 LastSimulationRepository.save(techLast_simul);
		 resp.setStatus("Success");
		 resp.setCode(String.valueOf(techLast_simul.getSimulid()));
		 
		 
		}catch(Exception es) {
			
			es.printStackTrace();
			 
		}
		 return resp;
		 
	}

	

	
	
    @Transactional
	public void insertIDS(List<String> numbers) {

    	tmp_ins_dsRepository.deleteIDS();
    	List<tmp_ins_ds> entities = numbers.stream()
    	    .map(Integer::parseInt)
    	    .map(num -> {
    	        tmp_ins_ds entity = new tmp_ins_ds();
    	        entity.setIdsss(num);
    	        return entity;
    	    })
    	    .collect(Collectors.toList());


    	tmp_ins_dsRepository.saveAll(entities);

	}
	
	

	@Override
	public CustomResponse deletelast_Simulation(int createdBy) {
	CustomResponse resp = CustomResponse.builder().build();
		
		try {
      
	        @SuppressWarnings("unused")
			techLast_simul techLast_simul = new techLast_simul();

		 LastSimulationRepository.deletelastSimualtion(createdBy);
		 resp.setStatus("deleted Successfuly");
		 
		 
		}catch(Exception es) {
			
			es.printStackTrace();
			 
		}
		 return resp;	}


	@Override
	public Object getLastSimualtionID(int createdBy) {
		return LastSimulationRepository.getLastSimualtionID(createdBy);
		 
	}

	@Override
	public void scanfixedelements( int simulationId) {
		
		  StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery(" LOCDBA.P_VCIS_FIXED_ELEMENTS_ACTIVITY_SCAN")//, P_FILL_DATA_FILTERING.class
				    .registerStoredProcedureParameter("v_method_log", Integer.class, ParameterMode.IN);
				    storedProcedure.setParameter("v_method_log", simulationId);
				    storedProcedure.execute();
		
		
   }
	
	@Override
	public void P_VCIS_RETAIL_RECORDS(int ReportJsonParamId) {
		  StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("locdba.PCK_DATACROWD_ENGINE.P_VCIS_RETAIL_RECORDS")//, P_FILL_DATA_FILTERING.class
				    .registerStoredProcedureParameter("REPORT_JSON_PARAM_ID", Integer.class, ParameterMode.IN);
				    storedProcedure.setParameter("REPORT_JSON_PARAM_ID", ReportJsonParamId);
				    storedProcedure.execute();
	}
 

	@Override
	public List<Object> getVcisfixedelementsID(Long simulationId) {
	
		System.out.println("getVcisfixedelementsID>>>>>>>>>"+simulationId); 

		System.out.println("simulationId  "+simulationId); 
		
        String tableName = "TECHDBA.TECH_VCIS_RETAIL_RECORD_" +simulationId;
          String sql = "SELECT BTS_CELL_ID,BTS_CELL_NAME,LOCATION_LONGITUDE,LOCATION_LATITUDE" + 
          		" FROM " + tableName;
          return jdbcTemplate.query(sql, new RowMapper<Object>() {
              @SuppressWarnings({ "null", "unused" })
			@Override
              public List<Object> mapRow(ResultSet rs, int rowNum) throws SQLException {

                     String BTS_CELL_ID = rs.getString("BTS_CELL_ID");
                     double LOCATION_LONGITUDE = rs.getDouble("LOCATION_LONGITUDE");
                     double LOCATION_LATITUDE = rs.getDouble("LOCATION_LATITUDE");
                     String BTS_CELL_NAME = rs.getString("BTS_CELL_NAME");
                     
                      
                  
                     List<Object> result = new ArrayList<>();
                     result.add(BTS_CELL_ID);
                     result.add(LOCATION_LONGITUDE);
                     result.add(LOCATION_LATITUDE);
                     result.add(BTS_CELL_NAME);
                     

                     
                     

           		System.out.println("result  "+result); 

                     if (result != null) {
                         return result;

                     }else {
                          return result;

                     }
              }
          });
	}

	@SuppressWarnings("null")
	@Override
	public  byte[]  getdirectionByTime(Integer v_method_log, String devices) {
	
	    StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("locdba.P_CYB_CALCULATE_TIMEBASED")
	    .registerStoredProcedureParameter("V_METHOD_LOG", Integer.class, ParameterMode.IN)
		.registerStoredProcedureParameter("DEVICES", String.class, ParameterMode.IN);
	    storedProcedure.setParameter("V_METHOD_LOG", v_method_log);
	    storedProcedure.setParameter("DEVICES", devices);
	    storedProcedure.execute();
	    
	    String tableName = "ssdx_tmp.TECH_geo_result_" + v_method_log;
        String sql = "SELECT Polyline FROM " + tableName;
        return jdbcTemplate.queryForObject(sql, new RowMapper<String>() {
	     	
			@Override
        	public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        	    Clob clob = rs.getClob("Polyline");
        	    System.out.println("JSON clob >> " + clob);
        	    if (clob != null) {
        	        String json = clob.getSubString(1, (int) clob.length());
        	        if (json.length() == 0) {
        	            return null;
        	        } else {
        	            return json;
        	        }
        	    } else {
        	        return "noData";
        	    }
        	}
        	      	
        }).getBytes();
        
	    
		
	}


	@Override
	public List<Object>  getcountry(List<String>  countrycode) {
		


		 String nativeQuery ="SELECT A.GCO_INTERNAL_CODE AS subregion,\r\n" + 
		 		"       (SELECT GCO_INTERNAL_CODE\r\n" + 
		 		"          FROM REF_COM_CNTRY_GRP H\r\n" + 
		 		"         WHERE H.GCO_ID = A.GCO_P_ID) AS REGION,\r\n" + 
		 		"       HH.ISO_COU_CODE_NUM AS country\r\n" + 
		 		"  FROM REF_COM_COUNTRY HH, REF_COM_CNTRY_GRP_COUNTRY B, REF_COM_CNTRY_GRP A \r\n" + 
		 		" WHERE HH.ISO_COU_CODE_ALPHA IN (:countrycode) \r\n" + 
		 		"   AND HH.COU_ID = B.COU_ID\r\n" + 
		 		"   AND B.GCO_ID = A.GCO_ID";
		 		
		
		 @SuppressWarnings("unchecked")
		List<Object>  result = (List<Object> ) entityManager
	                .createQuery(nativeQuery)
	                .setParameter("countrycode", countrycode)
//	                .setParameter("countrycode", "%" + countrycode + "%") // Using parameterized query to avoid SQL injection
	                .getResultList();
			System.out.println("result jsonArray" + result);

	        return result;
	}
	
	
	@Override
	public List<Object>  getcountry2(List<String>    countryIds) {
		


		 String nativeQuery ="\r\n" + 
		 		"SELECT A.GCO_INTERNAL_CODE AS subregion,\r\n" + 
		 		"       (SELECT GCO_INTERNAL_CODE\r\n" + 
		 		"          FROM REF_COM_CNTRY_GRP H\r\n" + 
		 		"         WHERE H.GCO_ID = A.GCO_P_ID) AS REGION,\r\n" + 
		 		"       H.ISO_COU_CODE_NUM AS country\r\n" + 
		 		"  FROM REF_COM_COUNTRY H, REF_COM_CNTRY_GRP_COUNTRY B, REF_COM_CNTRY_GRP A\r\n" + 
		 		" WHERE  \r\n" + 
		 		" ISO_COU_CODE_NUM IN (:countryIds) AND\r\n" + 
		 		" H.COU_ID = B.COU_ID\r\n" + 
		 		"   AND B.GCO_ID = A.GCO_ID" + 
		 		"  \r\n" + 
		 		"   \r\n" + 
		 		"   ";
		 		
		
		 @SuppressWarnings("unchecked")
		List<Object>  result = (List<Object> ) entityManager
	                .createQuery(nativeQuery)
	                .setParameter("countryIds", countryIds)
//	                .setParameter("countrycode", "%" + countrycode + "%") // Using parameterized query to avoid SQL injection
	                .getResultList();
	     
	        return result;
	}

	@Override
	public List<Object>  getALLcountryIDS() {
		



		 String nativeQuery ="\r\n" + 
		 		"SELECT  H.ISO_COU_CODE_NUM AS ID\r\n" + 
		 		"  FROM REF_COM_COUNTRY H, REF_COM_CNTRY_GRP_COUNTRY B, REF_COM_CNTRY_GRP A\r\n" + 
		 		" WHERE  \r\n" + 
		 		" H.COU_ID = B.COU_ID\r\n" + 
		 		"   AND B.GCO_ID = A.GCO_ID" + 
		 		"  \r\n" + 
		 		"   \r\n" + 
		 		"   ";
		 @SuppressWarnings("unchecked")
		List<Object>  result = (List<Object> ) entityManager
	                .createQuery(nativeQuery)
//	                .setParameter("countrycode", "%" + countrycode + "%") // Using parameterized query to avoid SQL injection
	                .getResultList();
	     
	        return result;
	}

/////malek
	@Override
	public String addFixedelements(String obj) {
		
		System.out.println("obj addFixedelements"+obj);
		JSONObject obj2=new JSONObject(obj);

		System.out.println("obj addFixedelements"+obj2.getString("Type"));
		System.out.println("obj addFixedelements"+obj2.getString("Name"));
		System.out.println("obj addFixedelements"+obj2.getString("lat"));
		System.out.println("obj addFixedelements"+obj2.getString("lng"));
//	    System.out.println("obj addFixedelements"+obj2.getJSONObject("Locationbts").getString("lat"));

		String lat=	obj2.getString("lat");
		String lng=obj2.getString("lng");
		String Type=obj2.getString("Type");
		String Name=obj2.getString("Name");
		int typee=Integer.parseInt(Type);
		
		
		LOC_TELCO_BTS_CELL_DEF fixedelement  = new LOC_TELCO_BTS_CELL_DEF();
		fixedelement.setLat(lat);
		fixedelement.setLng(lng);
		fixedelement.setName(Name);
		fixedelement.setEquipmentid(typee);
		fixedelement.setCreatedby(-6);
		
		
		System.out.println("getName>>>>>>>>>>>>>"+fixedelement.getName());
		
		System.out.println("getCreatedby>>>>>>>>>>>>>"+fixedelement.getCreatedby());
		System.out.println("getEquipmentid>>>>>>>>>>>>>"+fixedelement.getEquipmentid());
		System.out.println("vgetLat>>>>>>>>>>>>>"+fixedelement.getLat());
		System.out.println("getLng>>>>>>>>>>>>>"+fixedelement.getLng());
		System.out.println("getShapeid>>>>>>>>>>>>>"+fixedelement.getShapeid());
	 

		
		FixedElementsRepository.save(fixedelement);
		

			 
		
		return "success";
	}




	@Override
	public List<Object> getFixedElementsTye() {

		 String nativeQuery ="SELECT equipment_type_id, equipment_type_name FROM inv_equipment_type WHERE equipment_type_id != 3" ;
		 
		 	@SuppressWarnings("unchecked")
			List<Object>  result = (List<Object> ) entityManager.createNativeQuery(nativeQuery).getResultList();
		 System.out.println("result111111111111111111 "+result);
	        return result;
	}


	@SuppressWarnings("rawtypes")
	@Override
	public List getSimulationTypes() {
	return refsysRepository.getSimulationTypes();
	}


	


}