package com.example.demo.app.reportbuilder.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.app.reportbuilder.dto.ApiColumnsDto;
import com.example.demo.app.reportbuilder.dto.CustomAPIDto;
import com.example.demo.app.reportbuilder.dto.ReportDto;
import com.example.demo.app.reportbuilder.model.ReportsModel;
import com.example.demo.app.reportbuilder.repository.BirtRepository;
import com.example.demo.app.reportbuilder.service.ReportExecutionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lowagie.text.FontFactory;
import com.mongodb.MapReduceCommand.OutputType;

@Service
public class ReportExecutionServiceImpl implements ReportExecutionService {
	
	@Autowired
	private EntityManager entityManagerR;
	@Autowired
	private BirtRepository birtRepository;
	
	public ReportsModel getReportById(long id)
	{
		return birtRepository.getById(id);
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

	
	
	
	 private HTMLServerImageHandler htmlImageHandler = new HTMLServerImageHandler();

	   

	    private IReportEngine birtEngine;
	    private ApplicationContext context;
	    private String imageFolder;

	    @SuppressWarnings("unused")
		private Map<String, IReportRunnable> reports = new HashMap<>();

	    @SuppressWarnings("unchecked")
	    @PostConstruct
	    protected void initialize() throws BirtException {
	    	System.out.println("\n\n\n=================hello myyyy");
	        EngineConfig config = new EngineConfig();
	        System.out.println("\n\n\n========config.getAppContext()========"+config.getAppContext());
	        config.getAppContext().put("spring", this.context);
	        Platform.startup(config);
	        IReportEngineFactory factory = (IReportEngineFactory) Platform
	          .createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
	        birtEngine = factory.createReportEngine(config);
	       // imageFolder = System.getProperty("user.dir") + File.separatorChar + reportsPath + imagesPath;
	        //loadReports();
	    }

	    @Override
	    public void setApplicationContext(ApplicationContext context) {
	        this.context = context;
	    }

	    /**
	     * Load report files to memory
	     *
	     */
	    public void loadReports() throws EngineException {
	        File folder = new File("d:/");
	        for (String file : Objects.requireNonNull(folder.list())) {
	            if (!file.endsWith(".rptdesign")) {
	                continue;
	            }

	          //  reports.put(file.replace(".rptdesign", ""),
	             // birtEngine.openReportDesign(folder.getAbsolutePath() + File.separator + file));

	        }
	    }

	


	    public byte[] generatReport(ReportsModel report, OutputType output, HttpServletResponse response, HttpServletRequest request) throws Exception {
	      // case HTML:
	           // generateHTMLReport(reports.get(reportName), response, request);
//	            break;
//	        case PDF:
	    	//ReportsModel report  = getReportById(reportId);
	    	byte[] reportBytes = report.getMEDIA_FILE();
	    	InputStream is = new ByteArrayInputStream(reportBytes);
	        return  generatePDFReport(birtEngine.openReportDesign(is), response, request);
//	            break;
//	        default:
	         //   throw new IllegalArgumentException("Output type not recognized:" + output);
	            
//	            return null;
	        
	    }

	    /**
	     * Generate a report as HTML
	     */
	    @SuppressWarnings({ "unchecked", "unused" })
	    private void generateHTMLReport(IReportRunnable report, HttpServletResponse response, HttpServletRequest request) {
	        IRunAndRenderTask runAndRenderTask = birtEngine.createRunAndRenderTask(report);
	        response.setContentType(birtEngine.getMIMEType("html"));
	        IRenderOption options = new RenderOption();
	        HTMLRenderOption htmlOptions = new HTMLRenderOption(options);
	        htmlOptions.setOutputFormat("html");
	    //    htmlOptions.setBaseImageURL("/" + reportsPath + imagesPath);
	        htmlOptions.setImageDirectory(imageFolder);
	        htmlOptions.setImageHandler(htmlImageHandler);
	        runAndRenderTask.setRenderOption(htmlOptions);
	        runAndRenderTask.getAppContext().put(EngineConstants.APPCONTEXT_BIRT_VIEWER_HTTPSERVET_REQUEST, request);

	        try {
	            htmlOptions.setOutputStream(response.getOutputStream());
	            runAndRenderTask.run();
	        } catch (Exception e) {
	            throw new RuntimeException(e.getMessage(), e);
	        } finally {
	            runAndRenderTask.close();
	        }
	    }

	    /**
	     * Generate a report as PDF
	     */
	    @SuppressWarnings("unchecked")
	    private byte[] generatePDFReport(IReportRunnable report, HttpServletResponse response, HttpServletRequest request) {
	        System.out.println("\n\n\n\n\n---------------ya 3alam");
	    	IRunAndRenderTask runAndRenderTask = birtEngine.createRunAndRenderTask(report);
	        response.setContentType(birtEngine.getMIMEType("pdf"));
	        IRenderOption options = new RenderOption();
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	       
	        try {
	            FontFactory.registerDirectory("/usr/share/fonts/noto/NotoSansArabic-Regular.ttf");
	            System.out.println("Font NotoSansArabic-Regular.ttf registered successfully.");
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException("Failed to register font.", e);
	        }
	        
	        PDFRenderOption pdfRenderOption = new PDFRenderOption(options);
	        pdfRenderOption.setOutputFormat("pdf");
	        pdfRenderOption.setOutputStream(out);
	        runAndRenderTask.setRenderOption(pdfRenderOption);
	        runAndRenderTask.getAppContext().put(EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT, request);

	        try {
	            pdfRenderOption.setOutputStream(response.getOutputStream());
	            runAndRenderTask.run();
	        } catch (Exception e) {
	        	e.printStackTrace();
	            throw new RuntimeException(e.getMessage(), e);
	        } finally {
	            runAndRenderTask.close();
	        }
	        return out.toByteArray();
	    }

	    @Override
	    public void destroy() {
	        birtEngine.destroy();
	        Platform.shutdown();
	    }

		@Override
		public List<ObjectNode> getReportsData() {
			return getJson(entityManagerR, "SELECT q.id AS REPORT_ID,  q.REPORT_NAME AS REPORT_NAME,q.CREATION_DATE AS CREATION_DATE from ReportsModel q"); 
		}
	

	    public byte[] generatReportWithParameters(ReportsModel report,CustomAPIDto reportParameters, OutputType output, HttpServletResponse response, HttpServletRequest request) throws Exception {
	      // case HTML:
	           // generateHTMLReport(reports.get(reportName), response, request);
//	            break;
//	        case PDF:
	    	//ReportsModel report  = getReportById(reportId);
	    	byte[] reportBytes = report.getMEDIA_FILE();
	    	System.out.println("IN FIRST generatReportWithParameters");
	    	InputStream is = new ByteArrayInputStream(reportBytes);
	    	System.out.println("AFTER INPUT STREAM");

	        return  generatePDFReportWithParameters(birtEngine.openReportDesign(is),reportParameters, response, request);
//	            break;
//	        default:
	         //   throw new IllegalArgumentException("Output type not recognized:" + output);
	            
//	            return null;
	        
	    }
	    
	    
	    
	    @SuppressWarnings("unchecked")
	    private byte[] generatePDFReportWithParameters(IReportRunnable report,CustomAPIDto reportParameters, HttpServletResponse response, HttpServletRequest request) {
	       System.out.println("IN SERVICE IMPLEMENTATION");
	    	   EngineConfig config = new EngineConfig();
	           try {
				Platform.startup(config);
			} catch (BirtException e1) {
				e1.printStackTrace();
			}

	           // SPECIFY FONT DIRECTORY FOR LINUX SERVER
	           try {
	               URL fontUrl = new URL("file:/usr/share/fonts/noto/NotoSansArabic-Regular.ttf");
	               FontFactory.register(fontUrl.getFile());
	           } catch (Exception e) {
	               e.printStackTrace();
	           }
	    	
	    	System.out.println("\n\n\n\n\n---------------ya 3alam");
	    	IRunAndRenderTask runAndRenderTask = birtEngine.createRunAndRenderTask(report);
	    	//runAndRenderTask.setParameterValue("kycId", "17135");
	    	List<ApiColumnsDto> allReportParams=reportParameters.getColumns();
	    	
			Map<String, Object> parameters = new HashMap<>();
			
			


			
			for(int i=0;i<allReportParams.size();i++) {
				parameters.put(allReportParams.get(i).getColName(), allReportParams.get(i).getColVal());
				System.out.println("col name>>>>>>>>>>"+allReportParams.get(i).getColName());
				System.out.println("col VALLLL>>>>>>>>>>"+allReportParams.get(i).getColVal());

			}
			
			runAndRenderTask.setParameterValues(parameters);
	        response.setContentType(birtEngine.getMIMEType("pdf"));
	        IRenderOption options = new RenderOption();
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        
	        PDFRenderOption pdfRenderOption = new PDFRenderOption(options);
	        pdfRenderOption.setOutputFormat("pdf");
	        pdfRenderOption.setOutputStream(out);
	        runAndRenderTask.setRenderOption(pdfRenderOption);
	        runAndRenderTask.getAppContext().put(EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT, request);

	        try {
	            pdfRenderOption.setOutputStream(response.getOutputStream());
	            runAndRenderTask.run();
	        } catch (Exception e) {
	        	e.printStackTrace();
	            throw new RuntimeException(e.getMessage(), e);
	        } finally {
	            runAndRenderTask.close();
	        }
	        return out.toByteArray();
	    }
	    
	    
	    
	    
	    
	    
	    
	    
	    public byte[] generatReportWithOneParam(ReportsModel report,String reportParam, OutputType output, HttpServletResponse response, HttpServletRequest request) throws Exception {
		      // case HTML:
		           // generateHTMLReport(reports.get(reportName), response, request);
//		            break;
//		        case PDF:
		    	//ReportsModel report  = getReportById(reportId);
		    	byte[] reportBytes = report.getMEDIA_FILE();
		    	InputStream is = new ByteArrayInputStream(reportBytes);
		        return  generatePDFReportWithParam(birtEngine.openReportDesign(is),reportParam, response, request);
//		            break;
//		        default:
		         //   throw new IllegalArgumentException("Output type not recognized:" + output);
		            
//		            return null;
		        
		    }
	    
	    @SuppressWarnings("unchecked")
	    private byte[] generatePDFReportWithParam(IReportRunnable report,String reportParam, HttpServletResponse response, HttpServletRequest request) {
	        System.out.println("\n\n\n\n\n---------------ya 3alam");
	    	IRunAndRenderTask runAndRenderTask = birtEngine.createRunAndRenderTask(report);
	    	//runAndRenderTask.setParameterValue("kycId", "17135");

			Map<String, Object> parameters = new HashMap<>();
			parameters.put("kycId", reportParam);
			//17135
			runAndRenderTask.setParameterValues(parameters);
	        response.setContentType(birtEngine.getMIMEType("pdf"));
	        IRenderOption options = new RenderOption();
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        
	        PDFRenderOption pdfRenderOption = new PDFRenderOption(options);
	        pdfRenderOption.setOutputFormat("pdf");
	        pdfRenderOption.setOutputStream(out);
	        runAndRenderTask.setRenderOption(pdfRenderOption);
	        runAndRenderTask.getAppContext().put(EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT, request);

	        try {
	            pdfRenderOption.setOutputStream(response.getOutputStream());
	            runAndRenderTask.run();
	        } catch (Exception e) {
	        	e.printStackTrace();
	            throw new RuntimeException(e.getMessage(), e);
	        } finally {
	            runAndRenderTask.close();
	        }
	        return out.toByteArray();
	    }

		@Override
		public int addNewReport(ReportDto reportDto) {
			
			
			
			Date date=new Date();
			ReportsModel reportsModel=new ReportsModel();
			reportsModel.setREPORT_NAME(reportDto.getReportName());
			reportsModel.setREPORT_DESC(reportDto.getReportDesc());
			System.out.println("REPORT_DESCCCCC>>>>>>>>>>>"+reportDto.getReportDesc());
			reportsModel.setCREATION_DATE(date);
			reportsModel.setCREATED_BY(reportDto.getUserId());
			reportsModel.setMEDIA_FILE(reportDto.getFile().getBytes());
			reportsModel.setDATA_SOURCE(20);

			System.out.println(reportsModel.getMEDIA_FILE());
			
			birtRepository.save(reportsModel);
			
			
			return 0;
		}

		
		 @Transactional
			public int deleteReport(long reportId) {

			 birtRepository.deleteReport(reportId);
			 
				return 0;
			}

		 @Transactional
			public int updateReport(ReportDto reportDto) {
				
				
				
				Date date=new Date();
				ReportsModel reportsModel=new ReportsModel();
				reportsModel.setREPORT_NAME(reportDto.getReportName());
				reportsModel.setREPORT_DESC(reportDto.getReportDesc());
				System.out.println("REPORT_DESCCCCC>>>>>>>>>>>"+reportDto.getReportDesc());
				reportsModel.setCREATION_DATE(date);
				reportsModel.setCREATED_BY(reportDto.getUserId());
				reportsModel.setMEDIA_FILE(reportDto.getFile().getBytes());
				reportsModel.setDATA_SOURCE(20);

				System.out.println("REPORT ID>>>>>>>>>>>>"+reportDto.getReportId());
				System.out.println("UPDATE>>>>>>>>>>>"+reportsModel.getMEDIA_FILE());
				
				birtRepository.updateReport(reportDto.getReportId(),reportDto.getReportName(),reportDto.getReportDesc(),reportDto.getFile().getBytes());
				
				
				return 0;
			}

		@Override
		public List<ObjectNode> checkParameters(long reportId) {
			String allParams="[";
			byte[] reportFile=birtRepository.getMediaFile(reportId);
	        String reportString = new String(reportFile, StandardCharsets.UTF_8);
	        
	        String[] reportStringArray=reportString.split("<scalar-parameter");
			
	        for(int i=1;i<reportStringArray.length;i++) {
	        	System.out.println(reportStringArray[i].split("name=\"")[1].split("\"")[0]);
	        	String paramName=reportStringArray[i].split("name=\"")[1].split("\"")[0];
	        	if(i==reportStringArray.length-1) {
	        	allParams+="{\"paramName\":\""+paramName+"\"}";
	        	}else {
		        	allParams+="{\"paramName\":\""+paramName+"\"},";

	        	}
	        	}
			allParams+="]";
	        
	        ObjectMapper objectMapper = new ObjectMapper();
	        List<ObjectNode> objectNodeList = new ArrayList<>();

	        System.out.println("ALL PARAMS>>>>>>>>>>"+allParams);
	        
	        try {
	            // Read JSON array as JsonNode
	            JsonNode jsonNode = objectMapper.readTree(allParams);

	            // Check if the root is an array
	            if (jsonNode.isArray()) {
	                // Iterate over array elements and convert them to ObjectNode
	                for (JsonNode element : jsonNode) {
	                    if (element.isObject()) {
	                        ObjectNode objectNode = (ObjectNode) element;
	                        objectNodeList.add(objectNode);
	                    }
	                }
	            } else {
	                // If the root is not an array, you may want to handle it accordingly
	                System.out.println("The provided JSON string is not an array.");
	            }
	        } catch (JsonProcessingException e) {
	            e.printStackTrace();
	        }

	        
			return objectNodeList;
		}

		
	}