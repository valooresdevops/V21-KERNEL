package com.example.demo.app.reportbuilder.service;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import com.example.demo.app.reportbuilder.dto.CustomAPIDto;
import com.example.demo.app.reportbuilder.dto.ReportDto;
import com.example.demo.app.reportbuilder.model.ReportsModel;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.MapReduceCommand.OutputType;

public interface ReportExecutionService
{

	byte[] generatReport(ReportsModel report, OutputType output, HttpServletResponse response, HttpServletRequest request) throws Exception;
	
	byte[] generatReportWithOneParam(ReportsModel report,String reportParam, OutputType output, HttpServletResponse response, HttpServletRequest request) throws Exception;

	byte[] generatReportWithParameters(ReportsModel report,CustomAPIDto reportParameters, OutputType output, HttpServletResponse response, HttpServletRequest request) throws Exception;

	public void setApplicationContext(ApplicationContext context);
	
	void destroy();
	 
	public int addNewReport(ReportDto reportDto);
	 
	List<ObjectNode> getReportsData();
	ReportsModel getReportById(long id);
	
	public int deleteReport(long reportId);
	
	public int updateReport(ReportDto reportDto);

	public List<ObjectNode> checkParameters(long reportId);

}