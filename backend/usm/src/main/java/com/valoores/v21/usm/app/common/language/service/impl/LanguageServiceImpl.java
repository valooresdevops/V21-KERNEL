package com.valoores.v21.usm.app.common.language.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valoores.v21.usm.app.common.language.model.Language;
import com.valoores.v21.usm.app.common.language.repository.ILanguageRepository;
import com.valoores.v21.usm.app.common.language.service.ILanguageService;
@Service
public class LanguageServiceImpl implements ILanguageService {

	@Autowired
	private ILanguageRepository languageRepository;
	
	@Override
	public List<Language> getLanguage() {
		return languageRepository.findAll();

	}
}
