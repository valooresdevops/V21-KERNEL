package com.valoores.datacrowd.common.service.impl;



import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valoores.datacrowd.common.dto.ServiceProviderDto;
import com.valoores.datacrowd.common.repository.ServiceProviderRepository;
//import com.valoores.datacrowd.common.repository.fillDataFilteringRepository;
import com.valoores.datacrowd.common.service.ServiceProviderService;

@Service
public class ServiceProviderServiceImpl implements ServiceProviderService {

@Autowired 
private ServiceProviderRepository ServiceProviderRepository;

//@Autowired
//private fillDataFilteringRepository fillDataFilteringRepository;

@PersistenceContext
private EntityManager entityManager;

	@Override
	public int[] getallproviderType() {
		
		return ServiceProviderRepository.getallproviders();
	}


	@Override
	public int[] getproviderType(String  providers) {
		// TODO Auto-generated method stub
		
		String[] dataTypes = providers.split(",");
		
		List<Long> allIds = new ArrayList<>();
		for (int i = 0; i < dataTypes.length; i++) {
			allIds.add(Long.valueOf(dataTypes[i]));
		}
		
		
		return ServiceProviderRepository.getproviderType(allIds);
	}


	@Override
	public void callprocedure(Integer ReportJsonParamId, String userId) {
	    StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("locdba.PCK_CYB_ENGINE.P_FILL_DATA_FILTERING")//, P_FILL_DATA_FILTERING.class
	    .registerStoredProcedureParameter("REPORT_JSON_PARAM_ID", Integer.class, ParameterMode.IN)
		.registerStoredProcedureParameter("USERID", String.class, ParameterMode.IN);
	    storedProcedure.setParameter("REPORT_JSON_PARAM_ID", ReportJsonParamId);
	    storedProcedure.setParameter("USERID", userId);
	    storedProcedure.execute();
	    }


	@Override
	public void DTPloadfile(Integer queryId,Integer cnt) {

		  StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("locdba.PCK_DATACROWD_ENGINE.P_CYB_DTP_LOAD_FILE")//, locdba.PCK_CYB_ENGINE.P_CYB_LOAD_FILE.class
				  .registerStoredProcedureParameter("PARAM_ID", Integer.class, ParameterMode.IN)
		    		.registerStoredProcedureParameter("SHAPE_CNT", Integer.class, ParameterMode.IN);
		            storedProcedure.setParameter("SHAPE_CNT", cnt);
		            storedProcedure.setParameter("PARAM_ID", queryId);
				    storedProcedure.execute();
	}
	
	@Override
	public void DataSimulationpoi(Integer id, String userId, String devices, Integer meter, Integer meterto, Integer timelimit) {

		
		
		System.out.println(" meter to "+meterto);
		System.out.println(" timelimit to "+timelimit);
		System.out.println(" timelimit to "+id);
		
		
		  StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("SSDX_ENG.P_CYB_GET_SIMULATION_DATA_POI")//, locdba.PCK_CYB_ENGINE.P_CYB_LOAD_FILE.class
				  .registerStoredProcedureParameter("REPORT_JSON_PARAM_ID", Integer.class, ParameterMode.IN)
		    		.registerStoredProcedureParameter("USERID", String.class, ParameterMode.IN)
		    		.registerStoredProcedureParameter("DEVICES", String.class, ParameterMode.IN)
		    		.registerStoredProcedureParameter("METER", Integer.class, ParameterMode.IN)
		    		.registerStoredProcedureParameter("METERTO", Integer.class, ParameterMode.IN)
		    		.registerStoredProcedureParameter("TIMELIMIT", Integer.class, ParameterMode.IN);
		            storedProcedure.setParameter("REPORT_JSON_PARAM_ID", id);
		            storedProcedure.setParameter("USERID", userId);
		            storedProcedure.setParameter("DEVICES", devices);
		            storedProcedure.setParameter("METER", meter);
		            storedProcedure.setParameter("METERTO", meterto);
		            storedProcedure.setParameter("TIMELIMIT", timelimit);
				    storedProcedure.execute();
	}
	
