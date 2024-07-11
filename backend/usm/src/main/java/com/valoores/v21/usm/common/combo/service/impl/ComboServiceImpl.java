package com.valoores.v21.usm.common.combo.service.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valoores.v21.usm.common.combo.repository.ComboRepository;
import com.valoores.v21.usm.common.combo.service.IComboService;

@Service
public class ComboServiceImpl implements IComboService {
	@Autowired
	private ComboRepository comboRepository;
	
	@Autowired
	private EntityManager entityManager;

	@SuppressWarnings({ "rawtypes", "static-access" })
	public List getCombo(String entityName) {
		return comboRepository.combo(entityManager,entityName) ;

	}

}
	