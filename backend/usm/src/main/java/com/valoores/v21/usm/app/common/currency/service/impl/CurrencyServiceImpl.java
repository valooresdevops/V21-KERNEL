package com.valoores.v21.usm.app.common.currency.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valoores.v21.usm.app.common.currency.model.Currency;
import com.valoores.v21.usm.app.common.currency.repository.ICurrencyRepository;
import com.valoores.v21.usm.app.common.currency.service.ICurrencyService;

@Service
public class CurrencyServiceImpl implements ICurrencyService {

	@Autowired
	private ICurrencyRepository currencyRepository;

	@Override
	public List<Currency> getCurrency() {
		return currencyRepository.findAll();

	}
}