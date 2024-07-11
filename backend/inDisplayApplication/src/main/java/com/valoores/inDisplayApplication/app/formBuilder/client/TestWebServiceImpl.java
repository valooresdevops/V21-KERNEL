/**
 * TestWebServiceImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.valoores.inDisplayApplication.app.formBuilder.client;

public interface TestWebServiceImpl extends java.rmi.Remote {
    public void startScheduledAct(java.lang.String scheduleInfo) throws java.rmi.RemoteException;
    public void startSchedBatchAct(java.lang.String activityId) throws java.rmi.RemoteException;
    public void stopSchedBatchAct(java.lang.String actId) throws java.rmi.RemoteException;
    public void stopScheduledAct(java.lang.String schedInfo) throws java.rmi.RemoteException;
    public boolean checkServerListening() throws java.rmi.RemoteException;
    public void  executeRule (String ruleId)throws java.rmi.RemoteException;
}
