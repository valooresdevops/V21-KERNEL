/**
 * TestWebServiceImplService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.valoores.inDisplayApplication.app.formBuilder.client;

import java.util.List;

public interface TestWebServiceImplService extends javax.xml.rpc.Service 
{
    public java.lang.String getTestWebServiceWSAddress();

    public TestWebServiceImpl getTestWebServiceWS() 
    throws javax.xml.rpc.ServiceException;
    public TestWebServiceImpl getTestWebServiceWSListenr() 
    throws javax.xml.rpc.ServiceException;
    
    
    public List<TestWebServiceImpl> getTestWebServicesWS() 
    throws javax.xml.rpc.ServiceException;
    public List<TestWebServiceImpl> getTestWebServicesWSListenr() 
    throws javax.xml.rpc.ServiceException;
  

    public TestWebServiceImpl getTestWebServiceWS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;



    public  boolean isMultiUrls();
    public  String getServerUrl();
    public  java.util.List<String> getServerUrls();
    
    public  boolean isMultiUrlsListenr();
    public  String getServerUrlListenr();
    public  java.util.List<String> getServerUrlListenrs();
    
    public  boolean isListeningExistOnServer();


}
