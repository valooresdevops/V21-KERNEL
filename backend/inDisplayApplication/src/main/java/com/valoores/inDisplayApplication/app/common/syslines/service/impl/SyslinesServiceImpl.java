package com.valoores.inDisplayApplication.app.common.syslines.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valoores.inDisplayApplication.app.common.syslines.model.Syslines;
import com.valoores.inDisplayApplication.app.common.syslines.repository.ISyslinesRepository;
import com.valoores.inDisplayApplication.app.common.syslines.service.ISyslinesService;

@Service
public class SyslinesServiceImpl implements ISyslinesService {

	@Autowired
	private ISyslinesRepository syslinesRepository;

	@Override
	public List<Syslines> getSysLinesData(Integer heaCode) {
		return  syslinesRepository.getSysLinesData(heaCode);

	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Syslines> getSysLinesDataWithIds(Integer heaCode, String ids) {
System.out.println("ids >>>>>>>>> " + ids);
System.out.println("heaCode >>>>>>>>> " + heaCode);
		@SuppressWarnings("unchecked")
		List<Integer> linCodes = new ArrayList();
		String[] idsArr = ids.split(",");
		for(int i = 0; i < idsArr.length; i ++) {
			linCodes.add(Integer.parseInt(idsArr[i]));
		}
		return  syslinesRepository.getSysLinesDataWithIds(heaCode, linCodes);
	}
}