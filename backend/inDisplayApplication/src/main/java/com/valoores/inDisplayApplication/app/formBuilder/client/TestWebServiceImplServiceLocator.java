/**
 * TestWebServiceImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.valoores.inDisplayApplication.app.formBuilder.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class TestWebServiceImplServiceLocator extends org.apache.axis.client.Service implements TestWebServiceImplService {

	 //Filled when loading the class independent of the instance
	private static String serverUrl                            = null; //Contain  IPs Of the Servers That we Should Perform Listen if they are alive 
	private static String serverUrlListenr                     = null; //Contain  IPs Of the Servers who perform  listening on This Server
	
    private static java.util.List<String> serverUrls           = null;// in case we want to check many Servers 
    private static java.util.List<String> serverUrlListenrs    = null;// in case  many Servers  Listening
    
    private static boolean isMultiUrls                         = false;
    private static boolean isMultiUrlsListenr                  = false;
    
    
    private static  boolean  listeningExistOnServer            = true;
    
   // protected static softsolutions.util.logging.Log //log        = softsolutions.util.logging.Log.getInstance();
    
    // Instance Variables loaded when constructing the Instance
    private java.lang.String      TestWebServiceWS_address            = null;
    private java.util.Set<String> TestWebServiceWS_addresses          = null;
    
    private java.lang.String      TestWebServiceWS_addressListenr     = null;
    private java.util.Set<String> TestWebServiceWS_addressesListenr   = null;
    
    
    
    
    
    static
    {
    	//serverUrl        = System.getProperties().getProperty("softsolutions.wfm.serviceURL","");
    	serverUrl = "http://10.1.8.10:8080";
    	//serverUrlListenr = System.getProperties().getProperty("softsolutions.wfm.serviceURLListenr","");
    	serverUrlListenr = "http://10.1.8.10:8080";
    	try 
    	{
    		 do
    		 {
    			 if(serverUrl.trim().length() == 0)
    			 {
    				 ////log.info("WFM_Engine TestWebServiceImplServiceLocator : No  Checkserverlistening will occured for serverUrl ["+serverUrl+"]");
    			     break;
    			 }
    			 if(serverUrl.indexOf(",") != -1)
        	     {  
        	    	 serverUrls    = new ArrayList<String>();
        	    	 String[] urls = serverUrl.split(",");
        	         for(int i=0;i<urls.length;i++)
        	        	serverUrls.add(urls[i]);	
        	         
        	         isMultiUrls = true;
        	      }
    			 
    		 }
    		 while(false);
    		 
    		
    		 do
    		 {
    			 if(serverUrlListenr.trim().length() == 0)
    			 {
    				// //log.info("WFM_Engine TestWebServiceImplServiceLocator : No  Listening is performed on This Server  serverUrlListenr ["+serverUrlListenr+"]");
    				 listeningExistOnServer = false;
    				 break;
    			 }
    		 
	    		 if(serverUrlListenr.indexOf(",") != -1)
	    	     {  
	    			 serverUrlListenrs    = new ArrayList<String>();
	    	    	 String[] urls = serverUrlListenr.split(",");
	    	         for(int i=0;i<urls.length;i++)
	    	        	 serverUrlListenrs.add(urls[i]);	
	    	         
	    	         isMultiUrlsListenr = true;
	    	      }
    		 }
    		 while(false);
    		 
		} 
    	catch(Exception e) 
		{
    		e.printStackTrace();
    		////log.error(e, "WFM_Engine TestWebServiceImplServiceLocator : Error Web Service When loading Urls in Static mode");
    	}
    }
    
    public TestWebServiceImplServiceLocator() 
    {
    	super();
    	constructUrls();
    	
    }

    public TestWebServiceImplServiceLocator(org.apache.axis.EngineConfiguration config) 
    {
        super(config);
        constructUrls();
    }

    public TestWebServiceImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
        constructUrls();
    }
    
    private void  constructUrls()
    {
    	//Construct  Urls of The Servers That we Should perform Listening on
    	String serviceURLMulti=null, serviceListURL = null;
    	String testWebServiceWSString = "/services/TestWebServiceWS";
    	//log.info(" \n\n\n constructUrls isMultiUrls ============="+isMultiUrls);
    	if(isMultiUrls)
    	 {
    		 //synchronized (thread-safe) set backed by the specified set. 
    		 //In order to guarantee serial access
    		 TestWebServiceWS_addresses = Collections.synchronizedSet(new HashSet<String>());
    		 Iterator<String> it        = serverUrls.iterator();    		 
    		 while(it.hasNext())
    		 {
    			 serviceURLMulti = it.next()+testWebServiceWSString;
    			 if(serviceURLMulti.split("\"").length>1)
    			 {	    			 
    				 serviceURLMulti = serviceURLMulti.split("\"")[0]+serviceURLMulti.split("\"")[1];
	    			 //log.info(" \n\n\n constructUrls serviceURLMulti ============="+serviceURLMulti);
    			 }
    			 TestWebServiceWS_addresses.add(serviceURLMulti);
    		 }
    	 }
    	 else
    	 {
    		 serverUrl = serverUrl.replaceAll("\"", "");
    		 //log.info(" \n\n\n constructUrls serverUrl ============="+serverUrl);
    		 TestWebServiceWS_address          = serverUrl+testWebServiceWSString;
    		 //log.info(" \n\n\n constructUrls TestWebServiceWS_address ============="+TestWebServiceWS_address);
    	 }
    	
    	
    	//log.info(" \n\n\n constructUrls isMultiUrlsListenr ============="+isMultiUrlsListenr);
    	//Construct  Urls who perform  listening on This Server
    	if(isMultiUrlsListenr)
    	 {
    		 //synchronized (thread-safe) set backed by the specified set. 
    		 //In order to guarantee serial access
    		 TestWebServiceWS_addressesListenr = Collections.synchronizedSet(new HashSet<String>());
    		 
    		 Iterator<String> it        = serverUrlListenrs.iterator();
    		 while(it.hasNext())
    		 {
    			 serviceListURL = it.next()+testWebServiceWSString;
    			 if(serviceListURL.split("\"").length>1)
    			 {	    			 
    				 serviceListURL = serviceListURL.split("\"")[0]+serviceListURL.split("\"")[1];
	    			 //log.info(" \n\n\n constructUrls serviceListURL ============="+serviceListURL);
    			 }
    			 TestWebServiceWS_addressesListenr.add(serviceListURL);
    		 }
    	 }
    	else
   	 	{
    		serverUrlListenr = serverUrlListenr.replaceAll("\"", "");
    		TestWebServiceWS_addressListenr          = serverUrlListenr+testWebServiceWSString;
   	 	}
    	
    	
    	
    }
  
    // Use to get a proxy class for TestWebServiceWS
   

    public java.lang.String getTestWebServiceWSAddress() {
        return TestWebServiceWS_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String TestWebServiceWSWSDDServiceName = "TestWebServiceWS";

    public java.lang.String getTestWebServiceWSWSDDServiceName() {
        return TestWebServiceWSWSDDServiceName;
    }

    public void setTestWebServiceWSWSDDServiceName(java.lang.String name) {
        TestWebServiceWSWSDDServiceName = name;
    }
    
    public List<com.valoores.inDisplayApplication.app.formBuilder.client.TestWebServiceImpl> getTestWebServicesWS() throws javax.xml.rpc.ServiceException {
            
    	    java.net.URL endpoint;
            List<TestWebServiceImpl>  services = null; 
            services = new ArrayList<TestWebServiceImpl>();
         	//TestWebServiceWS_address = "http://10.1.10.61:7210"+"/services/TestWebServiceWS";
         	Iterator<String > it = this.TestWebServiceWS_addresses.iterator();
         	while(it.hasNext())
         	 { 
         	    try 
                 {
         		   endpoint = new java.net.URL(it.next());
                 }
         		  catch (java.net.MalformedURLException e) 
                 {
                    throw new javax.xml.rpc.ServiceException(e);
                 }
         		 services.add(getTestWebServiceWS(endpoint));
         	}
         	//log.trace("in getTestWebServicesWS when have multiple seevers ----------------------------"+services.size());
            return services;
     }
    
    
    
    public List<TestWebServiceImpl> getTestWebServicesWSListenr() throws javax.xml.rpc.ServiceException {
        
	    java.net.URL endpoint;
        List<TestWebServiceImpl>  services = null; 
        services = new ArrayList<TestWebServiceImpl>();
     	//TestWebServiceWS_address = "http://10.1.10.61:7210"+"/services/TestWebServiceWS";
     	Iterator<String > it = this.TestWebServiceWS_addressesListenr.iterator();
     	while(it.hasNext())
     	 { 
     	    try 
             {
     		   endpoint = new java.net.URL(it.next());
             }
     		  catch (java.net.MalformedURLException e) 
             {
                throw new javax.xml.rpc.ServiceException(e);
             }
     		 services.add(getTestWebServiceWS(endpoint));
     	}
     	////log.trace("in getTestWebServicesWSListenr when have multiple seevers ----------------------------"+services.size());
      return services;
 }
    
    

    public TestWebServiceImpl getTestWebServiceWS() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try 
        {
            endpoint = new java.net.URL(TestWebServiceWS_address);
           // //log.trace("in getTestWebServiceWS method ---TestWebServiceImplServiceLocator class ----after endpoint");
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTestWebServiceWS(endpoint);
    }
    
    
    
    public com.valoores.inDisplayApplication.app.formBuilder.client.TestWebServiceImpl getTestWebServiceWSListenr() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
         try 
         {
             endpoint = new java.net.URL(TestWebServiceWS_addressListenr);
            // //log.trace("in getTestWebServiceWS method ---TestWebServiceImplServiceLocator class ----after endpoint");
         }
         catch (java.net.MalformedURLException e) {
             throw new javax.xml.rpc.ServiceException(e);
         }
         return getTestWebServiceWS(endpoint);
     }
    
   /**
    * 
    */
    public TestWebServiceImpl getTestWebServiceWS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            TestWebServiceWSSoapBindingStub _stub = new TestWebServiceWSSoapBindingStub(portAddress, this);
            _stub.setPortName(getTestWebServiceWSWSDDServiceName());
           // //log.trace("in getTestWebServiceWS(url) method ---TestWebServiceImplServiceLocator class ----after setportname");
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
        	////log.error("Exceptiuon");
       
            return null;
        }
    }

    public void setTestWebServiceWSEndpointAddress(java.lang.String address) {
        TestWebServiceWS_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    @SuppressWarnings("rawtypes")
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (TestWebServiceImpl.class.isAssignableFrom(serviceEndpointInterface)) {
               TestWebServiceWSSoapBindingStub _stub = new TestWebServiceWSSoapBindingStub(new java.net.URL(TestWebServiceWS_address), this);
                _stub.setPortName(getTestWebServiceWSWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    @SuppressWarnings("rawtypes")
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("TestWebServiceWS".equals(inputPortName)) {
            return getTestWebServiceWS();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName(serverUrl+"/services/TestWebServiceWS", "TestWebServiceImplService");
    }

    @SuppressWarnings("rawtypes")
    private java.util.HashSet ports = null;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName(serverUrl+"/services/TestWebServiceWS", "TestWebServiceWS"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("TestWebServiceWS".equals(portName)) {
            setTestWebServiceWSEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

	public  boolean isMultiUrls() 
	{
		return isMultiUrls;
	}

	public  String getServerUrl()
	{
		return serverUrl;
	}
	
	public  java.util.List<String> getServerUrls()
	{
		return serverUrls;
	}

	
	public  boolean isMultiUrlsListenr()
	{
		return isMultiUrlsListenr;
	}
	public  String getServerUrlListenr()
	{
		return serverUrlListenr;
	}

	public  java.util.List<String> getServerUrlListenrs()
	{
		return serverUrlListenrs;
	}

	public  boolean isListeningExistOnServer()
	{
		return listeningExistOnServer;
	}

}
