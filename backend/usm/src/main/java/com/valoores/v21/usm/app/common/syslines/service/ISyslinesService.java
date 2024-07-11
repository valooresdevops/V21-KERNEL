package com.valoores.v21.usm.app.common.syslines.service;

import java.util.List;

import com.valoores.v21.usm.app.common.syslines.model.Syslines;

public interface ISyslinesService {

	List<Syslines> getSyslinesCombo(Integer heaCode);
	
	public List<Syslines> getSyslinesPeriodCombo();
	//public List<USMApplication> getApplicationCombo();
}
