package com.valoores.v21.usm.app.common.currency.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valoores.v21.usm.app.common.currency.model.Currency;
import com.valoores.v21.usm.app.common.currency.service.ICurrencyService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Currency", description = "Currency exposed APIs")
@RestController
@RequestMapping("/api")
public class CurrencyRestController {
	@Autowired
	private ICurrencyService currencyService;

	@GetMapping("/getCurrency")
	public List<Currency> getCurrency() {
		return currencyService.getCurrency();
	}
}
