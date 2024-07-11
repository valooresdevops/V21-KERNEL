package com.valoores.v21.usm.app.common.language.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valoores.v21.usm.app.common.language.model.Language;
import com.valoores.v21.usm.app.common.language.service.ILanguageService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Language", description = "Language exposed APIs")
@RestController
@RequestMapping("/api")
public class LanguageRestController {

	@Autowired
	private ILanguageService languageService;

	@GetMapping("/getLanguage")
	public List<Language> getLanguage() {
		return languageService.getLanguage();
	}

}
