package com.valoores.inDisplayApplication.app.common.syslines.service;

import java.util.List;

import com.valoores.inDisplayApplication.app.common.syslines.model.Syslines;

public interface ISyslinesService {

	public List<Syslines> getSysLinesData(Integer heaCode);
	public List<Syslines> getSysLinesDataWithIds(Integer heaCode, String ids);
}