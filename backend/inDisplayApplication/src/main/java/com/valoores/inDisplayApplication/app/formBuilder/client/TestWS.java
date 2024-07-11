package com.valoores.inDisplayApplication.app.formBuilder.client;



public class TestWS 
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
        // String path = "C:\\softsuite601\\Tomcat6\\webapps\\ROOT\\WEB-INF\\client_deploy.wsdd";
        // EngineConfiguration configuration = new FileProvider(path);
         TestWebServiceImplService locator = new TestWebServiceImplServiceLocator();
         TestWebServiceImpl service = locator.getTestWebServiceWS();
         
        // com.softsolutions.wfm.vo.engine.searchcriteria.ScheduledActSC schActScClient = new com.softsolutions.wfm.vo.engine.searchcriteria.ScheduledActSC();
        
//         java.util.Timer t = new java.util.Timer();
//         t.schedule( new java.util.TimerTask()
//          {
//           public void run()
//           {
        	   try
        	   {
        		  //service.startScheduledAct("6387,null");
        		  // startEngineBO.startScheduledAct(schActSc);
        		   service.executeRule("5751");
        	   }
        	   catch(Exception x){x.printStackTrace();}
//           }
//	         }
//		   , 100
//		 );  
//          
        }
        catch (Exception e )
        {
        	e.printStackTrace();
            //log.error(e,"");
        }
    }

}
