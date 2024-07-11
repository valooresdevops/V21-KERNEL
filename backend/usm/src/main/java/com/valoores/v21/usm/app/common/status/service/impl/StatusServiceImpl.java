package com.valoores.v21.usm.app.common.status.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valoores.v21.usm.app.common.status.model.Status;
import com.valoores.v21.usm.app.common.status.repository.IStatusRepository;
import com.valoores.v21.usm.app.common.status.service.IStatusService;

@Service
public class StatusServiceImpl implements IStatusService {

	@Autowired
	private IStatusRepository statusRepository;

	@Override
	public List<Status> getStatus() {
		return statusRepository.findAll();

	}
}
