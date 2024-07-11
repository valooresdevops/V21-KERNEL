package com.example.demo.utils;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.core.internal.registry.RegistryProviderFactory;


public class SSBirtReportEngine 
{
	private static IReportEngine reportEngine;
	
	public static IReportEngine getReportEngine() throws BirtException 
	{
		if (reportEngine == null) 
		{
            EngineConfig engineConf = new EngineConfig();
            Platform.startup(engineConf);
            IReportEngineFactory factory =
                    (IReportEngineFactory) Platform.createFactoryObject
                    (IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
         //   RegistryProviderFactory.releaseDefault();
            reportEngine = factory.createReportEngine(engineConf);
		}

		return reportEngine;
	}
	
	public static void destroyEngine()
	{
		reportEngine.destroy();
		Platform.shutdown();
		RegistryProviderFactory.releaseDefault();
		reportEngine = null;
	}
}