	@Override
	public void DataSimulation(Integer id,String userId) {

		  StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("locdba.PCK_DATACROWD_ENGINE.P_CYB_GET_SIMULATION_DATA")//, locdba.PCK_CYB_ENGINE.P_CYB_LOAD_FILE.class
				  .registerStoredProcedureParameter("REPORT_JSON_PARAM_ID", Integer.class, ParameterMode.IN)
		    		.registerStoredProcedureParameter("USERID", String.class, ParameterMode.IN);
		            storedProcedure.setParameter("REPORT_JSON_PARAM_ID", id);
		            storedProcedure.setParameter("USERID", userId);
				    storedProcedure.execute();
	}


	@Override
	public void DHloadfile(Integer queryId,String simulation) {System.out.println("queryId in DHloadfile>>> "+queryId);
		 StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("locdba.PCK_DATACROWD_ENGINE.P_CYB_LOAD_FILE")//, locdba.PCK_CYB_ENGINE.P_CYB_LOAD_FILE.class
				    .registerStoredProcedureParameter("PARAM_ID", Integer.class, ParameterMode.IN)
		    		.registerStoredProcedureParameter("DESCRIPTION", String.class, ParameterMode.IN);
				    storedProcedure.setParameter("DESCRIPTION",simulation);
				    storedProcedure.setParameter("PARAM_ID", queryId);
				    storedProcedure.execute();
		
	}


	
	@Override
	public void executemapexplore(Integer ReportJsonParamId, String userId) {
		
	    StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("locdba.PCK_CYB_ENGINE.P_EXECUTE_MAP_EXPLORE")//, P_EXECUTE_MAP_EXPLORE.class
	    .registerStoredProcedureParameter("REPORT_JSON_PARAM_ID", Integer.class, ParameterMode.IN)
		.registerStoredProcedureParameter("USERID", String.class, ParameterMode.IN);
	    storedProcedure.setParameter("REPORT_JSON_PARAM_ID", ReportJsonParamId);
	    storedProcedure.setParameter("USERID", userId);
	    storedProcedure.execute();
	    
	    }
	
	@Override
	public String loadCalledNoFile(Integer queryId) {
	 StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery(" LOCDBA.P_CYB_GET_CALLEDNO")//, locdba.P_CYB_GET_CALLEDNO.class
			    .registerStoredProcedureParameter("PARAM_ID", Integer.class, ParameterMode.IN)
	    		.registerStoredProcedureParameter("RES", String.class, ParameterMode.OUT);
			    storedProcedure.setParameter("PARAM_ID", queryId);
			    storedProcedure.execute();
			    
		        String result = storedProcedure.getOutputParameterValue("RES").toString();
		     return result;
	
}

	
	@Override
	public void loadKnowledgeGraph(String queryId,Integer linkType,String storeName,String itemName) {
		
		
		String description = "";
		
		if (!storeName.equals("") && !itemName.equals("")) {

			description = "and h.tech_sto_id in( " + storeName +")" + " and h.itm_id in( " + itemName +")";
		} else if (storeName.equals("") && !itemName.equals("")) {

			description = "and  h.itm_id in( " + itemName +")";
		} else if (!storeName.equals("") && itemName.equals("")) {

			description = "and h.tech_sto_id in( " + storeName +")";
		}
		
		
		
	 StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery(" LOCDBA.PCK_DATACROWD_GRAPH_ENGINE.P_CYB_GRAPH_LOAD_FILE")//, locdba.P_CYB_GET_CALLEDNO.class
			    .registerStoredProcedureParameter("PARAM_ID", Integer.class, ParameterMode.IN)
			    .registerStoredProcedureParameter("LINKTYPE", String.class, ParameterMode.IN)
	            .registerStoredProcedureParameter("OTHER_PARAM_ID", String.class, ParameterMode.IN);

			    storedProcedure.setParameter("PARAM_ID", Integer.parseInt(queryId));
			    storedProcedure.setParameter("LINKTYPE", String.valueOf(linkType));
			    storedProcedure.setParameter("OTHER_PARAM_ID", description);

			    
			    storedProcedure.execute();
	
}
	
