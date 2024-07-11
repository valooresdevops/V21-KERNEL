package com.valoores.v21.usm.app.common.currency.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.valoores.v21.usm.app.common.currency.model.Currency;

public interface ICurrencyRepository  extends JpaRepository  <Currency, Long> {
	
	@Query("select a from Currency a")
	 List<Currency> getCurrency();
}
