package com.valoores.datacrowd.app.service;



import java.util.List;

import org.springframework.stereotype.Service;

import com.valoores.datacrowd.app.dto.SimulationDto;
import com.valoores.datacrowd.backend.CustomResponse;

@Service
public interface DataCrowdService {
	
	
	public String getdatabaseType();
	
	public CustomResponse inserJsonParam(String executionParam);
	
	public CustomResponse inserJsonParam2(String executionParam);

	public  String getTypeName(int reportType);
	
	public  String getShapeType(int reportJsonParamId);

	public CustomResponse insertSimulation(SimulationDto simulationDto) ;
	
	public CustomResponse insertlast_Simulation(SimulationDto simulationDto) ;
	public CustomResponse deletelast_Simulation(int createdBy) ;


	public void DisplaySimulation(Integer queryId, String simulation);
	
	public byte[]  getSimulationpoi( int simulationId,String Usercode, String devices, int meter, int recipientuser, int edgeheight);
	public byte[]  getSimulation( int simulationId,String Usercode);
	public byte[]  getExecutionParam( int simulationId);

	
	public byte[]  getSelectedShape(int simulationId);

	@SuppressWarnings("rawtypes")
	public  List getbuttonname();

	@SuppressWarnings("rawtypes")
	public List getmoretools() ;

	public String getgeoresult( int queryId) ;

	@SuppressWarnings("rawtypes")
	public List getmaptypes() ;
	
	@SuppressWarnings("rawtypes")
	public List getmaptypesOffline() ;

 
	public boolean checkreportname(int queryId,String QueryName);
	public CustomResponse SaveShape(String obj);

	
	public CustomResponse updatequeryname(int queryId,String QueryName);
	public 	List<Object>   getfixedelementsObject(List<Object>  simulationId);
	public  String   getfixedelementsObject2Bts(List<String> BTSCELLID,Integer USERID, String sqlCond);
	public  	List<Object>   getfixedelementsObject2();

	
	public 	List<Object>  getShapelimit();
	public  byte[]  getdirection(Integer v_method_log,  String devices);
    @SuppressWarnings("rawtypes")
	public List  getdevices(int queryId);
    
    public Object  getLastSimualtionID(int createdBy);

	public List<Object> getVcisfixedelementsID(Long ItemId);

	public void scanfixedelements(int simulationId);
	public void insertIDS(List<String> numbers);
//	public void insertFixedElementIds(byte[] ids);


	public void P_VCIS_RETAIL_RECORDS(int ReportJsonParamId);
	
	public  byte[]  getdirectionByTime(Integer v_method_log,  String devices);

//	byte[] getSimulationTest(int simulationId, String usercode);

	public List<Object>   getcountry (List<String>   jsonArray );
	
	public List<Object>   getcountry2(List<String>  countryIds);
	
	
	public List<Object>   getALLcountryIDS();

	public  String   addFixedelements(String obj);

	public List<Object>   getFixedElementsTye();

	@SuppressWarnings("rawtypes")
	public List getSimulationTypes();


	
}
	