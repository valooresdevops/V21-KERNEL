package com.valoores.datacrowd.common.service;


import org.springframework.stereotype.Service;

@Service
public interface ServiceProviderService {

	public int[]  getallproviderType();
	public int[] getproviderType(String providers);
    public void callprocedure(Integer ReportJsonParamId,String UserId);
    public void DTPloadfile(Integer queryId, Integer cnt);
    public void DHloadfile(Integer queryId, String simulation);
    public void executemapexplore(Integer ReportJsonParamId,String UserId);
    public String loadCalledNoFile(Integer queryId);
    public void loadGraphEngine(String devices,long fromDatemillis,long toDatemillis,String queryId,Integer linkType,String layers,String callingNo,String storeName,String itemName );
    void DataSimulationpoi(Integer id, String userId, String devices, Integer meter, Integer meterto, Integer timelimit);
    void DataSimulation(Integer id, String userId);
	
	// public void getdirection(Integer v_method_log,  String devices);
	 public void executeImportDataEngine(Integer ReportJsonParamId, String userId);
	void loadKnowledgeGraph(String queryId, Integer linkType, String storeName, String itemName);
}
