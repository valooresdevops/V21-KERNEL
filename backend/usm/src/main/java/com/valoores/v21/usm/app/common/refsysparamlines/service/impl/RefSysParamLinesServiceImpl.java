package com.valoores.v21.usm.app.common.refsysparamlines.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valoores.v21.usm.app.common.refsysparamlines.model.RefSysParamLines;
import com.valoores.v21.usm.app.common.refsysparamlines.repository.IRefSysParamLinesRepository;
import com.valoores.v21.usm.app.common.refsysparamlines.service.IRefSysParamLinesService;

@Service
public class RefSysParamLinesServiceImpl implements IRefSysParamLinesService {
	@Autowired
	private IRefSysParamLinesRepository refSysParamLinesRepository;

	@Override
	public List<RefSysParamLines> getRefSysParamLines() {
		return refSysParamLinesRepository.findAll();

	}
}