	@Override
	public void loadGraphEngine(String devices,long fromDatemillis,long toDatemillis,String queryId,Integer linkType,String layer,String callingNo,String storeName,String itemName) {
//		System.out.println("linkType  loadGraphEngine>> "+ linkType);
		
		
		try {
			
			 StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("LOCDBA.P_CYB_EXECUTE_KNOWLEDGE_GRAPH_ENGINE_LAYERS")//, locdba.P_CYB_GET_CALLEDNO.class
					    .registerStoredProcedureParameter("DEVICES", String.class, ParameterMode.IN)
					    .registerStoredProcedureParameter("FROMDATEMILLIS", Long.class, ParameterMode.IN)
					    .registerStoredProcedureParameter("TODATEMILLIS", Long.class, ParameterMode.IN)
					    .registerStoredProcedureParameter("QUERYID", Integer.class, ParameterMode.IN)
					    .registerStoredProcedureParameter("LINKTYPE", Integer.class, ParameterMode.IN)
					    .registerStoredProcedureParameter("LAYERS", Integer.class, ParameterMode.IN)
					    .registerStoredProcedureParameter("callingNo", String.class, ParameterMode.IN);
//			            .registerStoredProcedureParameter("storeName", String.class, ParameterMode.IN)
//			            .registerStoredProcedureParameter("itemName", String.class, ParameterMode.IN);


					    storedProcedure.setParameter("DEVICES", devices);
					    storedProcedure.setParameter("FROMDATEMILLIS", fromDatemillis);
					    storedProcedure.setParameter("TODATEMILLIS",toDatemillis);
					    storedProcedure.setParameter("QUERYID", Integer.parseInt(queryId));
					    storedProcedure.setParameter("LINKTYPE", linkType);
					    storedProcedure.setParameter("LAYERS", Integer.parseInt(layer));
					    storedProcedure.setParameter("callingNo", callingNo);
//					    storedProcedure.setParameter("storeName", storeName);
//					    storedProcedure.setParameter("itemName", itemName);

					    
					    storedProcedure.execute();
		}catch (Exception e) {

			e.printStackTrace();
		}
	
	
}


//	@Override
//	public void getdirection(Integer v_method_log, String devices) {
//	
//	    StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("locdba.pck_datacrowd_engine.P_CYB_CALCULATE_DISTANCE")
//	    .registerStoredProcedureParameter("V_METHOD_LOG", Integer.class, ParameterMode.IN)
//		.registerStoredProcedureParameter("DEVICES", String.class, ParameterMode.IN);
//	    storedProcedure.setParameter("V_METHOD_LOG", v_method_log);
//	    storedProcedure.setParameter("DEVICES", devices);
//	    storedProcedure.execute();
//	    
//		
//	}


	private void registerStoredProcedureParameter(String string, Class<String> class1, ParameterMode in) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void executeImportDataEngine(Integer ReportJsonParamId, String userId) {
		// TODO Auto-generated method stub
		
	    StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("locdba.PCK_CYB_ENGINE.P_EXECUTE_IMPORT_DATA_ENGINE")//, P_EXECUTE_MAP_EXPLORE.class
	    .registerStoredProcedureParameter("REPORT_JSON_PARAM_ID", Integer.class, ParameterMode.IN)
		.registerStoredProcedureParameter("USERID", String.class, ParameterMode.IN);
	    storedProcedure.setParameter("REPORT_JSON_PARAM_ID", ReportJsonParamId);
	    storedProcedure.setParameter("USERID", userId);
	    storedProcedure.execute();
	    
	}

	
	
}