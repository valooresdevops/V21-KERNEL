package com.example.demo.app.reportbuilder.api;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.app.reportbuilder.dto.CustomAPIDto;
import com.example.demo.app.reportbuilder.dto.ReportDto;
import com.example.demo.app.reportbuilder.model.ReportsModel;
import com.example.demo.app.reportbuilder.service.ReportExecutionService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.MapReduceCommand.OutputType;
@SpringBootApplication
@RestController
@RequestMapping("/api")
public class BirtController {

	@Autowired
    private Environment environment;
	
	@Autowired
	ReportExecutionService reportService;
	
	
	@PostMapping("/getReportsData")
	public List<ObjectNode> getReportsData(){
 
		return reportService.getReportsData();

    }
	
	
	@PostMapping("/executeReport/{reportId}")
	@ResponseBody
	public  ResponseEntity<byte[]> executeReport(@PathVariable("reportId") long reportId,HttpServletResponse  response, HttpServletRequest request)
	{

		System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEE"+reportId);
		try 
		{
			OutputType output = OutputType.INLINE;
			ReportsModel report = reportService.getReportById(reportId);
			byte[] reportBytes = reportService.generatReport(report, output, response, request);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "inline; "+report.getREPORT_NAME()+".pdf");
			return  ResponseEntity.ok()
             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + report.getREPORT_NAME() + "\"")
             .body(reportBytes);
		} catch (Exception e) { // Handle exceptions return
								// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); }
		}

		return null;
	}

	

//	@PostMapping("/executeReportwithOneParam/{reportId}/{reportParam}")
//	@ResponseBody
//	public  ResponseEntity<byte[]> executeReportwithOneParam(@PathVariable("reportId") long reportId,@PathVariable("reportParam") String reportParam,HttpServletResponse  response, HttpServletRequest request)
//	{
//
//		System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEE"+reportId);
//		try 
//		{
//			OutputType output = OutputType.INLINE;
//			ReportsModel report = reportService.getReportById(reportId);
//			byte[] reportBytes = reportService.generatReportWithOneParam(report,reportParam, output, response, request);
//			HttpHeaders headers = new HttpHeaders();
//			headers.add("Content-Disposition", "inline; "+report.getREPORT_NAME()+".pdf");
//			return  ResponseEntity.ok()
//             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + report.getREPORT_NAME() + "\"")
//             .body(reportBytes);
//		} catch (Exception e) { // Handle exceptions return
//								// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); }
//		}
//
//		return null;
//	}
	
	
	@PostMapping("/executeReportwithOneParam/{reportId}")
	@ResponseBody
	public  ResponseEntity<byte[]> executeReportwithOneParam(@PathVariable("reportId") long reportId,@RequestBody CustomAPIDto customAPIDto,HttpServletResponse  response, HttpServletRequest request)
	{

		System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEE>>>>>>"+customAPIDto.toString());

			System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEE>>>>>>"+customAPIDto.getColumns().get(0));

		try 
		{
			OutputType output = OutputType.INLINE;
			ReportsModel report = reportService.getReportById(reportId);
			byte[] reportBytes = reportService.generatReportWithOneParam(report,customAPIDto.getColumns().get(0).getColVal(), output, response, request);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "inline; "+report.getREPORT_NAME()+".pdf");
			return  ResponseEntity.ok()
             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + report.getREPORT_NAME() + "\"")
             .body(reportBytes);
		} catch (Exception e) { // Handle exceptions return
								// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); }
		}

		return null;
	}
	
	@PostMapping("/executeReportwithParameters/{reportId}")
	@ResponseBody
	public  ResponseEntity<byte[]> executeReportwithParameters(@PathVariable("reportId") long reportId,@RequestBody CustomAPIDto customAPIDto,HttpServletResponse  response, HttpServletRequest request)
	{

		System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEE>>>>>>"+customAPIDto.toString());

			System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEE>>>>>>"+customAPIDto.getColumns().get(0));

		try 
		{
			OutputType output = OutputType.INLINE;
			ReportsModel report = reportService.getReportById(reportId);
			System.out.println("BEFORE SERVICEEEE");
			byte[] reportBytes = reportService.generatReportWithParameters(report,customAPIDto, output, response, request);
			System.out.println("AFTER SERVICEEEE");
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "inline; "+report.getREPORT_NAME()+".pdf");
			return  ResponseEntity.ok()
             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + report.getREPORT_NAME() + "\"")
             .body(reportBytes);
		} catch (Exception e) { // Handle exceptions return
				e.printStackTrace();				// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); }
		}

		return null;
	}
	
	@PostMapping("/addNewReport")
	@ResponseBody

	public int addNewReport(@RequestBody ReportDto reportDto) throws UnsupportedEncodingException{
		
		@SuppressWarnings("unused")
		String env=environment.getProperty("spring.datasource.url");
		
		System.out.println("DESCCCCC>>>>>>>>>>>>>"+reportDto.getReportDesc());
		String decodedReport=URLDecoder.decode(reportDto.getFile(), StandardCharsets.UTF_8.toString());
		
		decodedReport=decodedReport.replace("”","'");
		decodedReport=decodedReport.replace("“","'");
		decodedReport=decodedReport.replace("’","'");
		decodedReport=decodedReport.replace("<property name=\"odaJndiName\">SoftQbe</property>","");
		
		reportDto.setFile(decodedReport);
		
		
		//System.out.println("decodeReport>>>>>>>>>>>>"+decodedReport);
		return reportService.addNewReport(reportDto);

    }
	
	@PostMapping("/updateReport")
	@ResponseBody

	public int updateReport(@RequestBody ReportDto reportDto) throws UnsupportedEncodingException{
		
		@SuppressWarnings("unused")
		String env=environment.getProperty("spring.datasource.url");
		
		System.out.println("DESCCCCC>>>>>>>>>>>>>"+reportDto.getReportDesc());
		String decodedReport=URLDecoder.decode(reportDto.getFile(), StandardCharsets.UTF_8.toString());
		
		decodedReport=decodedReport.replace("”","'");
		decodedReport=decodedReport.replace("“","'");
		decodedReport=decodedReport.replace("’","'");
		decodedReport=decodedReport.replace("<property name=\"odaJndiName\">SoftQbe</property>","");
		
		reportDto.setFile(decodedReport);
		
		
		//System.out.println("decodeReport>>>>>>>>>>>>"+decodedReport);
		return reportService.updateReport(reportDto);

    }
	
	 @DeleteMapping("/deleteReport/{reportId}")
	    public int deleteParameter(@PathVariable("reportId") long reportId) {

	        return reportService.deleteReport(reportId);
	        
	    }
	
	 
	 @GetMapping("/checkParameters/{reportId}")
		public List<ObjectNode> checkParameters(@PathVariable("reportId") long reportId) {

			return reportService.checkParameters(reportId);
		}
	 
	 
	 
	
}
