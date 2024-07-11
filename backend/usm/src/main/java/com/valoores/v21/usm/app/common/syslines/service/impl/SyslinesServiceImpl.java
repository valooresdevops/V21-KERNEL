package com.valoores.v21.usm.app.common.syslines.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valoores.v21.usm.app.common.syslines.model.Syslines;
import com.valoores.v21.usm.app.common.syslines.repository.ISyslinesRepository;
import com.valoores.v21.usm.app.common.syslines.service.ISyslinesService;

@Service
public class SyslinesServiceImpl implements ISyslinesService {

	@Autowired
	private ISyslinesRepository syslinesRepository;

	@Override
	public List<Syslines> getSyslinesCombo(Integer heaCode) {
		return syslinesRepository.findByHeaCode(heaCode);

	}

	@Override
	public List<Syslines> getSyslinesPeriodCombo() {
		return  syslinesRepository.getSyslinesPeriodCombo();

	}
	
	

}
