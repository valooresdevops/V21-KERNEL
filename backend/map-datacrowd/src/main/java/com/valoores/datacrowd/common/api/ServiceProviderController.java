package com.valoores.datacrowd.common.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valoores.datacrowd.common.service.ServiceProviderService;

@RestController
@RequestMapping("/api")
public class ServiceProviderController {
	
	@Autowired
	private ServiceProviderService  ServiceProviderService;

	@PostMapping("/getallproviderType")
	public int[] getallproviderType() {
		return ServiceProviderService.getallproviderType();
	}
	
	@PostMapping("/getproviderType")
	public int[] getproviderType(@RequestBody String  providers){
		
		System.out.println(" providers >>>> "+providers);
		
		return ServiceProviderService.getproviderType(providers);
	
	}
	

	@GetMapping("/PFILLDATAFILTERING/{ReportJsonParamId}/{userId}")
	public void callprocedure(@PathVariable("ReportJsonParamId") Integer ReportJsonParamId, @PathVariable("userId") String userId){
		 ServiceProviderService.callprocedure(ReportJsonParamId, userId);
	
	}
	
	
	@GetMapping("/DTPloadfile/{queryId}/{cnt}")
	public void DTPloadfile(@PathVariable("queryId") Integer queryId, @PathVariable("cnt") Integer cnt){
		 ServiceProviderService.DTPloadfile(queryId, cnt);
	
	}
	
	@GetMapping("/DHloadfile/{queryId}/{simulation}")
	public void DHloadfile(@PathVariable("queryId") Integer queryId,@PathVariable("simulation") String simulation){
		 ServiceProviderService.DHloadfile(queryId,simulation);
	
	}
	
	
	@GetMapping("/executemapexplore2/{ReportJsonParamId}/{userId}")
	public void executemapexplore(@PathVariable("ReportJsonParamId") Integer ReportJsonParamId, @PathVariable("userId") String userId){
		 ServiceProviderService.executemapexplore(ReportJsonParamId, userId);
	
	}
	
	
	@GetMapping("/executeImportDataEngine/{ReportJsonParamId}/{userId}")
	public void executeImportDataEngine(@PathVariable("ReportJsonParamId") Integer ReportJsonParamId, @PathVariable("userId") String userId){
		 ServiceProviderService.executeImportDataEngine(ReportJsonParamId, userId);
	
	}
	@GetMapping("/loadCalledNoFile/{simulationId}")
	public void loadCalledNoFile(@PathVariable("simulationId") Integer simulationId){
		ServiceProviderService.loadCalledNoFile(simulationId);
		
	}
	
	
//	@GetMapping("/getdirection/{v_method_log}/{devices}")
//	public void getdirection(@PathVariable("v_method_log") Integer v_method_log, @PathVariable("devices") String devices){
//		 ServiceProviderService.getdirection(v_method_log, devices);
//	
//	}
	
	
}
